package com.xxx.reader;

import com.giants3.android.kit.ColorStateListFactory;
import com.giants3.android.storage.KVFactory;
import com.xxx.reader.turnner.sim.SettingContent;

public class TextSchemeContent {

    public static final String TEXT_SCHEME_NAME = "TEXT_SCHEME";
    public static final String BACK_GROUND_COLOR = "backGroundColor";
    public static final String BACK_GROUND_IMAGE_PATH = "backGroundImagePath";
    private static final String TEXT_COLOR = "textColor";

    private static   int mode =SettingContent.getInstance().getStyleMode();

    public static int getBackgroundType() {


        return KVFactory.getInstance(TEXT_SCHEME_NAME + mode).getInt("backgroundType", 0);

    }
    public static void setBackgroundType(int backgroundType) {

          KVFactory.getInstance(TEXT_SCHEME_NAME + mode).putInt("backgroundType", backgroundType);

    }


    public static String getBackGroundImagePath() {
        return KVFactory.getInstance(TEXT_SCHEME_NAME + mode).getString(BACK_GROUND_IMAGE_PATH, "");
    }


    public static void setBackGroundImagePath(String imagePath) {
          KVFactory.getInstance(TEXT_SCHEME_NAME + mode).putString(BACK_GROUND_IMAGE_PATH, imagePath);
    }

    public static int getBackGroundColor() {
        return KVFactory.getInstance(TEXT_SCHEME_NAME + mode).getInt(BACK_GROUND_COLOR, 0);
    }

    public static void setBackGroundColor(int color)
    {

          KVFactory.getInstance(TEXT_SCHEME_NAME + mode).putInt(BACK_GROUND_COLOR, color);

    }

     public static int getTextColor() {
        return KVFactory.getInstance(TEXT_SCHEME_NAME + mode).getInt(TEXT_COLOR, 0);
    }

    public static void setTextColor(int color)
    {

          KVFactory.getInstance(TEXT_SCHEME_NAME + mode).putInt(TEXT_COLOR, color);

    }




    public static boolean hasInit() {
        return KVFactory.getInstance(TEXT_SCHEME_NAME + mode).getBoolean("init", false);
    }

    public static void setInit() {
        KVFactory.getInstance(TEXT_SCHEME_NAME + mode).putBoolean("init", true);
    }

    public static void setTextScheme(TextScheme textSchemeName ) {


        setBackGroundColor(textSchemeName.getBackgroundColor());
        setBackGroundImagePath(textSchemeName.getBackgroundImagePath());
        setTextColor(textSchemeName.getTextColor());
        setBackgroundType(textSchemeName.getBackgroundType());
        setName(textSchemeName.getName());
        setBackgroundTileMode(textSchemeName.getBackgroundTileMode());


    }

    public  static void setName(String name) {



        KVFactory.getInstance(TEXT_SCHEME_NAME + mode).putString("name", name);
    }

    public static String getBackgroundTileMode() {



        return KVFactory.getInstance(TEXT_SCHEME_NAME + mode).getString("backgroundTileMode", "");
    }

    public  static void setBackgroundTileMode(String name) {



        KVFactory.getInstance(TEXT_SCHEME_NAME + mode).putString("backgroundTileMode", name);
    }

    public static String getName() {



        return KVFactory.getInstance(TEXT_SCHEME_NAME + mode).getString("name", "");
    }




    public static void setDayMode(boolean dayMode)
    {
        mode=dayMode?0:1;
        SettingContent.getInstance().setStyleMode(mode);

    }

    public static boolean getDayMode()
    {
        return mode==0;
    }

    public static int getMode()
    {
        return mode;
    }

    public static int getBackPageColor() {



        return ColorStateListFactory.alphaColor( getBackGroundColor(),0.625f);
    }

}
