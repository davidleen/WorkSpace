package com.xxx.reader.turnner.sim;

import android.content.Context;

import androidx.annotation.IntDef;

import com.giants3.android.frame.util.Log;
import com.giants3.android.storage.KVFactory;
import com.tencent.mmkv.MMKV;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by davidleen29 on 2018/3/22.
 */

public class SettingContent {

    public static final String SETTING_CACHE_NAME="settings";

    public static final String KEY_TEXT_SIZE ="text_size_to_read";
    public static final String KEY_PARA_SPACE ="para_gap";
    public static final float DEFAULT_TEXT_SIZE=36;
    private static final float DEFAULT_PARA_SPACE = 50;
    private static final float DEFAULT_LINE_SPACE = 20;
    private static final String KEY_LINE_SPACE = "line_space";
    private static final String KEY_STYLE_MODE = "style_mode";

    public static void init(Context context)
    {



    }

    public static final  int INDENT_DISABLE = 0;

    public static final int INDENT_TWO_CHAR = 2;

    public static final int INDENT_ONE_CHAR = 1;
    private static  SettingContent settingContent = new SettingContent();
    public static int MODE_NIGHT=1;
    public static int MODE_DAY=0;

    public static SettingContent getInstance() {
        return settingContent;
    }

    public int getStyleMode() {
        return KVFactory.getInstance(SETTING_CACHE_NAME).getInt(KEY_STYLE_MODE,MODE_DAY);
    }


    public void setStyleMode(int styleMode) {
          KVFactory.getInstance(SETTING_CACHE_NAME).putInt(KEY_STYLE_MODE,styleMode);
    }

    public   @IndentMode  int getSettingIndentMode() {
        return indentMode;
    }


    @IndentMode int indentMode;

    public int getSettingSpaceType() {
        return 0;
    }

    public float getTextSize() {
        return KVFactory.getInstance(SETTING_CACHE_NAME).getFloat(KEY_TEXT_SIZE,DEFAULT_TEXT_SIZE);
    }

    public void setTextSize(float textSize)
    {
        KVFactory.getInstance(SETTING_CACHE_NAME).putFloat(KEY_TEXT_SIZE,textSize);
    }

    public float getLineSpace() {


        return KVFactory.getInstance(SETTING_CACHE_NAME).getFloat(KEY_LINE_SPACE,DEFAULT_LINE_SPACE);
    }
    public void setLineSpace(float lineSpace) {


          KVFactory.getInstance(SETTING_CACHE_NAME).putFloat(KEY_LINE_SPACE,lineSpace);
    }

    public float getParaSpace() {
          return KVFactory.getInstance(SETTING_CACHE_NAME).getFloat(KEY_PARA_SPACE,DEFAULT_PARA_SPACE);
    }
  float[] paddings= new float[]{10,10,10,10};
    public float[] getPaddings() {
        return paddings;
    }

    //用 @IntDef "包住" 常量，这里使用@IntDef来代替Enum枚举，也可以使用@StringDef。它会像Enum枚举一样在编译时期检查变量的赋值情况！
    @IntDef({INDENT_DISABLE, INDENT_TWO_CHAR, INDENT_ONE_CHAR})
    // @Retention 定义策略，是默认注解
    @Retention(RetentionPolicy.SOURCE)
    //接口定义
    public @interface IndentMode {}


    public float getWordGap()
    {
        return 10;
    }

}
