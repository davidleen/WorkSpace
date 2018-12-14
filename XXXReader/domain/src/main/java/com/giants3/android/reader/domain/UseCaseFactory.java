package com.giants3.android.reader.domain;

import com.giants3.android.reader.domain.usecase.DefaultGetUseCase;
import com.giants3.reader.entity.Book;
import com.giants3.reader.noEntity.RemoteData;

/**
 * Created by davidleen29 on 2018/11/21.
 */

public class UseCaseFactory {

    public static UseCaseFactory factory = null;

    public synchronized static UseCaseFactory getInstance() {


        if (factory == null) {

            factory = new UseCaseFactory();

        }
        return factory;
    }

    private UseCaseFactory() {


    }

    public UseCase<RemoteData<Book>> createGetBookListUseCase(String url) {
        return new DefaultGetUseCase<>(url, new RemoteDataParser<Book>(Book.class));
    }
    public <T> UseCase<RemoteData<T>> createUseCase(String url,Class<T> tClass) {


            return new DefaultGetUseCase<>(url, new RemoteDataParser<T>(tClass));

    }
 public   UseCase createDownloadUseCase(String url,String filePath) {


            return new DefaultGetUseCase<>(url, filePath,null);

    }





}
