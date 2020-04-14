package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.entity_erp.ErpWorkFlowItem;
import com.giants3.hd.entity_erp.Sub_workflow_state;
import com.giants3.hd.entity_erp.WorkFlowMaterial;
import com.giants3.hd.entity_erp.Zhilingdan;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.*;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.repository_erp.ErpStockSubmitRepository;
import com.giants3.hd.server.repository_erp.ErpWorkFlowRepository;
import com.giants3.hd.server.repository_erp.ErpWorkRepository;
import com.giants3.hd.server.service_third.MessagePushService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.StringUtils;
import de.greenrobot.common.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * 订单生产流程， 物料采购流程相关业务
 * Created by davidleen29 on 2017/3/9.
 */
@Service
public class ErpWorkService extends AbstractErpService {

    public static final String CATEGORY = "workflows";
    @Value("${rootpath}")
    private String rootpath;

    @Value("${rootUrl}")
    private String rootUrl;
    @Autowired
    ErpWorkRepository erpWorkRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ErpWorkFlowRepository erpWorkFlowRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ErpStockSubmitRepository erpStockSubmitRepository;
    @Autowired
    ErpPrdtService erpPrdtService;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    WorkFlowMessageRepository workFlowMessageRepository;
    @Autowired
    WorkFlowTimeLimitRepository workFlowTimeLimitRepository;


    @Autowired
    ProductWorkMemoRepository productWorkMemoRepository;
    @Autowired
    WorkFlowAreaRepository workFlowAreaRepository;

    @Autowired
    OrderItemWorkMemoRepository orderItemWorkMemoRepository;

    private Date today;


    @Autowired
    ErpWorkFlowReportRepository erpWorkFlowReportRepository;
    @Autowired
    MessagePushService messagePushService;
//    @Autowired
//    OrderItemRepository orderItemRepository;

    @Autowired
    OrderItemWorkStateRepository orderItemWorkStateRepository;

    @Autowired
    WorkFlowWorkerRepository workFlowWorkerRepository;
    @Autowired
    ErpOrderItemProcessRepository erpOrderItemProcessRepository;
//    //流程排序
//    private Comparator<ErpOrderItemProcess> comparator    = new Comparator<ErpOrderItemProcess>() {
//        @Override
//        public int compare(ErpOrderItemProcess o1, ErpOrderItemProcess o2) {
//            int o1Index= ErpWorkFlow.findIndexByCode(o1.mrp_no.substring(0,1));
//            int o2Index=ErpWorkFlow.findIndexByCode(o2.mrp_no.substring(0,1));
//            return o1Index-o2Index;
//        }
//    };;


    @Override
    protected void onEntityManagerCreate(EntityManager manager) {


        today = Calendar.getInstance().getTime();
    }

    public RemoteData<Zhilingdan> searchZhilingdan(String osName, String startDate, String endDate) {


        final List<Zhilingdan> datas = erpWorkRepository.searchZhilingdan(osName, startDate, endDate);

        for (Zhilingdan zhilingdan : datas) {


            zhilingdan.isCaigouOverDue = isCaigouOverDue(zhilingdan);
            zhilingdan.isJinhuoOverDue = isJinhuoOverDue(zhilingdan);

            //日期截断处理

            zhilingdan.caigou_dd = StringUtils.clipSqlDateData(zhilingdan.caigou_dd);

            zhilingdan.jinhuo_dd = StringUtils.clipSqlDateData(zhilingdan.jinhuo_dd);

            zhilingdan.mo_dd = StringUtils.clipSqlDateData(zhilingdan.mo_dd);


        }
        return wrapData(datas);
    }


