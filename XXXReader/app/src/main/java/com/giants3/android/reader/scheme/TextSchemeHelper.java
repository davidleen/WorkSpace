package com.giants3.android.reader.scheme;

import android.content.Context;
import android.os.AsyncTask;

import com.giants3.algorithm.MD5;
import com.giants3.android.compress.ZipHelper;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.BuildConfig;
import com.giants3.io.FileUtils;
import com.xxx.reader.TextScheme;
import com.xxx.reader.TextSchemeContent;
import com.xxx.reader.ThreadConst;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.ZipFile;

import de.greenrobot.common.io.IoUtils;

public class TextSchemeHelper {
    /**
     * 初始化
     */
    public static void init(final Context context) {


        new AsyncTask()
        {


            @Override
            protected Object doInBackground(Object[] objects) {
                prepareTheme(context);
                return null;
            }
        }.executeOnExecutor(ThreadConst.THREAD_POOL_EXECUTOR);






    }

    /**
     * 初始化
     */
    public static void prepareTheme(Context context) {


        String filePath = "TextScheme.zip";
        //check storage textscheme directory
        String absolutePath = StorageUtils.getFilePath(filePath);


        if (StringUtil.isEmpty(absolutePath)) return;


        if(BuildConfig.DEBUG)
        {
            new File(absolutePath).delete();
        }
        boolean newZipFile = false;
        File file = new File(absolutePath);
        if (!file.exists()) {
            newZipFile = true;
        } else {


            MD5 md5=new MD5();
            String md5File = md5.md5File(file);
            String md5Stream="";
            InputStream is=null;
            try {
                is = context.getAssets().open(filePath);
                md5Stream = md5.md5Stream(is);
                newZipFile = !md5File.equals(md5Stream);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                FileUtils.safeClose(is);
            }


        }

        if (newZipFile) {

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

            try {
                ZipFile zipFile=new ZipFile(absolutePath);
                zipFile.size();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ZipHelper.unZip(absolutePath, StorageUtils.getRootPath( ));



            boolean dayMode= TextSchemeContent.getDayMode();

            TextScheme[] schemes=TextSchemeLoader.loadAllScheme(context);


            Arrays.sort(schemes, new Comparator<TextScheme>() {
                @Override
                public int compare(TextScheme o1, TextScheme o2) {
                    return  o1.getStyleIndex()-o2.getStyleIndex();
                }
            });




            TextSchemeContent.setDayMode(true);

            for (TextScheme scheme : schemes) {

                if(scheme.getStyleMode()==0)
                {
                    TextSchemeContent.setTextScheme(scheme);
                    TextSchemeContent.setInit();
                    break;

                }


            }




            TextSchemeContent.setDayMode(false);

            for (TextScheme scheme : schemes) {

                if(scheme.getStyleMode()==1)
                {
                    TextSchemeContent.setTextScheme(scheme);
                    TextSchemeContent.setInit();
                    break;
                }


            }


            TextSchemeContent.setDayMode(dayMode);





        }

    }

    public static String getAbsolutePath(String backgroundImagePath) {


        return StorageUtils.getFilePath(backgroundImagePath);
    }
}
