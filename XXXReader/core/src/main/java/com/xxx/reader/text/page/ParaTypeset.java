package com.xxx.reader.text.page;

import android.graphics.Paint;

import com.giants3.pools.PoolCenter;
import com.xxx.reader.turnner.sim.SettingContent;

/**
 * 段落排版
 */
public class ParaTypeset {


    static    Paint paint=new Paint() ;

    public static void setPaint(Paint mPaint) {

        paint=new Paint(mPaint);
        onTextSizeChange();
    }
    static Paint.FontMetrics fontMetrics=new Paint.FontMetrics();



    /**
     *
     * @param paraData
     * @param s
     * @param width
     * @param wordSpace
     * @param textSize
     * @param includeFontPadding
     */
    public static void type(ParaData paraData,String s ,float width, float wordSpace,float textSize,boolean includeFontPadding)
    {


          type(paraData,s,width,wordSpace,textSize,null,includeFontPadding);


    }
    public static void type(ParaData paraData,String s ,float width, float wordSpace,float textSize,float[] textSizes,boolean includeFontPadding)
    {




        if(textSize!=paint.getTextSize())
        {
            onTextSizeChange( );
            paint.setTextSize(textSize);
        }

//
//        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
//        float   height=includeFontPadding?(fontMetrics.descent-fontMetrics.ascent):textSize ;
        @SettingContent.IndentMode int indentCount=SettingContent.getInstance().getSettingIndentMode();


        float  aTextWidth=getTextWidth();

        float x=0                ;

        //缩进处理
        x+=indentCount*(aTextWidth+wordSpace);


        LineData lineData=   PoolCenter.getObjectFactory(LineData.class).newObject();
        paraData.addLine(lineData);
        float left=0;
        float right=width;

        //遍历所有字符
        int length=s.length();
        char currentChar;
        int maxHeight=0;
        for (int i = 0; i < length; i++) {

            currentChar=s.charAt(i);

            float charTextSie=textSizes==null||textSizes.length<=i?textSize:textSizes[i];
            float charHeight=getTextHeight(charTextSie,includeFontPadding);
            if(isEmptyChar(currentChar))
            {
                continue;
            }

            float textWidth=getTextWidth( currentChar,charTextSie);
            boolean newLine=false;
            if(x+textWidth>right)
            {
                //当前字符放不下
                //检查当前字符 是否不适合在下一行首

                //是，并且当前行的总字间距>当前字符宽，  加入当前行，并调整字间距（减少）
                adjustLine(lineData,right-(x-wordSpace) );
                lineData.width=right;
                lineData= PoolCenter.getObjectFactory(LineData.class).newObject();
                paraData.addLine(lineData);

                i--;
                x=left;
                continue;

               //
            }










                WordData wordData= PoolCenter.getObjectFactory(WordData.class).newObject();
                wordData.word=currentChar;
                wordData.index=i;

                wordData.textSize=charTextSie;
                wordData.width=textWidth;
                wordData.height=charHeight;
                wordData.x=x;
                wordData.y=0;
                lineData.addWord(wordData);
                lineData. width=x+textWidth-left;




                //存放不下下一个字符
                if(x+textWidth+wordSpace>=right)
                {

                    adjustLine(lineData,right-(x+textWidth));
                    lineData.width=right;

                    lineData= PoolCenter.getObjectFactory(LineData.class).newObject();
                    paraData.addLine(lineData);
                    x=left;

                    continue;

                }

                x+=textWidth+wordSpace;
            




        }
























    }

    private static void onTextSizeChange() {


        for (int i = 0; i < charWidth.length; i++) {
            charWidth[i]=0;
        }
        chineseCharWidth=0;


    }


    /**
     * 当前行调整位置，使其左右对齐
     * @param lineData
     * @param additionWidth
     */
    private static   void adjustLine(LineData lineData,float additionWidth)
    {

        int wordCount=lineData.wordDataList==null?0:lineData.wordDataList.size();

        //平均加到每个字符
        float averageWidth=additionWidth/(wordCount-1);
        for (int i = 0; i < wordCount; i++) {

            WordData wordData=lineData.wordDataList.get(i);
            wordData.x+=i*averageWidth;

        }





    }



    private static float getTextWidth( )
    {

       return getTextWidth(  ' ',paint.getTextSize());

    }

    public static final int CACHE_WIDTH_SIZE = 2048;
    static float[] charWidth=new float[CACHE_WIDTH_SIZE];
    static float  chineseCharWidth=0;
    static float[] textHeight=new float[200];
    private static float getTextWidth( char c,float textSize)
    {

        if(textSize==paint.getTextSize()) {

            if (c > '\u4e00' && c < '\u9fbf') {
                if (chineseCharWidth < 1) {
                    chineseCharWidth = (int) paint.measureText(String.valueOf(c));
                }
                return chineseCharWidth;
            }

            int hashCode = (int) c % CACHE_WIDTH_SIZE;
            float v = charWidth[hashCode];
            if (v == 0) {

                v = paint.measureText(String.valueOf(c));
                charWidth[hashCode] = v;
            }
            return v;

        }else
        {
            float originTextSize=paint.getTextSize();
            paint.setTextSize(textSize);
           float v = paint.measureText(String.valueOf(c));

           paint.setTextSize(originTextSize);
           return v;
        }




    }


    private static float getTextHeight(float textSize,boolean includeFontPadding)
    {


        if(!includeFontPadding)
        {
            return textSize;
        }
        float height=textHeight[(int) textSize];
        if(height<=0)
        { float originTextSize=paint.getTextSize();
            paint.setTextSize(textSize);
            height = paint.getFontMetrics(fontMetrics);

            height=fontMetrics.descent-fontMetrics.ascent;

            paint.setTextSize(originTextSize);
            textHeight[(int) textSize]=height;


        }
        return height;
    }



    // 空格判断参考 http://zh.wikipedia.org/zh/%E7%A9%BA%E6%A0%BC
    protected static final boolean isEmptyChar(char ch) {
        if (ch == 0x00a0 || ch == 0x0020 || ch == '\t' || (ch >= 0x2002 && ch <= 0x200D)
                || ch == '　' || ch == 0x202f) {
            return true;
        }
        return false;
    }


}
