package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * 获取生产进度二级类型数据
 * Created by david on 2015/10/7.
 */
public class GetWorkFlowSubTypeUseCase extends UseCase {

    private WorkFlowRepository workFlowRepository;

    protected GetWorkFlowSubTypeUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, WorkFlowRepository workFlowRepository) {
        super(threadExecutor, postExecutionThread);
        this.workFlowRepository = workFlowRepository;
    }


    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.getWorkFlowSubTypes();
    }
}
