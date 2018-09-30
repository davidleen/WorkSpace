package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.HdTaskRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 * 获取任务case
 * Created by david on 2015/10/7.
 */
public class HdTaskListUseCase extends UseCase {



    private HdTaskRepository taskRepository;

    public HdTaskListUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, HdTaskRepository taskRepository) {
        super(threadExecutor, postExecutionThread);

        this.taskRepository = taskRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return taskRepository.getTaskList();
    }
}
