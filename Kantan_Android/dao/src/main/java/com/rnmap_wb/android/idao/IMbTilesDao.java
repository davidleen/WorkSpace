package com.rnmap_wb.android.idao;

import com.rnmap_wb.android.entity.MbTiles;
import com.rnmap_wb.android.dao.MbTilesDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

public class IMbTilesDao {


    private com.rnmap_wb.android.dao.MbTilesDao dao;

    public IMbTilesDao(com.rnmap_wb.android.dao.MbTilesDao dao) {

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        this.dao = dao;
    }


    public boolean exist(int colIndex, int rowIndex, int zoom) {
        Query<MbTiles> build = dao.queryBuilder().where(MbTilesDao.Properties.Tile_column.eq(colIndex), MbTilesDao.Properties.Tile_row.eq(rowIndex), MbTilesDao.Properties.Zoom_level.eq(zoom)).build();
        return build.list().size() > 0;

    }

    public void save(MbTiles mbTiles) {
        dao.save(mbTiles);
    }
}
