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

public class DefaultPostUseCase<T> extends DefaultUseCase {

    private String url;
    private ResultParser<T> resultParser;

    public DefaultPostUseCase(String url, ResultParser<T> resultParser) {

        this.url = url;

        this.resultParser = resultParser;
    }

    @Override
    protected Observable<T> buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {


                String json = "url";
                try {
                    int index = url.indexOf("?");
                    String requestUrl = index == -1 ? url : url.substring(0, index);
                    String postData = index == -1 ? "" : url.substring(index
                            + 1);
                    json = ResApiFactory.getInstance().getResApi().post(requestUrl, postData);

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
