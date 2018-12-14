package com.giants3.hd.android.mvp.list;

import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.mvp.PageModel;
import com.giants3.hd.android.mvp.PageModelImpl;
import com.giants3.hd.android.mvp.RemoteDataSubscriber;
import com.giants3.hd.data.interractor.UseCase;
import com.giants3.hd.noEntity.RemoteData;

/**
 * Created by davidleen29 on 2018/3/17.
 */

public abstract class ListPresenterImpl<T> extends BasePresenter<ListMVP.Viewer, PageModel<T>> implements ListMVP.Presenter {

    @Override
    public PageModel<T> createModel() {
        return new PageModelImpl<T>();
    }


    @Override
    public void start() {

    }

    @Override
    public void setKey(String text) {

        getModel().setKey(text);

    }


    @Override
    public void searchData() {


        int pageIndex=0;
        int pageSize=getModel().getPageSize();

        executeListCase(getListUseCase(getModel().getKey(),pageIndex, pageSize));


    }

    private void bindData() {


        getView().bindData(getModel().getDatas());
    }

    @Override
    public void loadMoreData() {
        if (!getModel().hasNextPage()) {
            getView().hideWaiting();
            return;
        }

        int pageIndex = getModel().getPageIndex()+1;
        int pageSize = getModel().getPageSize();
        String key = getModel().getKey();

        UseCase listUseCase = getListUseCase(key, pageIndex+1, pageSize);
        executeListCase(listUseCase);


    }

    private void executeListCase(UseCase useCase) {

        useCase.execute(new RemoteDataSubscriber<T>(this) {
            @Override
            protected void handleRemoteData(RemoteData<T> data) {
                getModel().setRemoteData(data);
                getView().hideWaiting();
                bindData();

            }


        });
    }


}
