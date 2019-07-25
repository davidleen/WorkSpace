package com.giants3.yourreader.text.composite;

public class CompositeConfig {


    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    /**
     * 排版方向
     * {@link CompositeConfig#HORIZONTAL  横线排版}
     * {@link CompositeConfig#VERTICAL  纵向}
     */
    int orientation;


    /**
     * 字间距
     */
    float  wordGap;
    /**
     * 行间距
     */
    float lineGap;


    /**
     * 段落首行缩进字数
     */
    int indentCount;





}
