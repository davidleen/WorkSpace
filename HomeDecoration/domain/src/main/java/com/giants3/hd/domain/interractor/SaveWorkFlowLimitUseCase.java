package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.entity.WorkFlowTimeLimit;

import rx.Observable;

import java.util.List;

/**
 * Created by davidleen29 on 2017/12/27.
 */
public class SaveWorkFlowLimitUseCase extends DefaultUseCase {
    private List<WorkFlowTimeLimit> workFlowLimit;
    private boolean updateCompletedOrderItem;
    private WorkFlowRepository workFlowRepository;

    public SaveWorkFlowLimitUseCase(List<WorkFlowTimeLimit> workFlowLimit,boolean updateCompletedOrderItem,WorkFlowRepository workFlowRepository) {
        super();
        this.workFlowLimit = workFlowLimit;
        this.updateCompletedOrderItem = updateCompletedOrderItem;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.saveWorkFlowLimit(workFlowLimit,updateCompletedOrderItem);
    }
}
