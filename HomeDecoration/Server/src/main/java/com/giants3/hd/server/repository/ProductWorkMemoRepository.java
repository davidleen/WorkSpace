package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.ProductWorkMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 产品的生产备注
 * Created by davidleen29 on 2014/9/17.
 */
public interface ProductWorkMemoRepository extends JpaRepository<ProductWorkMemo, Long> {


      ProductWorkMemo findFirstByProductNameEqualsAndPVersionEqualsAndWorkFlowStepEquals(String productName,String pVersion,int step);

    List<ProductWorkMemo> findByProductNameEqualsAndPVersionEquals(String productName, String pVersion);
}
