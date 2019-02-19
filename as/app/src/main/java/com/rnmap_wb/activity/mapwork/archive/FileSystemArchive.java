package com.rnmap_wb.activity.mapwork.archive;

import com.rnmap_wb.map.TileUtil;

import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

public class FileSystemArchive implements IArchiveFile {


    public FileSystemArchive() {
    }


    @Override
    public void init(File pFile) throws Exception {

    }

    @Override
    public InputStream getInputStream(final ITileSource pTileSource, final long pMapTileIndex) {

        File file = new File(TileUtil.getFilePath(MapTileIndex.getX(pMapTileIndex), MapTileIndex.getY(pMapTileIndex), MapTileIndex.getZoom(pMapTileIndex)));
        if(!file.exists()) return null;
        try {

            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Set<String> getTileSources() {
        //the MBTiles spec doesn't store source information in it, so we can't return anything
        return Collections.EMPTY_SET;
    }

    @Override
    public void setIgnoreTileSource(boolean pIgnoreTileSource) {

    }

    @Override
    public void close() {

    }

    @Override
    public String toString() {
        return "FileSystemArchive [ ]";
    }

}
