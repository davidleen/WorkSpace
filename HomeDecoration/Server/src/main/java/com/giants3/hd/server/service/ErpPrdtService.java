package com.giants3.hd.server.service;

import com.giants3.hd.entity.ErpOrderItemProcess;
import com.giants3.hd.noEntity.ProductType;
import com.giants3.hd.server.repository_erp.ErpPrdtRepository;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by davidleen29 on 2017/11/4.
 */
@Service
public class ErpPrdtService extends AbstractErpService   {



    @Autowired
    ErpPrdtRepository erpPrdtRepository;
    @Override

    protected void onEntityManagerCreate(EntityManager manager) {

       // erpPrdtRepository=new ErpPrdtRepository(manager);
    }

    /**
     * 从指令单集合中，判断排厂类型
     *
     * @param process
     * @return
     */

    public   ProductType getProductTypeFromOrderItemProcess(ErpOrderItemProcess process) {


        ProductType productType = null;


        //指定单号含 t  m 开头，可直接判断铁木
        final String code = process.mrpNo.substring(1, 2);
        if (code.equals("T")) {

            productType = ProductType.TIE;

        } else if (code.equals("M")) {
            productType = ProductType.MU;
        }

        if (productType == null) {
            productType = getProductTypeFromPrdNo(process.prdNo);
        }
        if (productType == null)
            productType = ProductType.NONE;

        return productType;


    }

    /**
     * 根据产品名称 idx  类别判断  mjxx tjxxx
     *  铁木。。。。
     *
     * @param prd_no
     * @return
     */
    private   ProductType getProductTypeFromPrdNo(String prd_no) {




        String idx1=erpPrdtRepository.findIdx1ByPrdno(prd_no);
        if(!StringUtils.isEmpty(idx1))
        {

            if(idx1.startsWith("TJ")) return ProductType.TIE;
            if(idx1.startsWith("MJ")) return ProductType.MU;
        }

        return ProductType.NONE;
//        ProductType productType = null;
//        int prd_no_code = -1;
//
//        if (StringUtils.isChar(prd_no, 2)) {
//            try {
//                //13A1221 形式   抽取字母后4位
//                prd_no_code = Integer.valueOf(prd_no.substring(3, Math.min(7, prd_no.length())));
//            } catch (Throwable t) {
//            }
//        } else if (StringUtils.isChar(prd_no, 0)) {
//            try {
//                // A1221 形式   抽取字母后4位
//                prd_no_code = Integer.valueOf(prd_no.substring(1, Math.min(5, prd_no.length())));
//            } catch (Throwable t) {
//            }
//        }
//
//
//        if (prd_no_code != -1) {
//
//
//            if (prd_no_code >= 0 && prd_no_code <= 5000) {
//                productType = ProductType.TIE;
//
//            } else if (prd_no_code > 5000 && prd_no_code <= 9999) {
//
//
//                productType = ProductType.MU;
//            }
//        }
//
//        return productType;

    }

    /**
     * 从指令单集合中，判断排厂类型
     *
     * @param processes
     * @return
     */

    public   ProductType getProductTypeFromOrderItemProcess(List<ErpOrderItemProcess> processes) {


        ProductType productType = null;


        boolean tie = false;
        boolean mu = false;
        //指定单号含at am 开头，可直接判断铁木
        for (ErpOrderItemProcess erpOrderItemProcess : processes) {
            if (erpOrderItemProcess.mrpNo.toUpperCase().startsWith("AT")) {
                tie = true;

            }
            if (erpOrderItemProcess.mrpNo.toUpperCase().startsWith("AM")) {
                mu = true;
            }
        }


        if (tie && mu) {
            productType = ProductType.BOTH;
        } else if (tie) {
            productType = ProductType.TIE;
        } else if (mu) productType = ProductType.MU;


        //否则判断产品名字后四位数据  0-5000 铁  5000+ 木
        if (productType == null && processes.size() > 0) {


            String prd_no = processes.get(0).prdNo;
            productType = getProductTypeFromPrdNo(prd_no);

        }


        if (productType == null)
            productType = ProductType.NONE;

        return productType;


    }

    public String findIdx1ByPrdno(String productName) {

       return  erpPrdtRepository.findIdx1ByPrdno(productName);
    }
}
