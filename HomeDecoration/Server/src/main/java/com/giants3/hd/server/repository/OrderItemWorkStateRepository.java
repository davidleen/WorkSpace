package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.OrderItemWorkState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 订单的生产状态
* Created by davidleen29 on 2014/9/17.
*/
public interface OrderItemWorkStateRepository extends JpaRepository<OrderItemWorkState,Long> {


    List<OrderItemWorkState> findByWorkFlowStateEquals(int workflowstate);

    OrderItemWorkState findFirstByOsNoEqualsAndItmEquals(String osNo, int itm);
    @Modifying
    @Query("delete T_OrderItemWorkState p where   p.osNo=:os_no and p.itm=:itm   ")
    int deleteByOsNoAndItm(@Param("os_no") String os_no, @Param("itm") int itm);

}
