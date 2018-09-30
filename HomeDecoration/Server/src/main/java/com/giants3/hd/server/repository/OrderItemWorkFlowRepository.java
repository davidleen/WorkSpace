package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.OrderItemWorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 产品生产流程信息
 */
public interface OrderItemWorkFlowRepository extends JpaRepository<OrderItemWorkFlow, Long> {


    OrderItemWorkFlow findFirstByOrderItemIdEquals(long orderItemId);
}
