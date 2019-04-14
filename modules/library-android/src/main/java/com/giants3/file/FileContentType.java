package com.giants3.file;


import android.provider.MediaStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileContentType {



    public static final String EPUB = "application/epub+zip";

    public static  String getContentType(File file)
    {
        String contentType = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Path source = Paths.get(file.getPath());
            try {
                contentType = Files.probeContentType(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         if(contentType==null||"".equalsIgnoreCase(contentType)) {

            int lastIndexOfDot = file.getPath().lastIndexOf(".");
            String fileExtend = lastIndexOfDot < 0 ? "" : file.getPath().substring(lastIndexOfDot);
            contentType=contentTypeMap.get(fileExtend);
        }


        return contentType;
    }

    private static Map<String,String > contentTypeMap=new HashMap<>();
    static
    {

        contentTypeMap.put(".epub",EPUB);


    }
}
