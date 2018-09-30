package com.giants3.reader.datasource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 定义动态数据源，实现通过集成Spring提供的AbstractRoutingDataSource，只需要实现determineCurrentLookupKey方法即可
 * <p/>
 * 由于DynamicDataSource是单例的，线程不安全的，所以采用ThreadLocal保证线程安全，由DynamicDataSourceHolder完成。
 *
 * @author davidleen29
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger logger=Logger.getLogger(DynamicDataSource.class);
    private Integer slaves;//slaves的个数


    //如果基于JDK 7+，可以使用ThreadLocalRandom

    public void setSlaves(Integer slaves) {
        this.slaves = slaves;
    }

    @Override
    protected Object determineCurrentLookupKey() {

        logger.info("look up ing for datasource!!!!!!!!!!!!!!!!");
        // 使用DynamicDataSourceHolder保证线程安全，并且得到当前线程中的数据源key  
        return DynamicDataSourceHolder.getDataSourceKey(slaves);
    }

}  
