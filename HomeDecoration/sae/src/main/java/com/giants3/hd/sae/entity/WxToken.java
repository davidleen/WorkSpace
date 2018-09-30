package com.giants3.hd.sae.entity;

/**
 * Created by david on 2016/1/20.
 */
public class WxToken {


    public String access_token;
    public long expires_in;

    @Override
    public String toString() {
        return "WxToken{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                '}';
    }
}
