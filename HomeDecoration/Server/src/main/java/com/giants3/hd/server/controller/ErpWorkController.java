package com.giants3.hd.server.controller;

import com.giants3.hd.entity.*;
import com.giants3.hd.entity_erp.*;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.WorkFlowReportSummary;
import com.giants3.hd.server.repository.ErpWorkFlowReportRepository;
import com.giants3.hd.server.service.ErpSampleService;
import com.giants3.hd.server.service.ErpWorkService;
import com.giants3.hd.server.service.UserService;
import com.giants3.hd.server.service.WorkFlowService;
import com.giants3.hd.server.utils.Constraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/erpWork")
public class ErpWorkController extends BaseController {


    @Autowired
    ErpWorkService erpWorkService;

    @Autowired
    UserService userService;

    @Autowired
    ErpSampleService erpSampleService;



    /**
     * 查询指令单
     *
     * @param osName
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/searchZhiling", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<Zhilingdan> searchZhilingdan(@RequestParam("osName") String osName, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {


        return erpWorkService.searchZhilingdan(osName, startDate, endDate);
    }

//    /**
//     * 查询订单的排厂记录
//     *
//     * @param os_no
//     * @param prd_no
//     * @return
//     */
//    @RequestMapping(value = "/findOrderItemProcess", method = RequestMethod.GET)
//    @ResponseBody
//    public RemoteData<ErpOrderItemProcess> findOrderItemProcess(@RequestParam("os_no") String os_no, @RequestParam("prd_no") String prd_no, @RequestParam(value = "pVersion", required = false, defaultValue = "") String pversion) {
//
//
//        return wrapData(erpWorkService.findOrderItemProcess(os_no, prd_no, pversion));
//    }


