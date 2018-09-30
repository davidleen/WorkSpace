package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by davidleen29 on 2017/4/8.
 */
public class GetWorkFlowWorkerUseCase extends UseCase {
    private final WorkFlowRepository workFlowRepository;

    public GetWorkFlowWorkerUseCase(Scheduler scheduler, Scheduler immediate, WorkFlowRepository workFlowRepository) {
        super(scheduler,immediate);
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.loadWorkFlowWorkers();
    }
}
