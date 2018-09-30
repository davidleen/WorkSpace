package com.giants3.hd.server.repository;

import com.giants3.hd.entity.WorkFlowEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by davidleen29 on 2017/4/8.
 */
public interface WorkFlowEventRepository extends JpaRepository<WorkFlowEvent, Long> {

}
