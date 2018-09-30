package com.giants3.hd.controller;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 所有控制类的基类，提供共有方法。
 */

public class BaseController   implements InitializingBean, DisposableBean {

    protected static final int NUMBER_OF_PERSONS_PER_PAGE = 20;


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
