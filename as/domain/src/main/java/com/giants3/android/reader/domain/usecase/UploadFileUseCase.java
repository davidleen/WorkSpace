package com.giants3.android.reader.domain.usecase;

import com.giants3.android.reader.domain.DefaultUseCase;
import com.giants3.android.reader.domain.ResApiFactory;
import com.giants3.android.reader.domain.ResultParser;

import java.io.File;
import java.io.IOException;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 上传图片
 * @param <T>
 */
public class UploadFileUseCase<T> extends DefaultUseCase<T> {
    private final String[] localFilePaths;
    private final String url;
    private ResultParser<T> resultParser;

    @Override
    protected Observable<T> buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {


                try {


                    for (String filePath : localFilePaths) {
                        File file = new File(filePath);
                        String result = ResApiFactory.getInstance().getResApi().uploadFile(url, "file", file);

                        T parser = resultParser.parser(result);
                        subscriber.onNext(parser);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }


                subscriber.onCompleted();


            }
        });

    }


    public UploadFileUseCase( String url,String[] localFilePaths, ResultParser<T> resultParser) {
        super( Schedulers.immediate(), Schedulers.immediate());
        this.localFilePaths = localFilePaths;
        this.url = url;
        this.resultParser = resultParser;
    }
}
