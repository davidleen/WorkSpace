package com.giants3.hd.domain.api;

import com.giants3.hd.noEntity.ConstantData;
import com.giants3.hd.exception.HdException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * Created by david on 2016/2/12.
 */
public class ClientTest {

    private static final java.lang.String TAG = "ClientTest";
    Client client;
    @Before
    public void setUp() throws Exception {

        client=new Client();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetWithStringReturned() throws Exception {


        String url="http://127.0.0.1:8080/api/product/detail?id=4618&token=a55dbf9d9ec4716a5fd487f0b569829d&appVersion=55";

        invoke(url);
        invoke("http://127.0.0.1:8080/api/quotation/detail?id=100");

    }




    @Test
    public void testTimeUse() throws Exception {


        String url="http://127.0.0.1:8080/api/product/detail?id=4618&token=a55dbf9d9ec4716a5fd487f0b569829d&appVersion=55";

        long time=System.currentTimeMillis();
        for(int i=0;i<100;i++)
            invoke(url);


        Logger.getLogger(TAG).info("time use "+(System.currentTimeMillis()-time )+" with crypt:"+ ConstantData.IS_CRYPT_JSON);

    }
    private void invoke(String url) throws HdException {
        String s=  client.getWithStringReturned(url);

        Assert.assertNotNull(s);
    }
}