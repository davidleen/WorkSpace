package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.ProductLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户
* Created by davidleen29 on 2014/9/17.
*/
public interface ProductLogRepository extends JpaRepository<ProductLog,Long> {




    public ProductLog findFirstByProductIdEquals(long productId);
}
