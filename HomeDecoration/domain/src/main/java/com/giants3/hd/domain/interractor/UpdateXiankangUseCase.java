package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.ProductRepository;
import com.giants3.hd.domain.repository.XiankangRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by david on 2015/10/7.
 */
public class UpdateXiankangUseCase extends UseCase {



    private XiankangRepository xiankangRepository;

    public UpdateXiankangUseCase(Scheduler threadExecutor, Scheduler postExecutionThread,  XiankangRepository xiankangRepository) {
        super(threadExecutor, postExecutionThread);

        this.xiankangRepository = xiankangRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return xiankangRepository.updateXiankang();
    }
}
