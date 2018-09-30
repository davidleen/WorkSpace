package com.giants3.hd.domain.module;

import com.giants3.hd.domain.datasource.QuotationDataSourceImpl;
import com.giants3.hd.domain.datasource.QuotationDataStore;
import com.giants3.hd.domain.repository.HdTaskLogRepository;
import com.giants3.hd.domain.repository.HdTaskRepository;
import com.giants3.hd.domain.repository.QuotationRepository;
import com.giants3.hd.domain.repositoryImpl.HdTaskLogRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.HdTaskRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.QuotationRepositoryImpl;
import com.google.inject.AbstractModule;

/**
 * Created by david on 2015/12/16.
 */
public class HdTaskModule extends AbstractModule {


    @Override
    protected void configure() {


        bind(HdTaskRepository.class).to(HdTaskRepositoryImpl.class);
        bind(HdTaskLogRepository.class).to(HdTaskLogRepositoryImpl.class);



    }
}
