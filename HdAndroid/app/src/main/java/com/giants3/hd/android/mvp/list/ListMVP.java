package com.giants3.hd.android.mvp.list;

import com.giants3.hd.android.mvp.NewPresenter;
import com.giants3.hd.android.mvp.NewViewer;
import com.giants3.hd.android.mvp.PageModel;
import com.giants3.hd.android.mvp.customer.CustomerListMVP;
import com.giants3.hd.data.interractor.UseCase;
import com.giants3.hd.entity.Customer;

import java.util.List;

/**
 * Created by davidleen29 on 2018/11/9.
 */

public class ListMVP {


    interface Model<T> extends PageModel<T> {



    }

    public interface Presenter extends NewPresenter< Viewer> {


        void setKey(String text);

        void searchData();

        void loadMoreData();

        UseCase getListUseCase(String key ,int pageIndex, int pageSize);

    }

    public interface Viewer<T> extends NewViewer {
        void bindData(List<T> datas);
    }
}
