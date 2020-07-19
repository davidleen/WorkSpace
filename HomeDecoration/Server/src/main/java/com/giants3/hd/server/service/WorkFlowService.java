package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.CompanyPosition;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.WorkFlowMemoAuth;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service_third.MessagePushService;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 生产流程管理业务处理类
 * Created by david on 2016/2/15.
 */
@Service
public class WorkFlowService extends AbstractService implements InitializingBean, DisposableBean {

    private static final Logger logger = Logger.getLogger(WorkFlowService.class);
    @Autowired
    ErpPrdtService erpPrdtService; ;
    @Autowired
    ErpWorkService erpWorkService; ;
    @Autowired
    ProductService productService;

    @Autowired
    MessagePushService messagePushService;


    @Autowired
    private WorkFlowSubTypeRepository workFlowSubTypeRepository;

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private QuoteAuthRepository quoteAuthRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    OrderItemRepository orderIemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WorkFlowRepository workFlowRepository;


    @Autowired
    OrderItemWorkFlowRepository orderItemWorkFlowRepository;


    @Autowired
    WorkFlowProductRepository workFlowProductRepository;
    @Autowired
    WorkFlowWorkerRepository workFlowWorkerRepository;
    @Autowired
    WorkFlowArrangerRepository workFlowArrangerRepository;
    @Autowired
    WorkFlowMessageRepository workFlowMessageRepository;


    @Autowired
    WorkFlowEventRepository workFlowEventRepository;


    @Autowired
    WorkFlowEventWorkerRepository workFlowEventWorkerRepository;


    @Autowired
    WorkFlowAreaRepository workFlowAreaRepository;
    @Autowired
    OrderItemWorkMemoRepository orderItemWorkMemoRepository;
    @Autowired
    WorkFlowTimeLimitRepository workFlowTimeLimitRepository;



    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


    /**
     * 获取所有流程数据
     *
     * @return
     */
    public List<WorkFlow> getAllWorkFlow() {
        return workFlowRepository.findAll();
    }


    /**
     * 获取配置的二级流程类型数据
     *
     * @return
     */
    public RemoteData<WorkFlowSubType> getWorkFlowSubTypes() {

        return wrapData(workFlowSubTypeRepository.findAll());
    }

    /**
     * 初始数据配置
     */
    public void initData() {


        logger.info("开始流程相关数据初始化");


        if (workFlowSubTypeRepository.count() == 0) {

            int size = WorkFlowSubType.TYPES.length;
            for (int i = 0; i < size; i++) {

                WorkFlowSubType workFlowSubType = new WorkFlowSubType();
                workFlowSubType.typeId = WorkFlowSubType.TYPES[i];
                workFlowSubType.typeName = WorkFlowSubType.TYPENAMES[i];
                workFlowSubTypeRepository.save(workFlowSubType);
            }
        }


        // 生产流程数据初始化

        List<WorkFlow> workFlows = workFlowRepository.findAll();
        if (workFlows.size() == 0) {
            workFlowRepository.save(WorkFlow.initWorkFlowData());
            productService.setDefaultWorkFlowIds();

        }


        logger.info("流程相关数据初始化结束");

    }


    /**
     * 根据产品id查询 流程配置信息
     *
     * @param productId
     * @return
     */
    public RemoteData<WorkFlowProduct> findWorkFlowByProductId(long productId) {

        List<WorkFlowProduct> workFlowProducts = workFlowProductRepository.findByProductIdEquals(productId);
        WorkFlowProduct workFlowProduct;
        if (workFlowProducts != null && workFlowProducts.size() > 0) {
            workFlowProduct = workFlowProducts.get(0);
        } else {
            workFlowProduct = new WorkFlowProduct();
            workFlowProduct.productId = productId;
            Product product = productService.findProductById(productId);
            if (product != null) {
                workFlowProduct.productName = product.name;
                workFlowProduct.productPVersion = product.pVersion;
            }
        }

        return wrapData(workFlowProduct);

    }

    @Transactional
    public RemoteData<WorkFlowProduct> saveWorkFlowProduct(WorkFlowProduct workFlowProduct) {

        if (workFlowProduct.productId <= 0) return wrapError("产品不存在");

        return wrapData(workFlowProductRepository.save(workFlowProduct));

    }

    /**
     * 保存订单流程配置
     *
     * @param orderItemWorkFlow
     */
    @Transactional(rollbackFor = {HdException.class})
    public void saveOrderItemWorkFlow(OrderItemWorkFlow orderItemWorkFlow) {


    }


    /**
     * 排厂自动发送流程信息
     */
    private void sendMessageFromStart() {
    }

