package com.giants3.android.storage;

import android.app.Application;

import com.giants3.android.frame.util.Log;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;


public class KVFactory {
    public static final String SETTING_SHARED_FILE_NAME = "setting";

    private static Application application;

    public static void init(Application application) {
        KVFactory.application = application;
        String rootPath = MMKV.initialize(application);
        Log.e("rootPath:" + rootPath);


    }


    static Map<String, KV> keyMaps = new HashMap<>();

    public static KV getDefault() {


        /**
         * 默认 就是{@link SETTING_SHARED_FILE_NAME} 文件。
         * @return
         */

        return getInstance(SETTING_SHARED_FILE_NAME);
    }


    public static KV getInstance(String fileName) {


        KV result = keyMaps.get(fileName);
        if (result == null) {
            //  long time = System.currentTimeMillis();
            result = new MKVImpl(application, fileName);
            keyMaps.put(fileName, result);
            //   Log.e("Time use in  Changdu oncreate:" + (System.currentTimeMillis() - time));
        }


        return result;


    }

}
