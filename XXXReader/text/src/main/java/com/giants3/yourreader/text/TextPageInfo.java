package com.giants3.yourreader.text;

import com.giants3.yourreader.text.elements.WordElement;
import com.xxx.reader.core.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2018/12/31.
 */

public class TextPageInfo extends PageInfo {


    public List<WordElement> elements;

    public List<PagePara> pageParas;

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
}
