package com.xxx.reader.text.page;

import com.xxx.reader.text.page.PageData;
import com.xxx.reader.core.PageInfo;

/**
 * Created by davidleen29 on 2018/12/31.
 */

public class TextPageInfo2 extends PageInfo {

    public TextPageInfo2() {
        this(null);
    }

    public TextPageInfo2(PageData pageData) {

        this.pageData = pageData;
    }

    public PageData pageData;


    @Override
    public boolean isFirstPage() {
        return pageData.isFirstPage;
    }

    @Override
    public boolean isLastPage() {
        return pageData.isLastPage;
    }


    @Override
    public long getStartPos() {
        return pageData.posStart;
    }

    @Override
    public long getEndPos() {
        return pageData.posEnd;
    }

    @Override
    public long getFileSize() {
        return pageData.fileSize;
    }
}
