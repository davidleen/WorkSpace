package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2017/12/25.
 */
public class GetWorkFlowLimitUseCase extends DefaultUseCase {
    private WorkFlowRepository workFlowRepository;

    public GetWorkFlowLimitUseCase(WorkFlowRepository workFlowRepository) {
        super();
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.getWorkFlowLimit();
    }
}
