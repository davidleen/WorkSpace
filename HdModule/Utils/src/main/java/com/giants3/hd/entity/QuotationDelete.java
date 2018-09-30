package com.giants3.hd.entity;

import com.giants3.hd.utils.DateFormats;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**被删除的报价单的简要信息
 * Created by davidleen29 on 2015/8/25.
 */
@Entity(name="T_QuotationDelete")
public class QuotationDelete implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    @Lob
    public String qNumber;
    public long quotationId;
    public String saleMan;
    public String customer;


    public long userId;
    public String userName;
    public String userCName;
    public long time;
    public String timeString;


    public String toString() {
        return   qNumber + "_"+saleMan+ "_"+customer+ "_"+ "_"+userName+ "_"+userCName+ "_"+timeString;

    }



    public void setQuotationAndUser(Quotation quotation, User user)
    {


        qNumber=quotation.qNumber;

        saleMan=quotation.salesman;
        customer=quotation.customerName;
        quotationId=quotation.id;


        userId=user.id;
        userName=user.name;
        userCName=user.chineseName;

        time= Calendar.getInstance().getTimeInMillis();
        timeString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM_CHINESE.format(new Date(time));

    }






}
