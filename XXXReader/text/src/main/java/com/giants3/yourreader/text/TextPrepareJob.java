package com.giants3.yourreader.text;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.io.FileUtils;
import com.xxx.reader.Url2FileMapper;
import com.xxx.reader.book.ChapterMeasureResult;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.file.BufferedRandomAccessFile;
import com.xxx.reader.file.TXTReader;
import com.xxx.reader.prepare.Cancelable;
import com.xxx.reader.prepare.PrepareJob;
import com.xxx.reader.turnner.sim.SettingContent;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2019/1/1.
 */

public class TextPrepareJob implements PrepareJob<IChapter, TextPageInfo, DrawParam> {

    private Url2FileMapper<IChapter> chapterUrl2FileMapper;

    public TextPrepareJob(Url2FileMapper<IChapter> chapterUrl2FileMapper) {


        this.chapterUrl2FileMapper = chapterUrl2FileMapper;
    }

    @Override
    public ChapterMeasureResult<TextPageInfo> measureChapter(IChapter iChapter, DrawParam drawParam, Cancelable cancelable, int pageType) {


        String path = chapterUrl2FileMapper.map(iChapter, iChapter.getUrl());


//        ChapterMeasureResult<TextPageInfo> chapterMeasureResult=new ChapterMeasureResult<>();
//        chapterMeasureResult.pageCount=1;
//        chapterMeasureResult.fileSize=


//       String content= ContentExtractor.getContent(path);
//
//        TXTReader txtReader=new TXTReader(path,0);
//        try {
//            txtReader.openfile();
//            String s = txtReader.m_readline();
//            Log.e(s);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
        ChapterMeasureResult<TextPageInfo> result = new ChapterMeasureResult<TextPageInfo>();

        float textSize = SettingContent.getInstance().getTextSize();
        int width = drawParam.width;
        int hgap = (int) SettingContent.getInstance().getWordGap();

        TXTtypeset txTtypeset = new TXTtypeset(textSize, width, hgap);

        TextPageInfo textPageInfo=null;
        TextPageInfo lastTextPageInfo=null;
        BufferedRandomAccessFile bufferedRandomAccessFile = null;
        try {
            bufferedRandomAccessFile = new BufferedRandomAccessFile(path, "r");
            int code = TXTReader.regCode(path);

            bufferedRandomAccessFile.setCode(code);


        do {
            textPageInfo=generateNextPage(lastTextPageInfo,bufferedRandomAccessFile,drawParam,txTtypeset);
            if(textPageInfo!=null)
            {

                if(lastTextPageInfo==null) textPageInfo.pageIndex=0;
                else
                    textPageInfo.pageIndex=lastTextPageInfo.pageIndex+1;
                textPageInfo.chapterIndex=iChapter.getIndex();
                textPageInfo.updateElements( );
                result.pageValues.add(textPageInfo);
                lastTextPageInfo=textPageInfo;
            }


        }while (textPageInfo!=null);


            result.pageCount=result.pageValues.size();
            for (TextPageInfo item:result.pageValues)
            {
                item.pageCount=result.pageCount;
            }
            result.fileSize=bufferedRandomAccessFile.length();
            result.name="";
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            FileUtils.safeClose(bufferedRandomAccessFile);
        }


        return result;


//        float paraDistance = 10;
//        float lineDistance = 11;
//
//        List<ParagraghData> paragraghList = new ArrayList<>();
//        try {
//
//            String line;
//            TextPageInfo textPageInfo = null;
//
//            PagePara lastPagePara = null;
//            int lastPageLineIndex;
//            float pageHeight = 0;
//            while ((line = bufferedRandomAccessFile.m_readLine()) != null) {
//                if (textPageInfo == null) {
//                    textPageInfo = new TextPageInfo();
//                }
//
//
//                int[] lineHead = new int[line.length()];
//
//                float[] xPostions = txTtypeset.typeset(new StringBuffer(line), lineHead, 0);
//
//                ParagraghData paragraphData = new ParagraghData();
//                paragraphData.setContent(line);
//
//                ParaTypeset paraTypeset = new ParaTypeset();
//                paraTypeset.lineHead = lineHead;
//                paraTypeset.paragraghData = paragraphData;
//                paraTypeset.xPositions = xPostions;
//
//
//                if (paraTypeset.getLineCount() <= 0)
//                    continue;
//                PagePara pagePara = new PagePara();
//                pagePara.paraTypeset = paraTypeset;
//
//                float paraHeight = paraTypeset.getLineCount() * textSize + lineDistance * (paraTypeset.getLineCount() - 1);
//                if (pageHeight + paraHeight < drawParam.height) {
//                    pagePara.firstLine = -1;
//                    pagePara.lastLine = -1;
//                    textPageInfo.addParam(pagePara);
//                    pageHeight += paraHeight + paraDistance;
//                } else {
//                    float leftHeight = drawParam.height - pageHeight;
//
//
//                    int lastLineCount = (int) (leftHeight / (textSize + lineDistance));
//                    if (lastLineCount > 0) {
//                        pagePara.firstLine = -1;
//                        pagePara.lastLine = lastLineCount;
//                        textPageInfo.addParam(pagePara);
//                        lastPagePara = pagePara;
//                    } else {
//                        lastPagePara = null;
//                    }
//
//                    result.pageValues.add(textPageInfo);
//                    textPageInfo = null;
//
//                }
//
//
//                Log.e(line);
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }


