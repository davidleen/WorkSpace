package com.giants3.hd.server.controller;


import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.WorkFlowMemoAuth;
import com.giants3.hd.server.service.ErpWorkService;
import com.giants3.hd.server.service.WorkFlowService;
import com.giants3.hd.server.utils.Constraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产流程
 */
@Controller
@RequestMapping("/workFlow")
public class WorkFlowController extends BaseController {


    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private ErpWorkService erpWorkService;


    /**
     * 获取配置的二级流程类型数据
     *
     * @return
     */
    @RequestMapping(value = "/subTypes", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowSubType> getWorkFlowSubTypes() {

        return workFlowService.getWorkFlowSubTypes();
    }

    /**
     * 获取配置的二级流程类型数据
     *
     * @return
     */
    @RequestMapping(value = "/saveSubTypes", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<WorkFlowSubType> saveSubTypes(@RequestBody List<WorkFlowSubType> workFlowSubTypes) {

        return workFlowService.saveSubTypes(workFlowSubTypes);
    }


    /**
     * 获取配置的一级流程类型数据
     *
     * @return
     */
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlow> getWorkFlowTypes() {

        return wrapData(workFlowService.getAllWorkFlow());
    }

    /**
     * 根据产品id查询 流程配置信息
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/findWorkFlowByProductId", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowProduct> findWorkFlowByProductId(@RequestParam(value = "productId") long productId) {

        return workFlowService.findWorkFlowByProductId(productId);

    }

    /**
     * 保存产品的流程配置信息
     *
     * @param workFlowProduct
     * @return
     */
    @RequestMapping(value = "/saveWorkFlowProduct", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<WorkFlowProduct> saveWorkFlowProduct(@RequestBody WorkFlowProduct workFlowProduct) {

        return workFlowService.saveWorkFlowProduct(workFlowProduct);

    }


    /**
     * 查询流程工作人表
     *
     * @return
     */
    @RequestMapping(value = "/workers", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowWorker> findWorkers() {

        return workFlowService.findWorkers();

    }

    /**
     * 查询流程工作人表
     *
     * @return
     */
    @RequestMapping(value = "/arrangers", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowArranger> findArrangers() {

        return workFlowService.findArrangers();

    }

    /**
     * 查询流程事件列表
     *
     * @return
     */
    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowEvent> findWorkFlowEvents() {

        return workFlowService.findWorkFlowEvents();


    }

    /**
     * 查询流程事件工作人表
     *
     * @return
     */
    @RequestMapping(value = "/eventWorkers", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowEventWorker> findWorkFlowEventWorkers() {

        return workFlowService.findWorkFlowEventWorkers();


    }

    /**
     * 保存流程工作人表
     *
     * @return
     */
    @RequestMapping(value = "/saveWorker", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<WorkFlowWorker> saveWorker(@RequestBody WorkFlowWorker workFlowWorker) {

        return workFlowService.saveWorker(workFlowWorker);

    }

    /**
     * 保存排厂工作人表
     *
     * @return
     */
    @RequestMapping(value = "/saveArranger", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<WorkFlowArranger> saveArranger(@RequestBody WorkFlowArranger workFlowWorker) {

        return workFlowService.saveArranger(workFlowWorker);

    }

    /**
     * 删除排厂工作人表
     *
     * @return
     */
    @RequestMapping(value = "/deleteArranger", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Void> deleteArranger(@RequestParam(value = "id") long id) {

        return workFlowService.deleteWorkFlowArranger(id);

    }

    /**
     * 删除流程节点工作人
     *
     * @return
     */
    @RequestMapping(value = "/deleteWorker", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Void> deleteWorker(@RequestParam(value = "id") long id) {

        return workFlowService.deleteWorkFlowWorker(id);

    }

    /**
     * 读取指定订单流程，流程的消息列表
     * cancelOrderWorkFlow?orderItemWorkFlowId
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/workFlowMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> getOrderItemWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user


            , @RequestParam(value = "os_no") String os_no
            , @RequestParam(value = "itm") int itm
            , @RequestParam(value = "workFlowStep") int workFlowStep


    ) {


        return workFlowService.getOrderItemWorkFlowMessage(user, os_no, itm, workFlowStep);


    }

    /**
     * 读取指定订单流程，流程的消息列表
     * cancelOrderWorkFlow?orderItemWorkFlowId
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowArea> findWorkFlowArea(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user


    ) {


        return workFlowService.findWorkFlowAreas();


    }

    /**
     * @param
     * @return
     */
    @RequestMapping(value = "/saveArea", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<WorkFlowArea> saveWorkFlowArea(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user

            , @RequestBody WorkFlowArea data


    ) {


        return workFlowService.saveWorkFlowArea(data);


    }

    /**
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteArea", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<WorkFlowArea> deleteWorkFlowArea(

            @RequestParam(value = "id") long id

    ) {


        return workFlowService.deleteWorkFlowArea(id);


    }

    /**
     * @param
     * @return
     */
    @RequestMapping(value = "/memoAuth", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMemoAuth> getWorkFlowMemoAuth(
            @ModelAttribute(Constraints.ATTR_LOGIN_USER) User user


    ) {


        return workFlowService.getWorkFlowMemoAuth(user);


    }

    /**
     * @param
     * @return
     */
    @RequestMapping(value = "/checkMemo", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMemoAuth> checkWorkFlowMemo(
            @ModelAttribute(Constraints.ATTR_LOGIN_USER) User user
            ,
            @RequestParam(value = "orderItemWorkMemoId") long orderItemWorkMemoId,
            @RequestParam(value = "check") boolean check


    ) {


        return workFlowService.checkWorkFlowMemo(user, orderItemWorkMemoId, check);


    }


    /**
     * @param
     * @return
     */
    @RequestMapping(value = "/limit", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowTimeLimit> getWorkFlowLimit(
            @ModelAttribute(Constraints.ATTR_LOGIN_USER) User user


    ) {


        return wrapData(workFlowService.getWorkFlowLimit());


    }
    /** 查询消息
     * @param
     * @return
     */
    @RequestMapping(value = "/findMessageById", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> findMessageById(
            @RequestParam(value = "workFlowMessageId") long workFlowMessageId
    ) {


        return wrapData(workFlowService.findMessageById(workFlowMessageId));


    }


    /**
     * @param updateCompletedOrderItem 是否更新已经完成的订单货款进度数据
     * @return
     */
    @RequestMapping(value = "/saveLimit", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Void> saveLimit(
            @ModelAttribute(Constraints.ATTR_LOGIN_USER) User user
            , @RequestBody List<WorkFlowTimeLimit> workFlowLimit
            , @RequestParam(value = "updateCompletedOrderItem",required = false) boolean updateCompletedOrderItem


    ) {


        RemoteData<Void> result = workFlowService.saveWorkFlowLimit(workFlowLimit,updateCompletedOrderItem);



        return result;

    }




    /** 查询未处理消息报表
     * @param hourLimit  多少小时未处理
     * @return
     */
    @RequestMapping(value = "/getUnHandleWorkFlowMessageReport", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> getUnHandleWorkFlowMessageReport(
            @RequestParam(value = "hourLimit",required = false,defaultValue = "0" ) int hourLimit
    ) {


        return wrapData(workFlowService.getUnHandleWorkFlowMessageReport(hourLimit));


    }


    /**
     * 报表查询
     * @param dateStart
     * @param dateEnd
     * @param unhandle  是否未处理
     * @param overdue   是否超期
     * @return
     */
    @RequestMapping(value = "/getWorkFlowMessageReport", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> getWorkFlowMessageReport(
            @RequestParam(value = "dateStart"  ) String dateStart
           , @RequestParam(value = "dateEnd"  ) String dateEnd
           , @RequestParam(value = "unhandle"  ) boolean unhandle
           , @RequestParam(value = "overdue"  ) boolean overdue
    ) {


        return wrapData(workFlowService.getWorkFlowMessageReport(dateStart,dateEnd,unhandle,overdue));


    }


}
