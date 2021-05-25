package com.giants3.android.frame.util;

import android.os.Environment;
import android.text.TextUtils;

import com.giants3.io.FileUtils;

import java.io.File;

/**
 * Created by davidleen29 on 2018/8/4.
 */

public class StorageUtils {

    public static final long DEFAULT_FILE_SIZE = 20 * 1024 * 1024;  //默认文件大小
    private static   String SDCARD_BASEPATH;
    private static String root;

    public static   void  setRoot(String root)
    {


        StorageUtils.root = root;
        StorageUtils.SDCARD_BASEPATH = root;
    }


    public static  void writeString(String data,String fileName)
    {
        String filePath=getFilePath(fileName);
        try {
            FileUtils.writeStringToFile(data,filePath);
        } catch (Exception e) {
            Log.e(e);
        }


    }




    public static final String getFilePath(String fileName)
    {
        String filePath = getRootPath() + File.separator + fileName;
        return filePath;

    }


    public static String readStringFromFile(String keyInitData) {
        String filePath=getFilePath(keyInitData);
        return  FileUtils.readStringFromFile(filePath);
    }

    public static String getAbsolutePathIgnoreExist(String relativePath) {
        return getFilePath(relativePath);
    }

    public static String getShelfRootPath() {
        return "";
    }

    public static void buildStoragePath(String path ) {

        FileUtils.makeDirs(path);
    }

    public static String getRootPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + root;

    }
}
