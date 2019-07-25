package com.xxx.reader.file;

import android.text.TextUtils;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class TXTReader extends AbsReader {

    private static final String[] FILE_ENDING = new String[]{"html", "htm", "xml", "php", "asp",
            "mht", "jsp", "aspx"};
    private static final String PATH_HTML_TEMP = "/temp/html/";
    public static final String EXTENSION_TEMP_PARSE_PAHT = ".txt";

    public static final int UNKOWN = 0;
    public static final int GB18030 = 1;
    public static final int GB2312 = 2;

    public static final int GBK = 3;
    public static final int UTF_8 = 4;
    public static final int BIG5 = 5;
    public static final int UTF16_LE = 6;
    public static final int UTF16_BE = 7;
    public static final int ASCII = 8;

    /**
     * 所有段落结束位置， 即每一个回车字符位置。
     */
    long[] paragraphEndPositions;
    public static final String[] ENCODINGS = new String[]{"UTF-8", "GB18030", "GB2312", "GBK",
            "UTF-8", "BIG5", "UTF16-LE", "UTF16-BE", "ASCII"};

    //章末附加类型数据  赞图标
    public static final int APPENDIX_TYPE_PRAISE = 3;
    //章末附加类型数据  作者的话
    public static final int APPENDIX_TYPE_AUTHOR_WORD = 0;
    //章末附加类型数据  作者的话
    public static final int TYPE_CHAPTER_TITLE = 2;

    //章末附加类型数据 跳转
    public static final int APPENDIX_TYPE_LINK = 1;
    protected int code;
    private String data;
    private boolean isEnd = false;
    private String assistantPath;


    public TXTReader(String f, long os) {
        super(f, os);
        code = TXTReader.regCode(fileName);
    }

    /**
     * @param
     * @param isOpen
     */
    public void setOffset(long o, boolean isOpen) throws IOException {
        isEnd = false;
        if (mInReader == null)
            return;
        offset = o;

        // offset 调整
        if (offset > mInReader.length()) {
            mInReader.seek(mInReader.length());
            return;
        }

        if (isOpen) {
            mInReader.seek(offset);
            return;
        }
        if (o < 80) {
            offset = 0;
            this.openfile();
            return;
        }
        if (o >= mInReader.length()) {
            if (mInReader.length() > 128) {
                offset = mInReader.length() - 128;
                o = mInReader.length() - 128;
            } else {
                offset = 0;
                o = 0;
            }
        }
        mInReader.seek(o);
        mInReader.findLastLine();
        offset = mInReader.getFilePointer();
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public String openfile() throws IOException {
        String pasePath = judgePath();
        mInReader = new BufferedRandomAccessFile(TextUtils.isEmpty(pasePath) ? fileName : pasePath,
                "r");
        mInReader.setCode(code);
        long readerLength = mInReader.length();
        mInReader.seek(Math.min(offset, readerLength));


        return pasePath;
    }


    @Override
    public void closefile() {
        try {
            if (mInReader != null) {
                mInReader.close();
                mInReader = null;
            }
        } catch (Exception e) {
            Log.e(e);
        }
        offset = 0;
    }

    @Override
    public String m_readline() throws IOException {
        if (isEnd)
            return null;
        if (offset >= mInReader.length()) {
            mInReader.seek(mInReader.length());

            return "";

        }

        data = mInReader.m_readLine();
        if (data == null) {
            return null;
        }
        offset = mInReader.getFilePointer();

        return data;
    }

    @Override
    public void m_seekNextLine() throws IOException {
        if (isEnd)
            return;

        if (offset > mInReader.length()) {

            mInReader.seek(mInReader.length());


            return;
        }

        mInReader.m_seekNextLine();
        offset = mInReader.getFilePointer();
    }

    @Override
    public long getLocation() {
        return offset;
    }


//    /**
//     * @param
//     */
//
//    public ParagraghData m_readlineData() throws IOException {
//        if (isEnd)
//            return null;
//        ParagraghData paragraghData = new ParagraghData();
//        paragraghData.setCode(code);
//        paragraghData.setStartParagrag(offset);
//        paragraghData.mParagrphType = -1;
//
//        String text = null;
//        if (mInReader != null) {
//
//
//            if (offset >= (getSize())) {
//                // Log.e("final offset===========:" + offset);
//            } else if (offset >= mInReader.length() - 1) {
//
//                int size = appendixOffsets.size();
//                for (int i = size - 1; i >= 0; i--) {
//                    if (offset >= appendixOffsets.get(i)) {
//                        text = appendixStrings.get(i);
//                        offset = (i == size - 1) ? getSize() : (appendixOffsets.get(i) + text
//                                .length());
//                        paragraghData.mParagrphType = appendixTypes.get(i);
//                        if (paragraghData.mParagrphType == APPENDIX_TYPE_AUTHOR_WORD) {
//                            paragraghData.authorWord = authorWord;
//                        } else if (paragraghData.mParagrphType == APPENDIX_TYPE_LINK) {
//                            int index = chapterLinks.size() - (size - i);
//                            paragraghData.chapterLink = chapterLinks.get(index);
//                        }
//
//                        break;
//                    }
//
//                }
//
//            } else {
//                text = mInReader.m_readLine();
//                offset = mInReader.getFilePointer();
//            }
//
//        }
//        paragraghData.setContent(text);
//        // }
//
//        return paragraghData;
//
//    }


    /**
     * GB2312,GBK,UTF-16LE,UTF-16BE,UTF-8,BIG5
     *
     * @param filename
     * @return ANSI UTF16_LE UTF8 UTF16_BE BIG5
     */
    public static int regCode(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            Log.i("regCode(String filename) file not exits");
            return UNKOWN;
        }

        RandomAccessFileInputStream bInputStream;
        try {
            bInputStream = new RandomAccessFileInputStream(filename);
            bInputStream.seek(0);
        } catch (Exception e) {
            Log.e(e);
            return GBK;
        }
        int code = GBK;
        try {
            int p = (bInputStream.read() << 8) + bInputStream.read();
            switch (p) {
                case 0xefbb:
                    code = UTF_8;
                    break;
                case 0xfffe:
                    code = UTF16_LE;
                    break;
                case 0xfeff:
                    code = UTF16_BE;
                    break;
            }

            if (code == GBK) {
                String chset = new TextCharsetDetector().guestFileEncoding(file);
                if (chset.equals("GB18030")) {
                    code = GB18030;
                } else if (chset.equals("GB2312")) {
                    code = GB2312;
                } else if (chset.equals("GBK")) {
                    code = GBK;
                } else if (chset.equals("UTF-8")) {
                    code = UTF_8;
                } else if (chset.equals("Big5")) {
                    code = BIG5;
                } else if (chset.equals("UTF-16LE")) {
                    code = UTF16_LE;
                } else if (chset.equals("windows-1252")) {
                    InputStream is = null;
                    try {
                        is = new FileInputStream(filename);
                        byte[] bcode = new byte[2048];
                        int nNewLength = is.read(bcode);
                        code = guessEncoding(bcode, nNewLength);
                        Log.d("windows-1252" + code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (chset.equals("UTF-16BE")) {
                    code = UTF16_BE;
                } else if (chset.equals("ASCII")) {
                    code = ASCII;
                } else {
                    code = GBK;
                }
                Log.d("chset" + chset);
            }
            bInputStream.close();
        } catch (Exception e) {
            Log.e(e);
        } finally {
            if (bInputStream != null) {
                try {
                    bInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return code;
    }

    public static int guessEncoding(byte[] bcode) {
        return guessEncoding(bcode, bcode.length);
    }

    public static int guessEncoding(byte[] bcode, int nNewLength) {
        int code = GBK;

        int iUnicode = 0;
        int iGbkSymbol = 0;
        int iUtf8Symbol = 0;
        int iUniCodeBig = 0;

        for (int i = 0; i < nNewLength - 2; i++) {
            int ch1 = (int) bcode[i];
            int ch2 = (int) bcode[i + 1];
            int ch3 = (int) bcode[i + 2];
            if (ch1 < 0)
                ch1 += 256;
            if (ch2 < 0)
                ch2 += 256;
            if (ch3 < 0)
                ch3 += 256;
            if (ch1 > 0 && ch1 < 0x7F) {
                if (ch2 == 0) {
                    iUnicode++;
                    i++;
                } else {
                    iGbkSymbol++;
                    iUtf8Symbol++;
                }
            } else if (ch1 == 0 && ch2 < 0x7F) {
                iUniCodeBig++;
                i++;
            } else if ((ch1 == 0xA1 && ch2 == 0xA3) || (ch1 == 0xA3 && ch2 == 0xAC)
                    || (ch1 == 0xA3 && ch2 == 0xBF) || (ch1 == 0xA3 && ch2 == 0xA1)) {
                i++;
                iGbkSymbol++;
            } else if ((ch1 == 0xE3 && ch2 == 0x80 && ch3 == 0x82)
                    || (ch1 == 0xEF && ch2 == 0xBC && ch3 == 0x8C)
                    || (ch1 == 0xEF && ch2 == 0xBC && ch3 == 0x9F)
                    || (ch1 == 0xEF && ch2 == 0xBC && ch3 == 0x81)) {
                iUtf8Symbol++;
                i += 2;
            } else if ((ch1 == 0xff && ch2 == 0x0c) || (ch1 == 0x30 && ch2 == 0x02)
                    || (ch1 == 0xff && ch2 == 0x1f) || (ch1 == 0xff && ch2 == 0x01)) {
                i++;
                iUniCodeBig++;
            }
        }
        if (iGbkSymbol >= iUtf8Symbol && iGbkSymbol > iUniCodeBig && iGbkSymbol > iUnicode
                || iUtf8Symbol > iGbkSymbol && iUtf8Symbol > iUniCodeBig && iUtf8Symbol > iUnicode
                || iGbkSymbol == 0 && iUtf8Symbol == 0 && iUnicode == 0 && iUniCodeBig == 0) {
            if (iUnicode == 0) {
                code = GBK;
            } else {
                code = UTF16_LE;
            }
        } else if (iUniCodeBig > iGbkSymbol && iUniCodeBig > iUtf8Symbol && iUniCodeBig > iUnicode) {
            // m_bHasJugdeCode = TRUE;
            code = UTF16_BE;
        } else {
            // m_bHasJugdeCode = TRUE;
            code = GBK;// GEnCodeTextLittle;
        }
        return code;
    }

    /**
     * @param code
     * @return
     */
    public static String getEncoding(int code) {
        return ENCODINGS[code];
    }

    public static boolean isUTF8(byte[] str) {
        int i;
        int chc;
        int chp;
        int count_good_utf = 0;
        int count_bad_utf = 0;
        long iLen = str.length;

        for (i = 1; i < iLen; i++) {

            chc = (int) str[i];
            chp = (int) str[i - 1];
            if ((chc & 0xC0) == 0x80) {
                if ((chp & 0xC0) == 0xC0) {

                    count_good_utf++;
                } else if ((chp & 0x80) == 0x00) {
                    count_bad_utf++;
                }
            } else if ((chp & 0xC0) == 0xC0) {
                count_bad_utf++;
            }

        }

        if (count_good_utf > count_bad_utf) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public int getCode() {
        return code;
    }

    /**
     * @return
     */
    @Override
    public void findLastLine() throws IOException {
        if (mInReader == null)
            return;
        if (offset > mInReader.length()) {
            mInReader.seek(mInReader.length());


            return;

        }

        mInReader.findLastLine();
        offset = mInReader.getFilePointer();
    }

    private String judgePath() {
        String path = fileName;
        if (!TextUtils.isEmpty(fileName)) {
            for (String fileEnding : FILE_ENDING) {
                if (fileName.toLowerCase().endsWith(fileEnding)) {
                    code = TXTReader.regCode(fileName);// 每次在读取源文件前都要获取一下编码.
                    break;
                }
            }
        }
        Log.d(path);

        return path;
    }


    private String readTempFile() {
        StringBuilder sbHtml = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),
                    ENCODINGS[code]));
            int length = 2048;
            char[] buffer = new char[2048];
            while ((length = br.read(buffer, 0, 2048)) > 0) {
                sbHtml.append(buffer, 0, length);
            }
        } catch (Exception e) {
            Log.e(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.v(e);
                }
            }
        }

        return sbHtml.toString();
    }

    public String readFragment(long startPos, long endPos) {
        try {
            return mInReader.m_readFragment(startPos, endPos);
        } catch (Exception e) {
            Log.e(e);
            return "";
        }
    }

    /**
     * 读取指定区间的段落数据
     *
     * @param startPos
     * @param endPos
     * @return
     */
    public List<String> m_readParagraphs(long startPos, long endPos) {

        return mInReader.m_readParagraphs(startPos, endPos);

    }

    public int getLineIndex(long lineHeadLocation, long offsetLocation) {
        if (lineHeadLocation >= offsetLocation) {
            return 0;
        }
        String offsetStr = null;
        try {
            offsetStr = mInReader.m_readFragment(lineHeadLocation, offsetLocation);
        } catch (Exception e) {
            Log.e(e);
            offsetStr = "";
        }
        return StringUtil.isEmpty(offsetStr) ? 0 : offsetStr.length();
    }

    /**
     * 设置辅助路径,用于zip或rar解压图片,如果传入的是空值,表示沿用文件路径
     *
     * @param assistantPath
     */
    public void setAssistantPath(String assistantPath) {
        this.assistantPath = assistantPath;
    }

    public boolean isHead() throws IOException {
        if (offset >= mInReader.length() - 1) {
            return false;
        }
        if (mInReader != null) {
            return mInReader.isHead();
        }
        return false;
    }


    /**
     * 获取章节段落序号 下标从1开始
     *
     * @param filePos
     * @return
     */
    public int getParagraphIndex(long filePos) {

        int size = paragraphEndPositions.length;
        for (int i = 0; i < size; i++) {
            if (filePos <= paragraphEndPositions[i]) {
                return i + 1;
            }
        }


        return -1;
    }
}
