package com.xxx.reader.text.page;

import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.PageInfo;

public class TextPageInfo2Typing implements PageTyping<TextPageInfo2>{

    private TextReaderManager textReaderManager;

    public TextPageInfo2Typing(TextReaderManager textReaderManager)
    {
        this.textReaderManager = textReaderManager;
    }
    @Override
    public TextPageInfo2 typePage(IChapter iChapter, long startPos, TypeParam typeParam) {

        PageData pageData = Typing.typePageNext( iChapter, startPos, typeParam, null,textReaderManager);
        if(pageData!=null)
            return new TextPageInfo2(pageData);
        return null;

    }

    @Override
    public TextPageInfo2 typePageNext(IChapter iChapter, TypeParam typeParam, TextPageInfo2 lastPage) {


        PageData pageData = Typing.typePageNext(iChapter, 0, typeParam, lastPage==null?null:lastPage.pageData,textReaderManager);
        if(pageData!=null)
            return new TextPageInfo2(pageData);
        return null;
    }

    @Override
    public TextPageInfo2 typePagePre(IChapter iChapter, TypeParam typeParam, TextPageInfo2 lastPage) {
        if(lastPage==null) return  null;
        PageData pageData =typePrePageImpl(lastPage.pageData,iChapter,typeParam);
        if(pageData!=null)
            return new TextPageInfo2(pageData);
        return null;
    }




    private PageData typePrePageImpl(PageData lastPage,IChapter iChapter,TypeParam typeParam)
    {
     return    Typing.typePrePage( lastPage,iChapter,   typeParam,textReaderManager);
    }



}
