package com.giants.hd.desktop.reports;

import com.giants.hd.desktop.reports.excels.AbstractExcelReporter;
import com.giants3.hd.domain.interractor.DefaultUseCase;
import com.giants3.hd.noEntity.RemoteData;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by davidleen29 on 2018/10/5.
 */
public class ExcelReportTaskUseCase<T> extends DefaultUseCase {


    private final AbstractExcelReporter<T> reporter;
    private final T datas;
    private final String destFilePath;

    public ExcelReportTaskUseCase(AbstractExcelReporter<T> reporter, T datas, String destFilePath) {
        this.reporter = reporter;
        this.datas = datas;
        this.destFilePath = destFilePath;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<Void>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Void>> subscriber) {


                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    reporter.report(datas, destFilePath);
                    subscriber.onNext(new RemoteData<Void>());
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }


            }
        });
    }
}
