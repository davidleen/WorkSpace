package com.giants3.hd.domain.module;

import com.giants3.hd.domain.datasource.QuotationDataSourceImpl;
import com.giants3.hd.domain.datasource.QuotationDataStore;
import com.giants3.hd.domain.repository.ProductRepository;
import com.giants3.hd.domain.repository.QuotationRepository;
import com.giants3.hd.domain.repository.StockRepository;
import com.giants3.hd.domain.repositoryImpl.ProductRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.QuotationRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.StockRepositoryImpl;
import com.google.inject.AbstractModule;

/**
 * 库存模块绑定
 * Created by david on 2015/9/15.
 */
public class StockModule extends AbstractModule {





    @Override
    protected void configure() {
        bind(StockRepository.class).to(StockRepositoryImpl.class);
    }

//    @Provides
//    Account providePurchasingAccount(Product product) {
//        return product.quotations();
//    }
}
