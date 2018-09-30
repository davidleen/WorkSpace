package com.giants3.hd.server.controller;


import com.giants3.hd.entity.User;
import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.noEntity.ErpStockOutDetail;
import com.giants3.hd.server.service.StockService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.entity.StockXiaoku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 出入库存相关
 */
@Controller
@RequestMapping("/stock")
public class StockController extends BaseController {


    @Autowired
    StockService stockService;


    @RequestMapping(value = "/outList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpStockOut> outList(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key", required = false, defaultValue = "") String key, @RequestParam(value = "salesId", required = false, defaultValue = "-1") long salesId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {

        return stockService.search(user, key, salesId, pageIndex, pageSize);
    }


    @RequestMapping(value = "/xiaokuList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<StockXiaoku> xiaokuList(@RequestParam(value = "key", required = false, defaultValue = "") String  key,@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {

        return stockService.getStockXiaokuList(key, pageIndex, pageSize);
    }

    @RequestMapping(value = "/findOutDetail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpStockOutDetail> findOutDetail(@RequestParam(value = "ck_no") String ck_no) {

        return stockService.findDetail(ck_no);
    }


    @RequestMapping(value = "/out/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<ErpStockOutDetail> save(@RequestBody ErpStockOutDetail stockOutDetail) {


        RemoteData<ErpStockOutDetail> detailRemoteData = stockService.saveOutDetail(stockOutDetail);
        return detailRemoteData;
    }

    @RequestMapping(value = "/stockInAndSubmitList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<StockSubmit> getStockInAndSubmitList(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate) {


        RemoteData<StockSubmit> detailRemoteData = stockService.getStockInAndSubmitList(startDate, endDate);
        return detailRemoteData;
    }




@RequestMapping(value = "/xiaokuItemList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<StockSubmit> getStockXiaokuItemList(@RequestParam(value = "ps_no") String ps_no   ) {


        RemoteData<StockSubmit> detailRemoteData = stockService.getStockXiaokuItemList(ps_no);
        return detailRemoteData;
    }


    @RequestMapping(value = "/xiaokuItemSearch", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<StockSubmit> getStockXiaokuItemList(@RequestParam(value = "key") String key ,@RequestParam(value = "dateStart") String dateStart,@RequestParam(value = "dateEnd") String dateEnd   ) {

        RemoteData<StockSubmit> detailRemoteData = stockService.getStockXiaokuItemList(key,dateStart, dateEnd);
        return detailRemoteData;
    }

}
