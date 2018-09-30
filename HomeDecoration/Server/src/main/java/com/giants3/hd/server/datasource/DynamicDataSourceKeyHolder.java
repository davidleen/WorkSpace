package com.giants3.hd.server.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceKeyHolder {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceKeyHolder.class);
    
    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<String>();

    public static void setKey(String key) {
        dataSourceHolder.set(key);
    }

    public static String getDataSourceKey() {
        return (String) dataSourceHolder.get();
    }
}