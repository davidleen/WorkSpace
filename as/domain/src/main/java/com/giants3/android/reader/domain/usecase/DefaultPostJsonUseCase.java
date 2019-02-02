package com.giants3.android.reader.domain.usecase;

import com.giants3.android.reader.domain.DefaultUseCase;
import com.giants3.android.reader.domain.ResApiFactory;
import com.giants3.android.reader.domain.ResultParser;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by davidleen29 on 2018/11/22.
 */

public class DefaultPostJsonUseCase<T> extends DefaultUseCase {

    private String url;
    private String json;
    private ResultParser<T> resultParser;

    public DefaultPostJsonUseCase(String url, String json, ResultParser<T> resultParser) {

        this.url = url;
        this.json = json;

        this.resultParser = resultParser;
    }

    @Override
    protected Observable<T> buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {


                try {
                    json = ResApiFactory.getInstance().getResApi().postJson(url, json);

                    T data = resultParser.parser(json);
                    if (data != null) {
                        subscriber.onNext(data);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Exception("xxxxxxxxx"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }


            }
        });
    }


}
