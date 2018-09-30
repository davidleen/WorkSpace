package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/7/7.
 */
public class GetWorkFlowMessageReportUseCase extends DefaultUseCase {

    private final WorkFlowRepository workFlowRepository;
    private final String dateStart;
    private final String dateEnd;
    private final boolean unhandle;
    private final boolean overdue;

    public GetWorkFlowMessageReportUseCase(String dateStart, String dateEnd, boolean unhandle, boolean overdue, WorkFlowRepository workFlowRepository) {
        super();
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.unhandle = unhandle;
        this.overdue = overdue;

        this.workFlowRepository = workFlowRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {

        return workFlowRepository.getWorkFlowMessageReport(  dateStart,   dateEnd,   unhandle,   overdue);


    }
}