    private TextPageInfo generateNextPage(TextPageInfo lastTextPageInfo, BufferedRandomAccessFile randomAccessFile, DrawParam drawParam,TXTtypeset txTtypeset) throws IOException {


        float paraDistance = SettingContent.getInstance().getParaSpace();
        float lineDistance = SettingContent.getInstance().getLineSpace();
        float textSize = SettingContent.getInstance().getTextSize();
        TextPageInfo textPageInfo = null;
        float pageHeight = 0;
        PagePara lastPagePara = lastTextPageInfo == null ? null : lastTextPageInfo.getLastPagePara();
        if (lastPagePara != null && lastPagePara.lastLine != -1) {

            PagePara pagePara = new PagePara();
            pagePara.paraTypeset = lastPagePara.paraTypeset;
            pagePara.firstLine = lastPagePara.lastLine;

            int leftLineCount = lastPagePara.paraTypeset.getLineCount() - lastPagePara.lastLine;

            float leftParaHeight = leftLineCount * (textSize) + lineDistance * (leftLineCount - 1);
            if (leftParaHeight < drawParam.height)  //当前能放得下。
            {
                pagePara.lastLine = -1;
                pageHeight += leftParaHeight + paraDistance;
                if(textPageInfo==null)
                    textPageInfo=new TextPageInfo();
                textPageInfo.addParam(pagePara);

                //剩下的空间不足以放置一行文字，直接返回
                if (pageHeight > drawParam.height || pageHeight + textSize > drawParam.height) {
                    return textPageInfo;
                }

            } else {
                //当前段落就占据了剩余的一页 直接返回
                int lines = (int) (drawParam.height / (textSize + lineDistance));
                pagePara.lastLine = pagePara.firstLine + lines;
                if(textPageInfo==null)
                    textPageInfo=new TextPageInfo();
                textPageInfo.addParam(pagePara);
                return textPageInfo;

            }


        }


        if(lastPagePara!=null)
        {
            randomAccessFile.seek(lastPagePara.paraTypeset.paragraghData.paragraghEnd);
        }else
        {
            randomAccessFile.seek(0);
        }

        String line;
        long lineStartPoint=  randomAccessFile.getFilePointer();
        while ((line = randomAccessFile.m_readLine()) != null) {


            if(StringUtil.isEmpty(line)) continue;

            int lineLength = line.length();
            int[] lineHead = new int[lineLength];
            for (int i = 0; i < lineLength; i++) {
                lineHead[i]=-1;
            }

            float[] xPostions = txTtypeset.typeset(new StringBuffer(line), lineHead, 0);

            ParagraghData paragraphData = new ParagraghData();

            paragraphData.setContent(line);
            paragraphData.paragragStart=lineStartPoint;
            paragraphData.paragraghEnd=randomAccessFile.getFilePointer();
            lineStartPoint=paragraphData.paragraghEnd;


            ParaTypeset paraTypeset = new ParaTypeset();
            paraTypeset.lineHead = lineHead;
            paraTypeset.paragraghData = paragraphData;
            paraTypeset.xPositions = xPostions;


            if (paraTypeset.getLineCount() <= 0)
                continue;
            PagePara pagePara = new PagePara();
            pagePara.paraTypeset = paraTypeset;

            float paraHeight = paraTypeset.getLineCount() * textSize + lineDistance * (paraTypeset.getLineCount() - 1);
            if (pageHeight + paraHeight < drawParam.height) {
                pagePara.firstLine = -1;
                pagePara.lastLine = -1;
                if(textPageInfo==null)
                    textPageInfo=new TextPageInfo();
                textPageInfo.addParam(pagePara);
                pageHeight += paraHeight + paraDistance;

                //剩下的空间一行也放不下了。
                if(pageHeight+textSize>drawParam.height) break;
            } else {
                float leftHeight = drawParam.height - pageHeight;


                int lastLineCount = (int) (leftHeight / (textSize + lineDistance));
                if (lastLineCount > 0) {
                    pagePara.firstLine = -1;
                    pagePara.lastLine = lastLineCount;
                    if(textPageInfo==null)
                        textPageInfo=new TextPageInfo();
                    textPageInfo.addParam(pagePara);

                } else
                    if(leftHeight>textSize){
                        {
                            pagePara.firstLine = -1;
                            pagePara.lastLine = 1;
                            if(textPageInfo==null)
                                textPageInfo=new TextPageInfo();
                            textPageInfo.addParam(pagePara);
                        }
                }


                break;
            }


          //  Log.e(line);
        }
        textPageInfo.isLastPage=line==null;
        return textPageInfo;






    }
    private TextPageInfo generatePreviousPage(TextPageInfo currentPageInfo, BufferedRandomAccessFile randomAccessFile, DrawParam drawParam,TXTtypeset txTtypeset) throws IOException {


        float paraDistance = SettingContent.getInstance().getParaSpace();
        float lineDistance = SettingContent.getInstance().getLineSpace();
        float textSize = SettingContent.getInstance().getTextSize();
        TextPageInfo textPageInfo = null;
        float pageHeight = 0;
        PagePara firstParam = currentPageInfo == null ? null : currentPageInfo.getFirstPagePara();
        if (firstParam != null && firstParam.firstLine != -1) {

            PagePara pagePara = new PagePara();
            pagePara.paraTypeset = firstParam.paraTypeset;
            pagePara.lastLine = firstParam.firstLine;

            int leftLineCount =  firstParam.firstLine;

            float leftParaHeight = leftLineCount * (textSize) + lineDistance * (leftLineCount - 1);
            if (leftParaHeight < drawParam.height)  //当前能放得下。
            {
                pagePara.firstLine = -1;
                pageHeight += leftParaHeight + paraDistance;
                if(textPageInfo==null)
                    textPageInfo=new TextPageInfo();
                textPageInfo.addParam(pagePara);

                //剩下的空间不足以放置一行文字，直接返回
                if (pageHeight > drawParam.height || pageHeight + textSize > drawParam.height) {
                    return textPageInfo;
                }

            } else {
                //当前段落就占据了剩余的一页 直接返回
                int lines = (int) (drawParam.height / (textSize + lineDistance));
                pagePara.firstLine = firstParam.firstLine - lines;
                if(textPageInfo==null)
                    textPageInfo=new TextPageInfo();
                textPageInfo.addParam(pagePara);
                return textPageInfo;

            }


        }

        String line;
        long lineStartPoint =0;
        if(currentPageInfo==null)
        {
            //如果当前页未空， 则表示文件尾部开始
            lineStartPoint=randomAccessFile.length()-1;
        }else
        if(firstParam!=null)
        {
            lineStartPoint=firstParam.paraTypeset.paragraghData.paragragStart;

        }

        while (lineStartPoint>0){
            randomAccessFile.seek(lineStartPoint);
            randomAccessFile.findLastLine();
            lineStartPoint=randomAccessFile.getFilePointer();
           line = randomAccessFile.m_readLine();

            if (line==null) {

                break;
            }


            if(StringUtil.isEmpty(line)) continue;

            Log.e("line:"+line);
            Log.e("post:"+lineStartPoint+"-"+randomAccessFile.getFilePointer());

            int lineLength = line.length();
            int[] lineHead = new int[lineLength];
            for (int i = 0; i < lineLength; i++) {
                lineHead[i]=-1;
            }

            float[] xPostions = txTtypeset.typeset(new StringBuffer(line), lineHead, 0);

            ParagraghData paragraphData = new ParagraghData();

            paragraphData.setContent(line);
            paragraphData.paragragStart=lineStartPoint;
            paragraphData.paragraghEnd=randomAccessFile.getFilePointer();



            ParaTypeset paraTypeset = new ParaTypeset();
            paraTypeset.lineHead = lineHead;
            paraTypeset.paragraghData = paragraphData;
            paraTypeset.xPositions = xPostions;


            if (paraTypeset.getLineCount() <= 0)
                continue;
            PagePara pagePara = new PagePara();
            pagePara.paraTypeset = paraTypeset;

            float paraHeight = paraTypeset.getLineCount() * textSize + lineDistance * (paraTypeset.getLineCount() - 1);
            if (pageHeight + paraHeight < drawParam.height) {
                pagePara.firstLine = -1;
                pagePara.lastLine = -1;
                if(textPageInfo==null)
                    textPageInfo=new TextPageInfo();
                textPageInfo.addParam(pagePara);
                pageHeight += paraHeight + paraDistance;

                //剩下的空间一行也放不下了。
                if(pageHeight+textSize>drawParam.height) break;
            } else {
                float leftHeight = drawParam.height - pageHeight;


                int lastLineCount = (int) (leftHeight / (textSize + lineDistance));
                if (lastLineCount > 0) {
                    pagePara.firstLine =paraTypeset.getLineCount()-lastLineCount ;
                    pagePara.lastLine = -1;
                    if(textPageInfo==null)
                        textPageInfo=new TextPageInfo();
                    textPageInfo.addParam(pagePara);

                } else
                    if(leftHeight>textSize){
                        {
                            pagePara.firstLine = paraTypeset.getLineCount()-1;
                            pagePara.lastLine = -1;
                            if(textPageInfo==null)
                                textPageInfo=new TextPageInfo();
                            textPageInfo.addParam(pagePara);
                        }
                }


                break;
            }


          //  Log.e(line);
        }

        if(textPageInfo!=null)
            textPageInfo.descSortPara();
        if(lineStartPoint==0)
        {//到达章节头了，重新绘制第一页，。


            Log.e("到达章节头了，重新绘制第一页，。");
            textPageInfo=generateNextPage(null,randomAccessFile,drawParam,txTtypeset);
            textPageInfo.isFirstPage=true;
        }
        return textPageInfo;






    }


