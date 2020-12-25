package com.xxx.reader.prepare;

import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IDrawable;
import com.xxx.reader.core.PageBitmap;
import com.xxx.reader.core.PageInfo;

public class PagePlayerBuilder<C extends IChapter, P extends PageInfo, DP extends DrawParam, PB extends PageBitmap> {
    private PrepareJob<C, P, DP> prepareJob;
    private IDrawable iDrawable;
    private PageBitmapCreator<PB> creator;

    public PagePlayerBuilder setPrepareJob(PrepareJob<C, P, DP> prepareJob) {
        this.prepareJob = prepareJob;
        return this;
    }

    public PagePlayerBuilder setiDrawable(IDrawable iDrawable) {
        this.iDrawable = iDrawable;
        return this;
    }

    public PagePlayerBuilder setCreator(PageBitmapCreator<PB> creator) {
        this.creator = creator;
        return this;
    }

    public PagePlayer<C , P , DP , PB > createPrepareLayer() {
        return new PagePlayer( iDrawable, creator);
    }
}