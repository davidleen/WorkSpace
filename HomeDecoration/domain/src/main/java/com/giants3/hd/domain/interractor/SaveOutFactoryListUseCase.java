package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.FactoryRepository;
import com.giants3.hd.entity.OutFactory;
import rx.Observable;
import rx.Scheduler;

import java.util.List;

/**
 * Created by davidleen29 on 2017/1/15.
 */
public class SaveOutFactoryListUseCase extends UseCase {
    private final List<OutFactory> datas;
    private final FactoryRepository factoryRepository;

    public SaveOutFactoryListUseCase(Scheduler scheduler, Scheduler immediate, List<OutFactory> datas, FactoryRepository factoryRepository) {
        super(scheduler,immediate);
        this.datas = datas;
        this.factoryRepository = factoryRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return factoryRepository.saveOutFactories(datas);
    }
}
