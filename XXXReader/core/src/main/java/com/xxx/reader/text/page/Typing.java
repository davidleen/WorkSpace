package com.xxx.reader.text.page;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.io.FileUtils;
import com.giants3.pools.ObjectPool;
import com.giants3.pools.PoolCenter;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.file.BufferedRandomAccessFile;
import com.xxx.reader.file.TXTReader;

import java.io.IOException;
import java.util.Stack;

public class Typing {


    public Typing(int width, int height, int hGap, int vGap) {

    }



    private static float getDrawWidth( TypeParam typeParam)
    {
      return   typeParam.width-typeParam.padding[0]-typeParam.padding[2];
    }
    private static float getDrawHeight( TypeParam typeParam)
    {
      return   typeParam.height-typeParam.padding[01]-typeParam.padding[3];
    }

    public static PageData typePrePage(PageData lastPageData, IChapter iChapter, TypeParam typeParam)  {

        if (lastPageData == null||lastPageData.getFirstPara()==null) {
            return typePage(null, iChapter, -100, typeParam);
        }


        ParaData paraData=lastPageData.getFirstPara();


        PageData newPageData=createPageData(typeParam);
        newPageData.fileSize=lastPageData.fileSize;

        Stack<ParaData> paraDataStack=new Stack<>();
        float totalHeight=newPageData.height;
        float width=newPageData.width;

        long filePos=0;
        if(paraData.startLineIndex>0)
        {

            paraData=new ParaData( paraData);
            paraData.endLineIndex= paraData.startLineIndex-1;

            float paraHeight=measurePara(paraData,paraData.endLineIndex,totalHeight,typeParam.lineSpace,false);
            if(paraHeight>0)
            {
                paraDataStack.push(paraData);
                if(paraHeight<totalHeight)
                {
                    totalHeight-=paraHeight;
                    totalHeight-=typeParam.paragraphSpace;
                    filePos=paraData.posStart;
                }else {totalHeight=0;}
            }





        }

        if(totalHeight>0)
        {
            BufferedRandomAccessFile randomAccessFile = null;
            try {
                String filePath=iChapter.getFilePath();
                randomAccessFile = new BufferedRandomAccessFile(filePath, "r");
                int code = TXTReader.regCode(filePath);
                randomAccessFile.setCode(code);




                while(totalHeight-typeParam.paragraphSpace-typeParam.textSize>0&&filePos!=0)
                {


                    randomAccessFile.seek(filePos);
                    randomAccessFile.findLastLine();

                    while (randomAccessFile.getFilePointer()>filePos)
                    {
                        randomAccessFile.findLastLine();
                    }
                    filePos=randomAccessFile.getFilePointer();
                    String line=randomAccessFile.m_readLine();

                    if(StringUtil.isEmpty(line))
                    {

                        continue;
                    }

                    ObjectPool<ParaData> objectPool = PoolCenter.getObjectPool(ParaData.class);
                    paraData = objectPool.newObject();
                    paraData.code=code;
                    paraData.content=line;
                    ParaTypeset.type(paraData, line, width, typeParam.wordSpace, typeParam.textSize, typeParam.includeFontPadding);
                    paraData.posStart = filePos;
                    paraData.posEnd = randomAccessFile.getFilePointer();
                    float paraHeight=measurePara(paraData,lastPageData.height,typeParam.lineSpace,false);

                    if(paraHeight<totalHeight)
                    {

                        totalHeight-=paraHeight;
                        totalHeight-=typeParam.paragraphSpace;
                        paraDataStack.push(paraData);

                    }




                }





            }catch (Throwable t)
            {
                Log.e(t);
            }finally {

                FileUtils.safeClose(randomAccessFile);
            }








        }

        //到达章节首部
        if(filePos==0)
        {

            typePageNext(newPageData,iChapter,0,typeParam,null);
            newPageData.isFirstPage=true;
        }else {





            ParaData temp=null;
            float y=0;

           do {


               temp=paraDataStack.pop();
               float newY = fillPage(newPageData, y, temp, typeParam.lineSpace);
               temp.x = 0;
               temp.y = y;
               temp.height = newY - y;
               temp.width = lastPageData.width;
               y = newY;
               y+=typeParam.paragraphSpace;


           }while (!paraDataStack.isEmpty());



        }








        newPageData.update();

        return newPageData;
    }


