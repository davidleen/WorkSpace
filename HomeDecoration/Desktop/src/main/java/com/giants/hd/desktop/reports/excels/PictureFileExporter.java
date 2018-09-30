package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.local.ImageLoader;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.FileUtils;
import com.giants3.hd.utils.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by davidleen29 on 2017/3/22.
 */
public class PictureFileExporter {


    private String directoryPath;

    public PictureFileExporter(String directoryPath)
    {


        this.directoryPath = directoryPath+"/";
    }

    public  void exportFile(String url, String fileName)
    {


        String file = null;
        try {
            file = ImageLoader.getInstance().cacheFile(HttpUrl.loadPicture(url));

            FileUtils.copyFile(new File(directoryPath+fileName + ".JPG"), new File( file ));


        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
