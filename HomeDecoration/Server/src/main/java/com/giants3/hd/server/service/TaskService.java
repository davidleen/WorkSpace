package com.giants3.hd.server.service;

import com.giants3.hd.entity.HdTask;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.TaskLogRepository;
import com.giants3.hd.server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by davidleen29 on 2016/8/15.
 */
@Service
public class TaskService extends AbstractService {


    @Autowired
    TaskLogRepository taskLogRepository;
    @Autowired
    TaskRepository taskRepository;


    public RemoteData<HdTask> list(int pageIndex, int pageSize) {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<HdTask> pageValue = taskRepository.findByTaskNameLikeOrderByStartDateDesc("%%", pageable);

        List<HdTask> haTasks = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), haTasks);
    }

    @Transactional
    public RemoteData<HdTask> updateState(long taskId, int taskState) {

        HdTask task = taskRepository.findOne(taskId);

        if (task == null) return wrapError("未找到id=" + taskId + "的任务");
        task.state = taskState;

        taskRepository.save(task);
        return list(0, 100);
    }

    @Transactional
    public  RemoteData<HdTask> delete(  long taskId) {



        taskRepository.delete(taskId);


        return list(0, 100);
    }

    public RemoteData<HdTask> listLog( long taskId,  int pageIndex,   int pageSize) {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<HdTask> pageValue = taskLogRepository.findByTaskIdEqualsOrderByExecuteTimeDesc(taskId, pageable);
        List<HdTask> haTasks = pageValue.getContent();
        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), haTasks);




    }
}
