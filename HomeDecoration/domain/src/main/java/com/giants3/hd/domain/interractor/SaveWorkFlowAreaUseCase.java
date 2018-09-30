package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.entity.WorkFlowArea;
import rx.Observable;

/**
 * Created by davidleen29 on 2017/6/17.
 */
public class SaveWorkFlowAreaUseCase extends DefaultUseCase {
    private final WorkFlowArea data;
    private final WorkFlowRepository workFlowRepository;

    public SaveWorkFlowAreaUseCase(WorkFlowArea data, WorkFlowRepository workFlowRepository) {
        super();
        this.data = data;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {

     return    workFlowRepository.saveWorkFlowArea(data);
    }
}
