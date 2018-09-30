package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.HdTaskRepository;
import rx.Observable;

/**
 *
 * 执行case
 * Created by david on 2015/10/7.
 */
public class ExecuteHdTaskUseCase extends DefaultUseCase {



    private int taskType;
    private HdTaskRepository taskRepository;


    public ExecuteHdTaskUseCase(  int taskType, HdTaskRepository taskRepository) {
        super( );
        this.taskType = taskType;


        this.taskRepository = taskRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return taskRepository.executeHdTask(taskType);
    }
}
