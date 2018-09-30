package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2017/6/17.
 */
public class GetWorkFlowEventAreaListUseCase extends DefaultUseCase {
    private WorkFlowRepository workFlowRepository;

    public GetWorkFlowEventAreaListUseCase(WorkFlowRepository workFlowRepository) {
        super();
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.getWorkFlowAreaList();
    }
}
