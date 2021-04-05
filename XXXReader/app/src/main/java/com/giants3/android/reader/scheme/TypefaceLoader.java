package com.giants3.android.reader.scheme;

import android.content.Context;
import android.graphics.Color;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class TypefaceLoader {

    public static final String DEFAULT_TYPEFACE_PATH_NAME = "Typeface";


    private TypefaceLoader() {

    }


    public static TypefaceEntity[] loadAllScheme(Context context) {

        TypefaceEntity[] result = null;
        try {
            String themePath = StorageUtils.getFilePath(DEFAULT_TYPEFACE_PATH_NAME);
            String[] list = new File(themePath).list();


            int length = list.length;
            result = new TypefaceEntity[length];
            for (int i = 0; i < length; i++) {

                result[i] = new TypefaceEntity();
                result[i].setName(list[i]);
                readTypefaceFromFile(context, result[i], themePath + File.separator + list[i]);
            }


        } catch (Throwable ioException) {
            ioException.printStackTrace();
        }
        return result;


    }


    public static int argb(String strColor) {

        if (strColor.toLowerCase().startsWith("0x")) {
            strColor = "#" + strColor.substring(2);

        }

        if (!strColor.startsWith("#")) {
            Log.e("error color string :" + strColor);
            return 0;
        } else
            return Color.parseColor(strColor);

    }


    /**
     * 读取Assets中主题配色文件。
     *
     * @param entity
     * @param themeDirectory
     */
    private static void readTypefaceFromFile(Context context, TypefaceEntity entity, String themeDirectory) {

        InputStream open = null;
        BufferedReader bufferedReader = null;
        InputStreamReader in = null;
        Properties properties = new Properties();
        try {
            //asset 路径下的配置。
            String initPath = themeDirectory + "/cfg.ini";
            open = new FileInputStream(initPath);
            in = new InputStreamReader(open);
            bufferedReader = new BufferedReader(in);
            properties.load(bufferedReader);
        } catch (IOException ioException) {
            Log.e(ioException);
        } finally {
            FileUtils.safeClose(bufferedReader);
            FileUtils.safeClose(open);
            FileUtils.safeClose(in);
        }
        try {
            entity.setTitle(properties.getProperty("title", "").trim());

            String thumb = properties.getProperty("thumb", "").trim();

            entity.setThumb(StringUtil.isEmpty(thumb) ? "" : (themeDirectory + File.separator + thumb));






            String index = properties.getProperty("index", "0").trim();
            entity.setIndex(Integer.parseInt(index));


            String language = properties.getProperty("language", "").trim();
            entity.setLanguage(language);

            String typeface = properties.getProperty("typeface", "").trim();
            entity.setTypeface(StringUtil.isEmpty(typeface) ? "" : (themeDirectory + File.separator + typeface));



        } catch (Throwable e) {
            Log.e(e);
        }


    }


}
