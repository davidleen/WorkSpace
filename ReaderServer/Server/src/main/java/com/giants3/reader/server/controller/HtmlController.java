package com.giants3.reader.server.controller;


import com.giants3.reader.entity.Settings;
import com.giants3.reader.server.service.AuthService;
import com.giants3.utils.Assets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pages")
public class HtmlController {

    @Autowired
    AuthService authService;
    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "hello";
    }


    @RequestMapping(value = "uploadfile", method = RequestMethod.GET)
    public String uploadfile(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "uploadfile";
    }

    @RequestMapping(value = "auth_codes", method = RequestMethod.GET)
    public String auth_codes(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        model.addAttribute("server", Assets.ServerName);
        Settings settings = authService.getSettings();
        model.addAttribute("rate", settings==null?0:settings.rate);
        model.addAttribute("requestCodeTime", settings==null?0:settings.authCodeRequestTime);
        return "auth_codes";
    } @RequestMapping(value = "table_test", method = RequestMethod.GET)
    public String table_test(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        model.addAttribute("server", Assets.ServerName);
        return "table_test";
    }


//	 public static void main(String[] args) throws Exception {
//		SpringApplication.run(HelloController.class, args);
//	}
}