package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.OrderItemWorkMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 订单的生产备注
* Created by davidleen29 on 2014/9/17.
*/
public interface OrderItemWorkMemoRepository extends JpaRepository<OrderItemWorkMemo,Long> {


    List<OrderItemWorkMemo> findByOsNoEqualsAndItmEquals(String osNo, int itm);

    OrderItemWorkMemo findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(String os_no, int itm, int workFlowStep);

    @Modifying
    @Query("update T_OrderItemWorkMemo p set p.itm=:itm where   p.osNo=:os_no and p.productName=:prd_no    and p.pVersion=:pVersion  ")
    int  updateItmByOsNoAndPrdNo(@Param("os_no") String osNo, @Param("prd_no") String prdNo,@Param("pVersion") String pVersion, @Param("itm") int itm);
}
