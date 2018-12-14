package com.giants3.android.reader.domain.usecase;

import com.giants3.android.reader.domain.DefaultUseCase;
import com.giants3.android.reader.domain.ResultParser;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by davidleen29 on 2018/11/22.
 */

public     class DefaultPostUseCase<T> extends DefaultUseCase {

    private String url;
    private ResultParser<T> resultParser;

    public DefaultPostUseCase(String url, Map<String,Object> postData,ResultParser<T> resultParser) {

        this.url = url;
        this.resultParser = resultParser;
    }

    @Override
    protected Observable<T> buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {



                String json="url";

                   T data =resultParser.parser(json);
                    if (data != null) {
                        subscriber.onNext(data);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Exception("xxxxxxxxx"));
                    }


            }
        });
    }


}
