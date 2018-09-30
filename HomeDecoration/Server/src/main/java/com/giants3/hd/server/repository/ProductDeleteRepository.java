package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.ProductDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  被删除的产品数据记录
* Created by davidleen29 on 2014/9/17.
*/
public interface ProductDeleteRepository extends JpaRepository<ProductDelete,Long> {




    public Page<ProductDelete> findByProductNameLikeOrderByTimeDesc(String productName , Pageable pageable);
}
