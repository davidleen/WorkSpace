package com.giants3.yourreader.text;

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

public class TextPrepareLayer {

    public static PagePlayer<TextChapter, TextPageInfo, DrawParam, TextPageBitmap> createTextPrepareLayer(final Context context, final int screenWidth, final int screenHeight, final IDrawable iDrawable, final DownloadListener downloadListener) {
        PagePlayerBuilder<TextChapter, TextPageInfo, DrawParam, TextPageBitmap> builder = new PagePlayerBuilder();
        builder.setCreator(new PageBitmapCreator<TextPageBitmap>() {
            @Override
            public TextPageBitmap create() {
                return new TextPageBitmap(  context,   screenWidth,   screenHeight,  iDrawable );
            }
        });
        builder.setiDrawable(iDrawable);

        builder.setPrepareJob(new TextPrepareJob(new Url2FileMapper<TextChapter>() {
            @Override
            public String map(TextChapter iChapter, String url) {


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
