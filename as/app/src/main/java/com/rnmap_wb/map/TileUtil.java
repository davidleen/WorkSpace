package com.rnmap_wb.map;

import com.rnmap_wb.utils.StorageUtils;

public class TileUtil {


    public static final String TILE_FILE_PATH = StorageUtils.getFilePath("assets/z%d/y%d/x%dy%dz%d.png");

    public static String getFilePath(int x, int y, int zoom) {


        return String.format(TILE_FILE_PATH,zoom,y, x ,y,zoom);

    }

}
