package com.giants3.lanvideo.transaction;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.persistence.EntityManagerFactory;

/**
 * Created by davidleen29 on 2018/6/6.
 */
public class CustomJPATransactionManager extends JpaTransactionManager {

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        super.doCommit(status);
    }


    @Override
    protected Object doGetTransaction() {
        return super.doGetTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {


        return super.getEntityManagerFactory();
    }
}
