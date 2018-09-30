package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.HdTaskRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 * 删除任务case
 * Created by david on 2015/10/7.
 */
public class HdTaskDeleteUseCase extends UseCase {


    private final long taskId;
    private HdTaskRepository taskRepository;


    public HdTaskDeleteUseCase(Scheduler threadExecutor, Scheduler postExecutionThread,long taskId, HdTaskRepository taskRepository) {
        super(threadExecutor, postExecutionThread);
        this.taskId = taskId;

        this.taskRepository = taskRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return taskRepository.delete(taskId);
    }
}
