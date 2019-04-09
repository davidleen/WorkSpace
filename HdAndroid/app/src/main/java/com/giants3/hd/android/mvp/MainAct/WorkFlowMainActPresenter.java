package com.giants3.hd.android.mvp.MainAct;

import android.app.Activity;
import android.content.res.Resources;

import com.giants3.hd.android.BuildConfig;
import com.giants3.hd.android.R;
import com.giants3.hd.android.activity.UpdatePasswordActivity;
import com.giants3.hd.android.adapter.WorkFLowMainMenuAdapter;
import com.giants3.hd.android.helper.AuthorityUtil;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.mvp.RemoteDataSubscriber;
import com.giants3.hd.appdata.AUser;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.FileInfo;
import com.giants3.hd.noEntity.MessageInfo;
import com.giants3.hd.noEntity.RemoteData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;

/**
 * Created by davidleen29 on 2016/10/21.
 */

public class WorkFlowMainActPresenter extends BasePresenter<WorkFlowMainActMvp.Viewer, WorkFlowMainActMvp.Model> implements WorkFlowMainActMvp.Presenter {
    @Override
    public void start() {

    }

    public WorkFlowMainActPresenter() {

    }

    @Override
    public WorkFlowMainActMvp.Model createModel() {
        return new WorkFlowMainActModel();
    }

    @Override
    public void checkAppUpdateInfo(final boolean silence) {


        getModel().loadAppUpgradeInfo(new Subscriber<RemoteData<FileInfo>>() {
            @Override
            public void onCompleted() {

                if (!silence)
                    getView().hideWaiting();
            }

            @Override
            public void onError(Throwable e) {
                if (!silence) {
                    getView().hideWaiting();
                    getView().showMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(RemoteData<FileInfo> stringRemoteData) {

                getView().hideWaiting();
                if (stringRemoteData.isSuccess()) {


                    if (stringRemoteData.datas.size() > 0) {

                        FileInfo fileInfo = stringRemoteData.datas.get(0);


                        getView().showApkUpdate(fileInfo);

                    } else {
                        if (!silence)
                            getView().showMessage("当前已经是最新版本。");
                    }
                } else {
                    if (!silence)
                        getView().showMessage(stringRemoteData.message);
                }


            }
        });
        if (!silence)
            getView().showWaiting();
    }

    //上次返回键点击时间
    long lastBackPressTime;

    @Override
    public boolean checkBack() {


        long time = Calendar.getInstance().getTimeInMillis();

        if (time - lastBackPressTime < 2000) {
            return true;
        }
        lastBackPressTime = time;
        return false;


    }


    @Override
    public void setLoginUser(AUser loginUser) {


        getModel().setLoginUser(loginUser);
        getView().bindUser(loginUser);

    }

    @Override
    public void attemptUpdateNewMessageCount() {
        UseCaseFactory.getInstance().createGetNewMessageInfoUseCase().execute(new RemoteDataSubscriber<MessageInfo>(this) {


            @Override
            protected void handleRemoteData(RemoteData<MessageInfo> data) {


                int count = 0;
                if (data.isSuccess()) {
                    count = data.datas.get(0).newWorkFlowMessageCount;
                }

                getView().setNewWorkFlowMessageCount(count);
            }


        });
        ;

    }

    @Override
    public void updatePassword() {


        UpdatePasswordActivity.startActivity((Activity) getView(), 0);
    }

    @Override
    public void init(AUser loginUser) {


        List<WorkFLowMainMenuAdapter.MenuItem> menuItems = new ArrayList<>();

        Resources resources = getView().getContext().getResources();
        if (loginUser.isSalesman || BuildConfig.DEBUG) {

            String[] menuTitles = resources.getStringArray(R.array.quotation_menu_title);
            String[] menuFragmentClass = resources.getStringArray(R.array.quotation_menu_fragemnt_class);
            for (int i = 0; i < menuTitles.length; i++) {
                WorkFLowMainMenuAdapter.MenuItem item = new WorkFLowMainMenuAdapter.MenuItem();
                item.title = menuTitles[i];
                item.fragmentClass = menuFragmentClass[i];
                menuItems.add(item);

            }

        }





        boolean canSeeQuotation=false   ;
        try {
            canSeeQuotation=  AuthorityUtil.getInstance().viewQuotationList();
        }catch (Throwable t)
        {
            t.printStackTrace();
        }
        if (canSeeQuotation ) {

            String[] menuTitles = resources.getStringArray(R.array.quotation_inner_menu_title);
            String[] menuFragmentClass = resources.getStringArray(R.array.quotation_inner_menu_fragemnt_class);
            for (int i = 0; i < menuTitles.length; i++) {
                WorkFLowMainMenuAdapter.MenuItem item = new WorkFLowMainMenuAdapter.MenuItem();
                item.title = menuTitles[i];
                item.fragmentClass = menuFragmentClass[i];
                menuItems.add(item);

            }

        }


        {
            String[] menuTitles = resources.getStringArray(R.array.menu_title);
            String[] menuFragmentClass = resources.getStringArray(R.array.menu_fragemnt_class);

            for (int i = 0; i < menuTitles.length; i++) {
                WorkFLowMainMenuAdapter.MenuItem item = new WorkFLowMainMenuAdapter.MenuItem();
                item.title = menuTitles[i];
                item.fragmentClass = menuFragmentClass[i];
                menuItems.add(item);

            }
        }

        {
            String[] menuTitles = resources.getStringArray(R.array.product_menu_title);
            String[] menuFragmentClass = resources.getStringArray(R.array.product_menu_fragemnt_class);

            for (int i = 0; i < menuTitles.length; i++) {
                WorkFLowMainMenuAdapter.MenuItem item = new WorkFLowMainMenuAdapter.MenuItem();
                item.title = menuTitles[i];
                item.fragmentClass = menuFragmentClass[i];
                menuItems.add(item);

            }
        }

        getView().bindMenu(menuItems);


    }
}
