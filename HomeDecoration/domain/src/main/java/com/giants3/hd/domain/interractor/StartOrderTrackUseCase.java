package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * 用户启动订单跟踪
 * Created by david on 20160918
 */
public class StartOrderTrackUseCase extends UseCase {


    private WorkFlowRepository workFlowRepository;

    private String os_no ;

    public StartOrderTrackUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String os_no , WorkFlowRepository workFlowRepository) {
        super(threadExecutor, postExecutionThread);
        this.os_no = os_no;


        this.workFlowRepository = workFlowRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.startOrderTrack(os_no);
    }
}
