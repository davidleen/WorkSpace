package com.xxx.reader.turnner.sim;

import androidx.annotation.IntDef;
import androidx.annotation.IntegerRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by HP on 2018/3/22.
 */

public class SettingContent {

    public static final  int INDENT_DISABLE = 0;

    public static final int INDENT_TWO_CHAR = 2;

    public static final int INDENT_ONE_CHAR = 1;
    private static  SettingContent settingContent = new SettingContent();
    public static int MODE_NIGHT=1;
    public static int MODE_DAY=0;

    public static SettingContent getInstance() {
        return settingContent;
    }

    public int getDayNeightMode() {
        return MODE_DAY;
    }

    public   @IndentMode  int getSettingIndentMode() {
        return indentMode;
    }


    @IndentMode int indentMode;

    public int getSettingSpaceType() {
        return 0;
    }

    public float getTextSize() {
        return 36;
    }

    public float getLineSpace() {
        return 20;
    }

    public float getParaSpace() {
        return 50;
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
