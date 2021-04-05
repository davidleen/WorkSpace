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
        boolean newFileInited = AssetHelper.copyFileToSD(context, filePath);

        if (!newFileInited) return ;
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

    public static String getAbsolutePath(String backgroundImagePath) {


        return StorageUtils.getFilePath(backgroundImagePath);
    }
}
