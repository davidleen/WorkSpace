package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by davidleen29 on 2017/4/9.
 */
public class DeleteWorkFlowArrangerUseCase extends UseCase {
    private final long workFlowArrangerId;
    private final WorkFlowRepository workFlowRepository;

    public DeleteWorkFlowArrangerUseCase(Scheduler scheduler, Scheduler immediate, long workFlowArrangerId, WorkFlowRepository workFlowRepository) {
        super(scheduler,immediate);
        this.workFlowArrangerId = workFlowArrangerId;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {

        return workFlowRepository.deleteWorkFlowArranger(workFlowArrangerId);
    }
}
