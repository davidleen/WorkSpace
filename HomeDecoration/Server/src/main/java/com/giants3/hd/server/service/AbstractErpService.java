package com.giants3.hd.server.service;


import javax.persistence.EntityManager;

/**
 * Created by davidleen29 on 2017/11/4.
 */
public class AbstractErpService extends AbstractService {



    @Override
    public void destroy() throws Exception {
        super.destroy();

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();


    }


    protected void onEntityManagerCreate(EntityManager manager) {
    }
}
