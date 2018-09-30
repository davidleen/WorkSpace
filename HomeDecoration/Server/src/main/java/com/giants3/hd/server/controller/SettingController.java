package com.giants3.hd.server.controller;


import com.giants3.hd.entity.Company;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 产品类别
 */
@Controller
@RequestMapping("/setting")
public class SettingController extends BaseController {


    @Autowired
    private SettingService settingService;


    @RequestMapping(value = "/listCompany", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Company> listCompany() {

        return wrapData(settingService.getCompany());
    }

    @RequestMapping(value = "/updateCompany", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Company> updateCompany(@RequestBody Company company) {


        return settingService.update(company);

    }


}
