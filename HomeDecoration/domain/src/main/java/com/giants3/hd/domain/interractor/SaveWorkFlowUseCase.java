package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.entity.WorkFlow;
import rx.Observable;
import rx.Scheduler;

import java.util.List;

/**
 * 保存生产流程数据
 * Created by david on 20160917
 */
public class SaveWorkFlowUseCase extends UseCase {


    private WorkFlowRepository workFlowRepository;

    private List<WorkFlow> workFlows;

    public SaveWorkFlowUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, List<WorkFlow> workFlows, WorkFlowRepository workFlowRepository) {
        super(threadExecutor, postExecutionThread);
        this.workFlows = workFlows;


        this.workFlowRepository = workFlowRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.saveWorkFlowList(workFlows);
    }
}