    public RemoteData<OrderItemWorkFlow> findWorkFlowByOrderItemId(long orderItemId) {

        OrderItemWorkFlow orderItemWorkFlow = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(orderItemId);
        return wrapData(orderItemWorkFlow);
    }


    /**
     * 保存产品排厂类型
     *
     * @param workFlowSubTypes
     * @return
     */
    @Transactional
    public RemoteData<WorkFlowSubType> saveSubTypes(List<WorkFlowSubType> workFlowSubTypes) {


        for (WorkFlowSubType workFlowSubType : workFlowSubTypes) {

            if (workFlowSubType.id <= 0) {

                if (workFlowSubType.typeId <= 0 || workFlowSubTypeRepository.findFirstByTypeIdEquals(workFlowSubType.typeId) != null) {

                    return wrapError("类型Id,不能为空，且不能相同");
                }
            }

        }

        return wrapData(workFlowSubTypeRepository.save(workFlowSubTypes));


    }


    public RemoteData<OrderItemWorkFlow> getOrderItemWorkFlow(long orderItemId) {
        OrderItemWorkFlow orderItemWorkFlow = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(orderItemId);
        if (orderItemWorkFlow == null) return wrapData();
        return wrapData(orderItemWorkFlow);
    }

    public RemoteData<WorkFlowWorker> findWorkers() {


        List<WorkFlowWorker> workFlowWorkers = workFlowWorkerRepository.findByUserNameLikeOrderByUserNameAscWorkFlowStepAsc(StringUtils.sqlLike(""));
        return wrapData(workFlowWorkers);


    }

    @Transactional
    public RemoteData<WorkFlowWorker> saveWorker(WorkFlowWorker workFlowWorker) {


        if (workFlowWorker.userId <= 0) return wrapError("未选择用户");
        if (StringUtils.isEmpty(workFlowWorker.workFlowName)) return wrapError("未选择流程");

        return wrapData(workFlowWorkerRepository.save(workFlowWorker));

    }

    public RemoteData<WorkFlowArranger> saveArranger(WorkFlowArranger workFlowWorker) {
        return wrapData(workFlowArrangerRepository.save(workFlowWorker));
    }

    public RemoteData<WorkFlowArranger> findArrangers() {


        return wrapData(workFlowArrangerRepository.findAll());
    }

    public RemoteData<Void> deleteWorkFlowArranger(long id) {
        workFlowArrangerRepository.delete(id);
        return wrapData();
    }

    public RemoteData<Void> deleteWorkFlowWorker(long id) {


        workFlowWorkerRepository.delete(id);
        return wrapData();
    }

    public List<WorkFlowWorker> getWorkFlowWorkers(long userId) {


        return workFlowWorkerRepository.findByUserIdEquals(userId);


    }

    public WorkFlowArranger getWorkFlowArranger(long userId) {


        return workFlowArrangerRepository.findFirstByUserIdEquals(userId);

    }

    public RemoteData<WorkFlowMessage> getOrderItemWorkFlowMessage(User loginUser, String os_no, int itm, int workFlowStep) {


        List<WorkFlowMessage> messages = workFlowMessageRepository.findByToFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(workFlowStep, os_no, itm);


        return wrapData(messages);


    }

    public RemoteData<WorkFlowMessage> getOrderItemWorkFlowFromMessage(User loginUser, String os_no, int itm, int fromWorkFlowStep) {


        List<WorkFlowMessage> messages = workFlowMessageRepository.findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(fromWorkFlowStep, os_no, itm);


        return wrapData(messages);


    }

    public RemoteData<WorkFlowEvent> findWorkFlowEvents() {


        List<WorkFlowEvent> result = workFlowEventRepository.findAll();
        return wrapData(result);


    }

    public RemoteData<WorkFlowEventWorker> findWorkFlowEventWorkers() {


        List<WorkFlowEventWorker> result = workFlowEventWorkerRepository.findAll();
        return wrapData(result);


    }


    public RemoteData<WorkFlowArea> findWorkFlowAreas() {
        return wrapData(workFlowAreaRepository.findAll());
    }

    public RemoteData<WorkFlowArea> saveWorkFlowArea(WorkFlowArea data) {


        return wrapData(workFlowAreaRepository.save(data));


    }

    public RemoteData<WorkFlowArea> deleteWorkFlowArea(long id) {


        workFlowAreaRepository.delete(id);
        return wrapData();


    }

