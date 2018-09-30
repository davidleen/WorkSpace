package com.giants3.hd.server.controller;


import com.giants3.hd.entity.PClass;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.ProductRelateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 产品类别
 */
@Controller
@RequestMapping("/productClass")
public class ProductClassController extends BaseController {


    @Autowired
    private ProductRelateService productRelateService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<PClass> list() {
        return wrapData(productRelateService.listProductClasses());

    }


}
