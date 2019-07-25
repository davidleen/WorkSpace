package com.rnmap_wb.android.dao;

import com.rnmap_wb.android.entity.DownloadTask;
import com.rnmap_wb.android.entity.DownloadTaskDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class IDownloadTaskDao {

    private com.rnmap_wb.android.entity.DownloadTaskDao dao;

    public IDownloadTaskDao(com.rnmap_wb.android.entity.DownloadTaskDao dao) {

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        this.dao = dao;
    }


    public List<DownloadTask> findByLatLngs(String latLngs) {

        QueryBuilder<DownloadTask> queryBuilder = dao.queryBuilder();
        queryBuilder.where(DownloadTaskDao.Properties.LatLngs.eq(latLngs));


        List<DownloadTask> downloadItems = queryBuilder.list();
        return downloadItems;
    }


    public Long insert(DownloadTask downloadTask) {

        return dao.insert(downloadTask);
    }

    public List<DownloadTask> loadAll() {
        return dao.loadAll();
    }

    public void save(DownloadTask downloadTask) {

        dao.save(downloadTask);
    }

    public DownloadTask load(long taskId) {
        return dao.load(taskId);
    }

    public void delete(DownloadTask task) {
        dao.delete(task);
    }

    public List<DownloadTask> findByName(String taskName) {


        QueryBuilder<DownloadTask> queryBuilder = dao.queryBuilder();
        queryBuilder.where(DownloadTaskDao.Properties.Name.eq(taskName));


        List<DownloadTask> downloadItems = queryBuilder.list();
        return downloadItems;


    }

    public void removeAll() {


        dao.deleteAll();
    }
}
