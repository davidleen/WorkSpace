package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.entity.WorkFlowSubType;
import rx.Observable;
import rx.Scheduler;

import java.util.List;

/**保存排厂类型列表
 * Created by davidleen29 on 2017/2/19.
 */
public class SaveWorkFlowSubTypeListUseCase extends UseCase {
    private final List<WorkFlowSubType> datas;
    private final WorkFlowRepository workFlowRepository;

    public SaveWorkFlowSubTypeListUseCase(Scheduler scheduler, Scheduler immediate, List<WorkFlowSubType> datas, WorkFlowRepository workFlowRepository) {
        super(scheduler,immediate);
        this.datas = datas;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.saveWorkFlowSubTypeList(datas);
    }
}
