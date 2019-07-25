package com.giants3.android.reader;

import com.giants3.yourreader.text.TextChapter;
import com.giants3.yourreader.text.TextPageInfo;
import com.xxx.reader.book.ChapterMeasureResult;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.prepare.Cancelable;
import com.xxx.reader.prepare.PrepareJob;

public class BookPrepareJob implements PrepareJob<TextChapter, TextPageInfo, DrawParam> {
    @Override
    public ChapterMeasureResult<TextPageInfo> measureChapter(TextChapter iChapter, DrawParam drawParam, Cancelable cancelable, int pageType) {
        return null;
    }
}
