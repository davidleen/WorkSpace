package com.xxx.reader.api.tts;

/**
 * 语音合成异常处理。
 */
public class TtsError {


    public static  final int CODE_SUCCESS=0;
    public static  final int CODE_ERROR_COMPONENT_NOT_INSTALLED =1;
    public static  final int CODE_ERROR_ENGINE_CALL_FAIL=2;
    //机型不支持。
    public static  final int CODE_ERROR_NOT_SUPPORT=99;

    public  final int errorCode;
    public final String errorMsg;

    public TtsError(int errorCode, String errorMsg) {

        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


}
