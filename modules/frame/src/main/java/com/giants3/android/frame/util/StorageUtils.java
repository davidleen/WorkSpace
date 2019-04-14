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
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + root + File.separator + fileName;
        return filePath;

    }

    public static String readStringFromFile(String keyInitData) {
        String filePath=getFilePath(keyInitData);
        return  FileUtils.readStringFromFile(filePath);
    }

    /**
     * 获取绝对路径的相对路径
     *
     * @param absolutePath 　绝对路径
     * @return
     */
    public synchronized static String getRelativePath(String absolutePath) {
        if (absolutePath == null || absolutePath.length() == 0) {
            return "";
        }

            return absolutePath;

    }

    /**
     * 获取相对路径的绝对路径（适用于文件的读取）
     *
     * @param relativePath 　相对路径
     * @return 绝对路径，路径不存在，则返回null
     */
    public synchronized static String getAbsolutePath(String relativePath) {
        if ((relativePath == null) || (relativePath.equals(""))) {
            return "";
        }



        return relativePath;
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
}
