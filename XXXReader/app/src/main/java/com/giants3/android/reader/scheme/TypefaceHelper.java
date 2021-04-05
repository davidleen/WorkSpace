package com.giants3.android.reader.scheme;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;

import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.BuildConfig;
import com.xxx.reader.ThreadConst;

import java.io.File;
import java.util.List;

public class TypefaceHelper {


    public static final String PATH="fonts";

    public static Typeface createFromFile(String filePath)
    {


      return   Typeface.createFromFile(new File( filePath));
    }

    public static Typeface createFromAssets(Context context, String filePath)
    {


      return   Typeface.createFromAsset(context.getAssets(), filePath);
    }



    public  static List<TypefaceEntity> getAllFace()
    {





        return null;
    }

    public static void init(final Context context) {


        new AsyncTask()
        {


            @Override
            protected Object doInBackground(Object[] objects) {
                prepareTypefaces(context);
                return null;
            }
        }.executeOnExecutor(ThreadConst.THREAD_POOL_EXECUTOR);



    }



    public static final void prepareTypefaces(Context context)
    {
        String filePath = "Typeface.zip";
        AssetHelper.copyFileToSD(context,filePath);





    }
}
