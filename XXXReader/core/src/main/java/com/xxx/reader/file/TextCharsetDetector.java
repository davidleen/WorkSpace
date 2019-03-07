package com.xxx.reader.file;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StringUtil;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class TextCharsetDetector {

    private boolean found = false;
    private String encoding = null;

    public static String[][] fileCodeBuffer = null;


    public boolean judgeCode(byte[] pszJudge, int nLength) {

        int MIN_CHINESE_COUNT = 12;
        int GEnCodeTextNone = 0;
        int GEnCodeTextUtf8 = 1;
        int GEnCodeTextBig = 2;
        int GEnCodeTextLittle = 3;

        // char *pJudge = (char*)pszJudge;
        byte[] pJudge = pszJudge;
        // BOOL GotEnCode = TRUE;
        boolean GotEnCode = true;
        // BOOL bHasJugdeCode = FALSE;
        boolean bHasJugdeCode = false;

        // iBookInfo.iEncode = GEnCodeTextNone;
        // iBookInfo.bHasbom
        int iEncode = GEnCodeTextNone;
        boolean bHasbom;

        if (nLength >= 3 && 0xEF == (pJudge[0] & 0xff) && 0xBB == (pJudge[1] & 0xff) && 0xBF == (pJudge[2] & 0xff)) {
            iEncode = GEnCodeTextUtf8;
            bHasbom = true;
        } else if (nLength >= 2 && 0xFE == (pJudge[0] & 0xff) && 0xFF == (pJudge[1] & 0xff)) {
            iEncode = GEnCodeTextBig;
            bHasbom = true;
        } else if (nLength >= 2 && 0xFF == (pJudge[0] & 0xff) && 0xFE == (pJudge[1] & 0xff)) {
            bHasbom = true;
            iEncode = GEnCodeTextLittle;
        } else {
            GotEnCode = false;
        }

        if (GotEnCode) {
            bHasJugdeCode = true;
            return bHasJugdeCode;
        }

        bHasbom = false;

        int iUnicode = 0;
        int iGbkSymbol = 0;
        int iUtf8Symbol = 0;
        int iUniCodeBig = 0;

        int nNewLength = (nLength <= 300) ? nLength : 300;
        for (int i = 0; i < nNewLength - 2; i++) {
            int ch1 = pJudge[i];
            int ch2 = pJudge[i + 1];
            int ch3 = pJudge[i + 2];

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
            } else if ((ch1 == 0xA1 && ch2 == 0xA3) || (ch1 == 0xA3 && ch2 == 0xAC) || (ch1 == 0xA3 && ch2 == 0xBF)
                    || (ch1 == 0xA3 && ch2 == 0xA1)) {
                i++;
                iGbkSymbol++;
            } else if ((ch1 == 0xE3 && ch2 == 0x80 && ch3 == 0x82) || (ch1 == 0xEF && ch2 == 0xBC && ch3 == 0x8C)
                    || (ch1 == 0xEF && ch2 == 0xBC && ch3 == 0x9F) || (ch1 == 0xEF && ch2 == 0xBC && ch3 == 0x81)) {
                iUtf8Symbol++;
                i += 2;
            } else if ((ch1 == 0xff && ch2 == 0x0c) || (ch1 == 0x30 && ch2 == 0x02) || (ch1 == 0xff && ch2 == 0x1f)
                    || (ch1 == 0xff && ch2 == 0x01)) {
                i++;
                iUniCodeBig++;
            }
        }
        if (iGbkSymbol >= iUtf8Symbol && iGbkSymbol > iUniCodeBig && iGbkSymbol > iUnicode || iUtf8Symbol > iGbkSymbol
                && iUtf8Symbol > iUniCodeBig && iUtf8Symbol > iUnicode || iGbkSymbol == 0 && iUtf8Symbol == 0 && iUnicode == 0
                && iUniCodeBig == 0) {

            // char *pChangeLine = strchr(pJudge, '\n');
            byte[] pChangeLine = null;
            for (int i = 0; i < pJudge.length; i++) {
                if (pJudge[i] == '\n') {
                    pChangeLine = new byte[pJudge.length - i];
                    System.arraycopy(pJudge, i, pChangeLine, 0, pChangeLine.length);
                    break;
                }

            }

            // char szChinese[MIN_CHINESE_COUNT + 1] = {0};
            // char *pTemp = pJudge;
            // while((unsigned char)(*pTemp) <= 0x80 && *pTemp != '\0')
            // pTemp++;
            // memcpy(szChinese, pTemp, MIN_CHINESE_COUNT*sizeof(char));
            byte[] szChinese = new byte[MIN_CHINESE_COUNT + 1];
            int j = 0;
            while ((pJudge[j] & 0xff) <= 0x80 && (pJudge[j] & 0xff) != '\0') {
                j++;
            }
            System.arraycopy(pJudge, j, szChinese, 0, MIN_CHINESE_COUNT);

            if (IsUTF8(szChinese, szChinese.length)) {
                bHasJugdeCode = true;
                iEncode = GEnCodeTextUtf8;
            } else {
                // if(pChangeLine)
                if (pChangeLine != null) {
                    // pTemp = pChangeLine;
                    // while (pTemp <= 0x80 && pTemp != '\0')
                    // pTemp++;
                    // if(strlen(pTemp) > MIN_CHINESE_COUNT)
                    // memcpy(szChinese, pChangeLine, MIN_CHINESE_COUNT * 8);
                    j = 0;
                    while ((pChangeLine[j] & 0xff) <= 0x80 && pChangeLine[j] != '\0') {
                        j++;
                    }
                    if (pChangeLine.length - j > MIN_CHINESE_COUNT) {
                        System.arraycopy(pChangeLine, j, szChinese, 0, MIN_CHINESE_COUNT);
                        if (IsUTF8(szChinese, szChinese.length)) {
                            bHasJugdeCode = true;
                            iEncode = GEnCodeTextUtf8;
                            return bHasJugdeCode;
                        }
                    }
                }
                iEncode = GEnCodeTextNone;
            }
        } else if (iUniCodeBig > iGbkSymbol && iUniCodeBig > iUtf8Symbol && iUniCodeBig > iUnicode) {
            bHasJugdeCode = true;
            iEncode = GEnCodeTextBig;
        } else {
            bHasJugdeCode = true;
            iEncode = GEnCodeTextNone;// GEnCodeTextLittle;
        }
        return bHasJugdeCode;

    }

    /* IsUTF8
     *
     * UTF-8 is the encoding of Unicode based on Internet Society RFC2279
     * ( See http://www.cis.ohio-state.edu/htbin/rfc/rfc2279.html )
     *
     * Basicly:
     * 0000 0000-0000 007F - 0xxxxxxx  (ascii converts to 1 octet!)
     * 0000 0080-0000 07FF - 110xxxxx 10xxxxxx    ( 2 octet format)
     * 0000 0800-0000 FFFF - 1110xxxx 10xxxxxx 10xxxxxx (3 octet format)
     * 					   - 1111xxxx 10xxxxxx 10xxxxxx 10xxxxxx(4 octet format)
     *
     */
    public static boolean IsUTF8(byte[] str, int length) {
        int i;
        byte cOctets; // octets to go in this UTF-8 encoded character
        byte chr;

        cOctets = 0;
        boolean bAllAscii = true;
        int checkCount = 0;
        for (i = 0; i < length; i++) {
            chr = (byte) str[i];
            if (i > 0) {
                int data1 = str[i - 1] & 0xC0;
                int data2 = str[i] & 0xC0;
                // 10xx xxxx 不能是UTF8的第一个字符
                if (i == 1 && data1 == 0x80) {
                    return false;
                }
                // 10xx xxxx 前 必须是 11xx xxxx 或者 10xx xxxx
                if (data1 != 0xc0 && data1 != 0x80 && data2 == 0x80) {
                    return false;
                }
                // 11xx xxxx 后必须是 10xx xxxx
                if (data1 == 0xc0 && data2 != 0x80) {
                    return false;
                }
            }
            if ((chr & 0x80) != 0) {
                checkCount += 1;
                bAllAscii = false;
            }

            if (checkCount > 32 && !bAllAscii && i >= 36 && chr < 'z' && chr >= 0)
                break;
            if (cOctets == 0) {
                if (chr >= 0x80) {
                    do {
                        chr <<= 1;
                        cOctets++;
                    } while ((chr & 0x80) != 0);

                    cOctets--;
                    if (cOctets == 0)
                        return false;
                }
            } else {
                if ((chr & 0xC0) != 0x80)
                    return false;

                cOctets--;
            }
        }

        if (cOctets > 0) {
            return false;
        }

        if (bAllAscii)
            return false;

        return true;
    }

    private String getFileCodeFromBuffer(String keyString) {
        if (fileCodeBuffer == null) {
            fileCodeBuffer = new String[64][2];
            for (int i = 0; i < fileCodeBuffer.length; i++) {
                fileCodeBuffer[i] = new String[]{null, null};
            }
            return null;
        } else {
            for (int i = 0; i < fileCodeBuffer.length; i++) {
                if (fileCodeBuffer[i][0] != null && fileCodeBuffer[i][0].equals(keyString)) {
                    return fileCodeBuffer[i][1];
                }
            }
        }

        return null;
    }

    private void addFileCode(String keyString, String code) {
        if (fileCodeBuffer != null) {
            for (int i = 0; i < fileCodeBuffer.length; i++) {
                if (fileCodeBuffer[i][0] == null) {
                    fileCodeBuffer[i][0] = keyString;
                    fileCodeBuffer[i][1] = code;
                    fileCodeBuffer[(i + 1) % fileCodeBuffer.length][0] = null;
                    break;
                }
            }
        }
    }

    /**
     * 传入一个文件(File)对象，检查文件编码
     *
     * @param file File对象实例
     * @return 文件编码，若无，则返回null
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String guestFileEncoding(File file) throws FileNotFoundException, IOException {
        String keyString = file.length() + file.lastModified() + file.getAbsolutePath();
        encoding = getFileCodeFromBuffer(keyString);
        if (!StringUtil.isEmpty(encoding)) {
            return encoding;
        }

        nsDetector det = new nsDetector();
        encoding = null;
        det.Init(new nsICharsetDetectionObserver() {
            public void Notify(String charset) {
                found = true;
                encoding = charset;
            }
        });

        RandomAccessFileInputStream imp = new RandomAccessFileInputStream(file.getAbsolutePath());
        byte[] buf = new byte[1024 * 2];
        int len;
        encoding = "";
        imp.seek(0);
        if ((len = imp.read(buf, 0, buf.length)) != -1) {
            if (IsUTF8(buf, len)) {
                encoding = "UTF-8";
                Log.i("guess utf 8");
            } else if (det.isAscii(buf, len)) {
                encoding = "ASCII";
            } else if (!det.DoIt(buf, len, false)) {
                long currentPos = len;
                while (!isGoodBig5GbkJudgeBuf(buf) && file.length() - currentPos > buf.length) {
                    imp.seek(currentPos);
                    len = imp.read(buf, 0, buf.length);
                    currentPos += len;
                }
                int ret = judgeGBKorBig5(buf);
                if (ret == 1) {
                    encoding = "GBK";
                } else if (ret == 2) {
                    encoding = "Big5";
                }
            }
        }
        imp.close();
        if (encoding.length() == 0) {
            det.DataEnd();    //最后要调用此方法，此时，Notify被调用。
        } else {
            found = true;
        }

        Log.i(encoding);

        if (!found) {
            String prob[] = det.getProbableCharsets();
            if (prob.length > 0) {
                encoding = prob[0].toUpperCase();
            } else {
                addFileCode(keyString, "GBK");
                return "GBK";
            }
        }
        Log.d(encoding);
        addFileCode(keyString, encoding);
        return encoding;
    }

    public boolean isGoodBig5GbkJudgeBuf(byte[] buf) {
        int size = buf.length;
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (buf[i] > 0x80) {
                count++;
            }
        }
        return (count > 100);
    }

    /**
     * 传入一个文件(File)对象，检查文件编码
     *
     * @param file File对象实例
     * @return 文件编码，若无，则返回null
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String guestStringEncoding(String str) throws FileNotFoundException, IOException {
        nsDetector det = new nsDetector();
        det.Init(new nsICharsetDetectionObserver() {
            public void Notify(String charset) {
                found = true;
                encoding = charset;
            }
        });

        if (str == null || str.length() == 0) {
            return "ISO_8859_1";
        }
        BufferedInputStream imp = new BufferedInputStream(new ByteArrayInputStream(str.getBytes()));

        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;

        while ((len = imp.read(buf, 0, buf.length)) != -1) {
            // Check if the stream is only ascii.
            if (isAscii)
                isAscii = det.isAscii(buf, len);

            // DoIt if non-ascii and not done yet.
            if (!isAscii && !done)
                done = det.DoIt(buf, len, false);
        }
        det.DataEnd();
        if (isAscii) {
            encoding = "ASCII";
            found = true;
        }

        if (!found) {
            String prob[] = det.getProbableCharsets();
            if (prob.length > 0) {
                // 在没有发现情况下，则取第一个可能的编码
                encoding = prob[0];
            } else {
                return "GBK";
            }
        }
        return encoding;
    }

    boolean isGB2312(int byteFirst, int byteSecond) {
        if ((byteFirst >= 0xA1) && (byteFirst <= 0xFE)) {
            if ((byteSecond >= 0xA1) && (byteSecond <= 0xFE)) {
                return true;
            }
        }
        return false;
    }

    boolean isBig5(int byteFirst, int byteSecond) {
        if ((byteFirst >= 0xA1) && (byteFirst <= 0xF9)) {
            if ((byteSecond >= 0x40) && (byteSecond <= 0x7E)) {
                return true;
            } else if ((byteSecond >= 0xA1) && (byteSecond <= 0xFE)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是GBK 还是 BIG5编码
     *
     * @param 测试字段
     * @return -1 无法判断，1 GBK , 2 BIG5
     */
    private int judgeGBKorBig5(byte[] buf) {
        if (buf == null || buf.length < 2) {
            return -1;
        }

        int testLength = buf.length;
        if (testLength > 2048) {
            testLength = 2048;
        }
// 特殊字符辅助判断		
        int a1a2Count = 0;    // gbk 中的  "、"  big5中的 "〔"
        int a1a3Count = 0;  // gbk 中的  "。"  big5中的 "〕"

        int a1Count = 0;    // 符号区
        int sum = 0;
        int firstByte = 0;
        int secondByte = 0;
        int count = 0;
        for (int i = 0; i < testLength - 2; i += 2) {
            firstByte = buf[i] & 0xff;
            secondByte = buf[i + 1] & 0xff;

            if (firstByte == 0x0d || firstByte == 0x0a) {
                i -= 1;
                continue;
            }
            if (firstByte <= 0x80) {
                i -= 1;
                continue;
            }
            if (firstByte < 0xa0 || firstByte > 0xfe
                    || secondByte < 0x40 || (secondByte > 0x7e && secondByte < 0xa1)) {
                return 1;
            }

            if (firstByte == 0xa1) {
                a1Count += 1;
                if (secondByte == 0xa2) {
                    a1a2Count += 1;
                } else if (secondByte == 0xa3) {
                    a1a3Count += 1;
                }
            }
            sum += firstByte;
            count += 1;
        }

        sum = sum / count;

        if (count < 200 && (a1Count * 100 / count) > 30) {
            // 在采样中符号占有较多时进行判断，（不是对称的括号，并且有一定数量的"、"或者" 。" 认为是GBK编码）
            if (a1a2Count != a1a3Count && (a1a2Count > 3 || a1a3Count > 3)) {
                Log.i("check gbk by 0xa1xx");
                return 1;
            }
        }

        if (sum > 184) {
            return 1;
        } else {
            return 2;
        }
    }

}
