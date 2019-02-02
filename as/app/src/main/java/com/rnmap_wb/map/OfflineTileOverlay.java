//package com.rnmap_wb.map;
//
//import com.giants3.android.frame.util.FileUtils;
//import com.giants3.android.frame.util.Log;
//import com.giants3.android.frame.util.StringUtil;
//import com.google.android.gms.maps.model.Tile;
//import com.google.android.gms.maps.model.TileProvider;
//import com.google.maps.android.projection.SphericalMercatorProjection;
//
//
//import java.io.File;
//
//public class OfflineTileOverlay implements TileProvider {
//
//    private int mTileSize = 256;
//    private SphericalMercatorProjection mProjection = new SphericalMercatorProjection(mTileSize);
//    private int mScale = 2;
//    private int mDimension = mScale * mTileSize;
//
//    @Override
//    public Tile getTile(int x, int y, int zoom) {
//
//
//        String tileFilePath = TileUtil.getFilePath(x, y, zoom);
//        if (!StringUtil.isEmpty(tileFilePath)) {
//            Log.e(tileFilePath);
//            if (new File(tileFilePath).exists()) {
//                byte[] bytes = FileUtils.readByteFromFile(tileFilePath);
//
//                return new Tile(mDimension, mDimension, bytes);
//            }
//        }
//
//
//        return null;
//    }
//
//
//}