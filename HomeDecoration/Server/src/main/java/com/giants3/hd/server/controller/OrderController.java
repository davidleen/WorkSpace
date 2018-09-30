package com.giants3.hd.server.controller;


import com.giants3.hd.server.service.WorkFlowService;
import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.ErpOrderDetail;
import com.giants3.hd.noEntity.OrderReportItem;
import com.giants3.hd.server.service.ErpService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.noEntity.RemoteData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品类别
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {


    @Autowired
    private ErpService erpService;
    @Autowired
    private WorkFlowService workFlowService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrder> list(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key", required = false, defaultValue = "") String key, @RequestParam(value = "salesId", required = false, defaultValue = "-1") long salesId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {

        return erpService.findByKey(user, key, salesId, pageIndex, pageSize);
    }

    @RequestMapping(value = "/reportByCheckDate", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrder> reportByCheckDate(@RequestParam(value = "key", required = false, defaultValue = "") String key, @RequestParam(value = "dateStart") String dateStart, @RequestParam(value = "dateEnd") String dateEnd
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {

        return erpService.findByCheckDate(key, dateStart, dateEnd, pageIndex, pageSize);
    }

    @RequestMapping(value = "/reportItemByCheckDate", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderReportItem> reportItemByCheckDate(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "saleId", required = false, defaultValue = "-1") long saleId, @RequestParam(value = "dateStart") String dateStart, @RequestParam(value = "dateEnd") String dateEnd
    ) {

        return erpService.findItemByCheckDate(user, saleId, dateStart, dateEnd);
    }


    @RequestMapping(value = "/searchOrderItem", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderItem> searchOrderItem(@RequestParam(value = "key") String key , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {


        return erpService.searchOrderItem(key,pageIndex,pageSize);
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrderDetail> findOrderItems(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestParam(value = "os_no") String os_no) {


        return erpService.getOrderDetail(user,os_no);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<ErpOrderDetail> save(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestBody ErpOrderDetail stockOutDetail) {


        RemoteData<ErpOrderDetail> detailRemoteData = erpService.saveOrderDetail(user,stockOutDetail);
        return detailRemoteData;
    }


    /**
     * 启动订单跟踪
     *
     * @param
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/startOrderTrack", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> startTrack(@RequestParam(value = "os_no") String os_no) {


//        RemoteData<Void> detailRemoteData = erpService.startTrack(os_no);
//        return detailRemoteData;
        return null;
    }


    /**
     * 获取生产流程表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/listWorkFlow", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlow> listWorkFlow() {


        RemoteData<WorkFlow> remoteData = erpService.getWorkFlowList();
        return remoteData;
    }

    /**
     * 获取生产流程表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/saveWorkFlow", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<WorkFlow> saveWorkFlow(@RequestBody List<WorkFlow> workFlowList) {


        RemoteData<WorkFlow> remoteData = erpService.saveWorkFlowList(workFlowList);
        return remoteData;
    }


    /**
     * 获取生产流程表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/unHandleWorkFlowMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> getUnHandleWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key",defaultValue = "",required = false) String key) {


        RemoteData<WorkFlowMessage> remoteData = erpService.getUnHandleWorkFlowMessage(user,key);
        return remoteData;
    }

 /**
     * 获取生产流交接消息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/workFlowMessageByOrderItem", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> getWorkFlowMessageByOrderItem(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user , @RequestParam(value = "osNo") String osNo, @RequestParam(value = "itm") int itm) {

        RemoteData<WorkFlowMessage> remoteData = erpService.getWorkFlowMessageByOrderItem(user,osNo,itm);
        return remoteData;
    }
/**
     * 我的生产流交接消息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/myWorkFlowMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> myWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestParam(value = "key",required = false,defaultValue = "") String key
            , @RequestParam(value = "pageIndex" ,defaultValue = "0") int pageIndex
            , @RequestParam(value = "pageSize",defaultValue = "30") int pageSize

    ) {

        RemoteData<WorkFlowMessage> remoteData = erpService.getMyWorkFlowMessage(user,key ,pageIndex,pageSize);
        return remoteData;
    }










    /**
     * 审核生产流程递交
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/checkWorkFlowMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> checkOrderItemWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "workFlowMsgId") long workFlowMsgId) {


        return erpService.checkOrderItemWorkFlow(user, workFlowMsgId);

    }









    /**
     * 获取可以发送流程的订单货款
     * sendWorkFlowMessage?orderItemId=%d%flowStep=%d&tranQty=%d
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getSendWorkFlowMessageList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> getSendWorkFlowMessageList(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user

    ) {


        return erpService.getSendWorkFlowMessageList(user);

    }


    /**
     * 获取未完成的订单货款
     *
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/unCompleteOrderItem", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrderItemProcess> getUnCompleteOrderItem(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user   ) {


        return erpService.getUnCompleteOrderItem(user);

    }





/**
     *  获取订单的流程配置
     *
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getOrderItemWorkFlow", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderItemWorkFlow> getOrderItemWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user

            , @RequestParam(value = "orderItemId" ) long orderItemId



    ) {


        return workFlowService.getOrderItemWorkFlow(orderItemId);



    }

    /**
     *  获取用户可工作的订单列表
     *
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/searchOrderItems", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrderItem> searchUserWorkOrderItems(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user
            , @RequestParam(value = "key" ) String key
            , @RequestParam(value = "pageIndex" ,defaultValue = "0") int pageIndex
            , @RequestParam(value = "pageSize",defaultValue = "30") int pageSize
    ) {
        return erpService.searchUserWorkOrderItems(user, key,pageIndex,pageSize);



    }
    /**
     *  cancelOrderWorkFlow?orderItemWorkFlowId
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/cancelOrderWorkFlow", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> cancelOrderWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user


            , @RequestParam(value = "orderItemWorkFlowId" ) long orderItemWorkFlowId

    ) {


        return erpService.cancelOrderWorkFlow(user,orderItemWorkFlowId);



    }


}