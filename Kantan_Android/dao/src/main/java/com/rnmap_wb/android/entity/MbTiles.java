package com.rnmap_wb.android.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import static com.rnmap_wb.android.entity.MbTiles.COL_TILES_TILE_COLUMN;
import static com.rnmap_wb.android.entity.MbTiles.COL_TILES_TILE_ROW;
import static com.rnmap_wb.android.entity.MbTiles.COL_TILES_ZOOM_LEVEL;
import static com.rnmap_wb.android.entity.MbTiles.TABLE_TILES;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 地图数据
 */
@Entity(nameInDb = TABLE_TILES, indexes = {
        @Index(value = COL_TILES_ZOOM_LEVEL + "," + COL_TILES_TILE_COLUMN + ", " + COL_TILES_TILE_ROW, unique = false)
})
public class MbTiles {


    @Id
    private Long id;
    //	TABLE tiles (zoom_level INTEGER, tile_column INTEGER, tile_row INTEGER, tile_data BLOB);
    public final static String TABLE_TILES = "tiles";
    public final static String COL_TILES_ZOOM_LEVEL = "zoom_level";
    public final static String COL_TILES_TILE_COLUMN = "tile_column";
    public final static String COL_TILES_TILE_ROW = "tile_row";
    public final static String COL_TILES_TILE_DATA = "tile_data";

    public int zoom_level;
    public int tile_column;
    public double tile_row;
   
    public byte[] tile_data;



@Generated(hash = 1774697581)
public MbTiles(Long id, int zoom_level, int tile_column, double tile_row, byte[] tile_data) {
    this.id = id;
    this.zoom_level = zoom_level;
    this.tile_column = tile_column;
    this.tile_row = tile_row;
    this.tile_data = tile_data;
}

@Generated(hash = 1238240442)
public MbTiles() {
}



public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public int getZoom_level() {
    return this.zoom_level;
}

public void setZoom_level(int zoom_level) {
    this.zoom_level = zoom_level;
}

public int getTile_column() {
    return this.tile_column;
}

public void setTile_column(int tile_column) {
    this.tile_column = tile_column;
}

public double getTile_row() {
    return this.tile_row;
}

public void setTile_row(double tile_row) {
    this.tile_row = tile_row;
}

public byte[] getTile_data() {
    return this.tile_data;
}

public void setTile_data(byte[] tile_data) {
    this.tile_data = tile_data;
}


}
