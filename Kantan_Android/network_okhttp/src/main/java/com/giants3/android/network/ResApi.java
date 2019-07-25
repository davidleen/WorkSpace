package com.giants3.android.network;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by davidleen29 on 2018/11/23.
 */

public interface ResApi {


    byte[] post(String url, byte[] data) throws IOException;

    byte[] put(String url, byte[] data) throws IOException;

    String postBytes(String url, byte[] data) throws IOException;

    String uploadFile(String serverURL, File[] files) throws IOException;
    String uploadFile(String serverURL, String fieldName,File ile) throws IOException;

    String postJson(String url, String data) throws IOException;

    String post(String url, String data) throws IOException;

    byte[] get(String url) throws IOException;

    InputStream openStream(String url) throws IOException;

    String getString(String url) throws IOException;

    boolean download(String url, String filePath) throws IOException;

    boolean download(String url, String filePath, com.giants3.android.network.ProgressListener listener) throws IOException;

    void setHeader(String key, String value);
}
