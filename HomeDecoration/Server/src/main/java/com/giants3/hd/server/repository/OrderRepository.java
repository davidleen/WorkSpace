package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 订单附加数据表
* Created by davidleen29 on 2014/9/17.
*/
public interface OrderRepository extends JpaRepository<Order,Long> {


   Order findFirstByOsNoEquals(String ck_no);

}
