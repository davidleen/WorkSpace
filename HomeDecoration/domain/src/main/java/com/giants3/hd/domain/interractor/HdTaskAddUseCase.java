package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.HdTaskRepository;
import com.giants3.hd.entity.HdTask;
import rx.Observable;
import rx.Scheduler;

/**
 *
 * 添加任务case
 * Created by david on 2015/10/7.
 */
public class HdTaskAddUseCase extends UseCase {


    private final HdTask task;
    private HdTaskRepository taskRepository;

    public HdTaskAddUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, HdTask task, HdTaskRepository taskRepository) {
        super(threadExecutor, postExecutionThread);
        this.task = task;

        this.taskRepository = taskRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return taskRepository.addTask(task);
    }
}
