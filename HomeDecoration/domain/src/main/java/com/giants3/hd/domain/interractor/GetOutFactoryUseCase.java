package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.FactoryRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by davidleen29 on 2017/1/15.
 */
public class GetOutFactoryUseCase extends UseCase {
    private final FactoryRepository factoryRepository;

    public GetOutFactoryUseCase(Scheduler scheduler, Scheduler immediate, FactoryRepository factoryRepository) {
        super(scheduler,immediate);
        this.factoryRepository = factoryRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return factoryRepository.getOutFactories();
    }
}
