package com.giants3.hd.server.controller;

import com.giants3.hd.entity.HdTask;
import com.giants3.hd.entity.User;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.ScheduleService;
import com.giants3.hd.server.service.TaskService;
import com.giants3.hd.server.utils.Constraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 应用程序相关控制类
 * Created by davidleen29 on 2014/9/18.
 */
@Controller

@RequestMapping("/task")
public class TaskController extends BaseController {


    @Autowired
    ScheduleService scheduleService;

    @Autowired
    TaskService taskService;


    /**
     * 定时任务
     *
     * @return
     */
    @Deprecated  //关闭接口
    @RequestMapping(value = "/schedule", method = {RequestMethod.POST, RequestMethod.GET})

    public
    @ResponseBody
    RemoteData<HdTask> scheduleTask(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody HdTask hdTask) {

//
//        long current= Calendar.getInstance().getTimeInMillis();
//
//        //hdTask 数据检验
//        if(hdTask.startDate <=current)
//        {
//            return wrapError("提交的任务运行时刻不能比当前时小");
//        }
//        hdTask.activator=user.code+","+user.name+","+user.chineseName;
//        hdTask.activateTime= DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(current));
//
//        //添加新任务
//        postNewTask(hdTask);
//
//
//
//        return list(0,100);

        return null;


    }

    @RequestMapping(value = "/listLog", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<HdTask> listLog(@RequestParam(value = "taskId", required = true) long taskId, @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "100") int pageSize) {


        return taskService.listLog(taskId, pageIndex, pageSize);


    }

    @RequestMapping(value = "/updateState", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<HdTask> updateState(@RequestParam(value = "taskId", required = true) long taskId, @RequestParam(value = "taskState") int taskState) {


        return taskService.updateState(taskId, taskState);


    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<HdTask> list(@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "100") int pageSize) {


        return taskService.list(pageIndex, pageSize);


    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})

    public
    @ResponseBody
    RemoteData<HdTask> delete(@RequestParam(value = "id", required = true) long taskId) {


        return taskService.delete(taskId);


    }


    @RequestMapping(value = "/execute", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public RemoteData<Void> executeTask(@RequestParam(value = "taskType", required = true) int taskType) {

        return scheduleService.executeTask(taskType);
    }



}