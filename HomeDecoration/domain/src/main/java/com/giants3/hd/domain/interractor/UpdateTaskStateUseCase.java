package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.HdTaskRepository;
import rx.Observable;
import rx.Scheduler;

/**
 *
 * 删除任务case
 * Created by david on 2015/10/7.
 */
public class UpdateTaskStateUseCase extends DefaultUseCase {


    private final long taskId;
    private int taskState;
    private HdTaskRepository taskRepository;


    public UpdateTaskStateUseCase(  long taskId,int taskState, HdTaskRepository taskRepository) {
        super( );
        this.taskId = taskId;
        this.taskState = taskState;

        this.taskRepository = taskRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return taskRepository.updateTaskState(taskId,taskState);
    }
}
