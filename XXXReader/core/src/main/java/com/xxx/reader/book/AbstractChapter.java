package com.xxx.reader.book;

/**
 * Created by davidleen29 on 2019/1/1.
 */

public class AbstractChapter  implements  IChapter {



    /**
     * 本地文件路径 漫画以文件夹形式。
     */
    public String filePath;
    public String url;



    public AbstractChapter(String url, String filePath, String id, String name, int index) {
        this.filePath = filePath;
        this.url = url;
        this.id = id;
        this.name = name;
        this.index = index;
    }

    public String id;
    public String name;

    public int index;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public boolean hasPay() {
        return false;
    }
}
