package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.MaterialRepository;
import com.giants3.hd.entity.MaterialClass;
import rx.Observable;
import rx.Scheduler;

/**
 * 更新材料分类  不存在就添加
 * Created by davidleen29 on 2017/4/2.
 */
public class UpdateMaterialClassUseCase extends UseCase {
    private final MaterialClass materialClass;
    private final MaterialRepository materialRepository;

    public UpdateMaterialClassUseCase(Scheduler scheduler, Scheduler immediate, MaterialClass materialClass, MaterialRepository materialRepository) {
        super(scheduler, immediate);
        this.materialClass = materialClass;
        this.materialRepository = materialRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return materialRepository.updateMaterialClass(materialClass);
    }
}
