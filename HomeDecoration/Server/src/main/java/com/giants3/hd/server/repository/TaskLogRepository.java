package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.HdTask;
import com.giants3.hd.entity.HdTaskLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 任务执行状态列表
* Created by davidleen29 on 2014/9/17.
*/
public interface TaskLogRepository extends JpaRepository<HdTaskLog,Long> {


    Page<HdTask> findByTaskIdEqualsOrderByExecuteTimeDesc( long  taskId, Pageable pageable);

}
