package com.giants3.hd.server.controller;


import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.ProductRelateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 工序
 */
@Controller
@RequestMapping("/process")
public class ProductProcessController extends BaseController {


    @Autowired
    private ProductRelateService productRelateService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ProductProcess> list() {

        return productRelateService.listProductProcesses();

    }


    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<ProductProcess> search(@RequestParam(value = "name", required = false, defaultValue = "") String name
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    ) throws UnsupportedEncodingException {


        return productRelateService.searchProductProcesses(name, pageIndex, pageSize);


    }

    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<ProductProcess> update(@RequestBody ProductProcess productProcess  ) {


        return productRelateService.updateProductProcesses(productProcess);


    }

    @RequestMapping(value = "/delete", method = {  RequestMethod.DELETE})
    public
    @ResponseBody
    RemoteData<Void> update(@RequestParam(value = "id", required = true, defaultValue = "-1") long  processId  ) {


        return productRelateService.deleteProductProcess(processId);


    }


    @RequestMapping(value = "/saveList", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<ProductProcess> saveList(@RequestBody List<ProductProcess> productProcesses) {


        return productRelateService.saveProductProcessList(productProcesses);

    }


}
