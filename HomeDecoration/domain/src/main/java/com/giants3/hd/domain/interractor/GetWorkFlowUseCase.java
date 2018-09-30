package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * 获取生产进度元数据
 * Created by david on 2015/10/7.
 */
public class GetWorkFlowUseCase extends UseCase {

    private WorkFlowRepository workFlowRepository;

    protected GetWorkFlowUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, WorkFlowRepository workFlowRepository) {
        super(threadExecutor, postExecutionThread);
        this.workFlowRepository = workFlowRepository;
    }


    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.getWorkFlowList();
    }
}
