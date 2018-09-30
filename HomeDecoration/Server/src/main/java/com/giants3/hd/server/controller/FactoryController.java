package com.giants3.hd.server.controller;


import com.giants3.hd.entity.Factory;
import com.giants3.hd.entity.OutFactory;
import com.giants3.hd.server.service.FactoryService;
import com.giants3.hd.noEntity.RemoteData;
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
@RequestMapping("/factory")
public class FactoryController extends BaseController {


    @Autowired
    private FactoryService factoryService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Factory> list() {

        return factoryService.listFactory();
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Factory> save(@RequestBody List<Factory> factories) {


    return wrapData(    factoryService.save(factories));

    }


    /**
     * 外厂列表
     *
     * @return
     */
    @RequestMapping(value = "/out/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OutFactory> outList() {

        return factoryService.listOutFactory();
    } /**
     * 外厂列表
     *
     * @return
     */
    @RequestMapping(value = "/out/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<OutFactory> saveOutList(@RequestBody List<OutFactory> factories) {


       return  wrapData(factoryService.saveOutList(factories));

    }
}
