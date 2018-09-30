package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2017/5/13.
 */
public class GetErpOrderItemReportUseCase extends DefaultUseCase {
    private final String osNo;
    private final String prdNo;
    private final WorkFlowRepository workFlowRepository;

    public GetErpOrderItemReportUseCase(String osNo, String prdNo, WorkFlowRepository workFlowRepository) {
        super();
        this.osNo = osNo;
        this.prdNo = prdNo;
        this.workFlowRepository = workFlowRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return workFlowRepository.getErpOrderItemReport(osNo,prdNo);
    }
}
