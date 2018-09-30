package com.giants3.hd.server.controller;


import com.giants3.hd.entity.GlobalData;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service_third.PushService;
import com.giants3.hd.server.service.ScheduleService;
import com.giants3.hd.server.service.WorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.AbstractController;

@Controller
@RequestMapping("/schedule")
public class ScheduleTaskController  extends BaseController{


	@Autowired
	ScheduleService scheduleService;
	@Autowired
	WorkFlowService workFlowService;
	@RequestMapping(value = "hello",method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}


 	@RequestMapping(value = "alertUnHandleMessage",method = RequestMethod.GET)
	public  @ResponseBody
	RemoteData<Void> alertWorkFlowMessage()
	{


		workFlowService.alertUnHandleMessage();

		return wrapData();

	}


}