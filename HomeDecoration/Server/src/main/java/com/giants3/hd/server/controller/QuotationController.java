package com.giants3.hd.server.controller;


import com.giants3.hd.entity.Quotation;
import com.giants3.hd.entity.QuotationDelete;
import com.giants3.hd.entity.User;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.QuotationService;
import com.giants3.hd.server.utils.Constraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * 报价
 */
@Controller
@RequestMapping("/quotation")
public class QuotationController extends BaseController {


    @Autowired
    private QuotationService quotationService;


    @Value("${deleteQuotationFilePath}")
    private String deleteQuotationFilePath;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Quotation> list() {


        return quotationService.list();

    }


    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Quotation> search(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue, @RequestParam(value = "salesmanId", required = false, defaultValue = "-1") long salesmanId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    ) throws UnsupportedEncodingException {


        return quotationService.search(user, searchValue, salesmanId, pageIndex, pageSize);


    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> detail(@RequestParam(value = "id", required = false, defaultValue = "") long id) {


        QuotationDetail detail = loadDetailById(id);


        if (detail == null)
            return wrapError("未找到id=" + id + "的报价记录数据");


        return wrapData(detail);
    }


    /**
     * 读取详情数据
     *
     * @param id
     * @return
     */
    private QuotationDetail loadDetailById(long id) {


        return quotationService.loadQuotationDetail(id);


    }


    /**
     * 产品完整信息的保存
     *
     * @param quotationDetail 报价单全部信息
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<QuotationDetail> saveQuotationDetail(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody QuotationDetail quotationDetail) {


        return quotationService.saveQuotationDetail(user, quotationDetail);


    }


    /**
     * 删除产品信息
     *
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/logicDelete", method = {RequestMethod.GET, RequestMethod.POST})

    public
    @ResponseBody
    RemoteData<Void> logicDelete(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("id") long quotationId) {

        //检查是否有关联数据

        return quotationService.logicDelete(user, quotationId);


    }


    @RequestMapping(value = "/searchDelete", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDelete> listDelete(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword, @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageCount) {

        return quotationService.listDelete(keyword, pageIndex, pageCount);


    }

    @RequestMapping(value = "/detailDelete", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> detailDelete(@RequestParam(value = "id") long quotationDeleteId) {


        return quotationService.findDeleteDetail(quotationDeleteId);


    }

    @RequestMapping(value = "/resumeDelete", method = RequestMethod.GET)

    public
    @ResponseBody
    RemoteData<QuotationDetail> resumeDelete(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "deleteQuotationId") long deleteQuotationId) {


        return quotationService.resumeDelete(user, deleteQuotationId);


    }


    @RequestMapping(value = "/unVerify", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<QuotationDetail> unVerify(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "quotationId") long quotationId) {

        return quotationService.unVerify(user, quotationId);


    }

    /**
     * 产品完整信息的保存
     *
     * @param quotationDetail 报价单全部信息
     * @return
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<QuotationDetail> verifyQuotationDetail(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody QuotationDetail quotationDetail) {


        return quotationService.verifyQuotationDetail(user, quotationDetail);


    }
}
