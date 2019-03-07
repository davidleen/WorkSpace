package com.xxx.reader.file;

import java.io.IOException;
import java.util.LinkedList;

public abstract class AbsReader implements LineReader {

    protected String fileName = null;
    protected BufferedRandomAccessFile mInReader = null;
    protected long offset = 0;
    protected LinkedList<Long> offset_of_line;

    public AbsReader(String f, long os) {
        fileName = f;
        offset = os;
        offset_of_line = new LinkedList<Long>();
    }

    public abstract String openfile() throws IOException;

    public abstract void closefile();

    public long getSize() throws IOException {
        if (mInReader != null) {
            return mInReader.length();
        } else {
            return 100; //用于做分母，不能为0，暂定100先不崩溃
        }
    }

    public long getFileEndPos() throws IOException {
        return mInReader.getFileLastPosition();
    }

    public long getLocation() throws IOException {
        if (mInReader != null) {
            long x = mInReader.getFilePointer();
            return x;
        }
        return 0;
    }

    public abstract void setAssistantPath(String assistantPath);
}
