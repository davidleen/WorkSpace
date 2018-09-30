package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.MaterialRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class DeleteMaterialClassUseCase extends UseCase {
    private final long materialClassId;
    private final MaterialRepository materialRepository;

    public DeleteMaterialClassUseCase(Scheduler scheduler, Scheduler immediate, long materialClassId, MaterialRepository materialRepository) {
        super(scheduler,immediate);
        this.materialClassId = materialClassId;
        this.materialRepository = materialRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return materialRepository.deleteMaterialClass(materialClassId);
    }
}
