package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.WorkFlowTimeLimit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
*  客户信息
 *
*/
public interface WorkFlowTimeLimitRepository extends JpaRepository<WorkFlowTimeLimit,Long> {


    WorkFlowTimeLimit findFirstByOrderItemTypeEquals(int orderItemType);
}
