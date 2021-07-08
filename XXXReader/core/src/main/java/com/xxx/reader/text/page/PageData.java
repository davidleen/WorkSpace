package com.xxx.reader.text.page;



import java.util.ArrayList;
import java.util.List;

public class PageData extends ElementData{

    public List<ParaData> paraDataList=new ArrayList<>();


    public long posStart;
    public long posEnd;
    public long fileSize;

    public boolean isFirstPage;
    public boolean isLastPage;


    public void addParaData(ParaData paraData ) {

            paraDataList.add(  paraData);

    }



    public   ParaData getLastPara()
    {

        if(paraDataList==null||paraDataList.size()==0) return  null;
        return paraDataList.get(paraDataList.size()-1);

    }



    public ParaData getFirstPara() {

        if(paraDataList==null||paraDataList.size()==0) return  null;
        return paraDataList.get(0);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PageData{");
        sb.append("paraDataList=").append(paraDataList);
        sb.append(", posStart=").append(posStart);
        sb.append(", posEnd=").append(posEnd);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", width=").append(width);
        sb.append(", height=").append(height);
        sb.append('}');
        return sb.toString();
    }

    public void update() {

        ParaData firstPara = getFirstPara();
        posStart= firstPara==null?0:firstPara.getStartLinePos();
        ParaData lastPara = getLastPara();
        posEnd= lastPara==null?0:lastPara.getEndLindPos();

    }



}
