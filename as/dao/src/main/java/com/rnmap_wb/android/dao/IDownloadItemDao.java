package com.rnmap_wb.android.dao;

import com.rnmap_wb.android.entity.DownloadItem;
import com.rnmap_wb.android.entity.DownloadItemDao;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class IDownloadItemDao {

    private com.rnmap_wb.android.entity.DownloadItemDao dao;

    public IDownloadItemDao(com.rnmap_wb.android.entity.DownloadItemDao dao) {

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        this.dao = dao;
    }


    public List<DownloadItem> findAllByTaskId(long taskId) {

        QueryBuilder<DownloadItem> downloadItemQueryBuilder = dao.queryBuilder();
        downloadItemQueryBuilder.where(DownloadItemDao.Properties.TaskId.eq(taskId));
        List<DownloadItem> downloadItems = downloadItemQueryBuilder.list();
        return downloadItems;
    }


    public List<DownloadItem> findUnCompleteByTaskId(long taskId) {

       return findUnCompleteByTaskId(taskId,0);
    }
    public List<DownloadItem> findUnCompleteByTaskId(long taskId,int limit) {

        QueryBuilder<DownloadItem> downloadItemQueryBuilder = dao.queryBuilder();
        downloadItemQueryBuilder.where(DownloadItemDao.Properties.TaskId.eq(taskId),DownloadItemDao.Properties.State.eq(0));
        if(limit>0)
        downloadItemQueryBuilder.limit(limit);

        List<DownloadItem> downloadItems = downloadItemQueryBuilder.list();
        return downloadItems;
    }

    public void saveAll(List<DownloadItem> downloadItems) {

         
        dao.saveInTx(downloadItems);
    }

    public void save(DownloadItem downloadItem) {

        dao.saveInTx(downloadItem);


    }


    public void deleteItemByTaskId(Long taskId) {


        DeleteQuery<DownloadItem> downloadItemDeleteQuery = dao.queryBuilder().where(DownloadItemDao.Properties.TaskId.eq(taskId)).buildDelete();
        downloadItemDeleteQuery.executeDeleteWithoutDetachingEntities();


    }
    public DownloadItem findById(long itemId)
    {
        return dao.load(itemId);

    }
}
