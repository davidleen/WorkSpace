package com.xxx.reader;

/**
 * 主题数据类
 */
public class TextScheme {


    public static final int BACKGROUND_TYPE_IMAGE = 1;
    public static final int BACKGROUND_TYPE_COLOR = 2;

    private String title;

    private String name;
    private int backgroundType = BACKGROUND_TYPE_IMAGE;
    private String backgroundImagePath;
    private String backgroundThumbPath;
    private String backgroundTileMode;


    public int getStyleMode() {
        return styleMode;
    }

    private int backgroundColor;


    private int textColor;

    private int styleIndex;




    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name=name;
    }


    /**
     * 样式模式  0 白天，1 黑夜
     */
    private int styleMode = 0;


    public String getTitle() {
        return ((title == null) ? "" : title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBackgroundType() {
        return backgroundType;
    }

    public void setBackgroundType(int backgroundType) {
        this.backgroundType = backgroundType;
    }

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }

    public void setBackgroundImagePath(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
    }

    public String getBackgroundThumbPath() {
        return backgroundThumbPath;
    }

    public void setBackgroundThumbPath(String backgroundThumbPath) {
        this.backgroundThumbPath = backgroundThumbPath;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }



    public int getStyleIndex() {
        return styleIndex;
    }

    public void setStyleIndex(int styleIndex) {
        this.styleIndex = styleIndex;
    }






    public void setStyleMode(int styleMode) {
        this.styleMode=styleMode;

    }


    public void setBackgroundTileMode(String backgroundTileMode) {


        this.backgroundTileMode=backgroundTileMode;


    }

    public String getBackgroundTileMode() {
        return backgroundTileMode;
    }
}
