package com.giants3.report.jasper.quotation;

import com.giants3.hd.entity.Company;
import com.giants3.hd.entity.GlobalData;
import com.giants3.report.jasper.ReportData;

/**
 * Created by davidleen29 on 2018/3/19.
 */
public class CompanyReportData<T> extends ReportData<T> {
    private final Company company;

    public CompanyReportData(Company company, T data, Class<T> tClass) {
        super(data, tClass);
        this.company = company;
        init();
    }


    private  void init()
    {

        if(company==null) return ;

        put("companyName",company.name);
        put("companyEName",company.eName);
        put("companyEAddress",company.eAddress);
        put("tel",company.tel);
        put("fax",company.fax);

    }

    @Override
    public Object get(Object key) {
        final Object result = super.get(key);

//        System.out.println("field:" + key+",result :"+result);
//        if(result instanceof  String) {
//
//
//            return result + "测试";
//        }
        return result;
    }
}
