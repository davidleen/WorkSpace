package com.giants3.hd.server.utils;

import com.giants3.hd.noEntity.ConstantData;
import com.giants3.hd.utils.UrlFormatter;

/**
 * Created by davidleen29 on 2018/8/15.
 */
public class HttpUrl {


    public static  String versionCode="";
    public static  String    token;

    public static String additionInfo(UrlFormatter urlFormatter) {


        urlFormatter.append("appVersion", versionCode)
                .append("client", ConstantData.CLIENT_SERVER)
                .append("token", token);


        return urlFormatter.toUrl();


    }


    public static String findAppQuotationDetails(String urlHead, String startDate, String endDate,int pageIndex,int pageSize) {




        UrlFormatter urlFormatter = new UrlFormatter(urlHead+"/api/app/quotation/findDetails");
        urlFormatter.append("startDate",startDate);
        urlFormatter.append("endDate",endDate);
        urlFormatter.append("pageIndex",pageIndex);
        urlFormatter.append("pageSize",pageSize);
        return HttpUrl.additionInfo(urlFormatter);


    }public static String findCustomer(String urlHead) {




        UrlFormatter urlFormatter = new UrlFormatter(urlHead+"/api/customer/list");
        return HttpUrl.additionInfo(urlFormatter);


    }
}
