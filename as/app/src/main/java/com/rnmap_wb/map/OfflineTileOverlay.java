package com.rnmap_wb.map;

import com.giants3.android.frame.util.FileUtils;
import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StringUtil;

import com.google.maps.android.projection.SphericalMercatorProjection;


import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.modules.OfflineTileProvider;

import java.io.File;

public class OfflineTileOverlay extends OfflineTileProvider {

    private int mTileSize = 256;
    private SphericalMercatorProjection mProjection = new SphericalMercatorProjection(mTileSize);
    private int mScale = 2;
    private int mDimension = mScale * mTileSize;

    /**
     * Creates a {@link }.
     * throws with the source[] is null or empty
     *
     * @param pRegisterReceiver
     * @param source
     */
    public OfflineTileOverlay(IRegisterReceiver pRegisterReceiver, File[] source) throws Exception {
        super(pRegisterReceiver, source);
    }

//    @Override
//    public TileList.Tile getTile(int x, int y, int zoom) {
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


}