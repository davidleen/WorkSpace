package com.xxx.reader.prepare;

import com.xxx.reader.core.PageInfo;

import java.util.LinkedList;
import java.util.List;

public class PreparePageInfo {

    LinkedList<PageInfo> pageInfos;
    int midIndex;

    public static final int PREPARE_SIZE=6;

    int size;


    public PreparePageInfo()
    {
        pageInfos=new LinkedList<>();
          size=0;
        midIndex=0;
    }


    public void addPages(List<PageInfo> pageValues) {


        pageInfos.addAll(pageValues);
        size=pageInfos.size();

    }


    public void addPage(PageInfo pageInfo) {

        if(pageInfo==null) return;

        pageInfos.add(pageInfo);
        size=pageInfos.size();

    }


    public void addHead(PageInfo pageInfo) {
        if(pageInfo==null) return;
        pageInfos.add(0,pageInfo);
        size=pageInfos.size();
        midIndex++;
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



    }

    public boolean canTurnNext() {
        return midIndex<size-1;
    }

    public boolean canTurnPrevious() {
        return midIndex>0;
    }

}
