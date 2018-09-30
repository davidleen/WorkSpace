package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.entity.WorkFlowWorker;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by davidleen29 on 2017/4/8.
 */
public class UpdateWorkFlowWorkerUseCase extends UseCase {
    private final WorkFlowWorker workFlowWorker;
    private final WorkFlowRepository workFlowRepository;

    public UpdateWorkFlowWorkerUseCase(Scheduler scheduler, Scheduler immediate, WorkFlowWorker workFlowWorker, WorkFlowRepository workFlowRepository) {
        super(scheduler,immediate);
        this.workFlowWorker = workFlowWorker;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.saveWorkFlowWorker(workFlowWorker);
    }
}
