package com.xxx.reader.file;

import com.giants3.android.frame.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BufferedRandomAccessFile extends RandomAccessFile {

    private final static int BUFFERSIZE = 12288; // 在线阅读 章节大小 12KB

    private static final int BOM_SIZE = 4;
    private static final int BOM_SIZE_UTF8 = 3;

    // 缓存
    private byte buf[];
    // 缓存大小
    private int bufsize;
    // 获取起始位置
    private long bufmask;
    // 缓存中使用的大小
    private int bufusedsize;
    // 当前文件位置
    private long curpos;
    // 缓存起始位置
    private long bufstartpos;
    // 缓存结束位置
    private long bufendpos;
    // 文件结束位置，即大小
    private long fileendpos;
    // 编码
    private int code = 0;

    private boolean isUTF = false;

    public BufferedRandomAccessFile(String name, String mode) throws IOException {
        super(name, mode);
        this.init(name, mode, BUFFERSIZE);
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode()
    {
        return code;
    }

    public BufferedRandomAccessFile(String name, String mode, int bufferSize)
            throws IOException {
        super(name, mode);
        this.init(name, mode, bufferSize);
    }


    private void init(String name, String mode, int bufferSize) throws IOException {

        InputStream bin = null;
        try {
            bin = new FileInputStream(name);
            byte[] bom = new byte[BOM_SIZE];
            bin.read(bom);
            if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB)
                    && (bom[2] == (byte) 0xBF)) {
                isUTF = true;
            }
        } catch (FileNotFoundException e) {
            Log.e(e);
        } finally {
            if (bin != null) {
                bin.close();
            }
        }

        this.fileendpos = super.length();
        this.curpos = getAbsoluteFilePointer();

        this.bufsize = bufferSize;
        this.buf = new byte[this.bufsize];
        this.bufmask = ~(this.bufsize - 1L);
        this.bufusedsize = 0;
        this.bufstartpos = -1;
        this.bufendpos = -1;
    }


    private int fillbuf() throws IOException {
        super.seek(this.bufstartpos);
        return super.read(this.buf, 0, this.buf.length);
    }

    public byte read(long pos) throws IOException {
        if (pos < this.bufstartpos || pos > this.bufendpos) {
            this.seek(pos);
            if ((pos < this.bufstartpos) || (pos > this.bufendpos)) {
                throw new IOException();
            }
        }
        this.curpos = pos;
        return this.buf[(int) (pos - this.bufstartpos)];
    }


    @Override
    public int read(byte b[], int off, int len) throws IOException {

        long readendpos = this.curpos + len - 1;

        if (readendpos <= this.bufendpos && readendpos < this.fileendpos) { // read
            // in
            // buf
            System.arraycopy(this.buf, (int) (this.curpos - this.bufstartpos), b, off, len);
        } else { // read b[] size > buf[]

            if (readendpos > this.fileendpos) { // read b[] part in file
                len = (int) (this.length() - this.curpos + 1);
            }

            super.seek(this.curpos);
            len = super.read(b, off, len);
            readendpos = this.curpos + len - 1;
        }
        this.seek(readendpos + 1);
        return len;
    }

    @Override
    public int read(byte b[]) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public void seek(long pos) throws IOException {

        if (isUTF && (pos == 0)) {
            pos += BOM_SIZE_UTF8;
        }

        if (pos < 0) {
            pos = 0;
        }

        if ((pos < this.bufstartpos) || (pos > this.bufendpos)) { // seek pos
            // not in
            // buf
            if ((pos >= 0) && (pos <= this.fileendpos) && (this.fileendpos != 0)) { // seek
                // pos
                // in
                // file
                // (file
                // length > 0)
                this.bufstartpos = pos & this.bufmask;
                this.bufusedsize = this.fillbuf();

            } else if (((pos == 0) && (this.fileendpos == 0)) || (pos == this.fileendpos + 1)) { // seek
                // pos
                // is
                // append
                // pos

                this.bufstartpos = pos;
                this.bufusedsize = 0;
            }
            this.bufendpos = this.bufstartpos + this.bufsize - 1;
        }
        // Log.i("seek", pos + "");
        this.curpos = pos;
    }

    @Override
    public long length() throws IOException {
        return this.max(this.fileendpos + 1, super.length());
    }

    public String m_readLine() throws IOException {
        byte[] temp = null;

        //指针错位处理。
        if ((int) (this.curpos - this.bufstartpos) < 0) return null;

        int l = 0;
        int i = 0;
        int templen = 0;
        int readstart = 0;
        boolean isFirst = true; // buf中是否第一次找到，找到 ,就不需要复制
        String result = null;
        while (true) {
            readstart = (int) (this.curpos - this.bufstartpos);

            templen = 0;
            for (i = readstart; i < this.bufusedsize; i++) {
                templen++;
                if (buf[i] == 0x0A) {
                    if (isFirst) {
                        switch (code) {
                            case Charset.UTF16_LE:
                                if (buf[i + 1] != 0) {
                                    continue;
                                }
                                templen++;
                            default:
                                result = getString(this.buf, readstart, templen);
                                this.seek(this.curpos + templen);
                                break;
                        }
                    } else {
                        switch (code) {
                            case Charset.UTF16_LE:
                                if (buf[i + 1] != 0) {
                                    continue;
                                }
                                templen++;
                            default:
                                if (l + templen >= temp.length) {
                                    byte[] temp1 = new byte[l + templen];
                                    System.arraycopy(temp, 0, temp1, 0, temp.length);
                                    temp = temp1;
                                }
                                System.arraycopy(buf, readstart, temp, l, templen);
                                this.seek(this.curpos + templen);
                                break;
                        }
                        result = getString(temp, 0, l + templen);
                    }
                    return result;
                }
            }
            //自定义段落，原buf中首先寻找
            if (temp == null) {
                if (this.curpos + templen >= this.fileendpos) {
                    if (templen == 0)
                        return null;
                    else {
                        this.seek(this.curpos + templen);
                        return getString(buf, readstart, templen + l);
                    }
                }

                temp = new byte[this.bufsize];
            }

            if (l + i - readstart >= temp.length) {
                byte[] temp1 = new byte[temp.length << 1];
                System.arraycopy(temp, 0, temp1, 0, temp.length);
                temp = temp1;
            }

            isFirst = false;

            System.arraycopy(buf, readstart, temp, l, templen);
            if (this.curpos + templen >= this.fileendpos) {
                if (templen == 0)
                    return null;
                else {
                    this.seek(this.curpos + templen);
                    return getString(temp, 0, templen + l);
                }
            }
            this.seek(this.curpos + templen);
            l = l + templen;
        }
    }

    public String m_readLine(long maxLength) throws IOException {
        byte[] temp = null;
        int l = 0;
        int i = 0;
        int templen = 0;
        int readstart = 0;
        boolean isFirst = true; // buf中是否第一次找到，找到 ,就不需要复制
        String result = "";
        while (true) {
            readstart = (int) (this.curpos - this.bufstartpos);
            templen = 0;
            for (i = readstart; i < this.bufusedsize; i++) {
                templen++;
                if (buf[i] == 0x0A) {
                    if (isFirst) {
                        switch (code) {
                            case Charset.UTF16_LE:
                                if (buf[i + 1] != 0) {
                                    continue;
                                }
                                templen++;
                            default:
                                if (l + templen < maxLength) {
                                    result = getString(this.buf, readstart, templen);
                                }
                                this.seek(this.curpos + templen);
                                break;
                        }
                    } else {
                        switch (code) {
                            case Charset.UTF16_LE:
                                if (buf[i + 1] != 0) {
                                    continue;
                                }
                                templen++;
                            default:
                                if (l + templen >= temp.length) {
                                    byte[] temp1 = new byte[l + templen];
                                    System.arraycopy(temp, 0, temp1, 0, temp.length);
                                    temp = temp1;
                                }
                                System.arraycopy(buf, readstart, temp, l, templen);
                                this.seek(this.curpos + templen);
                                break;
                        }
                        if (l + templen < maxLength) {
                            result = getString(temp, 0, l + templen);
                        }
                    }
                    return result;
                }
            }
            //自定义段落，原buf中首先寻找
            if (temp == null) {
                if (this.curpos + templen >= this.fileendpos) {
                    if (templen == 0)
                        return null;
                    else {
                        this.seek(this.curpos + templen);
                        if (l + templen < maxLength) {
                            result = getString(buf, readstart, templen + l);
                        }
                        return result;
                    }
                }

                temp = new byte[this.bufsize];
            }

            if (l + i - readstart >= temp.length) {
                byte[] temp1 = new byte[temp.length << 1];
                System.arraycopy(temp, 0, temp1, 0, temp.length);
                temp = temp1;
            }

            isFirst = false;

            System.arraycopy(buf, readstart, temp, l, templen);
            if (this.curpos + templen >= this.fileendpos) {
                if (templen == 0)
                    return null;
                else {
                    this.seek(this.curpos + templen);
                    if (l + templen < maxLength) {
                        result = getString(temp, 0, templen + l);
                    }
                    return result;
                }
            }
            this.seek(this.curpos + templen);
            l = l + templen;
        }
    }

    public void m_seekNextLine() throws IOException {
        byte[] temp = null;
        int l = 0;
        int i = 0;
        int templen = 0;
        int readstart = 0;
        boolean isFirst = true; // buf中是否第一次找到，找到 ,就不需要复制
        while (true) {
            readstart = (int) (this.curpos - this.bufstartpos);
            templen = 0;
            for (i = readstart; i < this.bufusedsize; i++) {
                templen++;
                if (buf[i] == 0x0A) {
                    if (isFirst) {
                        switch (code) {
                            case Charset.UTF16_LE:
                                if (buf[i + 1] != 0) {
                                    continue;
                                }
                                templen++;
                                this.seek(this.curpos + templen);
                                break;
                            default:
                                this.seek(this.curpos + templen);
                                break;
                        }
                    } else {
                        switch (code) {
                            case Charset.UTF16_LE:
                                if (buf[i + 1] != 0) {
                                    continue;
                                }
                                templen++;
                                this.seek(this.curpos + templen);
                                break;
                            default:
                                this.seek(this.curpos + templen);
                                break;
                        }
                    }
                    return;
                }
            }

            if (temp == null) {
                if (this.curpos + templen >= this.fileendpos) {
                    if (templen == 0)
                        return;
                    else {
                        this.seek(this.curpos + templen);
                        return;
                    }
                }
                temp = new byte[this.bufsize];
            }

            if (l + i - readstart >= temp.length) {
                byte[] temp1 = new byte[temp.length << 1];
                System.arraycopy(temp, 0, temp1, 0, temp.length);
                temp = temp1;
            }

            isFirst = false;

            System.arraycopy(buf, readstart, temp, l, templen);
            if (this.curpos + templen >= this.fileendpos) {
                if (templen == 0)
                    return;
                else {
                    this.seek(this.curpos + templen);
                    return;
                }
            }
            this.seek(this.curpos + templen);
            l = l + templen;
        }
    }


    /**
     * 读取指定范围的段落数据
     *
     * @param startPos
     * @param endPos
     * @return
     */
    public List<String> m_readParagraphs(long startPos, long endPos) {
        if (endPos < startPos) return null;
        //保留现场
        long tempPos = curpos;


        List<String> paras = new ArrayList<String>();
        curpos = startPos;
        long lastParaPos = -1;
        //从当前绘制开始读取每一段位置。
        while (true) {


            try {
                lastParaPos = curpos;

                String para = m_readLine();


                if (curpos > endPos || para == null) {
                    break;
                }

                paras.add(para);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        //最后一段不完整，未读取
        if (curpos > endPos && lastParaPos > -1) {

            try {
                String lastPara = m_readFragment(lastParaPos, endPos);
                paras.add(lastPara);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //恢复现场
        try {
            this.seek(tempPos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        curpos = tempPos;


        return paras;

    }

    /**
     * use:读取指定范围内的数据
     */
    public String m_readFragment(long startPos, long endPos) throws IOException {
        byte[] temp = null;
        int l = 0;
        int i = 0;
        int templen = 0;
        int readstart = 0;
        long tempPos = this.curpos;
        this.seek(startPos);
        boolean isFirst = true; // buf中是否第一次找到，找到 ,就不需要复制
        String result = null;
        while (true) {
            readstart = (int) (this.curpos - this.bufstartpos);
            templen = 0;
            long searchPos = this.curpos;
            for (i = readstart; i <= this.bufendpos; i++) {
                templen++;
                if (searchPos == endPos) {
                    templen--;
                    if (isFirst) {
                        switch (code) {
                            case Charset.UTF16_LE:
                                templen++;
                            default:
                                result = getString(this.buf, readstart, templen);
                                break;
                        }
                    } else {
                        switch (code) {
                            case Charset.UTF16_LE:
                                templen++;
                            default:
                                if (l + templen >= temp.length) {
                                    byte[] temp1 = new byte[l + templen];
                                    System.arraycopy(temp, 0, temp1, 0, temp.length);
                                    temp = temp1;
                                }
                                System.arraycopy(buf, readstart, temp, l, templen);
                                break;
                        }
                        result = getString(temp, 0, l + templen);
                    }
                    this.seek(tempPos);
                    return result;
                }
                searchPos++;
            }

            if (temp == null) {
                if (this.curpos + templen >= this.fileendpos) {
                    if (templen == 0)
                        return null;
                    else {
                        this.seek(this.curpos + templen);
                        return getString(buf, readstart, templen + l);
                    }
                }

                temp = new byte[this.bufsize];
            }

            if (l + i - readstart >= temp.length) {
                byte[] temp1 = new byte[temp.length << 1];
                System.arraycopy(temp, 0, temp1, 0, temp.length);
                temp = temp1;
            }

            isFirst = false;

            System.arraycopy(buf, readstart, temp, l, templen);
            if (this.curpos + i - readstart >= this.fileendpos) {
                this.seek(tempPos);
                if (templen == 0)
                    return null;
                else {
                    return getString(temp, 0, templen + l);
                }
            }
            this.seek(this.curpos + templen);
            l = l + templen;
        }
    }

    public void findLastLine() throws IOException {
        int readstart;
        if (code == Charset.UTF16_BE || code == Charset.UTF16_LE)
            readstart = (int) (this.curpos - this.bufstartpos - 3);
        else
            readstart = (int) (this.curpos - this.bufstartpos - 2);
        int i = 0;
        while (true) {
            for (i = readstart; i > 0; i--) {
                if (buf[i] == 0x0A) {
                    switch (code) {
                        case Charset.UTF16_LE:
                            if (buf[i + 1] != 0) {
                                continue;
                            }
                            this.seek(this.bufstartpos + i + 2);
                            break;
                        default:
                            this.seek(this.bufstartpos + i + 1);
                            break;
                    }
                    return;
                }
            }
            if (curpos == 0) {
                return;
            }
            if (bufstartpos == 0) {
                this.curpos = 0;
            } else {
                //TODO  这里 bufferstartpos  不足以支持读取前一行，逐步递减，递归 直到文件头
//                if(bufstartpos>0)
//                {
                    this.bufstartpos = Math.max(0,bufstartpos-bufsize) ;
                    this.bufusedsize = this.fillbuf();
                     findLastLine();


//                }else{
//
//                }
//                this.seek(bufstartpos - this.bufsize);
//                if (i < 0)
//                    readstart = this.bufsize + i - 1;
//                else
//                    readstart = this.bufsize - 1;
            }
        }
    }

    @Override
    public void setLength(long newLength) throws IOException {
        if (newLength > 0) {
            this.fileendpos = newLength - 1;
        } else {
            this.fileendpos = 0;
        }
        super.setLength(newLength);
    }

    @Override
    public long getFilePointer() throws IOException {
        return this.curpos;
    }

    public long getAbsoluteFilePointer() throws IOException {
        if (isUTF) {
            return super.getFilePointer() + BOM_SIZE_UTF8;
        } else {
            return super.getFilePointer();
        }
    }

    private long max(long a, long b) {
        return a > b ? a : b;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    public long getFileLastPosition() {
        return fileendpos;
    }

    public boolean isHead() throws IOException {
        long curFilePointer = this.getFilePointer();
        if (isUTF) {
            curFilePointer -= BOM_SIZE_UTF8;
        }
        if (curFilePointer == 0) {
            return true;
        }
        return false;
    }

    private final String getString(byte[] data, int startIndex, int length) {
        String string = null;
        if (length <= 0) {
            return "";
        }
        if (data.length < length - startIndex) {
            length = data.length;
        }
        try {
            string = new String(data, startIndex < 0 ? 0 : startIndex, length,
                    Charset.getEncoding(code));
        } catch (UnsupportedEncodingException e) {
            string = null;
            Log.e(e);
        }
        if (string != null) {
            if ((string.indexOf('\t') != -1 || string.indexOf('\r') != -1)) {
                string = string.replace('\t', ' ');
            }
            if (string.indexOf('\r') != -1) {
                string = string.replace('\r', ' ');
            }
            if (string.indexOf('\n') != -1) {
                string = string.replace('\n', ' ');
            }
        }
        return string;
    }

}
