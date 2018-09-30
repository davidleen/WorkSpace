package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.server.utils.BackDataHelper;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * quotation 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class QuotationService extends AbstractService   {
    @Autowired
    private QuotationXKItemRepository quotationXKItemRepository;
    @Autowired
    private QuotationItemRepository quotationItemRepository;
    @Autowired
    private QuotationLogRepository quotationLogRepository;

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    QuoteAuthRepository quoteAuthRepository;

    @Autowired
    private OperationLogRepository operationLogRepository;


    @Value("${deleteQuotationFilePath}")
    private String deleteQuotationFilePath;
    @Autowired
    private QuotationDeleteRepository quotationDeleteRepository;


    public RemoteData<Quotation> search(User loginUser, String searchValue, long salesmanId
            , int pageIndex, int pageSize

    ) throws UnsupportedEncodingException {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<Quotation> pageValue;



        if (salesmanId < 0) {

         List<QuoteAuth> quoteAuths= quoteAuthRepository.findByUser_IdEquals(loginUser.id);
            if(quoteAuths.size()>0&&!StringUtils.isEmpty(quoteAuths.get(0).relatedSales))
            {

               String[] ids=quoteAuths.get(0).relatedSales.split(",|，");
                long[] longIds=new long[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    longIds[i]=Long.valueOf(ids[i]);
                }

                 pageValue = quotationRepository.findByCustomerNameLikeAndSalesmanIdInOrQNumberLikeAndSalesmanIdInOrderByQDateDesc("%" + searchValue + "%",longIds, "%" + searchValue + "%", longIds, pageable);



            }else {
                //表示所有人
                pageValue = quotationRepository.findByCustomerNameLikeOrQNumberLikeOrderByQDateDesc("%" + searchValue + "%", "%" + searchValue + "%", pageable);
            }
            } else {
            pageValue = quotationRepository.findByCustomerNameLikeAndSalesmanIdEqualsOrQNumberLikeAndSalesmanIdEqualsOrderByQDateDesc("%" + searchValue + "%", salesmanId, "%" + searchValue + "%", salesmanId, pageable);

        }
        List<Quotation> products = pageValue.getContent();

        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }

    /**
     *
     * @param id
     * @return
     */
    public QuotationDetail loadQuotationDetail(long id) {
        Quotation quotation = quotationRepository.findOne(id);

        if (quotation == null)
            return null;

        QuotationDetail detail = new QuotationDetail();
        detail.quotation = quotation;
        detail.quotationLog = quotationLogRepository.findFirstByQuotationIdEquals(id);
        detail.items = quotationItemRepository.findByQuotationIdEqualsOrderByIIndex(id);

        detail.XKItems = quotationXKItemRepository.findByQuotationIdEqualsOrderByIIndex(id);


        return detail;
    }

    @Transactional
    public RemoteData<QuotationDetail> saveQuotationDetail(User user, QuotationDetail quotationDetail) {
        Quotation newQuotation = quotationDetail.quotation;

        if (!quotationRepository.exists(newQuotation.id)) {
            //检查单号唯一性
            if (quotationRepository.findFirstByqNumberEquals(newQuotation.qNumber) != null) {

                return wrapError("报价单号:" + newQuotation.qNumber
                        + "已经存在,请更换");
            }

            newQuotation.id = -1;
        } else {


            Quotation oldQuotation = quotationRepository.findOne(newQuotation.id);

            if (!oldQuotation.qNumber.equals(newQuotation.qNumber)) {

                //检查单号唯一性
                if (quotationRepository.findFirstByqNumberEquals(newQuotation.qNumber) != null) {

                    return wrapError("报价单号:" + newQuotation.qNumber
                            + "已经存在,请更换");
                }

            }


            newQuotation.id = oldQuotation.id;
        }




        //空白行过滤
        if(quotationDetail.quotation.quotationTypeId== Quotation.QUOTATION_TYPE_NORMAL)
        {
            List<QuotationItem> valuableItem=new ArrayList<>();
            for(QuotationItem item:quotationDetail.items)
            {
                if(!item.isEmpty())
                    valuableItem.add(item);
            }
            quotationDetail.items.clear();
            quotationDetail.items.addAll(valuableItem);


        }else
        {
            List<QuotationXKItem> valuableItem=new ArrayList<>();
            for(QuotationXKItem item:quotationDetail.XKItems)
            {
                if(!item.isEmpty())
                    valuableItem.add(item);
            }
            quotationDetail.XKItems.clear();
            quotationDetail.XKItems.addAll(valuableItem);
        }

        //保存基本数据
        Quotation savedQuotation = quotationRepository.save(newQuotation);


        //更新修改记录
        updateQuotationLog(savedQuotation, user);

        long newId = savedQuotation.id;
        {

            //find items that removed
            List<QuotationItem> oldQuotationItems = quotationItemRepository.findByQuotationIdEqualsOrderByIIndex(newId);


            List<QuotationItem> removeItems = new ArrayList<>();
            for (QuotationItem oldQuotationItem : oldQuotationItems) {
                boolean find = false;
                for (QuotationItem item : quotationDetail.items) {

                    if (oldQuotationItem.id == item.id) {
                        find = true;
                        break;
                    }
                }

                if (!find) {
                    removeItems.add(oldQuotationItem);
                }
            }
            //移除被删除的记录
            quotationItemRepository.deleteInBatch(removeItems);
            removeItems.clear();
            int newIndex=0;
            for (QuotationItem item : quotationDetail.items) {

                item.quotationId = newId;
                item.iIndex=newIndex++;

            }
            quotationItemRepository.save( quotationDetail.items);
        }

        {
            //find items that removed
            List<QuotationXKItem> oldQuotationXKItems = quotationXKItemRepository.findByQuotationIdEqualsOrderByIIndex(newId);


            List<QuotationXKItem> removeXKItems = new ArrayList<>();
            for (QuotationXKItem oldQuotationXKItem : oldQuotationXKItems) {
                boolean find = false;
                for (QuotationXKItem item : quotationDetail.XKItems) {

                    if (oldQuotationXKItem.id == item.id) {
                        find = true;
                        break;
                    }
                }

                if (!find) {
                    removeXKItems.add(oldQuotationXKItem);
                }
            }
            //移除被删除的记录
            quotationXKItemRepository.deleteInBatch(removeXKItems);
            removeXKItems.clear();


            int newIndex=0;
            for (QuotationXKItem item : quotationDetail.XKItems) {

                item.quotationId = newId;
                item.iIndex=newIndex++;
            }

            quotationXKItemRepository.save( quotationDetail.XKItems);
        }

        return wrapData(loadQuotationDetail(newId));
    }

    /**
     * 记录报价修改信息
     */
    @Transactional
    public void updateQuotationLog(Quotation quotation, User user) {

        //记录数据当前修改着相关信息
        QuotationLog quotationLog = quotationLogRepository.findFirstByQuotationIdEquals(quotation.id);
        if (quotationLog == null) {
            quotationLog = new QuotationLog();
            quotationLog.quotationId = quotation.id;

        }

        quotationLog.quotationNumber = quotation.qNumber;
        quotationLog.setUpdator(user);
        quotationLogRepository.save(quotationLog);


        //增加历史操作记录
        operationLogRepository.save(OperationLog.createForQuotationModify(quotation, user));


    }

    @Transactional
    public RemoteData<QuotationDetail> verifyQuotationDetail(User user, QuotationDetail quotationDetail) {

        Quotation newQuotation = quotationDetail.quotation;

        if (!quotationRepository.exists(newQuotation.id)) {


            return wrapError("报价单号:" + newQuotation.qNumber
                    + "不存在,不能审核，请刷新数据");

        } else {



            //设定为已经审核
            quotationDetail.quotation.isVerified = true;


            RemoteData<QuotationDetail> result = saveQuotationDetail(user, quotationDetail);

            //记录修改记录

            //记录修改
             updateQuotationLog(newQuotation, user);
            //添加审核记录。
            OperationLog operationLog = OperationLog.createForQuotationVerify(newQuotation, user);
            operationLogRepository.save(operationLog);


            return result;

        }

    }

    @Transactional
    public RemoteData<QuotationDetail> unVerify(User user, long quotationId) {

        Quotation quotation = quotationRepository.findOne(quotationId);
        if (quotation == null) {
            return wrapError("id：" + quotationId + "，对应的报价单不存在");
        }

        if (!quotation.isVerified) {

            return wrapError("id" + quotationId + "，对应的报价单，状态已经为非审核，请刷新数据");
        }

        quotation.isVerified = false;
        quotationRepository.save(quotation);


        //记录修改
        updateQuotationLog(quotation, user);


        //添加取消审核记录。
        OperationLog operationLog = OperationLog.createForQuotationUnVerify(quotation, user);
        operationLogRepository.save(operationLog);


        return wrapData(loadQuotationDetail(quotationId));
    }

    @Transactional
    public RemoteData<QuotationDetail> resumeDelete(User user, long deleteQuotationId) {

        QuotationDelete quotationDelete = quotationDeleteRepository.findOne(deleteQuotationId);

        if (quotationDelete == null) {
            return wrapError("该报价单已经不在删除列表中");
        }


        QuotationDetail detail = BackDataHelper.getQuotationDetail(deleteQuotationFilePath, quotationDelete);

        if (detail == null)
            return wrapError("备份文件读取失败");


        //新增记录
        //移除id
        detail.quotation.id = 0;

        RemoteData<QuotationDetail> result = saveQuotationDetail(user, detail);

        if (result.isSuccess()) {


            QuotationDetail newQuotationDetail = result.datas.get(0);
            long newQuotationId = newQuotationDetail.quotation.id;
            //更新修改记录中所有旧productId 至新id；
            if (detail.quotationLog != null) {
                detail.quotationLog.quotationId = newQuotationId;
                quotationLogRepository.save(detail.quotationLog);
                newQuotationDetail.quotationLog = detail.quotationLog;
            }

            //更新修改记录中所有旧productId 至新id；
            operationLogRepository.updateProductId(quotationDelete.quotationId, Quotation.class.getSimpleName(), newQuotationId);

            //添加恢复消息记录。
            OperationLog operationLog = OperationLog.createForQuotationResume(newQuotationDetail.quotation, user);
            operationLogRepository.save(operationLog);


            //移除删除记录
            quotationDeleteRepository.delete(deleteQuotationId);

            //移除备份的文件
            BackDataHelper.deleteQuotation(deleteQuotationFilePath, quotationDelete);

        }


        return wrapData(detail);
    }

    @Transactional
    public RemoteData<Void> logicDelete(User user, long quotationId) {

        QuotationDetail detail = loadQuotationDetail(quotationId);
        if (detail == null) {
            return wrapError(" 该报价单不存在，请刷新数据");
        }


        Quotation quotation = quotationRepository.findOne(quotationId);

        quotationRepository.delete(quotation.id);

        int affectedRow = quotationItemRepository.deleteByQuotationIdEquals(quotationId);
        Logger.getLogger("TEST").info("quotationItemRepository delete affectedRow:" + affectedRow);
        affectedRow = quotationXKItemRepository.deleteByQuotationIdEquals(quotationId);
        Logger.getLogger("TEST").info("quotationXKItemRepository delete affectedRow:" + affectedRow);


        //增加历史操作记录
        operationLogRepository.save(OperationLog.createForQuotationDelete(quotation, user));


        //保存数据备份
        QuotationDelete quotationDelete = new QuotationDelete();
        quotationDelete.setQuotationAndUser(quotation, user);
        quotationDelete = quotationDeleteRepository.save(quotationDelete);
        BackDataHelper.backQuotation(detail, deleteQuotationFilePath, quotationDelete);


        return wrapData();
    }

    public RemoteData<QuotationDelete> listDelete(String keyword, int pageIndex, int pageCount) {

        Pageable pageable = constructPageSpecification(pageIndex, pageCount);
        String likeWord = "%" + keyword.trim() + "%";
        Page<QuotationDelete> pageValue = quotationDeleteRepository.findByQNumberLikeOrSaleManLikeOrCustomerLikeOrderByTimeDesc(likeWord, likeWord, likeWord, pageable);

        List<QuotationDelete> products = pageValue.getContent();

        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);
    }

    public RemoteData<QuotationDetail> findDeleteDetail(long quotationDeleteId) {

        QuotationDelete quotationDelete = quotationDeleteRepository.findOne(quotationDeleteId);

        if (quotationDelete == null) {
            return wrapError("该报价单已经不在删除列表中");
        }


        QuotationDetail detail = null;

        detail = BackDataHelper.getQuotationDetail(deleteQuotationFilePath, quotationDelete);

        if (detail == null)
            return wrapError("备份文件读取失败");

        return wrapData(detail);
    }

    public RemoteData<Quotation> list() {


      return   wrapData(quotationRepository.findAll());
    }
}
