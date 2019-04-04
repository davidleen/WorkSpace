package com.rnmap_wb.analytics;

import android.content.Context;


import com.rnmap_wb.MainApplication;
import com.rnmap_wb.android.api.analytics.AnalysisApi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 分析工具包装类
 * Created by davidleen29 on 2016/11/3.
 */

public class AnalysisFactory implements AnalysisApi {


    public static final String UMENG_IMPL_CLASS_NAME = "com.giants3.android.analysis.UmengAnalysisImpl";

    private static AnalysisApi analysis;


    public static AnalysisApi getInstance() {

        if (analysis == null) {

            try {
                Class<?> aClass = Class.forName(UMENG_IMPL_CLASS_NAME);
                Constructor<?> constructor = aClass.getConstructor(Context.class);
                analysis = (AnalysisApi) constructor.newInstance(MainApplication.baseContext);


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            if (analysis == null)
                analysis = new AnalysisFactory();
        }
        return analysis;
    }

    @Override
    public void onResume(Context context) {

    }

    @Override
    public void onPause(Context context) {

    }
}
