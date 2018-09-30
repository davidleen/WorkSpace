package com.giants3.hd.server.service;

import com.giants3.hd.entity_erp.SampleState;
import com.giants3.hd.server.repository_erp.ErpSampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 样品数据信息
 * Created by davidleen29 on 2017/11/4.
 */
@Service
public class ErpSampleService extends AbstractErpService {


    @Autowired
    ErpSampleRepository erpSampleRepository;


    @Override
    protected void onEntityManagerCreate(EntityManager manager) {


    }


    public SampleState getSampleState(String prd_no, String pVersion) {


        SampleState sampleState = new SampleState();
        sampleState.prdNo = prd_no;
        sampleState.pVersion = pVersion;

        final List<SampleState> byPrdNo = erpSampleRepository.findByPrdNo(prd_no, pVersion);


        if (byPrdNo.size() > 0) {


            final SampleState temp = byPrdNo.get(0);
            sampleState.ltime = temp.ltime.substring(0,16);
            sampleState.factory = temp.factory;
            sampleState.wareHouse = temp.wareHouse;
            sampleState.BL_ID = temp.BL_ID;

        }


        final List<SampleState> beibangByPrdNo = erpSampleRepository.findBeibangByPrdNo(prd_no, pVersion);
        if (beibangByPrdNo.size() > 0) {


            final SampleState temp = beibangByPrdNo.get(0);
            sampleState.bb = temp.bb;
            sampleState.ypbb = temp.ypbb;


        }

        return sampleState;

    }


}