    /**
     * 发送订单流程
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/sendWorkFlowMessage", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Void> sendWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user
            , @RequestBody ErpOrderItemProcess orderItemProcess
            , @RequestParam(value = "tranQty") int tranQty
            , @RequestParam(value = "areaId") long areaId
            , @RequestParam(value = "memo") String memo
    ) {


        RemoteData<Void> remoteData = null;
        try {
            remoteData = erpWorkService.sendWorkFlowMessage(user, orderItemProcess, tranQty, areaId, memo);
        } catch (HdException e) {
            e.printStackTrace();
            return wrapError(e.getMessage());
        }


        return remoteData;

    }

    /**
     * 查询订单的排厂记录
     *
     * @param os_no
     * @param itm
     * @return
     */
    @RequestMapping(value = "/getAvailableOrderItemProcess", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ErpOrderItemProcess> getAvailableOrderItemProcess(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("os_no") String os_no, @RequestParam("itm") int itm
            , @RequestParam("flowStep") int flowStep) {


        return erpWorkService.getAvailableOrderItemProcess(user, os_no, itm, flowStep);
    }

    /**
     * 接收生产流程递交
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/receiveWorkFlowMessage", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Void> receiveWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "workFlowMsgId") long workFlowMsgId, @RequestParam("image") MultipartFile[] files) {


        try {
            return erpWorkService.receiveOrderItemWorkFlow(user, workFlowMsgId, files, "测试区域");
        } catch (HdException e) {

            return wrapError(e.getMessage());
        }

    }

    /**
     * 撤销生产流程交接(交接已经完成然后撤销)
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/rollbackWorkFlowMessage", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Void> rollBackWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "workFlowMsgId") long workFlowMsgId, @RequestParam("memo") String memo) {


        return erpWorkService.rollBackOrderItemWorkFlow(user, workFlowMsgId, memo);

    }

    /**
     * 查詢所有排產未完工
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/searchUnCompleteOrderItems", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<ErpOrderItem> searchUnCompleteOrderItems(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key") String key

            , @RequestParam(value = "workFlowStep", defaultValue = "-1", required = false) int workFlowStep
            , @RequestParam(value = "pageIndex", defaultValue = "0", required = false) int pageIndex
            , @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize
    ) {


        return erpWorkService.searchUnCompleteOrderItems(key, workFlowStep, pageIndex, pageSize);

    }/**
     * 查詢所有排產未完工
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/queryOrderItems", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<ErpOrderItem> queryOrderItems(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key" ,defaultValue = "", required = false) String key

            , @RequestParam(value = "workFlowState", defaultValue = "-1", required = false) int workFlowState
            , @RequestParam(value = "workFlowStep", defaultValue = "-1", required = false) int workFlowStep
            , @RequestParam(value = "alertType", defaultValue = "-1", required = false) int alertType
            , @RequestParam(value = "pageIndex", defaultValue = "0", required = false) int pageIndex
            , @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize
    ) {


        return erpWorkService.queryOrderItem(key,workFlowState, workFlowStep,alertType, pageIndex, pageSize);

    }

    /**
     * 查詢所有已經完工
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/searchCompleteOrderItems", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<ErpOrderItem> searchCompleteOrderItems(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key", required = false) String key

            , @RequestParam(value = "pageIndex", defaultValue = "0", required = false) int pageIndex
            , @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize
    ) {


        return erpWorkService.searchCompleteOrderItems(key, pageIndex, pageSize);

    }  /**
     * 查詢成品入库未完工
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/searchStockInUnCompleteOrderItems", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<ErpOrderItem> searchStockInUnCompleteOrderItems(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key", required = false) String key

            , @RequestParam(value = "pageIndex", defaultValue = "0", required = false) int pageIndex
            , @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize
    ) {


        return erpWorkService.searchStockInButUnCompleteOrderItems(key, pageIndex, pageSize);

    }

    /**
     * 查询订单货款的生产进度
     *
     * @param os_no
     */
    @RequestMapping(value = "/findOrderItemReport", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ErpWorkFlowReport> findOrderItemReport(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestParam("os_no") String os_no, @RequestParam("itm") int itm

    ) {


        RemoteData<ErpWorkFlowReport> erpWorkFlowReport = erpWorkService.findErpWorkFlowReport(os_no, itm);
        if(erpWorkFlowReport.isSuccess())
        {




            for ( ErpWorkFlowReport report:erpWorkFlowReport.datas)
            {
                report.summary=  erpWorkService.findSummaryForReport(user,report);
            }
        }
        return erpWorkFlowReport;
    }


    /**
     * 流程审核拒绝  返工
     * sendWorkFlowMessage?orderItemId=%d%flowStep=%d&tranQty=%d
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/rejectWorkFlowMessage", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Void> rejectWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "workFlowMsgId") long workFlowMsgId, @RequestParam("image") MultipartFile[] files, @RequestParam(value = "memo") String memo) {
        return erpWorkService.rejectWorkFlowMessage(user, workFlowMsgId, files, memo);

    }





    /**
     * 流程审核拒绝  返工
     * sendWorkFlowMessage?orderItemId=%d%flowStep=%d&tranQty=%d
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/syncErpStockDataToReport", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Void> syncErpStockDataToReport(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("os_no") String osNo, @RequestParam("itm") int itm, @RequestParam("workflowstep") int workflowstep) {

//        testSyncAll();
//        return wrapData();

        return erpWorkService.syncErpStockDataToReport(user, osNo, itm, workflowstep);

    }


//    @Autowired
//    ErpWorkFlowReportRepository erpWorkFlowReportRepository;
//
//    private void testSyncAll()
//    {
//        List<ErpWorkFlowReport> reports=erpWorkFlowReportRepository.findAllByOrderStateEqualsAndFlowStepEquals(ErpWorkFlow.STATE_WORKING,ErpWorkFlow.STEP_CHENGPIN);
//        for(ErpWorkFlowReport erpWorkFlowReport:reports)
//        {
//            try {
//                erpWorkService.syncOnErpWorkFlowReport(erpWorkFlowReport);
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
    /**
     * 更新流程的生产期限参数
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/updateWorkFlowTimeLimit", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Void> updateWorkFlowTimeLimit(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("erpWorkFLowReportId") long erpWorkFLowReportId, @RequestParam("limitDay") int limitDay, @RequestParam("alertDay") int alertDay) {
        return erpWorkService.updateWorkFlowTimeLimit(user, erpWorkFLowReportId, limitDay, alertDay);

    }




    /**
     * 更新流程的生产期限参数
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/setReportMonitorState", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Void> setReportMonitorState(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("erpWorkFLowReportId") long erpWorkFLowReportId, @RequestParam("monitorState") int monitorState ) {
        return erpWorkService.setReportMonitorState(user, erpWorkFLowReportId, monitorState );

    }





    /**
     * 查询受监控的生产流程列表
     *
     * @param completeState  -1 所有 0 未完成 1 已完成
     * @return
     */
    @RequestMapping(value = "/searchMonitorList", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<ErpWorkFlowReport> searchMonitorList(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("key") String key

            , @RequestParam(value = "completeState", defaultValue = "-1", required = false) int completeState
            , @RequestParam(value = "pageIndex", defaultValue = "0", required = false) int pageIndex
            , @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize

    ) {
        return erpWorkService.listMonitorReport(user,key,completeState, pageIndex, pageSize);

    }





    /**
     * 查询订单的生产备注
     *
     * @param os_no
     * @param itm
     * @return
     */
    @RequestMapping(value = "/getOrderItemWorkMemos", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<OrderItemWorkMemo> getOrderItemWorkMemos(@RequestParam("os_no") String os_no, @RequestParam("itm") int itm

    ) {
        return erpWorkService.getOrderItemWorkMemo(os_no, itm);
    }

    /**
     * 查询 货款的生产备注
     *
     * @param productName
     * @param pVersion
     * @return
     */
    @RequestMapping(value = "/getProductWorkMemos", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ProductWorkMemo> getProductWorkMemos(@RequestParam("productName") String productName, @RequestParam("pVersion") String pVersion

    ) {
        return erpWorkService.getProductWorkMemo(productName, pVersion);
    }


    /**
     * 查询 货款的生产备注
     *
     * @return
     */
    @RequestMapping(value = "/saveWorkMemo", method = RequestMethod.POST)
    @ResponseBody
    public RemoteData<Void> saveWorkMemo(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody Map<String, String> data

    ) {


        int workFlowStep = Integer.valueOf(data.get("workFlowStep"));
        String os_no = (String) data.get("os_no");
        int itm = Integer.valueOf(data.get("itm"));
        String orderItemWorkMemo = (String) data.get("orderItemWorkMemo");
        String prd_name = (String) data.get("prd_name");
        String pVersion = (String) data.get("pVersion");
        String productWorkMemo = (String) data.get("productWorkMemo");

        return erpWorkService.saveWorkMemo(user, workFlowStep, os_no, itm, orderItemWorkMemo, prd_name, pVersion, productWorkMemo);
    }

    /**
     * 查询流程的生产材料列表
     *
     * @return
     */
    @RequestMapping(value = "/workFlowMaterials", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<WorkFlowMaterial> getWorkFlowMaterials(@RequestParam("osNo") String osNo, @RequestParam("itm") int itm, @RequestParam("workFlowCode") String workFlowCode

    ) {


        return erpWorkService.getWorkFlowMaterials(osNo, itm, workFlowCode);
    }


    /**
     * 查询样品的状态
     *
     * @return
     */
    @RequestMapping(value = "/findSampleState", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<SampleState> getSampleState(@RequestParam("prdNo") String prdNo, @RequestParam(value = "pVersion", required = false, defaultValue = "") String pVersion) {

        SampleState sampleState = erpSampleService.getSampleState(prdNo, pVersion);
        if (sampleState == null) return wrapData();
        return wrapData(sampleState);
    }


    /**
     * 查询指定期间，流程已经结束，小工序未完工的单据。
     */
    @RequestMapping(value = "/searchErpSubWorkFlow", method = RequestMethod.GET)
    @ResponseBody


    public RemoteData<Sub_workflow_state> searchErpSubWorkFlow(@RequestParam("key") String key, @RequestParam("dateStart") String dateStart, @RequestParam("dateEnd") String dateEnd) {

        List<Sub_workflow_state> sampleState = erpWorkService.searchErpSubWorkFlow(key, dateStart, dateEnd);
        if (sampleState == null) return wrapData();
        return wrapData(sampleState);
    }


    /**
     * 清除流程
     *
     * @return
     */
    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<Void> clearWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("osNO") String osNo, @RequestParam(value = "itm") int itm) {

        RemoteData<Void> result = null;
        try {
            result = erpWorkService.clearWorkFLow(user, osNo, itm);
        } catch (HdException e) {
            e.printStackTrace();

            return wrapError(e.getMessage());
        }

        return result;
    }

    /**
     * 校正流程的item字段信息
     *
     * @return
     */
    @RequestMapping(value = "/adjustItem", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<Void> adjustItem(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("osNo") String osNo, @RequestParam(value = "prdNo") String prdNo, @RequestParam(value = "pVersion") String pVersion, @RequestParam(value = "itm") int itm) {

        RemoteData<Void> result = null;
        try {
            result = erpWorkService.adjustWorkFlowItem(user, osNo, prdNo, pVersion, itm);
        } catch (HdException e) {
            e.printStackTrace();
            return wrapError(e.getMessage());
        }

        return result;
    }

    /**
     *
     *
     * @return
     */
    @RequestMapping(value = "/findErpWorkFlowItems", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ErpWorkFlowItem> findErpWorkFlowItems(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("osNo") String osNo, @RequestParam(value = "itm") int itm, @RequestParam(value = "flowCode") String flowCode) {
        return erpWorkService.findErpWorkFlowItems(user, osNo, itm, flowCode);

    }


    /**
     * 校正流程的item字段信息
     *
     * @return
     */
    @RequestMapping(value = "/findErpOrderItem", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ErpOrderItem> findErpWorkFlowItems(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("osNo") String osNo, @RequestParam(value = "itm") int itm ) {
        ErpOrderItem orderItem = erpWorkService.findOrderItem(osNo, itm);
        if(orderItem==null)
        {
            return wrapError("没找到osNo="+osNo+",item="+itm+"的订单款项");

        }
        return wrapData(orderItem);

    }


    /**
     * 每天凌晨5点 自动启动订单生产流程
     */
//    @Scheduled(cron = "0 0 5 * * ?")
    @Scheduled(cron = "0 0/60 8-20 * * ?")   //早上8点到晚上22点之前  每小时执行一次。
//    @Scheduled(fixedDelay = 1l*60*60*1000 )  //没间隔一小时，执行一次
   public void autoStartWorkFlow()
    {

        erpWorkService.autoStartWorkFlow();
    }

    /**
     * 清除流程
     *
     * @return
     */
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<Void> resetWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("osNO") String osNo, @RequestParam(value = "itm") int itm) {




        RemoteData<Void> result = null;
        try {

             result = erpWorkService.clearWorkFLow(user, osNo, itm);

            if(result.isSuccess()) {
                RemoteData<Void> voidRemoteData = erpWorkService.resetOrderItemWorkFlow(user, osNo, itm);
                result = voidRemoteData;
//            if(!voidRemoteData.isSuccess())
//                return voidRemoteData;
//            result = erpWorkService.resetOrderItemWorkFlow(user, osNo, itm);
            }
        } catch (HdException e) {
            e.printStackTrace();

            return wrapError(e.getMessage());
        }

        return result;
    }
}
