package com.giants3.android.reader.scheme;

public class TypefaceEntity {
    
    private String  url;
    private String filePath;
    private String title;
    private String name;
    private String thumb;
    private int index;
    private String language;
    private String typeface;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setIndex(int index) {

        this.index = index;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setTypeface(String typefaceFilePath) {
        this.typeface = typefaceFilePath;
    }

    public String getTypeface() {
        return typeface;
    }
}
