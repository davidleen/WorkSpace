package com.giants3.android.reader.scheme;

import android.content.Context;

import com.giants3.algorithm.MD5;
import com.giants3.android.compress.ZipHelper;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.BuildConfig;
import com.giants3.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import de.greenrobot.common.io.IoUtils;

public class AssetHelper {

    public static boolean copyFileToSD(Context context, String filePath) {


        //check storage textscheme directory
        String absolutePath = StorageUtils.getFilePath(filePath);


        if (StringUtil.isEmpty(absolutePath)) return false;


        if (BuildConfig.DEBUG) {
            new File(absolutePath).delete();
        }
        boolean newFile = false;
        File file = new File(absolutePath);
        if (!file.exists()) {
            newFile = true;
        } else {


            MD5 md5 = new MD5();
            String md5File = md5.md5File(file);
            String md5Stream = "";
            InputStream is = null;
            try {
                is = context.getAssets().open(filePath);
                md5Stream = md5.md5Stream(is);
                newFile = !md5File.equals(md5Stream);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                FileUtils.safeClose(is);
            }


        }

        if (newFile) {

            FileUtils.makeDirs(absolutePath);
            FileOutputStream fileOutputStream = null;
            InputStream open = null;
            try {

                open = context.getAssets().open(filePath);
                fileOutputStream = new FileOutputStream(file);
                FileUtils.copyStream(open, fileOutputStream);

                //ZipJNIInterface.UnZip()
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                IoUtils.safeClose(open);
                IoUtils.safeClose(fileOutputStream);
            }




        }

        if(newFile&&absolutePath.endsWith(".zip")) {
            try {
                ZipFile zipFile = new ZipFile(absolutePath);
                zipFile.size();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ZipHelper.unZip(absolutePath, StorageUtils.getRootPath());
        }
        return  newFile;
    }
}
