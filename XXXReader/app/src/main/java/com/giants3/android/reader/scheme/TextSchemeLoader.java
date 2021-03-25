package com.giants3.android.reader.scheme;

import android.content.Context;
import android.graphics.Color;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.io.FileUtils;
import com.xxx.reader.TextScheme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class TextSchemeLoader {

    public static final String DEFAULT_SCHEME_PATH_NAME = "TextScheme";


    private TextSchemeLoader() {

    }


    public static TextScheme[] loadAllScheme(Context context) {

        TextScheme[] result = null;
        try {
            String themePath = StorageUtils.getFilePath(DEFAULT_SCHEME_PATH_NAME);
            String[] list = new File(themePath).list();


            int length = list.length;
            result = new TextScheme[length];
            for (int i = 0; i < length; i++) {

                result[i] = new TextScheme();
                result[i].setName(list[i]);
                readThemeFromFile(context, result[i], themePath + File.separator + list[i]);
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
     * @param settingScheme
     * @param themeDirectory
     */
    private static void readThemeFromFile(Context context, TextScheme settingScheme, String themeDirectory) {

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
            settingScheme.setTitle(properties.getProperty("title", "").trim());
            String bkimg = properties.getProperty("bkimg", "").trim();
            if ("null".equals(bkimg) || StringUtil.isEmpty(bkimg)) {
                settingScheme.setBackgroundType(TextScheme.BACKGROUND_TYPE_COLOR);
            } else {
                settingScheme.setBackgroundType(TextScheme.BACKGROUND_TYPE_IMAGE);
                settingScheme.setBackgroundImagePath(themeDirectory + File.separator + bkimg);
            }
            String bkthumb = properties.getProperty("bkthumb", "").trim();

            settingScheme.setBackgroundThumbPath(StringUtil.isEmpty(bkthumb) ? "" : (themeDirectory + File.separator + bkthumb));


            String fontColorString = properties.getProperty("fontcolor", "").trim();
            settingScheme.setTextColor(argb(fontColorString));

            String bgColorString = properties.getProperty("bgcolor", "").trim();
            settingScheme.setBackgroundColor(argb(bgColorString));


            String styleIndex = properties.getProperty("styleIndex", "0").trim();
            settingScheme.setStyleIndex(Integer.parseInt(styleIndex));


            String styleMode = properties.getProperty("styleMode", "0").trim();
            settingScheme.setStyleMode(StringUtil.isEmpty(styleMode) ? 0 : Integer.valueOf(styleMode));


            String bkmode = properties.getProperty("bkmode", "stretch").trim();
            settingScheme.setBackgroundTileMode(bkmode);

        } catch (Throwable e) {
            Log.e(e);
        }


    }


}