    /**
     * 获取节点修改权限
     *
     * @param user
     * @return
     */
    public RemoteData<WorkFlowMemoAuth> getWorkFlowMemoAuth(User user) {


        final List<ErpWorkFlow> erpRealWorkFlows = ErpWorkFlow.erpRealWorkFlows;
        int size = erpRealWorkFlows.size();
        //只有厂长有审核权限
        boolean checkable = user.position == CompanyPosition.FACTORY_DIRECTOR_CODE;
        //管理人员可以修改
        boolean modifiable = user.position == CompanyPosition.WORK_FLOW_MANAGER_CODE;


        List<WorkFlowMemoAuth> workFlowMemoAuths = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            WorkFlowMemoAuth workFlowMemoAuth = new WorkFlowMemoAuth();

            ErpWorkFlow erpWorkFlow = erpRealWorkFlows.get(i);
            workFlowMemoAuth.workFlowStep = erpWorkFlow.step;
            workFlowMemoAuth.checkable = checkable;


            //流程节点人员 也可以修改
            List<WorkFlowWorker> workFlowWorkers = workFlowWorkerRepository.findByUserIdEqualsAndWorkFlowStepEquals(user.id, erpWorkFlow.step);

            //
            workFlowMemoAuth.modifiable = modifiable || (workFlowWorkers != null && workFlowWorkers.size() > 0);
            workFlowMemoAuths.add(workFlowMemoAuth);

        }

        return wrapData(workFlowMemoAuths);


    }



    public WorkFlowMessage findMessageById(long id)
    {

        final WorkFlowMessage one = workFlowMessageRepository.findOne(id);
        return one;


    }

    public RemoteData<WorkFlowMemoAuth> checkWorkFlowMemo(User user, long orderItemWorkMemoId, boolean check) {


        OrderItemWorkMemo orderItemWorkMemo = orderItemWorkMemoRepository.findOne(orderItemWorkMemoId);
        if (orderItemWorkMemo == null)
            return wrapError("备注信息未保存");


        orderItemWorkMemo.checked = check;


        orderItemWorkMemo.lastCheckerId = user.id;
        orderItemWorkMemo.lastCheckerName = user.toString();
        orderItemWorkMemo.lastCheckTime = Calendar.getInstance().getTimeInMillis();
        orderItemWorkMemo.lastCheckTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        orderItemWorkMemoRepository.save(orderItemWorkMemo);


        return wrapData();


    }


    public void initWorkFlowLimit() {

        if (workFlowTimeLimitRepository.count() == 0) {

            int orderTypeCount = WorkFlowTimeLimit.ORDER_ITEM_TYPES.length;
            int workFlowCount = WorkFlowTimeLimit.COMBINED_WORK_FLOW_STEPS.length;
            for (int i = 0; i < orderTypeCount; i++) {


                WorkFlowTimeLimit workFlowTimeLimit = new WorkFlowTimeLimit();
                workFlowTimeLimit.orderItemType = WorkFlowTimeLimit.ORDER_ITEM_TYPES[i];
                workFlowTimeLimit.orderItemTypeName = WorkFlowTimeLimit.ORDER_ITEM_TYPE_NAMES[i];


                workFlowTimeLimitRepository.save(workFlowTimeLimit);


            }


        }


    }


    public List<WorkFlowTimeLimit> getWorkFlowLimit() {


        return workFlowTimeLimitRepository.findAll();

    }

    /**
     * 保存工期数据
     *
     * @param workFlowLimit
     * @return
     */
    @Transactional
    public RemoteData<Void> saveWorkFlowLimit(List<WorkFlowTimeLimit> workFlowLimit,boolean updateCompletedOrderItem ) {


        for (WorkFlowTimeLimit workFlowTimeLimit : workFlowLimit) {
            if (workFlowTimeLimit.id <= 0) return wrapError("数据异常");

        }


        workFlowTimeLimitRepository.save(workFlowLimit);

        erpWorkService.updateAllProducingWorkFlowReports();

        if (updateCompletedOrderItem) {
            erpWorkService.updateCompleteWorkFlowReports();
        }


        return wrapData();
    }


    /**
     * 调整workflowmessage 的  mrpType prdType
     */
    public void adjustWorkFlowMessage() {


        List<WorkFlowMessage> workFlowMessages=workFlowMessageRepository.findByMrpTypeIsNullOrPrdTypeIsNull();

        for(WorkFlowMessage workFlowMessage:workFlowMessages)
        {

            try {
                workFlowMessage.mrpType =StringUtils.isEmpty(workFlowMessage.mrpNo)?"": workFlowMessage.mrpNo.substring(1, 2);

                final String idx1ByPrdno = erpPrdtService.findIdx1ByPrdno(workFlowMessage.productName);
                workFlowMessage.prdType =StringUtils.isEmpty(idx1ByPrdno)?"": idx1ByPrdno.substring(0, 2);
                workFlowMessageRepository.saveAndFlush(workFlowMessage);
            }catch (Throwable t){t.printStackTrace();}
        }


          workFlowMessages=workFlowMessageRepository.findByProduceTypeNameIsNull();

        for(WorkFlowMessage workFlowMessage:workFlowMessages)
        {

            ErpOrderItem erpOrderItem = erpWorkService.findOrderItem(workFlowMessage.orderName, workFlowMessage.itm);

            try {
                workFlowMessage.produceType =erpOrderItem.produceType;
                workFlowMessage.produceTypeName =erpOrderItem.produceTypeName;
                workFlowMessageRepository.saveAndFlush(workFlowMessage);
            }catch (Throwable t){t.printStackTrace();}
        }



    }
    /**
     * 每天早上8点，遍历所有消息， 检查超过24小时的未处理测消息 并提醒。
     */
    @Scheduled(cron = "0 0 8 * * ?")
