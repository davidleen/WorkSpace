package com.giants3.reader.server.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Created by davidleen29 on 2017/3/11.
 */
public class SqlScriptHelper {


    private static final String SQL = "sql/";

    public  static String readScript(String filePath)
    {


        Resource resource = new ClassPathResource(SQL +filePath);
        try {


            String sql = de.greenrobot.common.io.FileUtils.readUtf8(resource.getFile());


             return  sql;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";


    }
}
