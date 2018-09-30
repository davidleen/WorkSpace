package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.*;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.repository_erp.ErpOrderRepository;
import com.giants3.hd.server.repository_erp.ErpWorkRepository;
import com.giants3.hd.server.utils.AttachFileUtils;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Erp 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class ErpService extends AbstractErpService {

    @Autowired
    ErpOrderRepository repository;

    @Autowired
    ErpWorkService erpWorkService;

    @Autowired
    ErpPrdtService erpPrdtService; ;



    @Autowired
    ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    OrderItemRepository orderItemRepository;


    @Autowired
    OrderItemWorkFlowRepository orderItemWorkFlowRepository;

    @Autowired
    ErpOrderItemProcessRepository erpOrderItemProcessRepository;


    @Autowired
    WorkFlowMessageRepository workFlowMessageRepository;


    @Autowired
    WorkFlowRepository workFlowRepository;


    @Autowired
    WorkFlowProductRepository workFlowProductRepository;
    @Autowired
    ErpWorkFlowReportRepository workFlowReportRepository;

    @Autowired
    WorkFlowArrangerRepository workFlowArrangerRepository;
    @Autowired
    WorkFlowWorkerRepository workFlowWorkerRepository;


    @Autowired
    OrderRepository orderRepository;
    //临时文件夹
    @Value("${tempfilepath}")
    private String tempFilePath;

    //订单唛头文件夹
    @Value("${maitoufilepath}")
    private String maitoufilepath;

    //附件文件夹
    @Value("${attachfilepath}")
    private String attachfilepath;

    @Autowired
    ErpWorkRepository erpWorkRepository;

    @Autowired
    OrderAuthRepository orderAuthRepository;




    @Override
    protected void onEntityManagerCreate(EntityManager manager) {

    }


    /**
     * 查找订单
     *
     * @param loginUser
     * @param key
     * @param salesId   是否制定业务员 -1 表示所有人
     * @param pageIndex
     * @param pageSize
     * @return
     */

    public RemoteData<ErpOrder> findByKey(User loginUser, String key, long salesId, int pageIndex, int pageSize) {
        List<ErpOrder> result;
        int totalCount;
        if (loginUser.isAdmin() && salesId == -1) {
            result = repository.findOrders(key, pageIndex, pageSize);
            totalCount = repository.getOrderCountByKey(key);
        } else {
            List<String> salesNos = null;
            //查询所有
            OrderAuth orderAuth = orderAuthRepository.findFirstByUser_IdEquals(loginUser.id);
            if (orderAuth != null && !StringUtils.isEmpty(orderAuth.relatedSales)) {
                salesNos = userService.findUserCodes(loginUser.id, salesId, orderAuth.relatedSales);
            }


            if (salesNos == null || salesNos.size() == 0) return wrapData();
            result = repository.findOrders(key, salesNos, pageIndex, pageSize);
            totalCount = repository.getOrderCountByKey(key, salesNos);
        }
        //进行业务员配对。

        for (ErpOrder erpOrder : result) {

            User user = userRepository.findFirstByCodeEquals(erpOrder.sal_no);
            if (user != null) {
                attachData(erpOrder, user);
            }

            Order order = orderRepository.findFirstByOsNoEquals(erpOrder.os_no);

            attachData(erpOrder, order);


        }


        return wrapData(pageIndex, pageSize, (totalCount - 1) / pageSize + 1, totalCount, result);

    }


//


    /**
     * 查询订单详情
     *
     * @param os_no
     * @return
     */
    public RemoteData<ErpOrderDetail> getOrderDetail(User loginUser, String os_no) {


        ErpOrderDetail orderDetail = new ErpOrderDetail();


        ErpOrder erpOrder = repository.findOrderByNO(os_no);

        Order order = orderRepository.findFirstByOsNoEquals(erpOrder.os_no);

        attachData(erpOrder, order);


        User user = userRepository.findFirstByCodeEquals(erpOrder.sal_no);
        if (user != null) {
            attachData(erpOrder, user);
        }


        orderDetail.erpOrder = erpOrder;
        orderDetail.items = searchOrderItems(loginUser, os_no);


        return wrapData(orderDetail);

    }


    /**
     * 查找指定订单号的订单项目
     *
     * @param loginUser
     * @param os_no
     * @return
     */
    public List<ErpOrderItem> searchOrderItems(User loginUser, String os_no) {
        List<ErpOrderItem> orderItems = repository.findItemsByOrderNo(os_no);
        bindDataToOrderItem(loginUser, orderItems);
        return orderItems;

    }


    /**
     * 查詢指定用戶可排厂的订单
     * <p/>
     * 胚体加工的用户（第一个流程的用户） 返回所有未完成的订单
     * <p/>
     * <p/>
     * 其他流程的工作人员，返回当前流程关联的订单数据
     *
     * @param loginUser
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */

    public RemoteData<ErpOrderItem> searchUserWorkOrderItems(User loginUser, String key, int pageIndex, int pageSize) {

        List<WorkFlowWorker> workFlowWorkers = workFlowWorkerRepository.findByUserIdEquals(loginUser.id);
        if (workFlowWorkers == null || workFlowWorkers.size() == 0) //当前用户不是生产人员
            return wrapData();


        WorkFlowWorker firstStepWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndWorkFlowStepEquals(loginUser.id, ErpWorkFlow.FIRST_STEP);
        List<ErpOrderItem> orderItems;
        if (firstStepWorker == null) {
            //      非第一道的用户
            orderItems = erpWorkService.searchHasStartWorkFlowUnCompleteOrderItems(key);
        } else {
            orderItems = erpWorkService.searchUnCompleteOrderItems(key,-1,0,0).datas;

            List<ErpOrderItem> result = new ArrayList<>();

            //过滤  进行产品排产过滤  【0-5000】 铁件  【5000-9999】木件
            for (ErpOrderItem erpOrderItem : orderItems) {


                //先從指令單中判斷鐵幕
                List<ErpOrderItemProcess> processes = erpWorkRepository.findOrderItemProcesses(erpOrderItem.os_no, erpOrderItem.itm);


                ProductType productType =erpPrdtService.getProductTypeFromOrderItemProcess(processes);


                boolean shouldAddItem = false;

                if (productType.type == ProductType.TYPE_NONE) {
                    //无法确定类型  都可以应该返回
                    shouldAddItem = true;

                } else if ((productType.type & ProductType.TYPE_TIE) == ProductType.TYPE_TIE && firstStepWorker.tie) {
                    shouldAddItem = true;

                } else if ((productType.type & ProductType.TYPE_MU) == ProductType.TYPE_MU && firstStepWorker.mu) {
                    shouldAddItem = true;
                }
                if (shouldAddItem) {

//                    //查询当前item 在第一道的进度  100% 则也不显示
//                    ErpWorkFlowReport report = workFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(erpOrderItem.os_no, erpOrderItem.itm, ErpWorkFlow.FIRST_STEP);
//
//                    if (report == null || report.percentage < 1)
                    result.add(erpOrderItem);
                }


            }

            orderItems = result;


        }


        //第一流程人员  不进行筛选
        //  if (firstStepWorker == null) {
        //流程过滤  如果当前流程已经完成100% 去除  ，如果上一流程未满100% 也去除

        List<ErpOrderItem> result = new ArrayList<>();
        for (ErpOrderItem erpOrderItem : orderItems) {


            //遍历权限配置
            for (WorkFlowWorker workFlowWorker : workFlowWorkers) {

                ErpWorkFlowReport report = workFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(erpOrderItem.os_no, erpOrderItem.itm, workFlowWorker.workFlowStep);
                //如果当前流程已经完成100% 去除  ，
                if (report != null && report.percentage > 0.9999) continue;

                int previousStep = ErpWorkFlow.findPrevious(workFlowWorker.workFlowStep);
                //   上一道不受百分比控制 白胚加工
                if (previousStep != ErpWorkFlow.FIRST_STEP) {
                    report = workFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(erpOrderItem.os_no, erpOrderItem.itm, previousStep);
                    //如果上一流程未满100% 也去除//
                    if (report == null || report.percentage < 1) continue;
                }


                result.add(erpOrderItem);
                break;
            }
        }


        orderItems = result;
        // }
//
//


        return wrapData(0, orderItems.size(), 1, orderItems.size(), orderItems);

    }


    private void bindDataToOrderItem(User loginUser, List<ErpOrderItem> orderItems) {
        //从报价系统读取产品的单位信息，图片信息
        for (ErpOrderItem item : orderItems) {

            String productCode = item.prd_no;
            String pVersion = "";
            try {
                pVersion = item.id_no.substring(productCode.length() + 2);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            item.pVersion = pVersion;


            Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(item.prd_name, pVersion);
            //  item.prd_no

            if (product != null) {

                item.productId = product.id;


                item.packAttaches = product.packAttaches;
                item.packageInfo = product.packInfo;


            }

            item.thumbnail = item.url = FileUtils.getErpProductPictureUrl(item.id_no, "");

            // 附加数据
            OrderItem orderItem = orderItemRepository.findFirstByOsNoEqualsAndItmEquals(item.os_no, item.itm);
            if (orderItem != null) {

                item.id = orderItem.id;
                item.maitou = orderItem.maitou;
                item.guagou = orderItem.guagou;

                //如果订单无产品包装说明，默认使用产品的说明
                if (!StringUtils.isEmpty(orderItem.packageInfo))
                    item.packageInfo = orderItem.packageInfo;
                item.sendDate = orderItem.sendDate;
                item.verifyDate = orderItem.verifyDate;


                //绑定订单跟踪数据
                OrderItemWorkFlow workFlowOrderItem = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(orderItem.id);
                if (workFlowOrderItem != null) {
                    item.workFlowDescribe = workFlowOrderItem.workFlowDiscribe;
                }


            }
        }
    }

    @Transactional
    public RemoteData<ErpOrderDetail> saveOrderDetail(User loginUser, ErpOrderDetail orderDetail) {

        ErpOrder erpOrder = orderDetail.erpOrder;

        Order order = orderRepository.findFirstByOsNoEquals(erpOrder.os_no);
        if (order == null) {
            order = new Order();
        }


        String oldAttaches = erpOrder.attaches;
        detachData(erpOrder, order);


        //附件处理
        order.attaches = AttachFileUtils.updateProductAttaches(erpOrder.attaches, oldAttaches, "ORDER_" + order.osNo, attachfilepath, tempFilePath);
        orderRepository.save(order);


        List<ErpOrderItem> newStockOutItems = orderDetail.items;

        for (ErpOrderItem item : newStockOutItems) {

            OrderItem orderItem = orderItemRepository.findFirstByOsNoEqualsAndItmEquals(item.os_no, item.itm);
            if (orderItem == null) {
                orderItem = new OrderItem();
            }
            detachData(item, orderItem);


            orderItemRepository.save(orderItem);
        }
        return getOrderDetail(loginUser, erpOrder.os_no);
    }

    /**
     * 剥离数据  保存本系统
     *
     * @param erpOrderItem
     */
    private void detachData(ErpOrderItem erpOrderItem, OrderItem orderItem) {
        if (erpOrderItem == null || orderItem == null) {
            return;
        }

        orderItem.maitou = erpOrderItem.maitou;
        orderItem.guagou = erpOrderItem.guagou;
        orderItem.packageInfo = erpOrderItem.packageInfo;
        orderItem.sendDate = erpOrderItem.sendDate;
        orderItem.verifyDate = erpOrderItem.verifyDate;
        orderItem.osNo = erpOrderItem.os_no;
        orderItem.itm = erpOrderItem.itm;


        orderItem.qty = erpOrderItem.qty;
        orderItem.ut = erpOrderItem.ut;
        orderItem.prdNo = erpOrderItem.prd_no;
        orderItem.pVersion = erpOrderItem.pVersion;
        orderItem.url = erpOrderItem.url;
        orderItem.batNo = erpOrderItem.bat_no;


    }


    /**
     * 剥离数据
     *
     * @param erpOrder
     * @param order
     */
    private void detachData(ErpOrder erpOrder, Order order) {
        if (erpOrder == null || order == null) {
            return;
        }


        order.cemai = erpOrder.cemai;
        order.zhengmai = erpOrder.zhengmai;
        order.neheimai = erpOrder.neheimai;
        order.memo = erpOrder.memo;
        order.osNo = erpOrder.os_no;
        order.attaches = erpOrder.attaches;


        order.zuomai = erpOrder.zuomai;
        order.youmai = erpOrder.youmai;
        order.sal_no = erpOrder.sal_no;
        order.sal_name = erpOrder.sal_name;
        order.sal_cname = erpOrder.sal_cname;
        order.cus_no = erpOrder.cus_no;

    }


    /**
     * 附加数据
     *
     * @param order
     * @param erpOrder
     */
    private void attachData(ErpOrder erpOrder, Order order) {

        if (erpOrder != null) {

            File filePath = FileUtils.getMaitouFilePath(maitoufilepath, erpOrder.os_no);

            erpOrder.maitouUrl = filePath.exists() ? FileUtils.getMaitouFileUrl(erpOrder.os_no) : "";


        }

        if (order == null || erpOrder == null) {
            return;
        }


        erpOrder.cemai = order.cemai;
        erpOrder.zhengmai = order.zhengmai;
        erpOrder.neheimai = order.neheimai;
        erpOrder.zuomai = order.zuomai;
        erpOrder.youmai = order.youmai;
        erpOrder.memo = order.memo;
        erpOrder.attaches = order.attaches;


    }


    /**
     * 附加数据
     *
     * @param erpOrder
     * @param user
     */
    private void attachData(ErpOrder erpOrder, User user) {
        if (user == null || erpOrder == null) {
            return;
        }


        erpOrder.sal_no = user.code;
        erpOrder.sal_name = user.name;
        erpOrder.sal_cname = user.chineseName;


    }

    public RemoteData<ErpOrder> findByCheckDate(String key, String dateStart, String dateEnd, int pageIndex, int pageSize) {


        List<ErpOrder> result = repository.findOrderByKeyCheckDate(key, dateStart, dateEnd, pageIndex, pageSize);
        //进行业务员配对。

        for (ErpOrder erpOrder : result) {

            User user = userRepository.findFirstByCodeEquals(erpOrder.sal_no);
            if (user != null) {
                attachData(erpOrder, user);
            }

            Order order = orderRepository.findFirstByOsNoEquals(erpOrder.os_no);

            attachData(erpOrder, order);


        }
        int totalCount = repository.getOrderCountByKeyAndCheckDate(key, dateStart, dateEnd);
        return wrapData(pageIndex, pageSize, (totalCount - 1) / pageSize + 1, totalCount, result);
    }

    /**
     * 导出出货报表数据
     *
     * @param loginUser
     * @param salesId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public RemoteData<OrderReportItem> findItemByCheckDate(User loginUser, long salesId, String dateStart, String dateEnd) {

        List<String> salesNos = null;
        //查询所有
        OrderAuth orderAuth = orderAuthRepository.findFirstByUser_IdEquals(loginUser.id);
        if (orderAuth != null && !StringUtils.isEmpty(orderAuth.relatedSales)) {
            salesNos = userService.findUserCodes(loginUser.id, salesId, orderAuth.relatedSales);
        }


        if (salesNos == null || salesNos.size() == 0) return wrapData();

        List<OrderItem> orderItems = orderItemRepository.findByVerifyDateGreaterThanEqualAndVerifyDateLessThanEqual(dateStart, dateEnd);


        List<OrderReportItem> items = new ArrayList<>();
        for (OrderItem orderitem : orderItems) {
            Order order = orderRepository.findFirstByOsNoEquals(orderitem.osNo);
            if (order != null) {
                //所有人并且是管理员的 所有记录都通过。 否则只接受指定业务员记录
                if ((salesId == -1 && loginUser.isAdmin()) || salesNos.indexOf(order.sal_no) > -1) {
                    OrderReportItem orderReportItem = new OrderReportItem();
                    bindReportData(orderReportItem, orderitem, order);

                    //读取

                    String id_no = repository.findId_noByOrderItem(orderitem.osNo, orderitem.itm);
                    orderReportItem.id_no = id_no;
                    orderReportItem.thumbnail = orderReportItem.url = com.giants3.hd.server.utils.FileUtils.getErpProductPictureUrl(id_no, "");


                    items.add(orderReportItem);
                }

            }
        }


        return wrapData(items);
    }

    /**
     * 绑定生成报表数据
     *
     * @param orderReportItem
     * @param orderItem
     * @param order
     */
    private void bindReportData(OrderReportItem orderReportItem, OrderItem orderItem, Order order) {
        orderReportItem.cus_no = order.cus_no;
        orderReportItem.os_no = order.osNo;
        orderReportItem.cus_prd_no = orderItem.batNo;
        orderReportItem.qty = orderItem.qty;
        orderReportItem.prd_no = orderItem.prdNo;

        orderReportItem.thumbnail = orderItem.thumbnail;
        orderReportItem.sendDate = orderItem.sendDate;
        orderReportItem.verifyDate = orderItem.verifyDate;
        orderReportItem.unit = orderItem.ut;

        orderReportItem.saleName = order.sal_name + " " + order.sal_cname;


    }


    /**
     * 更新d订单附件
     * 将附件从临时文件转移到附件文件夹下
     */
    public void updateAttachFiles() {


        Page<Order> page;
        Pageable pageable = constructPageSpecification(0, 100);
        page = orderRepository.findAll(pageable);
        handleList(page.getContent());

        while (page.hasNext()) {
            pageable = page.nextPageable();
            page = orderRepository.findAll(pageable);
            List<Order> products = page.getContent();
            handleList(products);

        }


    }

    private void handleList(List<Order> products) {


        for (Order order : products) {

            String newAttaches = AttachFileUtils.getNewAttaches(order.attaches, attachfilepath, tempFilePath, AttachFileUtils.ORDER_ATTACH_PREFIX + order.osNo);
            if (!newAttaches.equals(order.attaches)) {
                order.attaches = newAttaches;
                orderRepository.save(order);

            }

        }


        productRepository.flush();

    }


    /**
     * 返回下一节点的 节点名称
     *
     * @return
     */
    private String[] getNextWorkFlow(int currentWorkFlowStep, String currentProductType, OrderItemWorkFlow orderItemWorkFlow) {

        String[] nextWorkFlow = new String[2];


        String[] workFlowSteps = StringUtils.split(orderItemWorkFlow.workFlowSteps);
        String[] workFlowNames = StringUtils.split(orderItemWorkFlow.workFlowNames);

        int flowStepIndex = StringUtils.index(workFlowSteps, String.valueOf(currentWorkFlowStep));
        int flowStepCount = workFlowSteps.length;
        if (flowStepIndex >= flowStepCount - 1)
            return null;
        String[] configs = StringUtils.split(orderItemWorkFlow.configs);
        String[] productTypes = orderItemWorkFlow.productTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);

        int productTypeIndex = StringUtils.index(productTypes, currentProductType);
        if (productTypeIndex > -1) {


            nextWorkFlow = new String[]{workFlowSteps[flowStepIndex + 1], workFlowNames[flowStepIndex + 1]};

        } else {


            for (int i = flowStepIndex + 1; i < flowStepCount; i++) {

                String[] configArray = StringUtils.split(configs[i], StringUtils.STRING_SPLIT_COMMA);
                if ("1".equals(configArray[productTypeIndex])) {

                    nextWorkFlow = new String[]{workFlowSteps[i], workFlowNames[i]};
                    break;
                }

            }


        }


        return nextWorkFlow;


    }


    /**
     * 审核订单的传递数据 审核结束， 订单进入下一流程
     *
     * @param loginUser
     * @param messageId
     */
    @Transactional
    public RemoteData<Void> checkOrderItemWorkFlow(User loginUser, long messageId) {

//        WorkFlowMessage message = workFlowMessageRepository.findOne(messageId);
//        if (message == null) {
//            return wrapError("消息不存在：" + messageId);
//        }
//        if (message.state != WorkFlowMessage.STATE_RECEIVE) {
//            return wrapError("当前消息的状态不对，必须是已接收未审核的");
//        }
//
//
//        WorkFlow workFlow = workFlowRepository.findFirstByFlowStepEquals(message.toFlowStep);
//        if (workFlow == null) {
//            return wrapError("目标流程不存在" + message.toFlowStep);
//        }
//
//
//        OrderItem orderItem = orderItemRepository.findOne(message.orderItemId);
//        if (orderItem == null) {
//            return
//                    wrapError("未找到该订单货款 :" + message.orderName + "   " + message.productName);
//        }
//
//        OrderItemWorkFlow workFlowOrderItem = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(message.orderItemId);
//        if (workFlowOrderItem == null) {
//            return
//                    wrapError("未找到该货款流程数据 :" + message.orderName + "   " + message.productName);
//        }
//        //人员验证
//        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndWorkFlowCodeEqualsAndReceiveEquals(loginUser.id,"", true);
//        boolean canOperate = workFlowWorker != null;
//
//
//        message.state = WorkFlowMessage.STATE_PASS;
//        message.checkTime = Calendar.getInstance().getTimeInMillis();
//        message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
//
//
//        orderItemWorkFlowRepository.save(workFlowOrderItem);
//
//
//        workFlowMessageRepository.save(message);


        return wrapData();

    }


    /**
     * 获取生产流程表
     *
     * @return
     */
    public RemoteData<WorkFlow> getWorkFlowList() {


        List<WorkFlow> workFlows = workFlowRepository.findAll();
        return wrapData(workFlows);
    }

    /**
     * 保存生产流程表
     *
     * @return
     */
    @Transactional
    public RemoteData<WorkFlow> saveWorkFlowList(List<WorkFlow> workFlowList) {


        for (WorkFlow workFlow : workFlowList) {
            if (workFlow.id > 0)
                workFlowRepository.save(workFlow);
        }

        return getWorkFlowList();

    }
    /**
     * 获取指定用户未处理的消息。
     *
     * @param loginUser
     * @return
     */
    public RemoteData<WorkFlowMessage> getUnHandleWorkFlowMessage1(User loginUser,String key) {
        List<WorkFlowWorker> workFlowWorkers = workFlowWorkerRepository.findByUserIdEqualsAndReceiveEquals(loginUser.id, true);


        if (!ArrayUtils.isNotEmpty(workFlowWorkers)) return wrapData();

        int size = workFlowWorkers.size();
        int[] flowSteps = new int[size];
        for (int i = 0; i < size; i++) {
            WorkFlowWorker workFlow = workFlowWorkers.get(i);
            flowSteps[i] = workFlow.workFlowStep;
        }
        int[] state = new int[]{WorkFlowMessage.STATE_SEND, WorkFlowMessage.STATE_REWORK}; //WorkFlowMessage.STATE_RECEIVE,
        //读取未处理的 就是 receiverId=0
        String keyLike=StringUtils.sqlLike(key);
        List<WorkFlowMessage> workFlowMessages = workFlowMessageRepository.findMyUnHandleWorkFlowMessages2(state ,flowSteps,loginUser.id,keyLike);

        return wrapData(workFlowMessages );

    }

    /**
     * 获取指定用户未处理的消息。
     *
     * @param loginUser
     * @return
     */
    public RemoteData<WorkFlowMessage> getUnHandleWorkFlowMessage(User loginUser,String key) {


         return getUnHandleWorkFlowMessage1(loginUser,key);



    }

    /**
     * 未处理的流程消息数量
     *
     * @param loginUser
     * @return
     */
    public int getUnHandleWorkFlowMessageCount(User loginUser) {


        RemoteData<WorkFlowMessage> remoteData = getUnHandleWorkFlowMessage1(loginUser,"");


        return remoteData.totalCount;

    }

    /**
     * 获取指定用户发送的流程消息列表
     *
     * @param loginUser
     * @return
     */

    public RemoteData<WorkFlowMessage> getSendWorkFlowMessageList(User loginUser) {


        List<WorkFlowWorker> workFlowWorkers = workFlowWorkerRepository.findByUserIdEqualsAndSendEquals(loginUser.id, true);

        if (!ArrayUtils.isNotEmpty(workFlowWorkers)) return wrapData();


        final int size = workFlowWorkers.size();
        int[] steps = new int[size];
        for (int i = 0; i < size; i++) {
            steps[i] = workFlowWorkers.get(i).workFlowStep;

        }

        return wrapData(workFlowMessageRepository.findByFromFlowStepInOrderByCreateTimeDesc(steps));
    }


    public RemoteData<ErpOrderItemProcess> getUnCompleteOrderItem(User user) {


        List<ErpOrderItemProcess> orderItems = erpOrderItemProcessRepository.findUnCompleteProcesses();


        return wrapData(orderItems);


    }


    public RemoteData<OrderItem> searchOrderItem(String key, int pageIndex, int pageSize) {
        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        String keyForSearch = "%" + key.trim() + "%";
        Page<OrderItem> pageValue = orderItemRepository.findByOsNoLikeOrPrdNoLikeOrderByOsNoDesc(keyForSearch, keyForSearch, pageable);
        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), pageValue.getContent());
    }

    /**
     * @param orderItemWorkFlowId
     * @return
     */
    @Transactional
    public RemoteData<Void> cancelOrderWorkFlow(User loginUser, long orderItemWorkFlowId) {


        return wrapData();


    }


    public RemoteData<WorkFlowMessage> getWorkFlowMessageByOrderItem(User user, String osNo, int itm) {


        return wrapData(workFlowMessageRepository.findByOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(osNo, itm));

    }

    public RemoteData<WorkFlowMessage> getMyWorkFlowMessage(User user, String key, int pageIndex, int pageSize) {

        Pageable pageable=constructPageSpecification(pageIndex,pageSize,null);
        final Page<WorkFlowMessage> page = workFlowMessageRepository.findMyWorkFlowMessages(user.id, StringUtils.sqlLike(key), pageable);
        return wrapData(pageIndex,page);

    }


}
