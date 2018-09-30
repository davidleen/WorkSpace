package com.giants3.hd.domain.module;

import com.giants3.hd.domain.repository.OrderRepository;
import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.domain.repositoryImpl.OrderRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.WorkFlowRepositoryImpl;
import com.google.inject.AbstractModule;

/**
 * Created by david on 2015/9/15.
 */
public class OrderModule extends AbstractModule {


    @Override
    protected void configure() {


        bind(OrderRepository.class).to(OrderRepositoryImpl.class);

        bind(WorkFlowRepository.class).to(WorkFlowRepositoryImpl.class);

    }

//    @Provides
//    Account providePurchasingAccount(Product product) {
//        return product.quotations();
//    }
}
