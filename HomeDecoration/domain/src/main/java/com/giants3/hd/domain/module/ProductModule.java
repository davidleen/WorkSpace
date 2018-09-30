package com.giants3.hd.domain.module;

import com.giants3.hd.domain.repository.FactoryRepository;
import com.giants3.hd.domain.repository.ProductRepository;
import com.giants3.hd.domain.repository.XiankangRepository;
import com.giants3.hd.domain.repositoryImpl.FactoryRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.ProductRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.XiankangRepositoryImpl;
import com.google.inject.AbstractModule;

/**
 * Created by david on 2015/9/15.
 */
public class ProductModule extends AbstractModule {


    @Override
    protected void configure() {


        bind(ProductRepository.class).to(ProductRepositoryImpl.class);
        bind(XiankangRepository.class).to(XiankangRepositoryImpl.class);
        bind(FactoryRepository.class).to(FactoryRepositoryImpl.class);

    }

//    @Provides
//    Account providePurchasingAccount(Product product) {
//        return product.quotations();
//    }
}