//	@Scheduled(fixedDelay = 300)
    public void alertUnHandleMessage() {



        //查找所有未处理的非白配制作阶段的消息，
        List<WorkFlowMessage> workFlowMessages=workFlowMessageRepository.findByFromFlowStepNotAndReceiverIdEqualsOrderByCreateTimeDesc(ErpWorkFlow.FIRST_STEP,0)  ;

        //测试数据
        String  combineTokens="AkzRgFtIxZUsuWHvYZTTtIzzLrJ9BC4XYXIvFN47eFSZ,AqNBFp1-NZwdQvWGiRdQCP4_q-4TddbU_P5xIqA1azmr,AqNBFp1-NZwdQvWGiRdQCP50nPRD47JPGFUvIxVMYhrY";
        if(workFlowMessages.size()>0)
            messagePushService.sendMessageToDevice(combineTokens,workFlowMessages.get(0));

        for (WorkFlowMessage message:workFlowMessages)
        {
            //异常数据
            if(message.receiveTime!=0||!StringUtils.isEmpty(message.receiveTimeString)) continue;
            //如果消息存在不超过24小时，不提醒
            long time=Calendar.getInstance().getTimeInMillis();
            if(time-message.createTime<24l*60*60*1000) continue;
            messagePushService.pushMessage(message);



        }





    }


    /**
     *
     * @param hourLimit 未处理的最少时长。小时为单位
     * @return
     */
    public List<WorkFlowMessage> getUnHandleWorkFlowMessageReport(int hourLimit) {

        //查找所有未处理的非白配制作阶段的消息，
        List<WorkFlowMessage> workFlowMessages=workFlowMessageRepository.findByFromFlowStepNotAndReceiverIdEqualsOrderByCreateTimeDesc(ErpWorkFlow.FIRST_STEP,0)  ;


        List<WorkFlowMessage> result=new ArrayList<>();
        long time = Calendar.getInstance().getTimeInMillis();
        for (WorkFlowMessage message:workFlowMessages) {

            //异常数据
            if(message.receiveTime!=0||!StringUtils.isEmpty(message.receiveTimeString)) continue;
            //未超过时限
            if (time - message.createTime < hourLimit * 60l * 60 * 1000) continue;

            result.add(message);
        }


        return result;

    }

    public List<WorkFlowMessage> getWorkFlowMessageReport(String dateStart, String dateEnd, boolean unhandle, boolean overdue) {


        long currentTime=Calendar.getInstance().getTimeInMillis();
        long limitMillis=24l*60*60*1000;
        List<WorkFlowMessage> workFlowMessages=workFlowMessageRepository.queryWorkFlowMessageReport(  dateStart,   dateEnd,unhandle,overdue,currentTime,limitMillis)  ;//,   unhandle,   overdue,currentTime
        return workFlowMessages;
    }

    public List<WorkFlowMessage> getNeedConfirmWorkFLowMessage(User user, String os_no, int itm, int workFlowStep,int produceType) {

       ;
        List<WorkFlowMessage> result=new ArrayList<>();
        List<WorkFlowMessage> messages = workFlowMessageRepository.findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(workFlowStep, os_no, itm);

        //当前流程发出的未处理消息，并且当前用户负责目标流程的接受。则统计数+1

            for (WorkFlowMessage message :messages) {
                if (message.state == WorkFlowMessage.STATE_SEND || message.state == WorkFlowMessage.STATE_REWORK) {
                    WorkFlowWorker worker = workFlowWorkerRepository.findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowCodeEqualsAndReceiveEquals(user.id, produceType, message.toFlowCode, true);

                    //当前用户在目标流程有接收权限workFlowService
                    if (worker != null&&(StringUtils.isEmpty(worker.jghnames)||worker.jghnames.contains(message.nextFlowFactoryName))) {
                        result.add(message);
                    }
                }

        }


        return result;
    }
}
