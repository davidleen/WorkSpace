package com.giants3.yourreader.text;

import com.giants3.android.frame.util.Utils;
import com.xxx.reader.text.page.PageData;
import com.xxx.reader.text.page.TextPageInfo2;
import com.xxx.reader.text.page.TypeParam;
import com.xxx.reader.text.page.Typing;
import com.xxx.reader.Url2FileMapper;
import com.xxx.reader.book.ChapterMeasureResult;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.prepare.Cancelable;
import com.xxx.reader.prepare.PrepareJob;
import com.xxx.reader.turnner.sim.SettingContent;

import java.io.IOException;

/**
 * Created by davidleen29 on 2019/1/1.
 */

public class TextPrepareJob2 implements PrepareJob<IChapter, TextPageInfo2, DrawParam> {

    private Url2FileMapper<IChapter> chapterUrl2FileMapper;

    public TextPrepareJob2(Url2FileMapper<IChapter> chapterUrl2FileMapper) {


        this.chapterUrl2FileMapper = chapterUrl2FileMapper;
    }

    @Override
    public ChapterMeasureResult<TextPageInfo2> measureChapter(IChapter iChapter, DrawParam drawParam, Cancelable cancelable, int pageType) {


        String path = chapterUrl2FileMapper.map(iChapter, iChapter.getUrl());

        return null;

    }




    @Override
    public TextPageInfo2 initPageInfo(IChapter iChapter, long fileOffset, DrawParam drawParam) throws IOException {


        TextPageInfo2 textPageInfo2 = generateNext(null, iChapter, drawParam);
        return textPageInfo2;
    }


    @Override
    public TextPageInfo2 generateNext(TextPageInfo2 currentPageInfo, IChapter iChapter, DrawParam drawParam) {



            PageData pageData = Typing.typePage(currentPageInfo==null?null:currentPageInfo.pageData, iChapter, 0, createTypePara(drawParam));
            TextPageInfo2 textPageInfo2=new TextPageInfo2();
            textPageInfo2.pageData=pageData;
            textPageInfo2.setChapterInfo(iChapter);
            return textPageInfo2;

    }

    @Override
    public TextPageInfo2 generatePrevious(TextPageInfo2 currentPageInfo, IChapter iChapter, DrawParam drawParam) {




            PageData pageData = Typing.typePrePage(currentPageInfo==null?null:currentPageInfo.pageData, iChapter,   createTypePara(drawParam));
            TextPageInfo2 textPageInfo2=new TextPageInfo2();
            textPageInfo2.pageData=pageData;
            textPageInfo2.setChapterInfo(iChapter);
            return textPageInfo2;



    }

    private TypeParam createTypePara(DrawParam drawParam)
    {
        TypeParam typeParam = new TypeParam();
        int[] screenWH = Utils.getScreenWH();
        typeParam.width =drawParam.width;
        typeParam.height = drawParam.height;
        typeParam.textSize = SettingContent.getInstance().getTextSize();
        typeParam.lineSpace = (int) SettingContent.getInstance().getLineSpace();
        typeParam.wordSpace = (int) SettingContent.getInstance().getWordSpace();
        typeParam.paragraphSpace = (int) SettingContent.getInstance().getParaSpace();
        typeParam.includeFontPadding = true;
        typeParam.padding = SettingContent.getInstance().getPaddings();

        return typeParam;
    }




}
