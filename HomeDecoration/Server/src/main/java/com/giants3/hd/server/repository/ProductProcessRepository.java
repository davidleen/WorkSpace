package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.ProductProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 工序
 *
*/
public interface ProductProcessRepository extends JpaRepository<ProductProcess,Long> {

    Page<ProductProcess> findByNameLikeOrCodeLike(String name, String code,Pageable pageable);


}
