package com.giants3.hd.android.mvp.MainAct;

import com.giants3.hd.android.adapter.WorkFLowMainMenuAdapter;
import com.giants3.hd.android.mvp.NewModel;
import com.giants3.hd.android.mvp.NewPresenter;
import com.giants3.hd.android.mvp.NewViewer;
import com.giants3.hd.entity.app.AUser;
import com.giants3.hd.noEntity.MessageInfo;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.FileInfo;

import java.util.List;

import rx.Subscriber;

/**
 * 主界面 mvp 接口
 * Created by davidleen29 on 2016/10/21.
 */

public interface WorkFlowMainActMvp {


    interface Model extends NewModel {


        /**
         * 返回app最新apk的路径
         * @param subscriber
         */
        void loadAppUpgradeInfo(Subscriber<RemoteData<FileInfo>> subscriber);

        void setLoginUser(AUser loginUser);
    }

    interface Presenter extends NewPresenter< Viewer> {

        /**
         * 檢查app應用更新信息
         */
        void checkAppUpdateInfo(boolean silence);

        boolean checkBack();

        void setLoginUser(AUser loginUser);

        void attemptUpdateNewMessageCount();

        void updatePassword();

        void init(AUser loginUser);
    }

    interface Viewer extends NewViewer {

        void startDownLoadApk(FileInfo newApkFileInfo);

        void bindUser(AUser loginUser);

        void updateMessageCounts(RemoteData<MessageInfo> msgInfo);

        void showApkUpdate(FileInfo fileInfo);

        void bindMenu(List<WorkFLowMainMenuAdapter.MenuItem> menuItems);
    }
}
