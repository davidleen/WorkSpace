package com.giants3.hd.domain.module;

import com.giants3.hd.domain.repository.FileRepository;
import com.giants3.hd.domain.repository.HdTaskLogRepository;
import com.giants3.hd.domain.repository.HdTaskRepository;
import com.giants3.hd.domain.repositoryImpl.FileRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.HdTaskLogRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.HdTaskRepositoryImpl;
import com.google.inject.AbstractModule;

/**
 *
 * 文件管理模块
 * Created by davidleen29 on 2016/7/20.
 */
public class FileModule extends AbstractModule {


    @Override
    protected void configure() {


        bind(FileRepository.class).to(FileRepositoryImpl.class);


    }

}
