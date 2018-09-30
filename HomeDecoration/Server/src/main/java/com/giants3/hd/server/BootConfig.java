package com.giants3.hd.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by davidleen29 on 2018/9/16.
 */
@Component
public class BootConfig {
    @Value("${rootpath}")
    private String rootpath;



    public boolean isDebug()
    {



        return new File(rootpath+"DEBUG.json").exists();
    }






}