    @Override
    public TextPageInfo generateNext(TextPageInfo currentPageInfo, IChapter iChapter, DrawParam drawParam) {

        float textSize = SettingContent.getInstance().getTextSize();
        int width = drawParam.width;
        String path = chapterUrl2FileMapper.map(iChapter, iChapter.getUrl());
        int hgap = (int) SettingContent.getInstance().getWordGap();
        BufferedRandomAccessFile bufferedRandomAccessFile = null;
        try {
            TXTtypeset txTtypeset = new TXTtypeset(textSize, width, hgap);
            bufferedRandomAccessFile = new BufferedRandomAccessFile(path, "r");
            int code = TXTReader.regCode(path);

            bufferedRandomAccessFile.setCode(code);
            TextPageInfo textPageInfo = generateNextPage(currentPageInfo, bufferedRandomAccessFile, drawParam, txTtypeset);

            if (textPageInfo != null) {

                textPageInfo.isFirstPage=currentPageInfo==null;
                if (currentPageInfo == null)

                    textPageInfo.pageIndex = 0;
                else
                    textPageInfo.pageIndex = currentPageInfo.pageIndex + 1;
                textPageInfo.chapterIndex = iChapter.getIndex();
                textPageInfo.updateElements();

            }

            return textPageInfo;
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            FileUtils.safeClose(bufferedRandomAccessFile);
        }


        return null;

    }


