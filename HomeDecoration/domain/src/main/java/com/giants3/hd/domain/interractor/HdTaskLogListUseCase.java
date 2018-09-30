package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.HdTaskLogRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 * 获取任务case
 * Created by david on 2015/10/7.
 */
public class HdTaskLogListUseCase extends UseCase {



    private HdTaskLogRepository taskRepository;
    private long taskId;

    public HdTaskLogListUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, long taskId, HdTaskLogRepository taskRepository) {
        super(threadExecutor, postExecutionThread);

        this.taskRepository = taskRepository;
        this.taskId = taskId;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return taskRepository.getTaskLogList(taskId);
    }
}
