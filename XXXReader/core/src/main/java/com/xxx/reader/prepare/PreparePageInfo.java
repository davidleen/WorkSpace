package com.xxx.reader.prepare;

import android.view.MotionEvent;

import com.xxx.reader.core.PageInfo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PreparePageInfo {

    int currentChapterIndex;
    int currentPageIndex;
    long  currentPageOffset;
    long  currentPageFileSize;

      LinkedList<PageInfo> pageInfos;
      int midIndex;

    public static final int PREPARE_SIZE=3;

    int size;
 

    public PreparePageInfo()
    {
        pageInfos=new LinkedList<>();
          size=0;
        midIndex=0;
    }


    public PageInfo getCurrentPageInfo()
    {
        if(size==0) return null;
        return pageInfos.get(midIndex);


    }

    public void addPages(List<PageInfo> pageValues) {


        pageInfos.addAll(pageValues);
        size=pageInfos.size();

    }


    public void addPage(PageInfo pageInfo) {

        if(pageInfo==null) return;

        pageInfos.add(pageInfo);
        size=pageInfos.size();
        adjustArray();
    }


    public void addHead(PageInfo pageInfo) {
        if(pageInfo==null) return;
        pageInfos.add(0,pageInfo);
        size=pageInfos.size();
        midIndex++;
        adjustArray();
    }

    public void turnNext() {

        if(midIndex<size-1)
        {
            midIndex++;
        }

        adjustArray();
    }

    public void turnPrevious() {
        if (midIndex > 0)
        {
            midIndex--;
        }
        adjustArray();
    }

    private  void adjustArray()
    {

        if(size==0) return;
        if(midIndex<size-PREPARE_SIZE)
        {
            PageInfo remove = pageInfos.remove(size - 1);
            recycle(remove);
            size--;

        }
        if (midIndex>=PREPARE_SIZE)
        {
            PageInfo remove = pageInfos.remove(0);
            recycle(remove);
            midIndex--;
            size--;

        }

        currentChapterIndex=pageInfos.get(midIndex).chapterIndex;
        currentPageFileSize=pageInfos.get(midIndex).fileSize;
        currentPageOffset=pageInfos.get(midIndex).startPos;


    }

    private void recycle(PageInfo remove) {


        if(remove!=null) {
            remove.recycle();
            remove.isReady=false;
        }




    }

    private void recycle(Collection<PageInfo> remove) {

        for (PageInfo pageInfo:remove)
        {
            recycle(pageInfo);
        }





    }

    public boolean canTurnNext() {
        return midIndex<size-1;
    }

    public boolean canTurnPrevious() {
        return midIndex>0;
    }


    /**
     * 章节内跳转
     * @param progress
     */
    public void jump(float progress)
    {




        this.currentPageOffset= (long) (currentPageFileSize*progress);

        clearPages();





    }


    public void onSettingChange()
    {


        if(pageInfos.size()>0)
        {
            PageInfo pageInfo = pageInfos.get(midIndex);
            currentPageOffset=  pageInfo.startPos ;
        }
        clearPages();

    }
    public  void clearPages()
    {
        recycle(pageInfos);
        pageInfos.clear();
        midIndex=0;
        size=0;
    }


    public void nextChapter()
    {
        PageInfo currentP=pageInfos==null||pageInfos.size()<=midIndex?null:pageInfos.get(midIndex);

        if(currentP!=null)
        {
            currentChapterIndex=currentP.chapterIndex+1;

        }else
        {
            currentChapterIndex++;
        }
        currentChapterIndex=Math.max(0,currentChapterIndex);

        clearPages();

    }

    public void previousChapter()
    {

        PageInfo currentP=pageInfos==null||pageInfos.size()<=midIndex?null:pageInfos.get(midIndex);

        if(currentP!=null)
        {
            currentChapterIndex=currentP.chapterIndex-1;

        }else
        {
            currentChapterIndex--;

        }
        currentChapterIndex=Math.max(0,currentChapterIndex);

        clearPages();

    }

}
