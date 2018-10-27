package com.giants3.hd.domain.api;



import java.io.IOException;
import java.io.InputStream;

/**
 * Created by davidleen29 on 2017/7/23.
 */

public class ApiManager {


    Client client;
    public ApiManager()
    {
        client= new Client();

    }



    public  boolean downloadUrlToFilePath(String url, String filePath)
    {
        boolean result=false;

        InputStream inputStream = null;
        try {
            inputStream = client.openInputStream(url);
        } catch ( Exception e) {
            e.printStackTrace();
        }

        if(inputStream!=null)
        {
            try {
                FileUtils.copy(inputStream,filePath);
                result=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;


    }


    public void close()
    {
        try {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String url) {

        try {
            return  client.getWithStringReturned(url);
        } catch ( Throwable  e) {
            e.printStackTrace();
        }
        return "";

    }
}
