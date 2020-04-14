package com.xxx.reader.book;


/**
 *
 * @param <T>
 */
public interface BookFactory<T extends IBook> {

    T  create(String path);
}
