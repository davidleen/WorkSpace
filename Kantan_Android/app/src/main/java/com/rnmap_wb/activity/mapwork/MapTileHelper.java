package com.rnmap_wb.activity.mapwork;

import android.content.Context;

import com.giants3.android.frame.util.FileUtils;
import com.rnmap_wb.BuildConfig;
import com.rnmap_wb.activity.mapwork.archive.FileSystemArchive;
import com.rnmap_wb.mapsource.GoogleTileSource;
import com.rnmap_wb.utils.StorageUtils;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.modules.ArchiveFileFactory;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.OfflineTileProvider;
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.views.MapView;

import java.io.File;
import java.util.List;
import java.util.Set;

public class MapTileHelper {


    private Context context;
    private MapView mMapView;

    public MapTileHelper(Context context, MapView mMapView) {
        this.context = context;
        this.mMapView = mMapView;


    }

    int sourceIndex = 0;

    public void setOnLineMode(boolean set) {
        if (set) {

            mMapView.setTileProvider(new MapTileProviderBasic(context));


//            List<ITileSource> tileSources = TileSourceFactory.getTileSources();
//            int size = tileSources.size();
//            mMapView.setTileSource(tileSources.get(sourceIndex++));
//            sourceIndex = sourceIndex % size;
//            mMapView.setTileSource(GoogleTileSource.GoogleSat);
//            mMapView.setTileSource(GoogleTileSource.GoogleTerrainHybrid);
            mMapView.setTileSource(GoogleTileSource.GoogleHybrid);
            mMapView.setUseDataConnection(set);
        } else {
            setOffLine(context);
        }
    }

    private void setOffLine(Context context) {
        Configuration.getInstance().setDebugMode(BuildConfig.DEBUG);
        String strFilepath = StorageUtils.getFilePath("map.mbtiles");
        FileUtils.makeDirs(strFilepath);
        File exitFile = new File(strFilepath);
        ArchiveFileFactory.registerArchiveFileProvider(FileSystemArchive.class, "mbtiles");
        String extension = strFilepath.substring(strFilepath.indexOf(".") + 1);
        if (ArchiveFileFactory.isFileExtensionRegistered(extension)) {
            try {
                OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(context), new File[]{exitFile});
                mMapView.setTileProvider(tileProvider);
                String source = "";
                IArchiveFile[] archives = tileProvider.getArchives();
                if (archives.length > 0) {
                    Set<String> tileSouce = archives[0].getTileSources();
                    if (!tileSouce.isEmpty()) {
                        source = tileSouce.iterator().next();
                        mMapView.setTileSource(FileBasedTileSource.getSource(source));
                    } else {
                        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
                    }
                } else
                    mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMapView.setUseDataConnection(false);
    }
}