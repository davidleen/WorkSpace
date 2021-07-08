package com.xxx.reader.text.page;

import com.xxx.reader.file.Charset;

import java.util.ArrayList;
import java.util.List;

public class ParaData extends ElementData {


    public List<LineData> lineDataList;

    /**
     * 字符串编码
     */
    public int code= Charset.UNKOWN;

    public int startLineIndex;
    public int endLineIndex;


    public long posStart;
    public long posEnd;
    public int lineCount;
    public String content;

    public ParaData() {
        this(null);
    }

    public ParaData(ParaData paraData) {

        if (paraData != null) {
            startLineIndex = paraData.startLineIndex;
            endLineIndex = paraData.endLineIndex;
            posStart = paraData.posStart;
            posEnd = paraData.posEnd;
            lineCount = paraData.lineCount;
            content = paraData.content;
            code = paraData.code;
            lineDataList = paraData.lineDataList;

        }

    }


    public void addLine(LineData lineData) {

        if (lineDataList == null) {
            lineDataList = new ArrayList<>();

        }
        lineDataList.add(lineData);
        lineCount = lineDataList.size();
    }


    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        if (lineDataList == null) {
            return "";

        }

        for (LineData lineData : lineDataList) {
            stringBuilder.append(lineData.toString());
            stringBuilder.append("\n\r");

        }


        return stringBuilder.toString();


    }



    public float getFirstLineHeight(boolean asc) {

        if (lineDataList == null || lineDataList.size() == 0) return 0;

        return lineDataList.get(asc ? 0 : lineDataList.size() - 1).height;


    }

    public long getStartLinePos( )
    {
       return getLinePos(startLineIndex);

    }

    /**
     * 段落结束的文件位置。
     * @return
     */
    public long getEndLindPos( )
    {
        if(endLineIndex<0||endLineIndex>=lineCount-1) return posEnd;
       return getLinePos(endLineIndex+1 );

    }


    public long getLinePos( int lineIndex)
    {
        if(lineIndex<=0)
        {
            return posStart;
        }

        int charIndex=lineDataList.get(lineIndex).wordDataList.get(0).index;
        String temp=content.substring(0,charIndex-1);
        try {
            byte[] bytes= temp.getBytes(Charset.getEncoding(code));
            return posStart+bytes.length;

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return posStart;

    }
}
