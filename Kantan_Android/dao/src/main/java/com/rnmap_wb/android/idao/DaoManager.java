package com.rnmap_wb.android.idao;

import com.rnmap_wb.android.dao.DaoMaster;
import com.rnmap_wb.android.dao.DaoSession;

import  android.content.Context;
import org.greenrobot.greendao.database.Database;

public class DaoManager {
    static DaoManager daoManager = new DaoManager();
    private static DaoSession daoSession;
//    private static DaoSession mapTileDaoSession;

    public static DaoManager getInstance() {
        return daoManager;

    }


    public void init(Context application) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, application.getPackageName() + "-db");
        Database db = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);

        daoSession = daoMaster.newSession();



//        DaoMaster.DevOpenHelper temp = new DaoMaster.DevOpenHelper(application, "map.mbtiles");
//        Database database = temp.getWritableDb();
//        mapTileDaoSession = new DaoMaster(database).newSession();
    }


    public void beginTransaction()
    {

            daoSession.getDatabase().beginTransaction();


    }

    public void endTransaction()
    {

        daoSession.getDatabase().endTransaction();


    }


    public void commitTransaction()
    {

        daoSession.getDatabase().setTransactionSuccessful();


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

   // IMbTilesDao iMapTileDao;
//    public IMbTilesDao getMapTileDao()
//    {
//
//        if (iMapTileDao == null) {
//            iMapTileDao = new IMbTilesDao(mapTileDaoSession.getMbTilesDao());
//        }
//        return iMapTileDao;
//
//    }

    private IMessageStateDao iTaskMessageDao;
    public IMessageStateDao getMessageStateDao() {

        if (iTaskMessageDao == null) {
            iTaskMessageDao = new IMessageStateDao(daoSession.getMessageStateDao());
        }
        return iTaskMessageDao;
    }
}
