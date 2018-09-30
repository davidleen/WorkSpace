package com.giants3.hd.server.controller;

import com.giants3.hd.entity.GlobalData;
import com.giants3.hd.entity.User;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.GlobalDataService;
import com.giants3.hd.server.service.ProductRelateService;
import com.giants3.hd.server.utils.Constraints;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

/**
 * 应用程序相关控制类
 * Created by davidleen29 on 2014/9/18.
 */
@Controller

@RequestMapping("/application")
public class ApplicationController extends BaseController {
    private static final Logger logger = Logger.getLogger(ApplicationController.class);

    @Autowired
    private GlobalDataService globalDataService;

    @Autowired
    private ProductRelateService productRelateService;


    @RequestMapping(value = "/setGlobal", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<GlobalData> setGlobal(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody GlobalData globalData) {
        logger.info("start  setGlobal  ");
        RemoteData<GlobalData> result;
        try {

            long time = Calendar.getInstance().getTimeInMillis();
            final RemoteData<GlobalData> globalDataRemoteData = globalDataService.updateGlobalData(user, globalData);
            logger.info("time use in setGlobal :" + (Calendar.getInstance().getTimeInMillis() - time));
            result = globalDataRemoteData;
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error(this, e);
            result = wrapError(e.getMessage());
        }

        logger.info("start  setGlobal end ..... " + result.message);
        return result;
    }


    /**
     * 特殊接口，临时使用
     * 更新咸康数据  如果product.xiankang_id 为空或者为0  则 更新值
     *
     * @return
     */
    @RequestMapping(value = "/updateXiankang", method = {RequestMethod.POST, RequestMethod.GET})

    public
    @ResponseBody
    RemoteData<Void> updateXiankang() {

   return      productRelateService.updateXiankang();






    }


}