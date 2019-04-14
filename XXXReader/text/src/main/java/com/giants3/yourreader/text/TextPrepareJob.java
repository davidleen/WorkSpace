package com.giants3.yourreader.text;

import com.xxx.reader.Url2FileMapper;
import com.xxx.reader.book.ChapterMeasureResult;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.prepare.Cancelable;
import com.xxx.reader.prepare.PrepareJob;

/**
 * Created by davidleen29 on 2019/1/1.
 */

public class TextPrepareJob implements PrepareJob<IChapter,TextPageInfo,DrawParam> {

    private Url2FileMapper<IChapter> chapterUrl2FileMapper;

    public TextPrepareJob(Url2FileMapper<IChapter> chapterUrl2FileMapper)
    {


        this.chapterUrl2FileMapper = chapterUrl2FileMapper;
    }
    @Override
    public ChapterMeasureResult<TextPageInfo> measureChapter(IChapter iChapter, DrawParam drawParam, Cancelable cancelable, int pageType) {


        String path = chapterUrl2FileMapper.map(iChapter.getFilePath());






//        ChapterMeasureResult<TextPageInfo> chapterMeasureResult=new ChapterMeasureResult<>();
//        chapterMeasureResult.pageCount=1;
//        chapterMeasureResult.fileSize=









        return null;
    }
}
