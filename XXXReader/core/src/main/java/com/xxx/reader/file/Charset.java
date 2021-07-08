package com.xxx.reader.file;

public class Charset {
    public static final int GB18030 = 1;
    public static final int UNKOWN = 0;
    public static final int GB2312 = 2;
    public static final int GBK = 3;
    public static final int UTF_8 = 4;
    public static final int BIG5 = 5;
    public static final int UTF16_LE = 6;
    public static final int UTF16_BE = 7;
    public static final int ASCII = 8;
    public static final String[] ENCODINGS = new String[]{"UTF-8", "GB18030", "GB2312", "GBK",
            "UTF-8", "BIG5", "UTF16-LE", "UTF16-BE", "ASCII"};

    /**
     * @param code
     * @return
     */
    public static String getEncoding(int code) {
        return ENCODINGS[code];
    }
}
