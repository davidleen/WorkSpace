package com.giants3.reader.book;


import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.StringUtil;
import com.xxx.reader.book.IChapter;

import java.io.File;
import java.io.IOException;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.SpineReference;

/**
 * Epub章节数据
 *
 * @author davidleen29
 */
public class EpubChapter implements IChapter {
    /**
     * chapterTitle 章节名
     */
    private String text;
    /**
     * chapterTitle 章节id
     */
//	private String id;
    /**
     * chapterTitle 章节地址 相对路径
     */
    private String src;

    /**
     * chapterTitle 章节地址 绝对路径
     */
    private String srcFilePath;



    private int pri = 0;

    private boolean mHasChild = false;

    private boolean expanded = false;
    private String path;
    private Book book;
    private SpineReference resource;
    private int index;

    public void setHasChild(boolean hasChild) {
        this.mHasChild = hasChild;
    }

    public boolean hasChild() {
        return mHasChild;
    }

    public void setExpanded(boolean isExpanded) {
        this.expanded = isExpanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getSrc() {
        return src;
    }

    // 部分epub书籍章节节点 为  xxx.htm#xxxx
    public void setSrc(String src) {
        int index1 = src.lastIndexOf('.');
        if (index1 != -1) {
            int index2 = src.indexOf("#", index1);
            if (index2 != -1) {
                this.src = src.substring(0, index2);
                return;
            }
        }
        this.src = src;
    }

    public int getPri() {
        return pri;
    }

    public void setPri(int pri) {
        this.pri = pri;
    }


    EpubChapter(Book book, SpineReference resource,int index) {
        this.book = book;
        this.resource = resource;
        this.index = index;
    }


    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getName() {
        String title = resource.getResource().getTitle();
        if(StringUtil.isEmpty(title))
        {
            title=resource.getResource().getHref();
        }
        return title;
    }

    @Override
    public String getUrl() {
        return resource.getResource().getHref();
    }

    @Override
    public String getFilePath() {

        String path = book.getTitle() + File.separator + resource.getResource().getHref();
        return  StorageUtils.getFilePath(path);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public boolean hasPay() {
        return false;
    }

    public void setBookPath(String path) {
        this.path = path;
    }

    public String getBookPath()
    {
        return path;
    }

    public byte[] getData() throws IOException {
        return resource.getResource().getData();
    }
}
