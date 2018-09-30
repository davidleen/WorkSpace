package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.WorkFlowSubType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 *  二级流程
* Created by davidleen29 on 2014/9/17.
*/
public interface WorkFlowSubTypeRepository extends JpaRepository<WorkFlowSubType,Long> {



    public WorkFlowSubType findFirstByTypeIdEquals(int typeId);
}
