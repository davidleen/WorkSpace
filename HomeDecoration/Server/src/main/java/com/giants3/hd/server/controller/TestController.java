package com.giants3.hd.server.controller;


import com.giants3.hd.entity.User;
import com.giants3.hd.server.config.Assets;
import com.giants3.hd.server.noEntity.AjaxData;
import com.giants3.hd.server.service.AuthorityService;
import com.giants3.hd.server.service.UserService;
import com.giants3.hd.server.service_third.MessagePushService;
import com.giants3.hd.utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController  extends BaseController{


	@Autowired
	private UserService userService;
	@Autowired
	private MessagePushService messagePushService;

	@RequestMapping(value = "hello",method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}
	@RequestMapping(value = "test_msg",method = RequestMethod.GET)
	public String goTestMsg(ModelMap model) {


		List<User> users = userService.list();
		model.addAttribute("message", "Hello world!");
		model.addAttribute("users", GsonUtils.toJson(users));
		model.addAttribute("server", Assets.ServerName);
		return "test_msg";
	}


	@RequestMapping(value = "sendMessage",method = RequestMethod.POST)
	public @ResponseBody
	AjaxData<Void>
	sendMessage(@RequestParam(value = "user"   ) long userId  , @RequestParam(value = "msg"   ) String message  ) {



		messagePushService.sendMessageToUser(userId,message);



		return new AjaxData<>();
	}

}