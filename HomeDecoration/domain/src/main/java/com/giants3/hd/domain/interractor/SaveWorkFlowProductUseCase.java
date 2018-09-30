package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.entity.WorkFlowProduct;
import rx.Observable;
import rx.Scheduler;

/**保存
 * Created by davidleen29 on 2017/1/9.
 */
public class SaveWorkFlowProductUseCase extends UseCase {
    private final WorkFlowProduct workFlowProduct;
    private final WorkFlowRepository workFlowRepository;

    public SaveWorkFlowProductUseCase(Scheduler scheduler, Scheduler immediate, WorkFlowProduct workFlowProduct, WorkFlowRepository workFlowRepository) {
        super(scheduler,immediate);
        this.workFlowProduct = workFlowProduct;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.saveWorkFlowProduct(workFlowProduct);
    }
}
