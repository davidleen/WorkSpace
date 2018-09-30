package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.entity.Product;
import org.junit.After;
import org.junit.Before;
import rx.Observable;
import rx.functions.Action1;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by david on 2015/10/6.
 */
public class ProductRepositoryImplTest {



    @Before
    public void setUp() throws Exception {

        new UserRepositoryImpl().login("ltw","123");

    }


    @org.junit.Test
    public void testDetail() throws Exception {


        Observable<List<Product>> quotationDetail=new ProductRepositoryImpl().loadByProductNameBetween("10A0001", "10A0009", true);
        quotationDetail.subscribe(new Action1<List<Product>>() {
            @Override
            public void call(List<Product> quotationDetail) {



                assertNotNull(quotationDetail);
            }
        });


    }

    @After
    public void tearDown() throws Exception {

    }
}