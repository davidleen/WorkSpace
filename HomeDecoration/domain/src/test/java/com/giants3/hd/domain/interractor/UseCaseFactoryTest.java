package com.giants3.hd.domain.interractor;

import com.giants3.hd.noEntity.ProductDetail;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rx.Subscriber;

/**
 * Created by david on 2016/3/30.
 */
public class UseCaseFactoryTest {

    UseCaseFactory factory;
    @Before
    public void setUp() throws Exception {
        factory=UseCaseFactory.getInstance();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateQuotationDetail() throws Exception {

    }

    @Test
    public void testCreateProductByNameBetween() throws Exception {

    }

    @Test
    public void testCreateUpdateXiankang() throws Exception {

    }

    @Test
    public void testReadTaskListUseCase() throws Exception {

    }

    @Test
    public void testAddHdTaskUseCase() throws Exception {

    }

    @Test
    public void testDeleteHdTaskUseCase() throws Exception {

    }

    @Test
    public void testFindTaskLogUseCase() throws Exception {

    }

    @Test
    public void testCreateProductByNameRandom() throws Exception {

    }

    @Test
    public void testCreateOrderListUseCase() throws Exception {

    }

    @Test
    public void testCreateOrderItemListUseCase() throws Exception {

    }

    @Test
    public void testCreateGetProductByPrdNo() throws Exception {


      factory.createGetProductByPrdNo("08B0331").execute(new Subscriber<ProductDetail>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {
              Assert.fail(e.getMessage());

          }

          @Override
          public void onNext(ProductDetail o) {

              Assert.assertNotNull(o);

          }
      });
        factory.createGetProductByPrdNo("Xbbbbdfafafaf").execute(new Subscriber<ProductDetail>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Assert.assertTrue(e!=null);

            }

            @Override
            public void onNext(ProductDetail o) {

                Assert.assertNull(o);

            }
        });
    }
}