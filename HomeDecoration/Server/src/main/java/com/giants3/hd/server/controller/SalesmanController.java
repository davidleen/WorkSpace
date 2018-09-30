package com.giants3.hd.server.controller;


import com.giants3.hd.entity.User;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 产品类别
 */
@Controller
@RequestMapping("/salesman")
public class SalesmanController extends BaseController {


    @Autowired
    private UserService userService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<User> list() {


        return userService.listSalesmans();

    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<User> save(@RequestBody List<User> salesmans) {


        return userService.saveSalesmans(salesmans);


    }

}
