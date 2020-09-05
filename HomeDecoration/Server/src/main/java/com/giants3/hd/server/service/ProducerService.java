package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ProducerValueItem;
import com.giants3.hd.noEntity.ProducerValueReport;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.repository_erp.OutFactoryRepository;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class ProducerService extends AbstractService implements InitializingBean, DisposableBean {


    @Autowired
    private OutFactoryRepository outFactoryRepository;

    @Autowired
    private ProducerValueConfigRepository producerValueConfigRepository;

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


    public RemoteData<ProducerValueConfig> getProducerValueConfig() {

        List<ProducerValueConfig> configs = outFactoryRepository.findValueConfig();
        return wrapData(configs);

    }


    public RemoteData<ProducerValueItem> getProducerValueItems(String jgh) {

        List<ProducerValueItem> items = outFactoryRepository.findProduceValueItems(jgh);
        return wrapData(items);

    }

    public RemoteData<ProducerValueReport> getProducerValueReport(String producer) {


        List<ProducerValueReport> valueReport = outFactoryRepository.findValueReport(producer);
        return wrapData(valueReport);

    }


    @Transactional
    public boolean  save(List<ProducerValueConfig> configs)
    {

        List<ProducerValueConfig> needToSave=new ArrayList<>();

        for (ProducerValueConfig config:configs)
        {
            if(config.id>0||config.minOutputValue>0||config.maxOutputValue>0||config.workerNum>0) {
                //校正id
                ProducerValueConfig byDeptEquals = producerValueConfigRepository.findByDeptEquals(config.dept);
                if (byDeptEquals != null && byDeptEquals.id != config.id) {
                    config.id = byDeptEquals.id;
                }
                needToSave.add(config);
            }
        }



        producerValueConfigRepository.save(needToSave);

        return  true;

    }




}
