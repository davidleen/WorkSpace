package com.giants3.reader.book;

import com.xxx.reader.book.AbstractBook;
import com.xxx.reader.book.IBook;

import java.util.ArrayList;
import java.util.List;

/**
 * EPUB:
 * 简单 EPUB 档案的目录和文件结构
 * mimetype  必须命名为 mimetype
 * META-INF/
 * container.xml EPUB 根目录下必须包含 META-INF 目录，而且其中要有一个文件 container.xml。EPUB 阅读系统首先查看该文件，
 * 它指向数字图书元数据的位置。
 * OEBPS/
 * content.opf        它指定了图书中所有 内容的位置，如文本和图像等其他媒体。它还给出了另一个元数据文件，
 * 内容的 Navigation Center eXtended (NCX) 表。 opf文件中guide中包含目录信息
 * title.html
 * content.html
 * stylesheet.css
 * toc.ncx
 * images/
 * cover.png
 *
 * @author Chenli
 */
public class EpubBook extends AbstractBook<EpubChapter> {


    /**
     * 书籍id ，自家的epub书籍带有bookId， 普通而普遍书籍没有。
     */
    private String bookId;
    private String title;
    //作者
    private String creator;
    private String subjects = "";
    private String contributors = "";
    private String publisher;
    private String language;
    private String description;
    private String rights;
    private String coverPath = null;
    private List<EpubChapter> chapters;
    private String path;
    private String opfPath;
    private String ncxPath;

    /**
     * 判断当前epub书籍是否为预览版
     */
    private boolean preview = false;


    EpubBook(String name) {
        path = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects += subjects;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors += contributors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public String getFilePath() {
        return path;
    }

    public List<EpubChapter> getChapters() {
        return chapters;
    }

    @Override
    public EpubChapter getChapter(int index) {
        return chapters.get(index);
    }

    @Override
    public int getChapterCount() {
        return chapters.size();
    }

    /**
     * 过时,该方法由EpubParse.getEpubChapterTitle()替代
     *
     * @return
     * @deprecated
     */
    public ArrayList<String> getChaptersTitle() {
        ArrayList<String> arrays = new ArrayList<String>();
        for (int i = 0; i < chapters.size(); i++) {
            arrays.add(chapters.get(i).getText());
        }
        return arrays;
    }

    /**
     * 过时,该方法由EpubParse.getEpubChapterPath()替代
     *
     * @return
     * @deprecated
     */
    public ArrayList<String> getChapterSrc() {
        ArrayList<String> arrays = new ArrayList<String>();
        for (int i = 0; i < chapters.size(); i++) {
            arrays.add(chapters.get(i).getSrc());
        }
        return arrays;
    }

    public void setChapters(List<EpubChapter> chapters) {
        if (chapters != null && chapters.size() > 0) {
            this.chapters = chapters;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOpfPath() {
        return opfPath;
    }

    public void setOpfPath(String opfPath) {
        this.opfPath = opfPath;
    }

    public String getNcxPath() {
        return ncxPath;
    }

    public void setNcxPath(String ncxPath) {
        this.ncxPath = ncxPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public void setIsPreview(boolean preview) {
        this.preview = preview;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setBookId(String id) {
        bookId = id;
    }


    public String getBookId() {
        return bookId;
    }


}
