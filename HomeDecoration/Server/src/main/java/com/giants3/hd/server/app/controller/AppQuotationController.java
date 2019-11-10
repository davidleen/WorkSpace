package com.giants3.hd.server.app.controller;


import com.giants3.hd.entity.User;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.hd.server.app.service.AppQuotationService;
import com.giants3.hd.server.controller.BaseController;
import com.giants3.hd.server.repository.ProductRepository;
import com.giants3.hd.server.service.ReportService;
import com.giants3.hd.server.service.TableRestoreService;
import com.giants3.hd.server.utils.Constraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 报价
 */
@Controller
@RequestMapping("/app/quotation")
public class AppQuotationController extends BaseController {


    @Autowired
    private TableRestoreService tableRestoreService;
    @Autowired
    private AppQuotationService quotationService;
    @Autowired
    private ReportService reportService;



    @Value("${deleteQuotationFilePath}")
    private String deleteQuotationFilePath;


    @RequestMapping(value = "/search", method = {RequestMethod.GET})
    public
    @ResponseBody
        RemoteData<Quotation> search(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user
            , @RequestParam(value = "searchValue" , required = false, defaultValue = "") String searchValue
            , @RequestParam(value = "dateStart" , required = false, defaultValue = "") String dateStart
            , @RequestParam(value = "dateEnd" , required = false, defaultValue = "") String dateEnd
            , @RequestParam(value = "userId" , required = false, defaultValue = "-1") long  userId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex
            , @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    )  {

        RemoteData<Quotation> result = quotationService.search(user, searchValue,dateStart,dateEnd,userId, pageIndex, pageSize);


        return result;

    }


    @RequestMapping(value = "/create", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<QuotationDetail> createTemp(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user) {


        QuotationDetail quotationDetail = quotationService.createNew(user);


        return wrapData(quotationDetail);


    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> detail(@RequestParam(value = "id",required = false,defaultValue = "0") long id,@RequestParam(value = "qNumber", required = false,defaultValue = "") String qNumber) {


        return quotationService.loadAQuotationDetail(id);


    }
  @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> delete(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestParam(value = "quotationId", defaultValue = "0") long quotationId) {


        return quotationService.deleteQuotation(user,quotationId);


    }

    /**
     * 保存报价单（功能只是将临时报价单生产正式报价单）
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> save(@RequestParam(value = "quotationId", defaultValue = "0") long id) {


        return quotationService.save(id);


    } /**
     * 保存报价单（功能只是将临时报价单生产正式报价单）
     *
     * @param quotationDetail
     * @return
     */
    @RequestMapping(value = "/saveDetail", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<QuotationDetail> save(@RequestBody QuotationDetail quotationDetail) {


        return quotationService.saveDetail(quotationDetail);


    }
    /**
     * 保存报价单（功能只是将临时报价单生产正式报价单）
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateField", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateField(@RequestParam(value = "quotationId" ) long id,@RequestParam(value = "field" ) String  field,@RequestParam(value = "data" ) String data) {


        return quotationService.updateFieldValue(id,field,data);


    }

    /**
     * 修改报价单产品
     *
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/updateProduct", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateProduct(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "itemIndex") int itemIndex, @RequestParam(value = "productId") long productId) {


        return quotationService.updateProduct(quotationId, itemIndex, productId);


    }

    /**
     * 修改报价单产品
     *
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/updateItemQuantity", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateItemQuantity(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "itemIndex") int itemIndex, @RequestParam(value = "quantity") int quantity) {


        return quotationService.updateItemQuantity(quotationId, itemIndex, quantity);


    }

    /**
     * 修改报价单产品折扣
     *
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/updateItemDiscount", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateItemDiscount(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "itemIndex") int itemIndex, @RequestParam(value = "discount") float discount) {


        return quotationService.updateItemDiscount(quotationId, itemIndex, discount);


    }
 /**
     * 修改报价单产品折扣
     *
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/updateItemMemo", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateItemMemo(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "itemIndex") int itemIndex, @RequestParam(value = "memo") String memo) {


        return quotationService.updateItemMemo(quotationId, itemIndex, memo);


    }

    /**
     * 修改报价单全部折扣
     *
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/updateQuotationDiscount", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateQuotationDiscount(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "discount") float discount) {


        return quotationService.updateQuotationDiscount(quotationId, discount);


    }

    /**
     * 删除报价单中产品
     *
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/removeItem", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> removeItem(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "itemIndex") int itemIndex) {


        return quotationService.removeItem(quotationId, itemIndex);


    }

    /**
     * 添加报价单中产品
     *
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> addItem(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "productId") int productId) {


        return quotationService.addItem(quotationId, productId);


    }


    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public
    @ResponseBody
    FileSystemResource print(   @RequestParam(value = "quotationId") long quotationId) {


        final String filePath = reportService.printAppQuotationToPdf(quotationId);

        return new FileSystemResource(filePath);


    }


    @RequestMapping(value = "/updateMemo", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateMemo(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "memo") String memo) {


        return quotationService.updateMemo(quotationId, memo);


    }


    @RequestMapping(value = "/updateCustomer", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateCustomer(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "customerId") long customerId) {


      return  quotationService.updateCustomer(quotationId, customerId);




    }


    @RequestMapping(value = "/updateSaleman", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateSaleman(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "saleId") long saleId) {


        return quotationService.updateSaleman(quotationId, saleId);




    }


    @RequestMapping(value = "/updateItemPrice", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateItemPrice(@RequestParam(value = "quotationId") long quotationId, @RequestParam(value = "itemIndex") int item, @RequestParam(value = "price") float price) {


        return quotationService.updateItemPrice(quotationId, item, price);


    }
    @RequestMapping(value = "/sync", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> sync(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestParam(value = "urlHead") String urlHead, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate",required = false,defaultValue = "") String endDate) {


        return quotationService.syncData(user,urlHead,startDate,endDate);


    }


    /**
     * 区间报价次数统计
     * @return
     */
    @RequestMapping(value = "/reportQuoteCount", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Map> reportQuoteCount(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate",required = false,defaultValue = "") String endDate ) {


        return quotationService.reportQuoteCount(startDate,endDate);

    }

    /**
     * 查找报价详细信息列表
     * @param startDate
     * @param endDate
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/findDetails", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> findDetails(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate",required = false,defaultValue = "") String endDate, @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {


        return quotationService.findDetails(startDate,endDate,pageIndex,pageSize);


    }
    /**
     * 初始化广交会相关数据
     * @return
     */
    @RequestMapping(value = "/gjh_init", method = RequestMethod.GET)
    public
    @ResponseBody
    @Deprecated
    RemoteData<Void> gjh_init(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user ) {

        if(!user.isAdmin())
        {
            return wrapError("非系统管理员，没有权限");
        }

        try {
            tableRestoreService.restoreGJHTableData();
            return wrapData();
        } catch (Throwable  e) {
            e.printStackTrace();
            return wrapError(e.getMessage());
        }


    }





}
