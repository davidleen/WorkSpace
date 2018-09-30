package com.giants3.hd.server.controller;


import com.giants3.hd.entity.PackMaterialClass;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.MaterialRelateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 材料信息控制类。
 */
@Controller
@RequestMapping("/packMaterialClass")
public class PackMaterialClassController extends BaseController {

    @Autowired
    private MaterialRelateService materialRelateService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<PackMaterialClass> list() {


        return wrapData(materialRelateService.listPackMaterialClasses());

    }


    @RequestMapping(value = "/saveList", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<PackMaterialClass> saveClassList(@RequestBody List<PackMaterialClass> materialClasses) {

        return materialRelateService.savePackMaterialClassList(materialClasses);
    }

}
