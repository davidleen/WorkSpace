package com.giants3.android.reader;

import com.giants3.yourreader.text.TextChapter;
import com.giants3.yourreader.text.TextPageInfo;
import com.xxx.reader.book.ChapterMeasureResult;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.prepare.Cancelable;
import com.xxx.reader.prepare.PrepareJob;

import java.io.IOException;

public class BookPrepareJob implements PrepareJob<TextChapter, TextPageInfo, DrawParam> {
    @Override
    public ChapterMeasureResult<TextPageInfo> measureChapter(TextChapter iChapter, DrawParam drawParam, Cancelable cancelable, int pageType) {
        return null;
    }

    @Override
    public TextPageInfo generateNext(TextPageInfo currentPageInfo, IChapter iChapter, DrawParam drawParam) {
        return null;
    }

    @Override
    public TextPageInfo generatePrevious(TextPageInfo currentPageInfo, IChapter iChapter, DrawParam drawParam) {
        return null;
    }

    @Override
    public TextPageInfo initPageInfo(IChapter chapter, float progress, DrawParam drawParam) throws IOException {
        return null;
    }
}
