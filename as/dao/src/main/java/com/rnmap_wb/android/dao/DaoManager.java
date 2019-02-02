package com.rnmap_wb.android.dao;

import android.app.Application;

import com.rnmap_wb.android.entity.DaoMaster;
import com.rnmap_wb.android.entity.DaoSession;
import com.rnmap_wb.android.entity.DownloadTaskDao;

import org.greenrobot.greendao.database.Database;

public class DaoManager {
    static DaoManager daoManager = new DaoManager();
    private static DaoSession daoSession;

    public static DaoManager getInstance() {
        return daoManager;

    }


    public void init(Application application) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, application.getApplicationInfo().name + "-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }


    public IDownloadTaskDao getDownloadTaskDao() {

        if (iDownloadTaskDao == null) {
            iDownloadTaskDao = new IDownloadTaskDao(daoSession.getDownloadTaskDao());
        }
        return iDownloadTaskDao;
    }

    IDownloadItemDao iDownloadItemDao;
    IDownloadTaskDao iDownloadTaskDao;

    public IDownloadItemDao getDownloadItemDao() {

        if (iDownloadItemDao == null) {
            iDownloadItemDao = new IDownloadItemDao(daoSession.getDownloadItemDao());
        }
        return iDownloadItemDao;
    }

//        public <T> AbstractDao<T,Long> getDao(T className)
//        {
//
//        }

}
