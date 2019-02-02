package com.giants3.yourreader.text;

import com.xxx.reader.Url2FileMapper;
import com.xxx.reader.book.ChapterMeasureResult;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.prepare.Cancelable;
import com.xxx.reader.prepare.PrepareJob;

/**
 * Created by davidleen29 on 2019/1/1.
 */

public class TextPrepareJob implements PrepareJob<TextChapter,TextPageInfo,DrawParam> {

    public TextPrepareJob(Url2FileMapper<TextChapter> chapterUrl2FileMapper)
    {}
    @Override
    public ChapterMeasureResult<TextPageInfo> measureChapter(TextChapter iChapter, DrawParam drawParam, Cancelable cancelable, int pageType) {
        return null;
    }
}
