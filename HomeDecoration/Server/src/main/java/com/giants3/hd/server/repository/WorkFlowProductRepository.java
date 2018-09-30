package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.WorkFlowProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 产品配置的生产流程信息
 */
public interface WorkFlowProductRepository extends JpaRepository<WorkFlowProduct, Long> {
    /**
     * @param productId
     * @return
     */
    List<WorkFlowProduct> findByProductIdEquals(long productId);
   /**
     * @param productId
     * @return
     */
 WorkFlowProduct findFirstByProductIdEquals(long productId);



}
