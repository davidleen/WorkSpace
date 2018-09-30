package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;
import rx.Scheduler;

/**查询指令单
 * Created by davidleen29 on 2017/3/10.
 */
public class SearchZhilingdanUseCase extends UseCase {
    private final String osName;
    private final String startDate;
    private final String endDate;
    private final WorkFlowRepository workFlowRepository;

    public SearchZhilingdanUseCase(Scheduler scheduler, Scheduler immediate, String osName, String startDate, String endDate, WorkFlowRepository workFlowRepository) {
        super(scheduler,immediate);
        this.osName = osName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.searchZhilingdan(osName,startDate,endDate);
    }
}