    @Override
    public TextPageInfo generatePrevious(TextPageInfo currentPageInfo,  IChapter iChapter, DrawParam drawParam)
    {


        float textSize = SettingContent.getInstance().getTextSize();
        int width = drawParam.width;
        String path = chapterUrl2FileMapper.map(iChapter, iChapter.getUrl());
        int hgap = (int) SettingContent.getInstance().getWordGap();
        BufferedRandomAccessFile bufferedRandomAccessFile = null;
        try {
            TXTtypeset txTtypeset = new TXTtypeset(textSize, width, hgap);
            bufferedRandomAccessFile = new BufferedRandomAccessFile(path, "r");
            int code = TXTReader.regCode(path);

            bufferedRandomAccessFile.setCode(code);
            TextPageInfo textPageInfo = generatePreviousPage(currentPageInfo, bufferedRandomAccessFile, drawParam, txTtypeset);

            if (textPageInfo != null) {

//                if (currentPageInfo == null)
//                    textPageInfo.pageIndex = 0;
//                else
//                    textPageInfo.pageIndex = currentPageInfo.pageIndex + 1;
                textPageInfo.chapterIndex = iChapter.getIndex();
                textPageInfo.updateElements();

            }

            return textPageInfo;
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            FileUtils.safeClose(bufferedRandomAccessFile);
        }


        return null;


    }

    /**
     * 段落排版数据
     */
    class ParaTypeset {
        ParagraghData paragraghData;
        float[] xPositions;
        int[] lineHead;
        int lineCount = -1;
        int wordCount;

        public int getLineCount() {
            if (lineCount == -1) {


                int line = 0;
                for (int i = 0; i < lineHead.length; i++) {

                    if (lineHead[i] == -1) {
                        break;
                    }
                    line++;
                }
                lineCount = line;

            }
            return lineCount;
        }
    }


}
