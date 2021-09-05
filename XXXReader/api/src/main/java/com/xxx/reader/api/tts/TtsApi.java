package com.xxx.reader.api.tts;


import android.content.Context;


import com.xxx.reader.api.Api;

import java.util.Locale;
import java.util.Map;

/**
 * 语音合成接口
 */
public interface TtsApi extends Api {


    void create(Context context, final InitListener listener);

    void stopSpeaking();

    void destroy();

    /**
     * @param content
     * @param listener
     * @return resultCode for Speak
     */
    int startSpeaking(String content, SpeakListener listener);

    void pauseSpeaking();

    void resumeSpeaking();

    /**
     * 设置语音朗读参数   发音人参数。
     *
     * @param speaker
     */
    void setSpeaker(String speaker);

    /**
     * 设置语速 取值范围 [0-100]
     *
     * @param speakSpeed
     */
    void setSpeakSpeed(String speakSpeed);

    /**
     * 设置语调   取值范围 [0-100]
     *
     * @param speakPitch
     */
    void setSpeakPitch(String speakPitch);

    void setOffLine(boolean offLine);

    /**
     * 获取发音人列表
     *
     * @param offLine 是否离线
     * @return 返回的结果  Key 是显示的发音人名称， value 是发音人参数。
     */
    Map<String, String> getSpeakers(boolean offLine);


    ComponentInfo getComponentInfo();

    boolean isServiceInstalled();

    void requestAddMoreSpeaker(Context context);


    boolean isSupportAddSpeaker(boolean offline);


    boolean isSupportLocal();

    class ComponentInfo {
        public String url;
        public String ttsName;

    }


    default void setLocal(Locale locale) {
    }

    ;


}
