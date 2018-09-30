package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;
import rx.Scheduler;

import java.sql.Statement;

/**
 * Created by davidleen29 on 2017/4/9.
 */
public class DeleteWorkFlowWorkerUseCase extends UseCase {
    private final long workFlowWorkerId;
    private final WorkFlowRepository workFlowRepository;

    public DeleteWorkFlowWorkerUseCase(Scheduler scheduler, Scheduler immediate, long workFlowWorkerId, WorkFlowRepository workFlowRepository) {
        super(scheduler,immediate);
        this.workFlowWorkerId = workFlowWorkerId;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.deleteWorkFlowWorker(workFlowWorkerId);
    }
}
