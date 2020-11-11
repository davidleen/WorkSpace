package com.giants3.reader.book;

import com.xxx.reader.book.AbstractBook;
import com.xxx.reader.book.IBook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;


public class EpubBook extends AbstractBook<EpubChapter> {



    private List<EpubChapter> chapters ;


    private Book book;


    public EpubBook(String path   ) {
        // read epub
        EpubReader epubReader = new EpubReader();
        Book book = null;
        try {
            book = epubReader.readEpubLazy(path,"GBK");
            List<SpineReference> spineReferences = book.getSpine().getSpineReferences();

            int size= spineReferences.size();
            chapters=new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                chapters.add(new EpubChapter(book,spineReferences.get(i),i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.book=book;
    }



    @Override
    public String getName() {
        return book.getTitle();
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public String getFilePath() {
        return "";
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
        return chapters==null?0:chapters.size();
    }







}
