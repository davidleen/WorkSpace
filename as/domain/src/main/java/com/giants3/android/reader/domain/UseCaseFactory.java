package com.giants3.android.reader.domain;

import com.giants3.android.reader.domain.usecase.DefaultGetUseCase;
import com.giants3.android.reader.domain.usecase.DefaultPostJsonUseCase;
import com.giants3.android.reader.domain.usecase.DefaultPostUseCase;
import com.giants3.android.reader.domain.usecase.UploadFileUseCase;
import com.rnmap_wb.android.data.RemoteData;
import com.rnmap_wb.android.data.Task;

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

    public UseCase<RemoteData<Task>> createGetBookListUseCase(String url) {
        return new DefaultGetUseCase<>(url, new RemoteDataParser<Task>(Task.class));
    }

    public <T> UseCase<RemoteData<T>> createUseCase(String url, Class<T> tClass) {


        return new DefaultGetUseCase<>(url, new RemoteDataParser<T>(tClass));

    }


    public <T> UseCase<RemoteData<T>> createPostUseCase(String url, Class<T> tClass) {


        return new DefaultPostUseCase<>(url, new RemoteDataParser<T>(tClass));

    }


    public UseCase createDownloadUseCase(String url, String filePath) {


        return new DefaultGetUseCase<>(url, filePath, null);

    }

    public <T> UseCase<RemoteData<T>> createPostJsonUseCase(String url, String json, Class<T> tClass) {


        return new DefaultPostJsonUseCase<>(url, json, new RemoteDataParser<T>(tClass));

    }


    public <T> UseCase<RemoteData<T>> createUploadFileUseCase(String url, String[] filePaths, Class<T> tClass) {


        return new UploadFileUseCase (url, filePaths, new RemoteDataParser<T>(tClass));

    }




}
