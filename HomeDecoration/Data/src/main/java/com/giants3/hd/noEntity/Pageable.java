package com.giants3.hd.noEntity;

public interface Pageable {


    int getPageIndex();
    int getPageSize();
    int getTotalCount();
    int getPageNum();
}
