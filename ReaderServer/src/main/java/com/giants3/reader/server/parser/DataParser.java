package com.giants3.reader.server.parser;


/**
 * Created by david on 2016/1/2.
 */

public interface DataParser<T,K> {

    public K parse(T data);
}
