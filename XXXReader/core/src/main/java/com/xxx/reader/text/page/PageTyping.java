package com.xxx.reader.text.page;

import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.PageInfo;

public interface PageTyping<P extends PageInfo> {
    P  typePage(IChapter iChapter,long startPos ,TypeParam typeParam);
    P  typePageNext(IChapter iChapter,   TypeParam typeParam, P lastPage);
    P  typePagePre(IChapter iChapter,   TypeParam typeParam, P lastPage);
}
