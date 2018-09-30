package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/1/27.
 */
public class GetSubWorkFlowListUseCase extends DefaultUseCase {
    private final String key;
    private final String dateStart;
    private final String dateEnd;
    private final WorkFlowRepository workFlowRepository;

    public GetSubWorkFlowListUseCase(String key, String dateStart, String dateEnd, WorkFlowRepository workFlowRepository) {
        super();
        this.key = key;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.getSubWorkFlowList(key,   dateStart,   dateEnd);
    }
}
