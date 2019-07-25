package com.xxx.reader.book;

import java.util.ArrayList;
import java.util.List;

public class AbstractBook<T extends IChapter> implements IBook<T> {


    List<T> chapters;

    public AbstractBook() {
        chapters = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "AbstractBook";
    }

    @Override
    public String getUrl() {
        return "AbstractBook";
    }

    @Override
    public String getFilePath() {
        return "AbstractBook";
    }

    @Override
    public List<T> getChapters() {
        return chapters;
    }

    @Override
    public T getChapter(int index) {
        return chapters.get(index);
    }

    @Override
    public int getChapterCount() {
        return chapters.size();
    }

    @Override
    public String getBookId() {
        return "";
    }

    @Override
    public void addChapter(T chapter) {
        chapters.add(chapter);
    }

    @Override
    public void addChapter(int index, T chapter) {
        chapters.add(index, chapter);
    }
}
