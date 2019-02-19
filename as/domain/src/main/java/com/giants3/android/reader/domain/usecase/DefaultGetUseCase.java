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

public class DefaultGetUseCase<T> extends DefaultUseCase {

    private String url;
    private String filePath;
    private ResultParser<T> resultParser;

    public DefaultGetUseCase(String url, ResultParser<T> resultParser) {
        this(url, null, resultParser);
    }

    public DefaultGetUseCase(String url, String filePath, ResultParser<T> resultParser) {


        this.url = url;
        this.filePath = filePath;
        this.resultParser = resultParser;
    }

    @Override
    protected Observable<T> buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {


                try {


                    if (null != filePath) {

                        if(url.contains("211.159.164.231"))
                        {
                            url=url.replace("211.159.164.231","193.112.141.231");
                        }

                        try {
                            ResApiFactory.getInstance().getResApi().download(url, filePath);


                        } catch (Throwable t) {

                            t.printStackTrace();
                        } finally {

                        }

                        subscriber.onNext(null);
                        subscriber.onCompleted();


                    } else {
                        String json = null;
                        json = ResApiFactory.getInstance().getResApi().getString(url );
                        T data = resultParser.parser(json);

                        if (data != null) {
                            subscriber.onNext(data);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("parse fail!===" + json));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }


            }
        });
    }


}
