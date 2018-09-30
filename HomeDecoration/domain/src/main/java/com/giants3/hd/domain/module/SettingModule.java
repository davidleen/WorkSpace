package com.giants3.hd.domain.module;

import com.giants3.hd.domain.repository.AuthRepository;
import com.giants3.hd.domain.repository.SettingRepository;
import com.giants3.hd.domain.repository.UserRepository;
import com.giants3.hd.domain.repositoryImpl.AuthRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.SettingRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.UserRepositoryImpl;
import com.google.inject.AbstractModule;

/**
 * 权限模块绑定
 * Created by david on 2015/9/15.
 */
public class SettingModule extends AbstractModule {





    @Override
    protected void configure() {
        bind(SettingRepository.class).to(SettingRepositoryImpl.class);

    }

//    @Provides
//    Account providePurchasingAccount(Product product) {
//        return product.quotations();
//    }
}
