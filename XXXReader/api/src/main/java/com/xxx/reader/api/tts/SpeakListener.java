package com.xxx.reader.api.tts;

public interface SpeakListener {



    void onSpeakBegin();

    void onBufferProgress(final int progress, final int beginPos, final int endPos,
                          final String info);

    void onSpeakPaused();

    void onSpeakResumed();

    void onSpeakProgress(  int progress,   int beginPos,   int endPos);

    void onSpeakCompleted( );

    void onSpeakError(TtsError error);

}
