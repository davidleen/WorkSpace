package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.HdTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  任务
* Created by davidleen29 on 2014/9/17.
*/
public interface TaskRepository extends JpaRepository<HdTask,Long> {



        Page<HdTask> findByTaskNameLikeOrderByStartDateDesc( String name, Pageable pageable);

        HdTask findFirstByTaskNameEquals( String name );


}
