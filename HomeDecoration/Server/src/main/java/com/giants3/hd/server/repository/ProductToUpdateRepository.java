package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.ProductToUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 要进行统计更新的产品id 列表
 *
*/
public interface ProductToUpdateRepository extends JpaRepository<ProductToUpdate,Long> {

    ProductToUpdate findFirstByProductIdEquals(long productId);


}
