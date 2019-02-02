package com.giants3.yourreader.text;

/**
 *
 * 书籍参数设定
 * Created by davidleen29 on 2018/1/25.
 */

public class BookSetting {
    public static final int INDENT_DISABLE =0 ;
    public static final int INDENT_TWO_CHAR = 1;
    public static final int INDENT_ONE_CHAR = 2;
    static BookSetting bookSetting=new BookSetting();

    public static BookSetting getInstance() {

        return bookSetting;

    }

    public int getSettingIndentMode() {
        return 0;
    }

    public int getSettingSpaceType() {
        return 0;
    }
}