    /**
     * 采购超期判断
     *
     * @param zhilingdan
     * @return
     */
    private boolean isCaigouOverDue(Zhilingdan zhilingdan) {

        if (StringUtils.isEmpty(zhilingdan.mo_no)) return false;
        if (zhilingdan.need_dd == 0) return false;
        Date mmDate = null;
        try {
            mmDate = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.mo_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (mmDate == null) return false;
        Date caigouDd = today;
        try {
            caigouDd = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.caigou_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }


        int day = de.greenrobot.common.DateUtils.getDayDifference(mmDate.getTime(), caigouDd.getTime());

        return day > zhilingdan.need_dd;


    }

    /**
     * 进货超期判断
     *
     * @param zhilingdan
     * @return
     */
    private boolean isJinhuoOverDue(Zhilingdan zhilingdan) {
        if (StringUtils.isEmpty(zhilingdan.mo_no)) return false;
        if (zhilingdan.need_days == 0) return false;


        Date mmDate = null;
        try {
            mmDate = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.mo_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (mmDate == null) return false;

        Date jinhuoDd = today;
        try {
            jinhuoDd = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.jinhuo_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }


        int day = de.greenrobot.common.DateUtils.getDayDifference(mmDate.getTime(), jinhuoDd.getTime());


        //进货单超期 要大于
        return day > zhilingdan.need_days + zhilingdan.need_dd;

    }


//    /**
//     * 查找指定订单的排厂数据
//     *
//     * @param os_no
//     * @return
//     */
//
//    public List<ErpOrderItemProcess> findOrderItemProcess(String os_no, String prd_no, String pVersion) {
//
//
//        return erpWorkRepository.findOrderItemProcesses(os_no, prd_no, pVersion);
//
//
//    }


    public RemoteData<ErpWorkFlowReport> findErpWorkFlowReport(String os_no, int itm) {
        ErpOrderItem erpOrderItem = erpWorkRepository.findOrderItem(os_no, itm);
        if (erpOrderItem == null) return wrapError("未查找到订单货款:" + os_no + ",item=" + itm);


        if (erpOrderItem.produceType == ProduceType.NOT_SET) {
            return wrapError("该订单未排厂");

        }

        RemoteData<ErpWorkFlowReport> erpWorkFlowReport = findErpWorkFlowReport(erpOrderItem);


        return erpWorkFlowReport;
    }

    public RemoteData<ErpWorkFlowReport> findErpWorkFlowReport(ErpOrderItem erpOrderItem) {

        return findErpWorkFlowReport(erpOrderItem.os_no, erpOrderItem.itm, erpOrderItem.prd_no, erpOrderItem.pVersion, erpOrderItem.url, erpOrderItem.produceType, erpOrderItem.produceTypeName);
    }

    /**
     * 查找xxxx的进度报表
     */
    public RemoteData<ErpWorkFlowReport> findErpWorkFlowReport(String os_no, int itm, String prd_no, String pVersion, String url, int produceType, String produceTypeName) {


        //查询本地数据库的报表记录
        final List<ErpWorkFlowReport> erpWorkFlowReports = erpWorkFlowReportRepository.findByOsNoEqualsAndItmEquals(os_no, itm);


        //本地不存在 查询erp。
        if (ArrayUtils.isEmpty(erpWorkFlowReports)) {


            //查詢是否排廠
            if (produceType == ProduceType.SELF_MADE) {
                //内厂的排厂数据处理

                List<ErpOrderItemProcess> processes = erpWorkRepository.findOrderItemProcesses(os_no, itm, true);

                if (ArrayUtils.isEmpty(processes))
                    return wrapError("该订单未排厂");


                //erp 排厂数据整合临时数据  已经排厂，未进行流程活动， 默认进度都是0，进行过流程活动了， 才保存在 ErpWorkFlowReport 表中。


                //不以abcd开头的process 数据，就是成品数据


                boolean hasZuzhuang = false;
                for (ErpOrderItemProcess process : processes) {
                    if (process.mrpNo.startsWith(ErpWorkFlow.CODE_ZUZHUANG)) {
                        hasZuzhuang = true;
                        break;

                    }

                }

                //从指令单集合中，判断排厂类型
                ProductType productType = erpPrdtService.getProductTypeFromOrderItemProcess(processes);
                for (ErpWorkFlow erpWorkFlow : ErpWorkFlow.WorkFlows) {


                    //判断排厂中是否有当前流程
                    boolean hasThisFlow = false;
                    ErpOrderItemProcess findProcess = null;


                    Set<String> typeSet = new HashSet<>();
                    for (ErpOrderItemProcess process : processes) {


                        if (process.mrpNo.startsWith(erpWorkFlow.code)) {
                            findProcess = process;
                            typeSet.add(process.mrpNo);

                            hasThisFlow = true;

                        }
                    }

                    if (!hasThisFlow) continue;
                    ErpWorkFlowReport erpWorkFlowReport = new ErpWorkFlowReport();
                    erpWorkFlowReport.workFlowCode = erpWorkFlow.code;

                    //包装类型， 检查是否有组装
                    if (ErpWorkFlow.CODE_BAOZHUANG.equals(erpWorkFlow.code)) {


                        erpWorkFlowReport.workFlowName = (hasZuzhuang ? ErpWorkFlow.NAME_ZUZHUANG : "") + ErpWorkFlow.NAME_BAOZHUANG;
                    } else {
                        erpWorkFlowReport.workFlowName = erpWorkFlow.name;
                    }

                    erpWorkFlowReport.workFlowStep = erpWorkFlow.step;
                    erpWorkFlowReport.osNo = os_no;
                    erpWorkFlowReport.itm = itm;
                    erpWorkFlowReport.prdNo = findProcess.prdNo;
                    erpWorkFlowReport.pVersion = findProcess.pVersion;
                    erpWorkFlowReport.productUrl = findProcess.photoUrl;
                    erpWorkFlowReport.typeCount = typeSet.size();
                    erpWorkFlowReport.percentage = 0;


                    erpWorkFlowReport.productType = productType.type;
                    erpWorkFlowReport.productTypeName = productType.name;


                    erpWorkFlowReports.add(erpWorkFlowReport);
                }


            } else


                //外购数据
                if (produceType == ProduceType.PURCHASE) {

                    for (ErpWorkFlow erpWorkFlow : ErpWorkFlow.purchaseWorkFLows) {

                        ErpWorkFlowReport erpWorkFlowReport = createErpWorkFlowFromProcess(erpWorkFlow, os_no, itm, prd_no, pVersion, url);
                        erpWorkFlowReports.add(erpWorkFlowReport);
                    }


                }


            for (ErpWorkFlowReport erpWorkFlow : erpWorkFlowReports) {
                erpWorkFlow.produceType = produceType;
                erpWorkFlow.produceTypeName = produceTypeName;

            }


        }


        return wrapData(erpWorkFlowReports);
    }


    /**
     * 构建排外厂的erpworkflow
     *
     * @param erpWorkFlow
     * @return
     */
    private ErpWorkFlowReport createErpWorkFlowFromProcess(ErpWorkFlow erpWorkFlow, String osNo, int itm, String prdNo, String pVersion, String photoUrl) {
        ErpWorkFlowReport erpWorkFlowReport = new ErpWorkFlowReport();
        erpWorkFlowReport.workFlowCode = erpWorkFlow.code;

        erpWorkFlowReport.workFlowStep = erpWorkFlow.step;
        erpWorkFlowReport.workFlowName = erpWorkFlow.name;
        erpWorkFlowReport.osNo = osNo;
        erpWorkFlowReport.itm = itm;
        erpWorkFlowReport.prdNo = prdNo;
        erpWorkFlowReport.pVersion = pVersion;
        erpWorkFlowReport.productUrl = photoUrl;
        erpWorkFlowReport.typeCount = 1;
        erpWorkFlowReport.percentage = 0;


        //外厂产品类型无铁木
        erpWorkFlowReport.productType = ProductType.TYPE_NONE;
        erpWorkFlowReport.productTypeName = ProductType.TYPE_NONE_NAME;
        return erpWorkFlowReport;
    }


    /**
     * 查找 ， 完成的订单款项
     */
    public RemoteData<ErpOrderItem> searchCompleteOrderItems(String key, int pageIndex, int pageSize) {


        int count = erpWorkRepository.getCompleteOrderItemCount(key);
        //配置 url
        final List<ErpOrderItem> erpWorkFlowOrderItems = erpWorkRepository.searchCompleteOrderItems(key, pageIndex, pageSize);


        updatePhotoUrl(erpWorkFlowOrderItems);


        return wrapData(pageIndex, pageSize, (count - 1) / pageSize + 1, count, erpWorkFlowOrderItems);


    }

    /**
     * 查找 ，并且未完成的订单款项  款项包含已下单，未启动流程
     *
     * @param workFlowStep 下单未排  白胚未入  白胚  颜色  包装  产品库
     * @param pageIndex
     * @param pageSize
     */
    public RemoteData<ErpOrderItem> searchUnCompleteOrderItems(String key, int workFlowStep, int pageIndex, int pageSize) {

//配置 url
        final List<ErpOrderItem> erpWorkFlowOrderItems;
        int count;
        if (workFlowStep == -1)//查詢全部
        {


            count = erpWorkRepository.getUnCompleteOrderItemCount(key);

            erpWorkFlowOrderItems = erpWorkRepository.searchUnCompleteOrderItems(key, pageIndex, pageSize);


        } else {
            count = erpWorkRepository.getOrderItemCountOnWorkFlow(key, workFlowStep);
            erpWorkFlowOrderItems = erpWorkRepository.searchUnCompleteOrderItemsOnWorkFlow(key, workFlowStep, pageIndex, pageSize);
        }


        updatePhotoUrl(erpWorkFlowOrderItems);


        return wrapData(pageIndex, pageSize, (count - 1) / pageSize + 1, count, erpWorkFlowOrderItems);


    }

    private void updatePhotoUrl(List<ErpOrderItem> erpWorkFlowOrderItems) {
        for (ErpOrderItem orderItem : erpWorkFlowOrderItems) {


            orderItem.url = FileUtils.getErpProductPictureUrl(orderItem.id_no, String.valueOf(orderItem.photoUpdateTime));
            orderItem.thumbnail = orderItem.url;
        }
    }


    /**
     * 查找已经开启流程活动，并且未完成的订单款项
     */
    public List<ErpOrderItem> searchHasStartWorkFlowUnCompleteOrderItems(String key) {


        //配置 url
        final List<ErpOrderItem> erpWorkFlowOrderItems = erpWorkRepository.searchHasStartWorkFlowUnCompleteOrderItems(key);


        updatePhotoUrl(erpWorkFlowOrderItems);

        return erpWorkFlowOrderItems;


    }

    /**
     * 外购 可用流程处理
     *
     * @param loginUser
     * @param os_no
     * @param itm
     * @param flowStep
     * @return
     */
    public RemoteData<ErpOrderItemProcess> getAvailablePurchaseOrderItemProcess(User loginUser, String os_no, int itm, int flowStep) {


        //外购 流程
        //查找前一个流程
        if (flowStep != ErpWorkFlow.FIRST_STEP)  //第一道 不需要上一道100%完成
        {
            int previousStep = ErpWorkFlow.findPurchasePrevious(flowStep);

            final ErpWorkFlowReport erpWorkFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(os_no, itm, previousStep);
            if (erpWorkFlowReport == null || erpWorkFlowReport.percentage < 1) {
                return wrapError("上一道流程未交接完毕，不能发起交接");
            }

        }


        List<ErpOrderItemProcess> orderItemProcesses = erpWorkRepository.findPurchaseOrderItemProcesses(os_no, itm);


        //下一个节点
        ErpWorkFlow nextFlow = flowStep == ErpWorkFlow.LAST_STEP ? null : ErpWorkFlow.findPurchaseNext(flowStep);
        ErpWorkFlow workFlow = ErpWorkFlow.findPurchaseByStep(flowStep);

        List<ErpOrderItemProcess> result = new ArrayList<>();
        for (ErpOrderItemProcess process : orderItemProcesses) {

            if (!process.mrpNo.startsWith(workFlow.code)) continue;


            process.currentWorkFlowCode = workFlow.code;
            process.currentWorkFlowStep = workFlow.step;
            process.currentWorkFlowName = workFlow.name;
            process.unSendQty = process.qty;


            ErpOrderItemProcess localProcess = erpOrderItemProcessRepository.findFirstByOsNoEqualsAndItmEqualsAndMoNoEqualsAndMrpNoEquals(process.osNo, process.itm, process.moNo, process.mrpNo);
            if (localProcess != null)
                attachData(process, localProcess);

            process.nextWorkFlowCode = nextFlow == null ? "" : nextFlow.code;
            process.nextWorkFlowStep = nextFlow == null ? 0 : nextFlow.step;
            process.nextWorkFlowName = nextFlow == null ? "" : nextFlow.name;
            if (process.unSendQty > 0)
                result.add(process);
        }


        return wrapData(result);
    }

    /**
     * 查找指定节点可发送的订单流程数据
     *
     * @param os_no
     * @param itm
     * @param flowStep
     * @return
     */
    public RemoteData<ErpOrderItemProcess> getAvailableOrderItemProcess(User loginUser, String os_no, int itm, int flowStep) {
        ErpOrderItem erpOrderItem = erpWorkRepository.findOrderItem(os_no, itm);
        return getAvailableOrderItemProcess(loginUser, erpOrderItem, flowStep);
    }

    /**
     * 查找指定节点可发送的订单流程数据
     *
     * @param erpOrderItem
     * @param flowStep
     * @return
     */
    public RemoteData<ErpOrderItemProcess> getAvailableOrderItemProcess(User loginUser, ErpOrderItem erpOrderItem, int flowStep) {
        String os_no = erpOrderItem.os_no;
        int itm = erpOrderItem.itm;

        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowStepEquals(loginUser.id, erpOrderItem.produceType, flowStep);

        if (workFlowWorker == null)
            return wrapError("当前节点未配置工作人员");
        if (!workFlowWorker.send) {
            return wrapError("当前流程无发送权限");
        }


        if (erpOrderItem.produceType == ProduceType.PURCHASE) {


            return getAvailablePurchaseOrderItemProcess(loginUser, os_no, itm, flowStep);
        }


        ErpWorkFlow workFlow = ErpWorkFlow.findByStep(flowStep);


        //自制 流程
        //查找前一个流程
        if (flowStep != ErpWorkFlow.FIRST_STEP)  //第一道第二道 都不需要上一道100%完成
        {
            int previousStep = ErpWorkFlow.findPrevious(flowStep);

            final ErpWorkFlowReport erpWorkFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(os_no, itm, previousStep);


            if (erpWorkFlowReport == null || erpWorkFlowReport.percentage == 0)
                return wrapError("当前未接收任何数量，不能发起交接");


            if (flowStep != ErpWorkFlow.STEP_PEITI && erpWorkFlowReport.percentage < 1) {
                return wrapError("上一道流程未交接完毕，不能发起交接");
            }


        }
        List<ErpOrderItemProcess> processes = erpWorkRepository.findOrderItemProcesses(os_no, itm);

        if (processes.size() <= 0)
            return wrapError("当前无可以提交流程");


        List<ErpOrderItemProcess> result = new ArrayList<>();


        ErpWorkFlow nextFlow = null;
        boolean found = false;
        for (ErpOrderItemProcess process : processes) {
            if (process.mrpNo.startsWith(workFlow.code)) {
                found = true;
                result.add(process);
            } else {
                if (found) {

                    nextFlow = ErpWorkFlow.findByCode(process.mrpNo.substring(0, 1));
                    found = false;
                }
            }


        }


        //最后一道不需要验证排厂
        if (workFlow.step != ErpWorkFlow.LAST_STEP) {
            for (ErpOrderItemProcess process : result) {

                if (StringUtils.isEmpty(process.jgh)) {
                    return wrapError("当前流程未排厂加工户", MSG_CODE_ERP);
                }
            }
        }

        List<ErpOrderItemProcess> availableResult = new ArrayList<>();
        for (ErpOrderItemProcess process : result) {
            process.currentWorkFlowCode = workFlow.code;
            process.currentWorkFlowStep = workFlow.step;
            process.currentWorkFlowName = workFlow.name;
            process.unSendQty = process.qty;


            process.nextWorkFlowCode = nextFlow == null ? "" : nextFlow.code;
            process.nextWorkFlowStep = nextFlow == null ? 0 : nextFlow.step;
            process.nextWorkFlowName = nextFlow == null ? "" : nextFlow.name;


            ErpOrderItemProcess localProcess = erpOrderItemProcessRepository.findFirstByOsNoEqualsAndItmEqualsAndMoNoEqualsAndMrpNoEquals(process.osNo, process.itm, process.moNo, process.mrpNo);
            if (localProcess != null)
                attachData(process, localProcess);


            //判断判断类型 铁木
            //只有白胚，颜色才区分铁木
            if (process.mrpNo.startsWith(ErpWorkFlow.CODE_YANSE) || process.mrpNo.startsWith(ErpWorkFlow.SECOND_STEP_CODE)

                    || process.mrpNo.startsWith(ErpWorkFlow.FIRST_STEP_CODE)  //第一道也增加鐵木

            ) {

                String code = process.mrpNo.substring(1, 2);
                if (code.equals(ErpWorkFlow.CODE_MU) || code.equals(ErpWorkFlow.CODE_TIE)) {

                    //木流程，当前用户没有木权限
                    if (!workFlowWorker.mu && code.equals(ErpWorkFlow.CODE_MU)) {
                        continue;
                    }
//铁流程，当前用户没有铁权限
                    if (!workFlowWorker.tie && code.equals(ErpWorkFlow.CODE_TIE)) {
                        continue;
                    }
                }

            }


            if (process.unSendQty > 0) {

                availableResult.add(process);
            }
        }


        return wrapData(availableResult);


    }


    public void attachData(ErpOrderItemProcess erpOrderItemProcess, ErpOrderItemProcess localData) {

        erpOrderItemProcess.id = localData.id;
        erpOrderItemProcess.unSendQty = localData.unSendQty;
        erpOrderItemProcess.sendingQty = localData.sendingQty;
        erpOrderItemProcess.sentQty = localData.sentQty;
        erpOrderItemProcess.so_zxs = localData.so_zxs;
//        erpOrderItemProcess.sentQty=localData.sentQty;
    }

    /**
     * 向指定流程发起生产提交
     *
     * @param user
     * @param erpOrderItemProcess 订单项对应流程状态
     * @param tranQty             传递数量
     * @param memo                备注
     */


    @Transactional(rollbackFor = {HdException.class})
    public synchronized RemoteData<Void> sendWorkFlowMessage(User user, ErpOrderItemProcess erpOrderItemProcess, int tranQty, long areaId, String memo) throws HdException {

        ErpOrderItem erpOrderItem = erpWorkRepository.findOrderItem(erpOrderItemProcess.osNo, erpOrderItemProcess.itm);

        return sendWorkFlowMessage(user, erpOrderItemProcess, erpOrderItem, tranQty, areaId, memo);

    }

    /**
     * 向指定流程发起生产提交
     *
     * @param user
     * @param erpOrderItemProcess 订单项对应流程状态
     * @param tranQty             传递数量
     * @param memo                备注
     */
    @Transactional(rollbackFor = {HdException.class})
    public synchronized RemoteData<Void> sendWorkFlowMessage(User user, ErpOrderItemProcess erpOrderItemProcess, ErpOrderItem erpOrderItem, int tranQty, long areaId, String memo) throws HdException {


        //增加数据小验证

        ErpOrderItemProcess findErpOrderitemProcess = erpOrderItemProcessRepository.findFirstByOsNoEqualsAndItmEqualsAndMoNoEqualsAndMrpNoEquals(erpOrderItemProcess.osNo, erpOrderItemProcess.itm, erpOrderItemProcess.moNo, erpOrderItemProcess.mrpNo);
        if (findErpOrderitemProcess != null && findErpOrderitemProcess.unSendQty < tranQty) {

            return wrapError("当前流程数量已经有发送记录了。 现有未发送数量" + findErpOrderitemProcess.unSendQty + ",不足以发送" + tranQty);
        }

        if (findErpOrderitemProcess != null) {
            erpOrderItemProcess = findErpOrderitemProcess;
        }


        WorkFlowArea workFlowArea = workFlowAreaRepository.findOne(areaId);
        ;
        if (erpOrderItemProcess.currentWorkFlowStep != ErpWorkFlow.LAST_STEP) {

            if (workFlowArea == null) {
                return wrapError("未选择交接区域");
            }
            //最后一个节点不需要验证下一流程数据
            if (erpOrderItemProcess.nextWorkFlowStep <= 0)
                return wrapError("该订单下一流程数据不存在");

//        WorkFlow workFlow = workFlowRepository.findFirstByFlowStepEquals(workFlowOrderItemState.currentWorkFlowStep);
//        if (workFlow == null)
//            return wrapError("数据异常，该订单当前流程不存在");
//        WorkFlow newWorkFlow = workFlowRepository.findFirstByFlowStepEquals(workFlowOrderItemState.nextWorkFlowStep);
//        if (newWorkFlow == null)
//            return wrapError("数据异常，该订单下一流程数据不存在");
        }

        if (tranQty > erpOrderItemProcess.unSendQty) {

            return wrapError("提交数量超过当前流程数量");
        }


        //白胚颜色  发起 需要检查上一道提交量
        if (erpOrderItemProcess.currentWorkFlowStep == ErpWorkFlow.STEP_PEITI || erpOrderItemProcess.currentWorkFlowStep == ErpWorkFlow.STEP_YANSE) {

            String lastStepCode = erpOrderItemProcess.currentWorkFlowStep == ErpWorkFlow.STEP_PEITI ? ErpWorkFlow.FIRST_STEP_CODE : ErpWorkFlow.SECOND_STEP_CODE;
            int lastStep = erpOrderItemProcess.currentWorkFlowStep == ErpWorkFlow.STEP_PEITI ? ErpWorkFlow.FIRST_STEP : ErpWorkFlow.STEP_PEITI;


            ErpWorkFlowReport erpWorkFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(erpOrderItemProcess.osNo, erpOrderItemProcess.itm, lastStep);
            if (erpWorkFlowReport.percentage < 1) {
                //查找上一节mrp_no;
                String lastStepMrpNo = lastStepCode + erpOrderItemProcess.mrpNo.substring(1);
                //查找记录
                ErpOrderItemProcess lastStepProcess = erpOrderItemProcessRepository.findFirstByOsNoEqualsAndItmEqualsAndMrpNoEquals(erpOrderItemProcess.osNo, erpOrderItemProcess.itm, lastStepMrpNo);
                if (lastStepProcess == null) {
                    return wrapError("上一流程未完成！");
                }
                if (lastStepProcess.sentQty < tranQty) {


                    return wrapError("当前流程产品数量:" + lastStepProcess.sentQty + ",不够发送" + tranQty);
                }

            }
        }


        //验证人员

        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowCodeEqualsAndSendEquals(user.id, erpOrderItem.produceType, erpOrderItemProcess.currentWorkFlowCode, true);
        if (workFlowWorker == null) {
            return wrapError("无权限在当前节点:" + erpOrderItemProcess.currentWorkFlowName + " 发送流程");
        }


        OrderItemWorkState orderItem = orderItemWorkStateRepository.findFirstByOsNoEqualsAndItmEquals(erpOrderItemProcess.osNo, erpOrderItemProcess.itm);

        if (orderItem == null) {
            orderItem = new OrderItemWorkState();
            orderItem.osNo = erpOrderItemProcess.osNo;
            orderItem.itm = erpOrderItemProcess.itm;
            orderItem.url = erpOrderItemProcess.photoUrl;
            orderItem.prdNo = erpOrderItemProcess.prdNo;
            orderItem.pVersion = erpOrderItemProcess.pVersion;
            orderItem.maxWorkFlowStep = erpOrderItemProcess.currentWorkFlowStep;
            orderItem.maxWorkFlowCode = erpOrderItemProcess.currentWorkFlowCode;
            orderItem.maxWorkFlowName = erpOrderItemProcess.currentWorkFlowName;
        }

        //标记当前订单生产状态
        orderItem.workFlowState = ErpWorkFlow.STATE_WORKING;
        orderItem.workFlowDescribe = "消息发送自:" + erpOrderItemProcess.currentWorkFlowCode + erpOrderItemProcess.currentWorkFlowName;


        orderItem = orderItemWorkStateRepository.save(orderItem);


        //扣减数量
        erpOrderItemProcess.unSendQty -= tranQty;
        erpOrderItemProcess.sendingQty += tranQty;


        erpOrderItemProcess = erpOrderItemProcessRepository.save(erpOrderItemProcess);


        //构建信息发出消息
        WorkFlowMessage workFlowMessage = new WorkFlowMessage();


        workFlowMessage.orderItemProcessId = erpOrderItemProcess.id;

        workFlowMessage.orderName = erpOrderItemProcess.osNo;

        workFlowMessage.orderItemQty = erpOrderItemProcess.orderQty;
        workFlowMessage.transportQty = tranQty;
        workFlowMessage.sendMemo = memo == null ? "" : memo;
        workFlowMessage.area = workFlowArea == null ? "" : workFlowArea.name;
        workFlowMessage.name = WorkFlowMessage.NAME_SUBMIT;

        workFlowMessage.senderId = user.id;
        workFlowMessage.senderName = user.toString();

        workFlowMessage.fromFlowStep = erpOrderItemProcess.currentWorkFlowStep;
        workFlowMessage.fromFlowName = erpOrderItemProcess.currentWorkFlowName;
        workFlowMessage.fromFlowCode = erpOrderItemProcess.currentWorkFlowCode;
        workFlowMessage.toFlowStep = erpOrderItemProcess.nextWorkFlowStep;
        workFlowMessage.toFlowName = erpOrderItemProcess.nextWorkFlowName;
        workFlowMessage.toFlowCode = erpOrderItemProcess.nextWorkFlowCode;


        if (erpOrderItemProcess.currentWorkFlowStep == ErpWorkFlow.FIRST_STEP && !StringUtils.isEmpty(erpOrderItem.sys_date)) {
            try {
                workFlowMessage.createTime = DateFormats.FORMAT_YYYY_MM_DD.parse(erpOrderItem.sys_date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            workFlowMessage.createTimeString = erpOrderItem.sys_date;

        } else {
            workFlowMessage.createTime = Calendar.getInstance().getTimeInMillis();
            workFlowMessage.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
        }
        workFlowMessage.state = WorkFlowMessage.STATE_SEND;

        workFlowMessage.productName = erpOrderItemProcess.prdNo;
        workFlowMessage.itm = erpOrderItemProcess.itm;
        workFlowMessage.mrpNo = erpOrderItemProcess.mrpNo;

        workFlowMessage.pVersion = erpOrderItemProcess.pVersion;
        workFlowMessage.url = erpOrderItemProcess.photoUrl;
        workFlowMessage.thumbnail = erpOrderItemProcess.photoThumb;

        workFlowMessage.factoryName = erpOrderItemProcess.jgh;
        workFlowMessage.bat_no = erpOrderItem.bat_no;
        workFlowMessage.cus_no = erpOrderItem.cus_no;


        workFlowMessage.produceType = erpOrderItem.produceType;
        workFlowMessage.produceTypeName = erpOrderItem.produceTypeName;

        workFlowMessage.mrpType = StringUtils.isEmpty(workFlowMessage.mrpNo) ? "" : workFlowMessage.mrpNo.substring(1, 2);
        workFlowMessage.prdType = StringUtils.isEmpty(erpOrderItem.idx1) ? "" : erpOrderItem.idx1.substring(0, 2);

        workFlowMessage = workFlowMessageRepository.save(workFlowMessage);


        if (workFlowMessage.fromFlowStep != ErpWorkFlow.FIRST_STEP && workFlowMessage.fromFlowStep != ErpWorkFlow.LAST_STEP) {

            try {

                messagePushService.pushMessage(workFlowMessage);
            } catch (Throwable t) {
                logger.error(t.getMessage());
            }
        }


        generateWorkFlowReports(erpOrderItemProcess, erpOrderItem);


        if (erpOrderItemProcess.currentWorkFlowStep == ErpWorkFlow.STEP_CHENGPIN) {


            //自动审核通过
            workFlowMessage.state = WorkFlowMessage.STATE_PASS;
            workFlowMessage.receiveTime = Calendar.getInstance().getTimeInMillis();
            workFlowMessage.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

            workFlowMessage.receiverId = user.id;
            workFlowMessage.receiverName = user.toString();


            workFlowMessageRepository.save(workFlowMessage);


            //上流程 数量整理
            erpOrderItemProcess.sendingQty -= tranQty;
//        erpOrderItemProcess.unSendQty -= message.transportQty;
            erpOrderItemProcess.sentQty += tranQty;


            erpOrderItemProcessRepository.save(erpOrderItemProcess);
            ErpWorkFlowReport workFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(workFlowMessage.orderName, workFlowMessage.itm, workFlowMessage.fromFlowStep);


            //更新生产进度
            workFlowReport.percentage += (float) workFlowMessage.transportQty / erpOrderItemProcess.orderQty / (workFlowReport.typeCount == 0 ? 1 : workFlowReport.typeCount);


            //统计发送未处理数量
            final List<WorkFlowMessage> currentWorkFlowMessage = workFlowMessageRepository.findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(workFlowReport.workFlowStep, workFlowReport.osNo, workFlowReport.itm);
            int sendingQty = 0;
            for (WorkFlowMessage tem : currentWorkFlowMessage) {
                if (tem.receiverId == 0) {
                    sendingQty += tem.transportQty;
                }
            }

            workFlowReport.sendingQty = sendingQty;


            if (workFlowReport.percentage >= 1) {
                workFlowReport.endDate = workFlowMessage.receiveTime;
                workFlowReport.endDateString = workFlowMessage.receiveTimeString;
            }
            workFlowReport = erpWorkFlowReportRepository.save(workFlowReport);


            if (workFlowReport.percentage >= 1) {
                //最后一个流程发出信息  标记出货   当进度达到1该货款生产结束

                orderItem.workFlowState = ErpWorkFlow.STATE_COMPLETE;
                orderItem.workFlowDescribe = ErpWorkFlow.STATE_NAME_COMPLETE;
                orderItemWorkStateRepository.save(orderItem);
            }

        }

//        if(true)
//        {
//            throw HdException.create("测试");
//        }


        return wrapData();


    }

    public void generateWorkFlowReports(ErpOrderItemProcess erpOrderItemProcess, ErpOrderItem erpOrderItem) {
        RemoteData<ErpWorkFlowReport> workFlowReports = findErpWorkFlowReport(erpOrderItem);


        if (workFlowReports.isSuccess()) {

            updateErpWorkFlowReports(erpOrderItemProcess, erpOrderItem, workFlowReports);
        }
    }

    private void updateErpWorkFlowReports(ErpOrderItemProcess erpOrderItemProcess, ErpOrderItem erpOrderItem, RemoteData<ErpWorkFlowReport> workFlowReports) {
        WorkFlowTimeLimit.OrderItemType orderItemType = findOrderItemTypeForTimeLimit(erpOrderItemProcess, erpOrderItem);

        WorkFlowTimeLimit timeLimit = workFlowTimeLimitRepository.findFirstByOrderItemTypeEquals(orderItemType.orderItemType);


        for (ErpWorkFlowReport erpWorkFlowReport : workFlowReports.datas) {

            erpWorkFlowReport.orderItemType = orderItemType.orderItemType;
            erpWorkFlowReport.orderItemTypeName = orderItemType.orderItemTypeName;
            erpWorkFlowReport.idx1 = orderItemType.idx1;


            if (erpWorkFlowReport.workFlowStep == ErpWorkFlow.FIRST_STEP && !StringUtils.isEmpty(erpOrderItem.sys_date)) {
                try {
                    erpWorkFlowReport.startDate = DateFormats.FORMAT_YYYY_MM_DD.parse(erpOrderItem.sys_date).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                erpWorkFlowReport.startDateString = erpOrderItem.sys_date;

            }

            updateErpWorkFlowReport(erpWorkFlowReport, timeLimit);


        }


        erpWorkFlowReportRepository.save(workFlowReports.datas);
        erpWorkFlowReportRepository.flush();
    }


    /**
     * 更新流程报告的 生产进度状态数据
     *
     * @param erpWorkFlowReport
     * @param workFlowTimeLimit
     */
    private void updateErpWorkFlowReport(ErpWorkFlowReport erpWorkFlowReport, WorkFlowTimeLimit workFlowTimeLimit) {


        if (!erpWorkFlowReport.hasUpdateLimit) {

            String idx1 = erpWorkFlowReport.idx1;
            //铁木判定
            boolean isMu = idx1.toLowerCase().startsWith("mj");
            int alertDay = 0;
            int limitDay = 0;
            switch (erpWorkFlowReport.workFlowStep) {
                case ErpWorkFlow.FIRST_STEP:
                    if (isMu) {
                        alertDay = workFlowTimeLimit.alert_mu_baipeijg;
                        limitDay = workFlowTimeLimit.limit_mu_baipeijg;
                    } else {
                        alertDay = workFlowTimeLimit.alert_tie_baipeijg;
                        limitDay = workFlowTimeLimit.limit_tie_baipeijg;
                    }
                    break;
                case ErpWorkFlow.STEP_CHENGPIN:

                    erpWorkFlowReport.isOverDue = false;
                    erpWorkFlowReport.overDueDay = 0;

                    alertDay = 0;
                    limitDay = 0;
                    break;

                case ErpWorkFlow.STEP_PEITI:
                    if (isMu) {
                        alertDay = workFlowTimeLimit.alert_mu_baipei;
                        limitDay = workFlowTimeLimit.limit_mu_baipei;
                    } else {
                        alertDay = workFlowTimeLimit.alert_tie_baipei;
                        limitDay = workFlowTimeLimit.limit_tie_baipei;
                    }
                    break;
                case ErpWorkFlow.STEP_YANSE:

                    if (isMu) {
                        alertDay = workFlowTimeLimit.alert_mu_yanse;
                        limitDay = workFlowTimeLimit.limit_mu_yanse;
                    } else {
                        alertDay = workFlowTimeLimit.alert_tie_yanse;
                        limitDay = workFlowTimeLimit.limit_tie_yanse;
                    }
                    break;
                case ErpWorkFlow.STEP_BAOZHUANG: {


                    if (isMu) {
                        alertDay = workFlowTimeLimit.alert_baozhuang;
                        limitDay = workFlowTimeLimit.limit_baozhuang;
                    } else {
                        alertDay = workFlowTimeLimit.alert_tie_baozhuang;
                        limitDay = workFlowTimeLimit.limit_tie_baozhuang;
                    }


                }

                break;


            }

            if (erpWorkFlowReport.workFlowStep == ErpWorkFlow.LAST_STEP) {

                erpWorkFlowReport.isOverDue = false;
                erpWorkFlowReport.overDueDay = 0;

                alertDay = 0;
                limitDay = 0;
            }

            erpWorkFlowReport.limitDay = limitDay;
            erpWorkFlowReport.alertDay = alertDay;

        }


        if (erpWorkFlowReport.limitDay > 0) {


            if (erpWorkFlowReport.startDate > 0) {
                updateWorkFlowReportOverDueState(erpWorkFlowReport);
            } else {
                erpWorkFlowReport.isOverDue = false;
                erpWorkFlowReport.overDueDay = 0;
            }

        } else {

            erpWorkFlowReport.isOverDue = false;
            erpWorkFlowReport.overDueDay = 0;
        }


    }


    private void updateWorkFlowReportOverDueState(ErpWorkFlowReport erpWorkFlowReport) {

        if (erpWorkFlowReport.startDate <= 0) {
            erpWorkFlowReport.isOverDue = false;
            erpWorkFlowReport.overDueDay = 0;


        } else {
            final long now = Calendar.getInstance().getTimeInMillis();


            long endDate = erpWorkFlowReport.endDate;
            if (endDate == 0) {
                endDate = now;
            }

            int daybetween = DateUtils.getDayDifference(erpWorkFlowReport.startDate, endDate);
            erpWorkFlowReport.isOverDue = daybetween > erpWorkFlowReport.limitDay;
            erpWorkFlowReport.overDueDay = daybetween - erpWorkFlowReport.limitDay;
        }
    }

    /**
     * 更新所有已完成产品的进度数据
     */
    @Transactional
    public void updateCompleteWorkFlowReports() {
        updateWorkFlowReports(ErpWorkFlow.STATE_COMPLETE);
    }

    /**
     * 更新所有在产产品的进度数据
     */
    @Transactional
    public void updateAllProducingWorkFlowReports() {
        updateWorkFlowReports(ErpWorkFlow.STATE_WORKING);
    }

    /**
     * 更新产产品的进度数据
     */
    @Transactional
    public void updateWorkFlowReports(int workFlowCompleteState) {


        List<OrderItemWorkState> orderItemWorkStates = orderItemWorkStateRepository.findByWorkFlowStateEquals(workFlowCompleteState);

        for (OrderItemWorkState orderItemWorkState : orderItemWorkStates) {

            adjustOrderItemWorkState(orderItemWorkState);
        }


    }


    public void testAdjustOrderItemWorkState() {

        OrderItemWorkState orderItemWorkState = orderItemWorkStateRepository.findFirstByOsNoEqualsAndItmEquals("17YF018", 11);

        correctWorkFlowReportData(orderItemWorkState);
        adjustOrderItemWorkState(orderItemWorkState);
    }

    private void adjustOrderItemWorkState(OrderItemWorkState orderItemWorkState) {


        List<ErpWorkFlowReport> workFlowReports = erpWorkFlowReportRepository.findByOsNoEqualsAndItmEqualsOrderByWorkFlowStepAsc(orderItemWorkState.osNo, orderItemWorkState.itm);

        int totalLimit = 0;
        ErpWorkFlowReport currentReport = null;
        boolean hasStart = false;
        for (ErpWorkFlowReport report : workFlowReports) {

            WorkFlowTimeLimit workFlowTimeLimit = workFlowTimeLimitRepository.findFirstByOrderItemTypeEquals(report.orderItemType);

            if (workFlowTimeLimit == null) continue;

            updateErpWorkFlowReport(report, workFlowTimeLimit);
            if (report.percentage < 1 && currentReport == null) {
                currentReport = report;
            }
            if (!hasStart)
                hasStart = report.percentage > 0;
            totalLimit += report.overDueDay;

        }
        if (currentReport == null && workFlowReports.size() > 0) {
            currentReport = workFlowReports.get(workFlowReports.size() - 1);
        }

        //if(currentReport==null) return;
//            /**
//             * 表示 所有流程未启动 未接收任何流程
//             */
//            if(!hasStart  )
//            {
//                //找到最早发起流程的消息记录 作为开始时间，进行计算
//               List<WorkFlowMessage> workFlowMessages= workFlowMessageRepository.findByOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(orderItemWorkState.osNo,orderItemWorkState.itm);
//                if(workFlowMessages.size()>0)
//                {
//                    WorkFlowMessage workFlowMessage=workFlowMessages.get(workFlowMessages.size()-1);
//
//                    int dayBetween=DateUtils.getDayDifference(workFlowMessage.createTime,Calendar.getInstance().getTimeInMillis());
//
//                    int totalLimit=workFlowTimeLimit.
//
//                }
//
//            }

        if (workFlowReports.size() > 0) {
            erpWorkFlowReportRepository.save(workFlowReports);
            erpWorkFlowReportRepository.flush();
        }

        orderItemWorkState.totalLimit = totalLimit;
        orderItemWorkState.maxWorkFlowCode = currentReport == null ? "" : currentReport.workFlowCode;
        orderItemWorkState.maxWorkFlowName = currentReport == null ? "" : currentReport.workFlowName;
        orderItemWorkState.maxWorkFlowStep = currentReport == null ? 0 : currentReport.workFlowStep;
        orderItemWorkState.currentOverDueDay = currentReport == null ? 0 : currentReport.overDueDay;
        orderItemWorkState.currentLimitDay = currentReport == null ? 0 : currentReport.limitDay;
        orderItemWorkState.currentAlertDay = currentReport == null ? 0 : currentReport.alertDay;
        orderItemWorkStateRepository.saveAndFlush(orderItemWorkState);
    }

    /**
     * 获取获取的排厂生产类型。
     *
     * @return
     */
    private WorkFlowTimeLimit.OrderItemType findOrderItemTypeForTimeLimit(ErpOrderItemProcess erpOrderItemProcess, ErpOrderItem orderItem) {


        return WorkFlowTimeLimit.findOrderItemTypeForTimeLimit(erpOrderItemProcess.scsx, orderItem.cus_no, orderItem.idx1);

    }


    /**
     * 调整所有报表数据的相关数值， 旧数据的调整
     */
    @Transactional
    public void correctAllWorkFlowReportData() {

        List<OrderItemWorkState> workStates = orderItemWorkStateRepository.findAll();
        for (OrderItemWorkState state : workStates) {
            correctWorkFlowReportData(state);

        }


    }

    private void correctWorkFlowReportData(OrderItemWorkState state) {
        List<ErpWorkFlowReport> reports = erpWorkFlowReportRepository.findByOsNoEqualsAndItmEqualsOrderByWorkFlowStepAsc(state.osNo, state.itm);
        if (reports.size() == 0 || reports.get(0).orderItemType > 0) {
            return;
        }


        List<ErpOrderItemProcess> processes = erpWorkRepository.findOrderItemProcesses(state.osNo, state.itm);
        if (processes.size() == 0)
            return;


        ErpOrderItem erpOrderItem = erpWorkRepository.findOrderItem(state.osNo, state.itm);


        WorkFlowTimeLimit.OrderItemType orderItemType = findOrderItemTypeForTimeLimit(processes.get(0), erpOrderItem);


        for (int i = 0; i < reports.size(); i++) {
            ErpWorkFlowReport erpWorkFlowReport = reports.get(i);

            erpWorkFlowReport.orderItemType = orderItemType.orderItemType;
            erpWorkFlowReport.orderItemTypeName = orderItemType.orderItemTypeName;
            erpWorkFlowReport.idx1 = orderItemType.idx1;


            /**
             * 开始时间未生成
             */
            if (erpWorkFlowReport.startDate <= 0) {

                if (i == 0) {
                    //第一道起始时间，以第一个发送消息为准
                    List<WorkFlowMessage> workFlowMessages = workFlowMessageRepository.findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeAsc(erpWorkFlowReport.workFlowStep, erpWorkFlowReport.osNo, erpWorkFlowReport.itm);


                    if (workFlowMessages.size() > 0) {
                        erpWorkFlowReport.startDate = workFlowMessages.get(0).createTime;
                        erpWorkFlowReport.startDateString = workFlowMessages.get(0).createTimeString;
                    }

                } else {
                    ErpWorkFlowReport previousReport = reports.get(i - 1);
                    //前一节点已经完成的最后接收时间 即当前节点的开始时间
                    if (previousReport.percentage >= 1) {

                        List<WorkFlowMessage> workFlowMessages = workFlowMessageRepository.findByToFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByReceiveTimeDesc(erpWorkFlowReport.workFlowStep, erpWorkFlowReport.osNo, erpWorkFlowReport.itm);
                        if (workFlowMessages.size() > 0) {
                            erpWorkFlowReport.startDate = workFlowMessages.get(0).receiveTime;
                            erpWorkFlowReport.startDateString = workFlowMessages.get(0).receiveTimeString;
                        }
                    }
                }

            }
            //当前节点已经完成  补充结束数据
            if (erpWorkFlowReport.endDate <= 0 && erpWorkFlowReport.percentage >= 1) {

                List<WorkFlowMessage> workFlowMessages = workFlowMessageRepository.findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByReceiveTimeDesc(erpWorkFlowReport.workFlowStep, erpWorkFlowReport.osNo, erpWorkFlowReport.itm);


                if (workFlowMessages.size() > 0) {
                    erpWorkFlowReport.endDate = workFlowMessages.get(0).receiveTime;
                    erpWorkFlowReport.endDateString = workFlowMessages.get(0).receiveTimeString;
                }
            }


        }


        erpWorkFlowReportRepository.save(reports);
        erpWorkFlowReportRepository.flush();
    }


    /**
     * 接收产品订单的递交数据
     * 如果该流程未配置审核人， 则自动通过审核。并使该订单进入下一个流程
     */
    @Transactional
    public synchronized RemoteData<Void> receiveOrderItemWorkFlow(User loginUser, long messageId, MultipartFile[] files, String memo) throws HdException {


        final int length = files.length;
        if (length < ErpWorkFlow.PICTURE_COUNT) {
            return wrapError("确认，至少需要上传" + ErpWorkFlow.PICTURE_COUNT + "张图片");
        }


        WorkFlowMessage message = workFlowMessageRepository.findOne(messageId);
        if (message == null) {
            return wrapError("消息不存在：" + messageId);
        }

        if (message.state != WorkFlowMessage.STATE_SEND) {
            return wrapError("异常，消息已经被处理：" + messageId);
        }


        ErpOrderItemProcess erpOrderItemProcess = erpOrderItemProcessRepository.findOne(message.orderItemProcessId);
        if (erpOrderItemProcess == null) {
            return wrapError("未找到对应的流程状态信息");
        }


        ErpWorkFlowReport workFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(message.orderName, message.itm, message.fromFlowStep);

        if (workFlowReport == null) {


            ErpOrderItem erpOrderItem = erpWorkRepository.findOrderItem(message.orderName, message.itm);
            generateWorkFlowReports(erpOrderItemProcess, erpOrderItem);
            workFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(message.orderName, message.itm, message.fromFlowStep);
        }
        if (workFlowReport == null) {
            throw HdException.create("系统异常，未找到流程数据");
        }
        //人员验证
        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowCodeEqualsAndReceiveEquals(loginUser.id, workFlowReport.produceType, message.toFlowCode, true);
        boolean canOperate = workFlowWorker != null;

        if (!canOperate) {
            return wrapError("未配置对该流程处理的权限");

        }


        //上流程 数量整理
        erpOrderItemProcess.sendingQty -= message.transportQty;
//        erpOrderItemProcess.unSendQty -= message.transportQty;
        erpOrderItemProcess.sentQty += message.transportQty;


        erpOrderItemProcessRepository.save(erpOrderItemProcess);

        if (erpOrderItemProcess.sentQty == erpOrderItemProcess.qty) {
            //修改erp數據的狀態  //当前流程完工
            int updateCount = erpWorkRepository.updateErpWorkFlowTime(erpOrderItemProcess.osNo, erpOrderItemProcess.itm, erpOrderItemProcess.mrpNo);
            logger.info("update erp mrp_no time success:" + updateCount);
        }
        //更新生产进度
        workFlowReport.percentage += (float) message.transportQty / erpOrderItemProcess.orderQty / (workFlowReport.typeCount == 0 ? 1 : workFlowReport.typeCount);


        //统计发送未处理数量
        final List<WorkFlowMessage> currentWorkFlowMessage = workFlowMessageRepository.findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(workFlowReport.workFlowStep, workFlowReport.osNo, workFlowReport.itm);
        int sendingQty = 0;
        for (WorkFlowMessage tem : currentWorkFlowMessage) {
            if (tem.receiverId == 0 && tem.id != message.id) {
                sendingQty += tem.transportQty;
            }
        }

        workFlowReport.sendingQty = sendingQty;


        workFlowReport = erpWorkFlowReportRepository.save(workFlowReport);


        //当前流程完成处理
        final long timeInMillis = Calendar.getInstance().getTimeInMillis();
        if (Math.abs(workFlowReport.percentage - 1) < 0.00001) {

            WorkFlowTimeLimit timeLimit = workFlowTimeLimitRepository.findFirstByOrderItemTypeEquals(workFlowReport.orderItemType);
            //标记当前流程完成时间
            workFlowReport.endDate = timeInMillis;
            final String dateString = DateFormats.FORMAT_YYYY_MM_DD.format(Calendar.getInstance().getTime());
            workFlowReport.endDateString = dateString;
            updateErpWorkFlowReport(workFlowReport, timeLimit);
            workFlowReport = erpWorkFlowReportRepository.save(workFlowReport);
            if (message.toFlowStep > 0) {
                //标记下一道流程开始时间。（当前流程结束时间，即下一流程开始时间）
                ErpWorkFlowReport nextWorkFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(message.orderName, message.itm, message.toFlowStep);


                nextWorkFlowReport.startDate = timeInMillis;
                nextWorkFlowReport.startDateString = dateString;
                updateErpWorkFlowReport(nextWorkFlowReport, timeLimit);
                nextWorkFlowReport = erpWorkFlowReportRepository.save(nextWorkFlowReport);
            }
        }


        handleOnMessage(message, files, memo);


        if (message.state == WorkFlowMessage.STATE_SEND) {

            //无审核人 系统自动审核通过
            message.state = WorkFlowMessage.STATE_PASS;
            message.checkTime = timeInMillis;
            message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

            message.receiverId = loginUser.id;
            message.receiverName = loginUser.toString();


        } else if (message.state == WorkFlowMessage.STATE_REWORK) {
            //返工 状态 自动通过。
            message.state = WorkFlowMessage.STATE_PASS;
            message.receiveTime = timeInMillis;
            message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

            message.receiverId = loginUser.id;
            message.receiverName = loginUser.toString();
        }

        workFlowMessageRepository.save(message);


        OrderItemWorkState orderItem = orderItemWorkStateRepository.findFirstByOsNoEqualsAndItmEquals(erpOrderItemProcess.osNo, erpOrderItemProcess.itm);

        if (orderItem != null) {

            if (Math.abs(workFlowReport.percentage - 1) < 0.00001) {
                //当前流程结束 跳转 下一流程
                orderItem.maxWorkFlowStep = message.toFlowStep;
                orderItem.maxWorkFlowCode = message.toFlowCode;
                orderItem.maxWorkFlowName = message.toFlowName;
                orderItemWorkStateRepository.save(orderItem);


            }


        }


//        if(1==1)
//            throw HdException.create("测试") ;

        return wrapData();
    }


    private void handleOnMessage(WorkFlowMessage message, MultipartFile[] files, String memo) {

        if (files == null) return;
        final int length = files.length;

        message.receiveTime = Calendar.getInstance().getTimeInMillis();
        message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        message.memo = memo;
        //图片文件处理， 生成url‘
        String[] urlPaths = new String[length];

        for (int i = 0; i < length; i++) {
            MultipartFile file = files[i];


            String fileName = Calendar.getInstance().getTimeInMillis() + file.getName() + FileUtils.SUFFIX_JPG;
            final String absoluteFilePath = FileUtils.combinePath(rootpath, CATEGORY, fileName);

            try {
                FileUtils.copy(file, absoluteFilePath);
                urlPaths[i] = FileUtils.combineUrl(rootUrl, CATEGORY, fileName);
            } catch (IOException e) {
                e.printStackTrace();

                logger.error(e.getMessage());
            }

        }

        message.pictures = StringUtils.combine(urlPaths);

        message.receiveTime = Calendar.getInstance().getTimeInMillis();
        message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


    }


    /**
     * 流程拒绝接收处理。
     *
     * @param workFlowMsgId
     * @param files
     * @param memo
     * @return
     */
    @Transactional
    public RemoteData<Void> rejectWorkFlowMessage(User loginUser, long workFlowMsgId, MultipartFile[] files, String memo) {

        final int length = files.length;
        if (length < ErpWorkFlow.PICTURE_COUNT) {
            return wrapError("确认，要传递" + ErpWorkFlow.PICTURE_COUNT + "张图片");
        }

        WorkFlowMessage message = workFlowMessageRepository.findOne(workFlowMsgId);
        if (message == null) {
            return wrapError("消息不存在：" + workFlowMsgId);
        }

        ErpWorkFlowReport workFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(message.orderName, message.itm, message.fromFlowStep);


        //人员验证
        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowCodeEqualsAndReceiveEquals(loginUser.id, workFlowReport.produceType, message.toFlowCode, true);
        boolean canOperate = workFlowWorker != null;

        if (!canOperate) {
            return wrapError("未配置对该流程处理的权限");

        }


        handleOnMessage(message, files, memo);

        ErpOrderItemProcess erpOrderItemProcess = erpOrderItemProcessRepository.findOne(message.orderItemProcessId);
        if (erpOrderItemProcess == null) {
            return wrapError("未找到对应的流程状态信息");
        }


        //上流程 数量整理
        erpOrderItemProcess.sendingQty -= message.transportQty;
        erpOrderItemProcess.unSendQty += message.transportQty;
        //   erpOrderItemProcess.sentQty += message.transportQty;


        erpOrderItemProcessRepository.save(erpOrderItemProcess);


        if (message.state == WorkFlowMessage.STATE_SEND) {

            //无审核人 系统自动审核通过
            message.state = WorkFlowMessage.STATE_REJECT;
            message.checkTime = Calendar.getInstance().getTimeInMillis();
            message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        } else if (message.state == WorkFlowMessage.STATE_REWORK) {
            //返工 状态 自动通过。
            message.state = WorkFlowMessage.STATE_PASS;
            message.receiveTime = Calendar.getInstance().getTimeInMillis();
            message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
        }

        workFlowMessageRepository.save(message);

        return wrapData();


    }


    public RemoteData<ProductWorkMemo> getProductWorkMemo(String productName, String pVersion) {


        return wrapData(productWorkMemoRepository.findByProductNameEqualsAndPVersionEquals(productName, pVersion));
    }

    public RemoteData<OrderItemWorkMemo> getOrderItemWorkMemo(String osNo, int itm) {
        return wrapData(orderItemWorkMemoRepository.findByOsNoEqualsAndItmEquals(osNo, itm));
    }

    @Transactional
    public RemoteData<Void> saveWorkMemo(User loginUser, int workFlowStep, String os_no, int itm, String orderItemWorkMemo, String prd_name, String pVersion, String productWorkMemo) {


        ErpWorkFlow erpWorkFlow = ErpWorkFlow.findByStep(workFlowStep);

        if (erpWorkFlow == null)
            return wrapError("未找到流程：" + workFlowStep);

        OrderItemWorkMemo orderItemWorkMemoData = orderItemWorkMemoRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(os_no, itm, workFlowStep);

        if (orderItemWorkMemoData == null) {
            orderItemWorkMemoData = new OrderItemWorkMemo();
            orderItemWorkMemoData.workFlowCode = erpWorkFlow.code;
            orderItemWorkMemoData.workFlowName = erpWorkFlow.name;
            orderItemWorkMemoData.workFlowStep = erpWorkFlow.step;
            orderItemWorkMemoData.osNo = os_no;
            orderItemWorkMemoData.itm = itm;

        }
        orderItemWorkMemoData.memo = orderItemWorkMemo;
        orderItemWorkMemoData.lastModifierId = loginUser.id;
        orderItemWorkMemoData.lastModifierName = loginUser.toString();
        orderItemWorkMemoData.lastModifyTime = Calendar.getInstance().getTimeInMillis();
        orderItemWorkMemoData.lastModifyTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        orderItemWorkMemoRepository.save(orderItemWorkMemoData);


        ProductWorkMemo productWorkMemoData = productWorkMemoRepository.findFirstByProductNameEqualsAndPVersionEqualsAndWorkFlowStepEquals(prd_name, pVersion, workFlowStep);


        if (productWorkMemoData == null) {
            productWorkMemoData = new ProductWorkMemo();
            productWorkMemoData.workFlowCode = erpWorkFlow.code;
            productWorkMemoData.workFlowName = erpWorkFlow.name;
            productWorkMemoData.workFlowStep = erpWorkFlow.step;
            productWorkMemoData.productName = prd_name;
            productWorkMemoData.pVersion = pVersion;

        }
        productWorkMemoData.memo = productWorkMemo;

        productWorkMemoData.lastModifierId = loginUser.id;
        productWorkMemoData.lastModifierName = loginUser.toString();
        productWorkMemoData.lastModifyTime = Calendar.getInstance().getTimeInMillis();
        productWorkMemoData.lastModifyTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

        productWorkMemoRepository.save(productWorkMemoData);


        return wrapData();

    }


    public RemoteData<WorkFlowMaterial> getWorkFlowMaterials(String osNo, int itm, String workFlowCode) {


        List<WorkFlowMaterial> result = erpWorkRepository.searchWorkFlowMaterials(osNo, itm, workFlowCode);


        return wrapData(result);


    }


    @Transactional(rollbackFor = {HdException.class})
    public RemoteData<Void> clearWorkFLow(User user, String osNo, int itm) throws HdException {


        if (!user.name.equals(User.ADMIN)) {
            return wrapError("只有系统管理员才能重置流程数据");
        }
        try {
            int count = workFlowMessageRepository.deleteByOsNoAndItm(osNo, itm);
            final int reportCount = erpWorkFlowReportRepository.deleteByOsNoAndItm(osNo, itm);
            int stateCount = orderItemWorkStateRepository.deleteByOsNoAndItm(osNo, itm);
            int processCount = erpOrderItemProcessRepository.deleteByOsNoAndItm(osNo, itm);


            return wrapMessageData("workFlowMessage delete " + count + ","
                    + "erpWorkFlowReport delete " + reportCount + ","
                    + "orderItemWorkState delete " + stateCount + ","
                    + "erpOrderItemProcess delete " + processCount


            );
        } catch (Throwable t) {
            t.printStackTrace();
            throw HdException.create(t.getMessage());
        }


    }


    @Transactional
    public void clearWorkFlowSince(String date) {
        User user = userRepository.findFirstByNameEquals(User.ADMIN);
        List<WorkFlowMessage> workFlowMessages = workFlowMessageRepository.findBySenderIdEqualsAndStateEquals(user.id, 0);
        System.out.println("totalcount:" + workFlowMessages.size());
        int size = 0;
        for (WorkFlowMessage message : workFlowMessages) {

            if (message.createTimeString.compareTo(date) > 0) {
                size++;
//                try {
//                    clearWorkFLow(user, message.orderName, message.itm);
//                } catch (HdException e) {
//                    e.printStackTrace();
//                }
            }
        }
        System.out.println("totalcount:" + size);
    }

    /**
     * 查询指定期间，流程已经结束，小工序未完工的单据。
     */
    @Transactional
    public List<Sub_workflow_state> searchErpSubWorkFlow(String key, String dateStart, String dateEnd) {

//        long startTime=0;
//
//        try {
//            startTime=DateFormats.FORMAT_YYYY_MM_DD.parse(dateStart).getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        long endTime=0;
//        try {
//            endTime=DateFormats.FORMAT_YYYY_MM_DD.parse(dateEnd).getTime()+24l*60*60*1000-1;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        return erpWorkRepository.searchErpSubWorkFlow(key, dateStart, dateEnd);

    }

    public ErpOrderItem findOrderItem(String orderName, int itm) {

        return erpWorkRepository.findOrderItem(orderName, itm);

    }


    public void updateWorkFlowReportSendingQty() {

        List<ErpWorkFlowReport> reports = erpWorkFlowReportRepository.findByPercentageLessThanAndStartDateGreaterThan(1, 0);


        for (ErpWorkFlowReport workFlowReport : reports) {
            //统计发送未处理数量
            final List<WorkFlowMessage> currentWorkFlowMessage = workFlowMessageRepository.findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(workFlowReport.workFlowStep, workFlowReport.osNo, workFlowReport.itm);
            int sendingQty = 0;
            for (WorkFlowMessage tem : currentWorkFlowMessage) {
                if (tem.receiverId == 0) {
                    sendingQty += tem.transportQty;
                }
            }

            workFlowReport.sendingQty = sendingQty;
            erpWorkFlowReportRepository.save(workFlowReport);
            erpWorkFlowReportRepository.flush();

        }
    }

    /**
     * 撤销任务交接
     *
     * @param user
     * @param messageId 任务消息id
     * @param memo      理由
     * @return
     */
    public RemoteData<Void> rollBackOrderItemWorkFlow(User user, long messageId, String memo) {


        WorkFlowMessage message = workFlowMessageRepository.findOne(messageId);
        if (message == null) {
            return wrapError("消息不存在：" + messageId);
        }

        if (message.state != WorkFlowMessage.STATE_PASS) {
            return wrapError("异常，消息未正常被处理：" + messageId);
        }

        if (!user.name.equals(User.ADMIN)) {

            return wrapError("只有系统管理员 ，才能撤销交接=" + messageId);
        }

//        {  暂时不处理下一道关联数量问题。
//            ErpOrderItemProcess erpOrderItemProcess = erpOrderItemProcessRepository.findFirstByOsNoEqualsAndItmEqualsAndCurrentWorkFlowStepEquals(message.orderName, message.itm, message.toFlowStep);
//
//
//            if (erpOrderItemProcess != null) {
//                if (erpOrderItemProcess.unSendQty < message.transportQty) {
//
//                    return wrapError("接受的流程 " + erpOrderItemProcess.currentWorkFlowName + "，未发送的数量：" + erpOrderItemProcess.unSendQty + ",不够进行撤回处理。");
//                }
//
//
//                //扣除接收流程的 当前未发送数量。
//                erpOrderItemProcess.unSendQty -= message.transportQty;
//
//                erpOrderItemProcessRepository.save(erpOrderItemProcess);
//            }
//        }
        ErpOrderItemProcess fromErpOrderItemProcess = erpOrderItemProcessRepository.findFirstByOsNoEqualsAndItmEqualsAndCurrentWorkFlowStepEquals(message.orderName, message.itm, message.fromFlowStep);


        //发起流程数量调整。
        fromErpOrderItemProcess.unSendQty += message.transportQty;
        fromErpOrderItemProcess.sentQty -= message.transportQty;
        erpOrderItemProcessRepository.save(fromErpOrderItemProcess);


        //发起流程的状态调整。
        ErpWorkFlowReport workFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(message.orderName, message.itm, message.fromFlowStep);
        //扣除发起方生产进度
        workFlowReport.percentage -= (float) message.transportQty / fromErpOrderItemProcess.orderQty / (workFlowReport.typeCount == 0 ? 1 : workFlowReport.typeCount);


        //统计发送未处理数量
        final List<WorkFlowMessage> currentWorkFlowMessage = workFlowMessageRepository.findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(workFlowReport.workFlowStep, workFlowReport.osNo, workFlowReport.itm);
        int sendingQty = 0;
        for (WorkFlowMessage tem : currentWorkFlowMessage) {
            if (tem.receiverId == 0) {
                sendingQty += tem.transportQty;
            }
        }

        workFlowReport.sendingQty = sendingQty;


        //消息状态改 撤销
        message.state = WorkFlowMessage.STATE_ROLL_BACK;
        message.memo += "\n 撤销原因：" + memo;
        message.memo += "\n 撤销时间：" + DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

        workFlowReport = erpWorkFlowReportRepository.save(workFlowReport);


        return wrapData();
    }

    /**
     * 校正流程相关数据的item值，保证一致性。
     *
     * @param user
     * @param osNo
     * @param prdNo
     * @return
     */
    @Transactional(rollbackFor = {HdException.class})
    public RemoteData<Void> adjustWorkFlowItem(User user, String osNo, String prdNo, String pVersion, int itm) throws HdException {

        if (!user.name.equals(User.ADMIN)) {
            return wrapError("只有系统管理员才能 校正流程相关数据的item值");
        }


        StringBuilder stringBuilder = new StringBuilder();

        int count = erpWorkFlowReportRepository.updateItmByOsNoAndPrdNo(osNo, prdNo, pVersion, itm);
//        if (count > 1) {
//            throw HdException.create("WorkFlowReport 更新超过一条记录");
//        }
        stringBuilder.append("WorkFlowReport 更新" + count + "条记录\n");
        count = erpOrderItemProcessRepository.updateItmByOsNoAndPrdNo(osNo, prdNo, pVersion, itm);
//        if (count > 1) {
//            throw HdException.create("erpOrderItemProcess 更新超过一条记录");
//        }
        stringBuilder.append("erpOrderItemProcess 更新" + count + "条记录\n");

//        count = orderItemWorkStateRepository.updateItmByOsNoAndPrdNo(osNo, prdNo, pVersion,itm);
////        if (count > 1) {
////            throw HdException.create("orderItemWorkState 更新超过一条记录");
////        }
//
//        stringBuilder.append("orderItemWorkState 更新" + count + "条记录\n");

        count = orderItemWorkMemoRepository.updateItmByOsNoAndPrdNo(osNo, prdNo, pVersion, itm);
//        if (count > 1) {
//            throw HdException.create("orderItemWorkMemo 更新超过一条记录");
//        }
        stringBuilder.append("orderItemWorkMemo 更新" + count + "条记录\n");

        count = workFlowMessageRepository.updateItmByOsNoAndPrdNo(osNo, prdNo, pVersion, itm);
//        if (count > 1) {
//            throw HdException.create("workFlowMessage 更新超过一条记录");
//        }
        stringBuilder.append("workFlowMessage 更新" + count + "条记录\n");
        // throw HdException.create(stringBuilder.toString());


//        //外购进度数据修复
//        List<ErpOrderItemProcess> processes=erpOrderItemProcessRepository.findFirstByOsNoEqualsAndPrdNoEqualsAndPVersionEquals(osNo,prdNo,pVersion);
//        for(ErpOrderItemProcess itemProcess:processes)
//        {
//
//            if(itemProcess.sentQty>0)
//            {
//             ErpWorkFlowReport report=erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(itemProcess.osNo,itemProcess.itm,itemProcess.currentWorkFlowStep);
//            if (report==null) {
//
//                    ErpWorkFlowReport erpWorkFlowReport = new ErpWorkFlowReport();
//                    erpWorkFlowReport.workFlowCode = itemProcess.currentWorkFlowCode;
//
//                    erpWorkFlowReport.workFlowStep = itemProcess.currentWorkFlowStep;
//                    erpWorkFlowReport.workFlowName = itemProcess.currentWorkFlowName;
//                    erpWorkFlowReport.osNo = itemProcess.osNo;
//                    erpWorkFlowReport.itm = itemProcess.itm;
//                    erpWorkFlowReport.prdNo = itemProcess.prdNo;
//                    erpWorkFlowReport.pVersion = itemProcess.pVersion;
//                    erpWorkFlowReport.typeCount =1;
//                    erpWorkFlowReport.percentage = itemProcess.sentQty/itemProcess.qty;
//                    //外厂产品类型无铁木
//                    erpWorkFlowReport.productType = ProductType.TYPE_NONE;
//                    erpWorkFlowReport.productTypeName = ProductType.TYPE_NONE_NAME;
//                    erpWorkFlowReportRepository.save(erpWorkFlowReport);
//
//
//            }
//
//
//        }


        final RemoteData<Void> voidRemoteData = wrapMessageData(stringBuilder.toString());
        return voidRemoteData;


    }


    /**
     * 查詢流程下 工單明細
     *
     * @param os_no
     * @param itm
     * @param flowCode
     * @return
     */
    public RemoteData<ErpWorkFlowItem> findErpWorkFlowItems(User user, String os_no, int itm, String flowCode) {


        List<ErpWorkFlowItem> items = erpWorkFlowRepository.findErpWorkFlowItems(os_no, itm);
        List<ErpWorkFlowItem> result = new ArrayList<>();
        for (ErpWorkFlowItem item : items) {

            if (item.mrpNo.startsWith(flowCode)) {

                item.tz_dd = StringUtils.clipSqlDateData(item.tz_dd);
                result.add(item);
            }
        }

        return wrapData(result);
    }

    public WorkFlowReportSummary findSummaryForReport(User user, ErpWorkFlowReport report) {
        WorkFlowReportSummary summary = new WorkFlowReportSummary();

        if (report.workFlowStep > ErpWorkFlow.LAST_STEP) {
            summary.canSendMessageCount = 0;
            summary.canReceiveMessageCount = 0;
        } else {


            List<WorkFlowMessage> needConfirmWorkFLowMessage = workFlowService.getNeedConfirmWorkFLowMessage(user, report.osNo, report.itm, report.workFlowStep, report.produceType);
            summary.canReceiveMessageCount = needConfirmWorkFLowMessage == null ? 0 : needConfirmWorkFLowMessage.size();
            int canSendMessageCount = 0;
            String errorMessage = null;
            if (report.percentage < 1) {
                RemoteData<ErpOrderItemProcess> availableOrderItemProcess = getAvailableOrderItemProcess(user, report.osNo, report.itm, report.workFlowStep);
                canSendMessageCount = availableOrderItemProcess.isSuccess() ? availableOrderItemProcess.totalCount : 0;
                errorMessage = availableOrderItemProcess.isSuccess() ? "" : (availableOrderItemProcess.msgCode == MSG_CODE_ERP ? availableOrderItemProcess.message : "");
            }
            summary.canSendMessageCount = canSendMessageCount;
            summary.errorMessage = errorMessage;

        }
        return summary;
    }


    /**
     * 每天凌晨5点 自动启动订单生产流程
     */
    @Transactional
    public void autoStartWorkFlow() {

        long time = System.currentTimeMillis();
        User loginUser = userRepository.findFirstByNameEquals(User.ADMIN);
        WorkFlowArea area = workFlowAreaRepository.findAll().get(0);
        List<ErpOrderItem> result = erpWorkRepository.searchUnStartOrderItems("", 0, 10000);
        logger.info("UnCompleteOrderItems:" + result.size());

        if (result.size() > 0) {

            for (ErpOrderItem orderItem : result) {

                logger.info(orderItem.os_no + orderItem.prd_name + orderItem.itm);
                startOrderItemWorkFlow(loginUser, orderItem, area);

            }

        }


        logger.info("use time in autoStart:" + (System.currentTimeMillis() - time));
    }


    /**
     * 启动生产流程数据
     *
     * @param loginUser
     * @param erpOrderItem
     * @param area
     * @return
     */
    public RemoteData startOrderItemWorkFlow(User loginUser, ErpOrderItem erpOrderItem, WorkFlowArea area) {


        RemoteData<ErpOrderItemProcess> itemProcessRemoteData = getAvailableOrderItemProcess(loginUser, erpOrderItem, ErpWorkFlow.FIRST_STEP);
        if (!itemProcessRemoteData.isSuccess()) {
            logger.info("itemProcessRemoteData:" + itemProcessRemoteData.message);

        } else if (itemProcessRemoteData.datas.size() > 0) {


            for (ErpOrderItemProcess itemProcess : itemProcessRemoteData.datas) {
                generateWorkFlowReports(itemProcess, erpOrderItem);
                OrderItemWorkState orderitemState = orderItemWorkStateRepository.findFirstByOsNoEqualsAndItmEquals(itemProcess.osNo, itemProcess.itm);

                if (orderitemState == null) {
                    orderitemState = new OrderItemWorkState();
                    orderitemState.osNo = itemProcess.osNo;
                    orderitemState.itm = itemProcess.itm;
                    orderitemState.url = itemProcess.photoUrl;
                    orderitemState.prdNo = itemProcess.prdNo;
                    orderitemState.pVersion = itemProcess.pVersion;
                    orderitemState.maxWorkFlowStep = itemProcess.currentWorkFlowStep;
                    orderitemState.maxWorkFlowCode = itemProcess.currentWorkFlowCode;
                    orderitemState.maxWorkFlowName = itemProcess.currentWorkFlowName;
                }
                //标记当前订单生产状态
                orderitemState.workFlowState = ErpWorkFlow.STATE_WORKING;
                orderitemState.workFlowDescribe = "系统自动发起";
                orderItemWorkStateRepository.save(orderitemState);

            }


        } else {
            logger.info("itemProcessRemoteData:" + "no available ErpOrderItemProcess");
        }
        return itemProcessRemoteData;
    }


    @Transactional(rollbackFor = HdException.class)
    public RemoteData<Void> resetOrderItemWorkFlow(User loginUser, String osNo, int itm) throws HdException {


        WorkFlowArea area = workFlowAreaRepository.findAll().get(0);
        ErpOrderItem erpOrderItem = erpWorkRepository.findOrderItem(osNo, itm);
        RemoteData remoteData = startOrderItemWorkFlow(loginUser, erpOrderItem, area);

        return wrapMessageData("重置成功" + (remoteData == null || remoteData.isSuccess() ? "" : ("，数据有异常:" + remoteData.message)));
    }


    public static final int MSG_CODE_ERP = 77;





    /**
     * 自动完成產品出庫流程
     */
    @Transactional
    public void autoCompleteStockOut() {
        //成品出库处理
        List<ErpWorkFlowReport> reports = erpWorkFlowReportRepository.findUnCompletedStep(ErpWorkFlow.STEP_CHENGPIN);
        for (ErpWorkFlowReport report : reports) {
            updateReportOnStockOut(report);

        }

    }
    /**
     * 自动完成外购产品入库流程
     */
    @Transactional
    public void autoCompletePurchaseStockIn() {
        //外厂进仓处理
        List<ErpWorkFlowReport> reports = erpWorkFlowReportRepository.findUnCompletedStep(ErpWorkFlow.FIRST_STEP, ProduceType.PURCHASE);
        for (ErpWorkFlowReport report : reports) {
            updateReportOnStockInPurchase(report);
        }
    }
    /**
     * 自动自制产品入库流程
     */
    @Transactional
    public void autoCompleteSelfMadeStockIn() {
        //自制进仓处理
        List<ErpWorkFlowReport> reports = erpWorkFlowReportRepository.findUnCompletedStep(ErpWorkFlow.STEP_BAOZHUANG, ProduceType.SELF_MADE);

        for (ErpWorkFlowReport report : reports) {

            updateReportOnStockInSelfMade(report);
        }
    }


    private void updateReportOnStock(ErpWorkFlowReport report, int i, List<StockSubmit> stockList) {
        ErpOrdItm erpOrderItem = erpWorkRepository.findOrdItm(report.osNo, report.itm);
        if(erpOrderItem==null) return;
        int count = 0;
        String dateString = "";
        for (StockSubmit stockSubmit : stockList) {
            if (stockSubmit.type == i) {
                count += stockSubmit.qty;
                if (stockSubmit.dd.compareTo(dateString) > 0) {
                    dateString = stockSubmit.dd;
                }
            }

        }
        if (count == 0) return;
        float percentage = (float) count / erpOrderItem.qty;
        if (Float.compare(percentage, report.percentage) == 0) return;
        report.percentage = percentage;
        updateWorkFlowQty(report, dateString);


        OrderItemWorkState orderItemState = orderItemWorkStateRepository.findFirstByOsNoEqualsAndItmEquals(erpOrderItem.os_no, erpOrderItem.itm);
        if (orderItemState != null) {
            boolean needUpdate = false;
            if (report.startDate > 0 && report.endDate <= 0) {
                orderItemState.maxWorkFlowStep = report.workFlowStep;
                orderItemState.maxWorkFlowCode = report.workFlowCode;
                orderItemState.maxWorkFlowName = report.workFlowName;
                needUpdate = true;
            }
            //最后一道如果已经完成 标记订单完成
            if (report.workFlowStep == ErpWorkFlow.STEP_CHENGPIN && report.percentage >= 1) {


                //最后一个流程完成  该货款生产结束
                orderItemState.workFlowState = ErpWorkFlow.STATE_COMPLETE;
                orderItemState.workFlowDescribe = ErpWorkFlow.STATE_NAME_COMPLETE;


                needUpdate = true;
            }


            if (needUpdate) {
                orderItemWorkStateRepository.save(orderItemState);
            }
        }

    }


    private void updateReportOnStockInSelfMade(ErpWorkFlowReport report) {
        if (Float.compare(report.percentage, 1) == 0) return;
        updateReportOnStock(report, 1, erpStockSubmitRepository.getOrderItemStockSubmitList(report.osNo, report.itm));
    }

    private void updateReportOnStockInPurchase(ErpWorkFlowReport report) {
        if (Float.compare(report.percentage, 1) == 0) return;
        updateReportOnStock(report, 2, erpStockSubmitRepository.getOrderItemStockSubmitList(report.osNo, report.itm));
    }


    @Transactional
    public  void updateReportOnStockOut(ErpWorkFlowReport report) {
        if (Float.compare(report.percentage, 1) == 0) return;
        updateReportOnStock(report, 3, erpStockSubmitRepository.getStockXiaokuList(report.osNo, report.itm));
    }


    @Transactional
    public RemoteData<Void> syncErpStockDataToReport(User user, String os_no, int itm, int flowStep) {

        ErpWorkFlowReport workFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(os_no, itm, flowStep);
        if (workFlowReport == null)
            return wrapError("没找到对应流程数据");


        if (flowStep == ErpWorkFlow.STEP_CHENGPIN) {


            //同步自制入库 组装-成品
            if (workFlowReport.produceType == ProduceType.SELF_MADE) {
                ErpWorkFlowReport stepBaozhuang = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(workFlowReport.osNo, workFlowReport.itm, ErpWorkFlow.STEP_BAOZHUANG);
                if (stepBaozhuang != null) {
                    updateReportOnStockInSelfMade(stepBaozhuang);
                }
            } else {
                //外厂 外厂加工进仓 白胚加工-成品
                ErpWorkFlowReport stepFirst = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(workFlowReport.osNo, workFlowReport.itm, ErpWorkFlow.FIRST_STEP);
                if (stepFirst != null) {
                    updateReportOnStockInPurchase(stepFirst);
                }
            }

            //同步仓库出库
            updateReportOnStockOut(workFlowReport);


            return wrapMessageData("同步完成");


        }


        return wrapError("当前流程不支持同步");


    }


    public void updateWorkFlowQty(ErpWorkFlowReport report, String dateString) {
        long timeInMillis = 0;
        try {
            if (!StringUtils.isEmpty(dateString))
                timeInMillis = DateFormats.FORMAT_YYYY_MM_DD.parse(dateString).getTime();
        } catch (Throwable t) {
        }
        if (timeInMillis == 0) {
            timeInMillis = Calendar.getInstance().getTimeInMillis();
        }
        if (report.percentage >= 1) {
            report.endDate = timeInMillis;
            report.endDateString = DateFormats.FORMAT_YYYY_MM_DD.format(report.endDate);
            updateWorkFlowReportOverDueState(report);
            ErpWorkFlow nextFlow = report.workFlowStep == ErpWorkFlow.LAST_STEP ? null : ErpWorkFlow.findNext(report.workFlowStep, report.produceType);
            if (nextFlow != null) {
                ErpWorkFlowReport erpWorkFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(report.osNo, report.itm, nextFlow.step);
                if (erpWorkFlowReport != null) {

                    erpWorkFlowReport.startDate = timeInMillis;
                    erpWorkFlowReport.startDateString = report.endDateString;
                    WorkFlowTimeLimit timeLimit = workFlowTimeLimitRepository.findFirstByOrderItemTypeEquals(erpWorkFlowReport.orderItemType);
                    updateErpWorkFlowReport(erpWorkFlowReport, timeLimit);
                    erpWorkFlowReportRepository.save(erpWorkFlowReport);
                }
            }


        }
        WorkFlowTimeLimit timeLimit = workFlowTimeLimitRepository.findFirstByOrderItemTypeEquals(report.orderItemType);
        updateErpWorkFlowReport(report, timeLimit);
        erpWorkFlowReportRepository.save(report);


    }

    @Transactional
    public RemoteData<Void> updateWorkFlowTimeLimit(User user, long erpWorkFLowReportId, int limitDay, int alertDay) {

        if (!user.isAdmin() && user.position != CompanyPosition.FACTORY_DIRECTOR_CODE) {


            return wrapError("只有管理员,厂长可以调整工期");
        }


        ErpWorkFlowReport report = erpWorkFlowReportRepository.findOne(erpWorkFLowReportId);
        if (report == null) {
            return wrapError("未找到流程数据");

        }

        report.limitDay = limitDay;
        report.alertDay = alertDay;
        report.hasUpdateLimit = true;
        updateWorkFlowReportOverDueState(report);

        erpWorkFlowReportRepository.save(report);


        return wrapMessageData("工期已经设置");


    }

    public RemoteData<Void> setReportMonitorState(User user, long erpWorkFLowReportId, int monitorState) {
        if (!user.isAdmin() && user.position != CompanyPosition.FACTORY_DIRECTOR_CODE) {


            return wrapError("只有管理员,厂长可以进行生产计划调整");
        }

        ErpWorkFlowReport report = erpWorkFlowReportRepository.findOne(erpWorkFLowReportId);
        if (report == null) {
            return wrapError("未找到流程数据");

        }

        report.state = monitorState;
        if(report.state==ErpWorkFlowReport.STATE_MONITOR)
        {

            report.monitorTime = Calendar.getInstance().getTimeInMillis();
            report.monitorTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
        }
        if (StringUtils.isEmpty(report.productUrl)) {


            Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(report.prdNo, report.pVersion);
            if (product != null) {
                report.thumbnail = product.thumbnail;
                report.productUrl = product.url;
            }
        }
        erpWorkFlowReportRepository.save(report);

        return wrapMessageData("生产计划已经调整");
    }

    public RemoteData<ErpWorkFlowReport> listMonitorReport(User user,String key, int pageIndex, int pageSize) {


        String sqlKey = StringUtils.sqlLike(key);
        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<ErpWorkFlowReport> page = erpWorkFlowReportRepository.findMonitoredWorkFlowReports(sqlKey , ErpWorkFlowReport.STATE_MONITOR,pageable);
        return wrapData(pageable, page);


    }
}
