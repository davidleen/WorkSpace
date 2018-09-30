package com.giants3.hd.server.app.controller;


import com.giants3.hd.app.AProduct;
import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.controller.BaseController;
import com.giants3.hd.server.parser.DataParser;
import com.giants3.hd.server.parser.RemoteDataParser;
import com.giants3.hd.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 产品信息
 */
@Controller
@RequestMapping("/prdt")
public class PrdtController extends BaseController {


    @Autowired
    ProductService productService;


    @Autowired
    @Qualifier("productParser")
    private DataParser<Product, AProduct> dataParser;




    /**
     * 提供移动端接口  查询
     *
     * @param name
     * @param pageIndex
     * @return
     * @Param pageSize
     */
    @RequestMapping(value = "/search", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<AProduct> appSearch(@RequestParam(value = "name", required = false, defaultValue = "") String name
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    )   {


        RemoteData<Product> productRemoteData = productService.searchProductList(name, pageIndex, pageSize);
        RemoteData<AProduct> result = RemoteDataParser.parse(productRemoteData, dataParser);


        return result;


    }




}
