package com.xxx.reader.comic;

import android.content.Context;

import com.giants3.android.frame.util.StorageUtils;
import com.xxx.reader.Url2FileMapper;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.download.DownloadListener;
import com.xxx.reader.prepare.PageBitmapCreator;
import com.xxx.reader.prepare.PagePlayer;
import com.xxx.reader.prepare.PagePlayerBuilder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by davidleen29 on 2018/3/23.
 */

public class ComicPrepareLayer {

    public static PagePlayer<ComicChapter, ComicPageInfo, DrawParam, ComicPageBitmap> createComicPrepareLayer(final Context context, final int screenWidth, final int screenHeight, final IDrawable iDrawable, final DownloadListener downloadListener) {
        PagePlayerBuilder<ComicChapter, ComicPageInfo, DrawParam, ComicPageBitmap> builder = new PagePlayerBuilder();
        builder.setCreator(new PageBitmapCreator<ComicPageBitmap>() {
            @Override
            public ComicPageBitmap create() {
                return new ComicPageBitmap(  context,   screenWidth,   screenHeight,  iDrawable,    downloadListener);
            }
        });
        builder.setiDrawable(iDrawable);

        builder.setPrepareJob(new ComicPrepareJob(new Url2FileMapper<ComicChapter>() {
            @Override
            public String map(ComicChapter iChapter, String url) {


                String fileName= String.valueOf(url.hashCode());
                try {
                    fileName = URLDecoder.decode(url.substring( url.lastIndexOf("/")),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return StorageUtils.getFilePath(fileName);
            }

            @Override
            public String map(String chapterName) {
                return null;
            }
        }));



        return builder.createPrepareLayer();
    }


}
