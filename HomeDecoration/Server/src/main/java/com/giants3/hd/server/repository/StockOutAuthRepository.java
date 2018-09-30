package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.StockOutAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 出库单权限
* Created by davidleen29 on 2014/9/17.
*/
public interface StockOutAuthRepository extends JpaRepository<StockOutAuth,Long> {





    public  StockOutAuth findFirstByUser_IdEquals(long userId);
    public List<StockOutAuth> findByUser_IdEquals(long userId);
}
