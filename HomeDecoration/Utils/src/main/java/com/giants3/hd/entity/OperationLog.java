package com.giants3.hd.entity;

import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.StringUtils;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * 数据操作记录表
 * Created by davidleen29 on 2015/8/21.
 */
@Entity(name = "T_OperationLog")
public class OperationLog implements Serializable{


    public static final String TYPE_MODIFY = "修改";
    public static final String TYPE_DELETE="删除";
    public static final String TYPE_RESUME="恢复";
    public static final String TYPE_GLOBAL_SET="系统参数修改";
    public static final String TYPE_VERIFY_QUOTATION="报价单审核";
    public static final String TYPE_UNVERIFY_QUOTATION="报价单撤销审核";
    public static final String[] OPERATION_TYPES = new String[]{TYPE_MODIFY,TYPE_DELETE,TYPE_RESUME,TYPE_GLOBAL_SET,TYPE_VERIFY_QUOTATION,TYPE_UNVERIFY_QUOTATION};
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

   public  String tableName;
   public  String message = "";
    public long recordId;
    public long userId;
    public String userCode;
    public String userName;
    public String userCName;

    public long operationId;
    public String operationType;

    public long time;
    public String timeString;


    public static  OperationLog createForProductModify(Product product, User user )
    {
        return createForProduct(product,user,TYPE_MODIFY);
    }



    public static  OperationLog createForProductResume(Product product,User user )
    {
        return createForProduct(product,user,TYPE_RESUME);
    }
    final static StringBuilder stringBuilder = new StringBuilder();
    private static  OperationLog createForProduct(Product product,User user,String operationType)
    {


        stringBuilder.setLength(0);
       String tableName=Product.class.getSimpleName();
      long recordId=product.id;

        String message= stringBuilder.append("货号:").append(product.getName()).append(StringUtils.isEmpty(product.getpVersion()) ? "" : ("-" + product.getpVersion())).toString();




        return  create(tableName, recordId, message, user, operationType);

    }
    public static  OperationLog createForQuotationModify(Quotation quotation, User user) {

    return createForQuotation(quotation, user, TYPE_MODIFY);
    }

    private static  OperationLog createForQuotation(Quotation quotation,User user,String operationType)
    {



        String tableName=Quotation.class.getSimpleName();
        long recordId=quotation.id;
        stringBuilder.setLength(0);
        String message= stringBuilder.append("报价单号:").append(quotation.qNumber).append(",客户：").append(quotation.customerName).append(",业务员:").append(quotation.salesman).toString();




        return  create(tableName, recordId, message, user, operationType);

    }



    private static  OperationLog create(String tableName,long recordId,String message  ,User user,String operationType)
    {


        OperationLog log=new OperationLog();
        log.tableName=tableName;
        log.recordId=recordId;
        log.message=message;

        log.userId=user.id;
        log.userCode=user.code;
        log.userName=user.name;
        log.userCName=user.chineseName;
        log.operationId= StringUtils.index(OPERATION_TYPES,operationType);
        log.operationType=operationType;
        log.time= Calendar.getInstance().getTimeInMillis();
        log.timeString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(log.time));
        return log;

    }


    public static OperationLog createForQuotationDelete(Quotation quotation, User user) {

       return createForQuotation(quotation,user,TYPE_DELETE);
    }

    public static OperationLog createForProductDelete(Product product, User user) {
        return   createForProduct(product, user, TYPE_DELETE);
    }

    public static OperationLog createForQuotationResume(Quotation quotation, User user) {
           return createForQuotation(quotation, user, TYPE_RESUME);
    }

    public static OperationLog createForQuotationVerify(Quotation quotation, User user) {
        return createForQuotation(quotation, user, TYPE_VERIFY_QUOTATION);
    }

    public static OperationLog createForQuotationUnVerify(Quotation quotation, User user) {
        return createForQuotation(quotation, user, TYPE_UNVERIFY_QUOTATION);
    }

    public static OperationLog createForGloBalDataChange(Product product, User user) {
        return createForProduct(product, user, TYPE_GLOBAL_SET);
    }
}
