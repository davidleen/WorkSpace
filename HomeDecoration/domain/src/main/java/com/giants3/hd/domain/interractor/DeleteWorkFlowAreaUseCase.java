package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2017/6/17.
 */
public class DeleteWorkFlowAreaUseCase extends DefaultUseCase {
    private final long id;
    private final WorkFlowRepository workFlowRepository;

    public DeleteWorkFlowAreaUseCase(long id, WorkFlowRepository workFlowRepository) {
        super();
        this.id = id;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.deleteWorkFlowArea(id);
    }
}
