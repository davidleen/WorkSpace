package com.xxx.reader.comic;



import com.xxx.reader.book.AbstractChapter;
import com.xxx.reader.book.IChapter;

import java.util.List;

/**
 * Created by davidleen29 on 2017/8/25.
 */

public class ComicChapter  extends AbstractChapter {





    public List<ComicChapterItem> chapters;


    public ComicChapter(String id, String name, int index) {
        super("","",id,name,index);


    }






    @Override
    public boolean hasPay() {

       return false;


    }




}
