package com.giants3.android.reader.domain;

import java.io.InputStream;

/**
 * Created by davidleen29 on 2018/11/23.
 */

public interface ResultParser<T> {

    T parser(String result);
    T parser(InputStream inputStream);
}
