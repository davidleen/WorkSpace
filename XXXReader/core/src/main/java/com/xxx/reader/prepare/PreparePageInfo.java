package com.xxx.reader.prepare;

import com.xxx.reader.core.PageInfo;

import java.util.LinkedList;
import java.util.List;

public class PreparePageInfo {

    int currentChapterIndex;
    int currentPageIndex;

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

        if(midIndex<size-PREPARE_SIZE)
        {
             pageInfos.remove(size-1);
             size--;

        }
        if (midIndex>=PREPARE_SIZE)
        {
            pageInfos.remove(0);
            midIndex--;
            size--;

        }

        currentChapterIndex=pageInfos.get(midIndex).chapterIndex;
        currentPageIndex=pageInfos.get(midIndex).pageIndex;


    }

    public boolean canTurnNext() {
        return midIndex<size-1;
    }

    public boolean canTurnPrevious() {
        return midIndex>0;
    }

    public void jump(float progress)
    {





        PageInfo currentP=size==0?null:pageInfos.get(midIndex);

        if(currentP==null) return;
        int destPageIndex= (int) (currentP.pageCount*progress);
        destPageIndex=Math.min(destPageIndex,currentP.pageCount-1);


        for ( PageInfo  pageInfo:pageInfos)
        {
            if(pageInfo.chapterIndex==currentChapterIndex&&pageInfo.pageIndex==destPageIndex)
            {
                midIndex= pageInfos.indexOf(pageInfo);
                adjustArray();
                return;
            }
        }
        currentChapterIndex=currentP.chapterIndex;
        currentPageIndex=destPageIndex;
        clearPages();





    }
    private void clearPages()
    {
        pageInfos.clear();
        midIndex=0;
        size=0;
    }


    public void nextChapter()
    {
        PageInfo currentP=pageInfos.get(midIndex);

        if(currentP!=null)
        {
            currentChapterIndex=currentP.chapterIndex+1;

        }else
        {
            currentChapterIndex=0;
        }
        currentPageIndex=0;
        clearPages();

    }

    public void previousChapter()
    {

        PageInfo currentP=pageInfos.get(midIndex);

        if(currentP!=null)
        {
            currentChapterIndex=currentP.chapterIndex-1;

        }else
        {
            currentChapterIndex=0;
        }
        currentPageIndex=0;

        clearPages();

    }

}
