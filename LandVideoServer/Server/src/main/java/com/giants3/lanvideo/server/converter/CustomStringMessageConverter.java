package com.giants3.lanvideo.server.converter;

import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;

/**
 * Created by davidleen29 on 2018/7/5.
 */
public class CustomStringMessageConverter extends StringHttpMessageConverter {

    public CustomStringMessageConverter() {
        super( Charset.forName("UTF-8"));
    }
}
