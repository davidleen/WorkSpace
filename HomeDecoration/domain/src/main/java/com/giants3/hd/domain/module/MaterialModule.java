package com.giants3.hd.domain.module;

import com.giants3.hd.domain.repository.MaterialRepository;
import com.giants3.hd.domain.repositoryImpl.MaterialRepositoryImpl;
import com.google.inject.AbstractModule;

/**
 *
 * 材料相关的
 * Created by davidleen29 on 2017/4/2.
 */
public class MaterialModule  extends AbstractModule{
    /**
     * Configures a {@link Binder} via the exposed methods.
     */
    @Override
    protected void configure() {


        bind(MaterialRepository.class).to(MaterialRepositoryImpl.class);

    }
}
