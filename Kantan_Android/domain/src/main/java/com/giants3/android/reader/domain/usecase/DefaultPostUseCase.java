package com.giants3.android.reader.domain.usecase;

import com.giants3.android.frame.util.FileUtils;
import com.giants3.android.reader.domain.DefaultUseCase;
import com.giants3.android.reader.domain.ResApiFactory;
import com.giants3.android.reader.domain.ResultParser;

import java.io.File;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by davidleen29 on 2018/11/22.
 */

public class DefaultPostUseCase<T> extends DefaultUseCase {

    private String url;
    private String localFilePath;
    private ResultParser<T> resultParser;

    public DefaultPostUseCase(String url, ResultParser<T> resultParser) {

        this.url = url;

        this.resultParser = resultParser;
    }

    public DefaultPostUseCase(String url, String localFilePath, ResultParser<T> resultParser) {

        this.url = url;
        this.localFilePath = localFilePath;

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

                    try {
                        if (localFilePath != null)
                            FileUtils.writeStringToFile(json, localFilePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    T data = resultParser.parser(json);
                    if (data != null) {
                        subscriber.onNext(data);
                        subscriber.onCompleted();
                    } else {
                        if (!useLocalFile(subscriber)) {
                            subscriber.onError(new Exception("parseError:" + json));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (!useLocalFile(subscriber)) {

                        subscriber.onError(e);
                    }
                }


            }
        });
    }


    private boolean useLocalFile(Subscriber<? super T> subscriber) {
        if (localFilePath != null && new File(localFilePath).exists()) {

            String json = FileUtils.readStringFromFile(localFilePath);
            T data = resultParser.parser(json);
            subscriber.onNext(data);
            subscriber.onCompleted();
            return true;
        }
        return false;


    }


}
