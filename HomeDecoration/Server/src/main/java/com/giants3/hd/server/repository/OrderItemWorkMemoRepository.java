package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.OrderItemWorkMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 订单的生产备注
* Created by davidleen29 on 2014/9/17.
*/
public interface OrderItemWorkMemoRepository extends JpaRepository<OrderItemWorkMemo,Long> {


    List<OrderItemWorkMemo> findByOsNoEqualsAndItmEquals(String osNo, int itm);

    OrderItemWorkMemo findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(String os_no, int itm, int workFlowStep);
}
