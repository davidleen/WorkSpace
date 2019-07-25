package com.rnmap_wb.utils;

import android.os.Environment;

import com.giants3.android.frame.util.FileUtils;
import com.giants3.android.frame.util.Log;

import java.io.File;

/**
 * Created by davidleen29 on 2018/8/4.
 */

public class StorageUtils {


    private static String root;

    public static void setRoot(String root) {


        StorageUtils.root = root;
    }


    public static void writeString(String data, String fileName) {
        String filePath = getFilePath(fileName);
        try {
            FileUtils.writeStringToFile(data, filePath);
        } catch (Exception e) {
            Log.e(e);
        }


    }


    public static final String getFilePath(String fileName) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + root + File.separator + fileName;
        return filePath;

    }

    public static String readStringFromFile(String keyInitData) {
        String filePath = getFilePath(keyInitData);
        return FileUtils.readStringFromFile(filePath);
    }
}
