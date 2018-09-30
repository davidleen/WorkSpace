package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.OrderAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 订单权限
* Created by davidleen29 on 2014/9/17.
*/
public interface OrderAuthRepository extends JpaRepository<OrderAuth,Long> {





    public  OrderAuth findFirstByUser_IdEquals(long userId);
    public List<OrderAuth> findByUser_IdEquals(long userId);
}