    public static PageData typePage(PageData lastPage, IChapter iChapter, long startPos, TypeParam typeParam)  {


        PageData pageData = typePageNext( iChapter, startPos, typeParam, lastPage);
        pageData.update();
        return pageData;

    }



    private static PageData createPageData(TypeParam typeParam)
    {


        PageData pageData = PoolCenter.getObjectPool(PageData.class).newObject();
        pageData.x=typeParam.padding[0];
        pageData.y=typeParam.padding[1];
        pageData.width=getDrawWidth(typeParam);
        pageData.height=getDrawHeight(typeParam);
        pageData.isFirstPage=false;
        pageData.isLastPage=false;
        return pageData;
    }


    public static PageData  typePageNext(  IChapter iChapter, long startPos, TypeParam typeParam, PageData lastPage)  {

        PageData pageData=createPageData(typeParam);
        typePageNext(pageData,iChapter,startPos,typeParam,lastPage);
        return pageData;

    }

    public static void   typePageNext(PageData pageData, IChapter iChapter, long startPos, TypeParam typeParam, PageData lastPage)  {






        long lineStartPoint = startPos;

        float y = 0;

        if (lastPage != null&&lastPage.getLastPara()!=null) {
            pageData.fileSize=lastPage.fileSize;
            ParaData lastPara = lastPage.getLastPara();
            if (lastPara.endLineIndex >=0   && lastPara.endLineIndex < lastPara.lineCount - 1) {

                ParaData paraData = new ParaData(lastPara);
                paraData.startLineIndex=lastPara.endLineIndex+1;
                float newY = fillPage(pageData, y, paraData, typeParam.lineSpace);
                if (newY >= pageData.height) {
                    return  ;
                } else {


                    y=newY+typeParam.paragraphSpace;
                }


            }


            lineStartPoint = lastPara.posEnd;
        }

        ObjectPool<ParaData> objectPool = PoolCenter.getObjectPool(ParaData.class);

        boolean isFirstPage=(lastPage==null||!lastPage.isFirstPage)&&lineStartPoint==0;


        //绘制 章节头
        if (isFirstPage) {

            ParaData paraData=objectPool.newObject();
            String name = iChapter.getName();
            ParaTypeset.type(paraData, name,pageData.width,typeParam.wordSpace, typeParam.textSize,new float[]{typeParam.textSize*2} ,typeParam.includeFontPadding);
            paraData.content= name;
            paraData.posStart = 0;
            paraData.posEnd =0;
            paraData.startLineIndex = -1;
            paraData.endLineIndex = -1;
            float newY = fillPage(pageData, y, paraData, typeParam.lineSpace);
            paraData.x = 0;
            paraData.y = newY;
            paraData.height =   newY-y;
            paraData.width = pageData.width;

            y = newY;

            y+=typeParam.paragraphSpace*5;

        }
        String line;



        boolean fileEnd = false;


        BufferedRandomAccessFile randomAccessFile = null;
        try {
            String filePath=iChapter.getFilePath();

            randomAccessFile = new BufferedRandomAccessFile(filePath, "r");
            int code = TXTReader.regCode(filePath);
            randomAccessFile.setCode(code);

            pageData.fileSize=randomAccessFile.length();
            if (lineStartPoint < 0) {
                lineStartPoint = randomAccessFile.length() + lineStartPoint;
            }

            randomAccessFile.seek(lineStartPoint);


            pageData.isFirstPage=lastPage==null&&lineStartPoint==0;
            while (lineStartPoint >= 0) {
//            randomAccessFile.seek(lineStartPoint);
//            randomAccessFile.findLastLine();
//            lineStartPoint = randomAccessFile.getFilePointer();
                lineStartPoint=randomAccessFile.getFilePointer();
                line = randomAccessFile.m_readLine();

                if (line == null) {
                    fileEnd = true;
                    break;
                }


                if (StringUtil.isEmpty(line)) {
                    continue;
                }

               // Log.e("line:" + line);
               // Log.e("post:" + lineStartPoint + "-" + randomAccessFile.getFilePointer());




                ParaData paraData = objectPool.newObject();
                paraData.code=code;
                paraData.content=line;
                ParaTypeset.type(paraData, line, pageData.width, typeParam.wordSpace, typeParam.textSize, typeParam.includeFontPadding);
                paraData.posStart = lineStartPoint;
                paraData.posEnd = randomAccessFile.getFilePointer();
                paraData.startLineIndex = -1;
                paraData.endLineIndex = -1;

//            Log.e("paraData:"+paraData.toString());
                if (y + paraData.getFirstLineHeight(true) > pageData.height) {

                    break;
                }


                float newY = fillPage(pageData, y, paraData, typeParam.lineSpace);


                paraData.x = 0;
                paraData.y = y;
                paraData.height = newY - y;
                paraData.width = pageData.width;
                y = newY;
                //预测是否能放下 下一个段落的第一行。
                if (y + typeParam.paragraphSpace + typeParam.textSize > pageData.height) {
                    break;
                }

                y += typeParam.paragraphSpace;


            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.safeClose(randomAccessFile);
        }
       // Log.e(pageData);


        pageData.isLastPage=fileEnd;


    }


    private static float fillPage(PageData pageData, float y, ParaData paraData, float lineSpace) {

        float height = pageData.height;


        float paraHeight=measurePara(paraData,paraData.startLineIndex==-1?0:paraData.startLineIndex,height-y,lineSpace);
        if(paraHeight>0)
        {
            pageData.addParaData(paraData);
        }
       return y+paraHeight;

    }


    private static float measurePara(ParaData paraData, float height ,float lineSpace )
    {
        return measurePara(paraData,0,height,lineSpace);
    }


    private static float measurePara(ParaData paraData, float height ,float lineSpace,boolean asc )
    {
        return measurePara(paraData,asc?0:paraData.lineCount-1,height,lineSpace,asc);
    }
    private static float measurePara(ParaData paraData,int startIndex,float height ,float lineSpace )
    {

        return measurePara(paraData,startIndex,height,lineSpace,true);

    }


    private static float measurePara(ParaData paraData,int startIndex,float height ,float lineSpace ,boolean asc)
    {



        int startLineY=0;
        int y=0;
        int size = paraData.lineDataList.size();
        int endIndex = -1;
        int step=asc?1:-1;
        if (startIndex==-1)
        {
            startIndex=0;
        }
        for (int i = startIndex; true;i+=step) {
            if(i<0||i>=size)  break;
            LineData lineData = paraData.lineDataList.get(i);
            if (y+ lineData.height > height) {
                break;
            }

            lineData.x = 0;
            lineData.y = y - startLineY;

            endIndex = i;

            y += lineData.height;
            //放不下下一行
            if (y + lineSpace > height) {

                break;
            }
            if (i < size - 1&&i>0) {
                if(paraData.lineDataList.get(i+step).height+y+lineSpace> height)
                {
                    break;
                }else
                    y += lineSpace;
            }


        }


        paraData.startLineIndex = asc?startIndex:endIndex;
        paraData.endLineIndex = asc?endIndex:startIndex;
        return y-startLineY;

    }
//    private  int findStartLineIndex(ParaData paraData,int endIndex,int height )
//    {
//
//
//    }
//
//    private  int findNextLineIndex(ParaData paraData,int startIndex,int height,boolean desc)
//    {
//
//
//
//
//
//
//
//
//    }





    public String read(BufferedRandomAccessFile accessFile, boolean previous) {


        return "";

    }


    /**
     * 多余的空位 平均分配到每一行 调整行间距
     */
    private void adjustLineGap() {
    }
}
