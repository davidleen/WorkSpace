package com.giants3.hd.server.converter;

import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;

/**
 * Created by david on 2016/1/17.
 */
public class StringMessageConverter extends StringHttpMessageConverter
{

    public StringMessageConverter( ) {
        super(Charset.forName("UTF-8"));
    }
}
