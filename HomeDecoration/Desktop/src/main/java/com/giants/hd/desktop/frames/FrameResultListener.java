package com.giants.hd.desktop.frames;

/**
 * Created by davidleen29 on 2018/8/1.
 */
public interface FrameResultListener {

    int RESULT_OK=1;

     void onFrameResult(int requestCode, int resultCode,Object o);
}
