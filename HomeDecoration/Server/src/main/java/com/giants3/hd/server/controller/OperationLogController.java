package com.giants3.hd.server.controller;


import com.giants3.hd.entity.OperationLog;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 材料信息控制类。
 */
@Controller
@RequestMapping("/operationLog")
public class OperationLogController extends BaseController {

    @Autowired
    private LoggerService loggerService;


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OperationLog> search(@RequestParam("className") String className, @RequestParam("recordId") long id) {


        return wrapData(loggerService.search(className, id));


    }

}
