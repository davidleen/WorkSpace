package com.rnmap_wb.map;

import com.rnmap_wb.utils.StorageUtils;

public class TileUtil {


    public static final String TILE_FILE_PATH = StorageUtils.getFilePath("assets/x%dy%dz%d");

    public static String getFilePath(int x, int y, int zoom) {


        return String.format(TILE_FILE_PATH, x, y, zoom);

    }

}
