package com.giants3.yourreader.text;

import com.giants3.yourreader.text.elements.WordElement;
import com.xxx.reader.core.PageInfo;
import com.xxx.reader.turnner.sim.SettingContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2018/12/31.
 */

public class TextPageInfo extends PageInfo {


    public List<WordElement> elements;

    public List<PagePara> pageParas;
    public long startPos;
    public long endPos;


    public void addParam(PagePara pagePara) {
        if (pageParas == null) {
            pageParas = new ArrayList<>();
        }
        pageParas.add(pagePara);
    }

    public PagePara getLastPagePara() {

        if (pageParas == null || pageParas.size() <= 0) return null;


        return pageParas.get(pageParas.size() - 1);
    }

    public void updateElements( ) {


        if (elements != null) {
            elements.clear();
        } else {
            elements = new ArrayList<>();
        }
        float textSize=SettingContent.getInstance().getTextSize();
        float y = textSize;
        for (PagePara pagePara : pageParas) {

            float[] xPositions = pagePara.paraTypeset.xPositions;
            int[] lineHead = pagePara.paraTypeset.lineHead;


            int startLine = pagePara.firstLine == -1 ? 0 : pagePara.firstLine;
            int lastline = pagePara.lastLine == -1 ? pagePara.paraTypeset.lineCount - 1 : pagePara.lastLine - 1;
            for (int i = startLine; i <= lastline; i++) {

                int index = lineHead[i];
                int lastIndex = (i == pagePara.paraTypeset.lineCount - 1) ? (xPositions.length - 1) : lineHead[i + 1] - 1;
                for (int j = index; j <= lastIndex; j++) {


                    WordElement wordElement = new WordElement();
                    wordElement.word = pagePara.paraTypeset.paragraghData.getContent().substring(j, j + 1);
                    wordElement.x = (int) xPositions[j];
                    wordElement.y = (int) y;

                    elements.add(wordElement);
                }


                y+=textSize;

                if(i!=lastline)
                {
                    y+= SettingContent.getInstance().getLineSpace();
                }
            }

            y+=SettingContent.getInstance().getParaSpace();

        }
    }

    public  PagePara getFirstPagePara() {

        if (pageParas == null || pageParas.size() <= 0) return null;


        return pageParas.get(0);
    }


    /**
     * 将段落倒排处理。
     */
    public void descSortPara() {


        int size = pageParas.size();
        int minIndex = size / 2;
        PagePara temp;
        for (int i = 0; i < minIndex; i++) {
            temp=pageParas.get(i);
            int switchIndex = size - i - 1;
            pageParas.set(i,pageParas.get(switchIndex));
            pageParas.set(switchIndex,temp);
        }
    }

    public boolean isFirstPageOfChapter() {

        return isFirstPage;


    }

    public boolean isLastPageOfChapter() {

        return isFirstPage;


    }
}
