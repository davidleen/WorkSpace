package com.giants3.hd.domain.repository;

import com.giants3.hd.domain.repositoryImpl.QuotationRepositoryImpl;
import com.giants3.hd.noEntity.QuotationDetail;
import rx.Observable;
import rx.functions.Action1;

import static org.junit.Assert.*;

/**
 * Created by david on 2015/9/30.
 */
public class QuotationRepositoryImplTest {


    QuotationRepositoryImpl quotationRepository;
    @org.junit.Before
    public void setUp() throws Exception {

        quotationRepository=new QuotationRepositoryImpl();

    }

    @org.junit.After
    public void tearDown() throws Exception {
        quotationRepository=null;

    }

    @org.junit.Test
    public void testDetail() throws Exception {


        Observable<QuotationDetail> quotationDetail=quotationRepository.detail(100);
        quotationDetail.subscribe(new Action1<QuotationDetail>() {
            @Override
            public void call(QuotationDetail quotationDetail) {

                assertNotNull(quotationDetail);
            }
        });


    }
}