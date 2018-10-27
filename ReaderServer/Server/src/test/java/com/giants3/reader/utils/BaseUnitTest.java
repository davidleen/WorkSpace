package com.giants3.reader.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chenkaihua on 15-11-27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:../../main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = true)
@Transactional
public class BaseUnitTest  {
    @Test
    public void test(){
    }
}