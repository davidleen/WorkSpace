package com.giants3.hd.server.datasource;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Random;

/*
 *  
 * 使用ThreadLocal技术来记录当前线程中的数据源的key 
 *  
 * @author zhijun 
 * 
 */
public class DynamicDataSourceHolder {


    public static final String WRITE = "WRITE";
    public static final String READ = "READ_";
    private static Random random = new Random();


    //使用ThreadLocal记录当前线程的数据源key  
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();


    /**
     * 获取数据源key
     *
     * @return
     */
    public static String getDataSourceKey(int slaveCount) {


//        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
//        if (!isReadOnly) {
//            return WRITE;
//        }
        if(holder.get()==null)return WRITE;

        if(holder.get().equals(WRITE)) return WRITE;


        //如果是只读，可以从任意一个slave中执行
        return READ + random.nextInt(slaveCount);
        //如果基于JDK 7+
        //ThreadLocalRandom random = ThreadLocalRandom.current();

    }


    public static void markSlave() {
        holder.set(READ);
    }

    public static void markMaster() {
        holder.set(WRITE);
    }
}