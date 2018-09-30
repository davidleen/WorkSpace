package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.entity.WorkFlowArranger;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by davidleen29 on 2017/4/9.
 */
public class UpdateWorkFlowArrangerUseCase extends UseCase {
    private final WorkFlowArranger workFlowArranger;
    private final WorkFlowRepository workFlowRepository;

    public UpdateWorkFlowArrangerUseCase(Scheduler scheduler, Scheduler immediate, WorkFlowArranger workFlowArranger, WorkFlowRepository workFlowRepository) {
        super(scheduler,immediate);
        this.workFlowArranger = workFlowArranger;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.saveWorkFlowArranger(workFlowArranger);
    }
}
