package com.giants3.yourreader.text;

import com.xxx.reader.api.tts.InitListener;
import com.xxx.reader.api.tts.SpeakListener;
import com.xxx.reader.api.tts.TtsApi;
import com.xxx.reader.api.tts.TtsError;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;


import java.util.List;


public class BookPlayer {

    public static final int PLAY_STATE_STOP = 0;

    public static final int PLAY_STATE_ERROR = PLAY_STATE_STOP + 1;
    public static final int PLAY_STATE_PLAYING = PLAY_STATE_ERROR + 1;
    public static final int PLAY_STATE_PAUSED = PLAY_STATE_PLAYING + 1;
    public static final int PLAY_STATE_QUIT = PLAY_STATE_PAUSED + 1;
    public static final int PLAY_STATE_REAL_PREPARING = PLAY_STATE_QUIT + 1;
    protected PlayData playData;
    private static final String TAG = "TtsBookPlayer";
    private int speed;


    private TtsApi mTts;
    private Context context;
    private boolean isinit;
    private int retryCount;
    private Handler handler;
    /**
     * 当前播放状态
     */
    private int playState;
    private String speaker;
    private TtsInfoUpdateListener ttsInfoUpdateListener;
    private SpeakListener speakListener;


    public BookPlayer(Context context) {


        this.context = context;
        handler=new Handler(Looper.getMainLooper());
    }

    public void initTTS() {
        if (mTts != null) {
            mTts.create(context, mTtsInitListener);
        }
    }




    public void setSpeed(int speed) {

        this.speed = speed;
    }


    public int getSpeed() {
        return speed;
    }





    public void onDestroy(boolean isClose) {
        releaseTts();



    }


    public void beginPlay() {



        playerWork();

    }

    private void releaseTts() {
        // 退出时释放连接
        if (mTts != null) {
            mTts.stopSpeaking();
            mTts.destroy();
            mTts = null;
        }
    }

    /**
     * 初期化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {

        @Override
        public void onError(TtsError ttsError) {



        }

        @Override
        public void onSuccess() {

        }
    };

    private void playerWork() {

        if (!isinit) return;
        if (mTts != null) {

            if (playData == null || TextUtils.isEmpty(playData.content.toString())) return;
            playState = PLAY_STATE_PLAYING;




            mTts.setOffLine(false);
            mTts.setSpeaker(speaker);
            /** 设置语速 **/
            mTts.setSpeakSpeed(getSpeed() + "");

            /** 设置音调 **/
            mTts.setSpeakPitch("50");

            int code = TtsError.CODE_ERROR_ENGINE_CALL_FAIL;
            try {
                code = mTts.startSpeaking(playData.getContent(), mTtsListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (code != TtsError.CODE_SUCCESS) {
                String errorMsg = "start speak error : " + code;
                Log.e(TAG, errorMsg);

                if (retryCount < 2) {


                    try {
                        if (mTts != null) {
                            mTts.destroy();
                        }
                        initTTS();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    retryCount++;
                } else {

                   speakListener.onSpeakError(new TtsError(TtsError.CODE_ERROR_ENGINE_CALL_FAIL,""));
                }
            } else {
                retryCount = 0;
            }
        }
    }
    public void setDataAndPlay(PlayData play)
    {

    }
    /**
     * 所有回调方法均是在子线程中执行的，使用时需小心
     **/
    private SpeakListener mTtsListener = new SpeakListener() {

        @Override
        public void onBufferProgress(final int progress, final int beginPos, final int endPos,
                                     final String info) {

        }

        @Override
        public void onSpeakCompleted() {

        }

        @Override
        public void onSpeakError(TtsError error) {


            setPlayState(PLAY_STATE_STOP);
            /** 朗读结束 **/
            if (error != null /*&& error.getErrorCode() == ErrorCode.SUCCESS || error.getErrorCode() == 10108*/) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (speakListener != null) {
                            try { //异步: 友盟日志有空指针
                                speakListener.onSpeakCompleted();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            /** 暂不作支持 **/
//			if (code == ErrorCode.ERROR_LOCAL_RESOURCE) {// 无本地发音人资源，请到语音+中下载发音人！
//				runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						Intent intent = new Intent();
//						intent.setAction("com.iflytek.speechcloud.activity.speaker.SpeakerSetting");
//						startActivity(intent);
//					}
//				});
//			}
        }

        @Override
        public void onSpeakBegin() {


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (speakListener != null) {
                        speakListener.onSpeakBegin();
                    }
                }
            });
        }

        @Override
        public void onSpeakPaused() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (speakListener != null) {
                        speakListener.onSpeakPaused();
                    }
                }
            });
        }

        @Override
        public void onSpeakProgress(final int progress, final int beginPos, final int endPos) {
            /** progress 最大只会跑到 99 **/
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (speakListener != null) {
                        speakListener.onSpeakProgress(progress, beginPos, endPos);
                    }
                }
            });
        }

        @Override
        public void onSpeakResumed() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (speakListener != null) {
                        speakListener.onSpeakResumed();
                    }
                }
            });
        }


    };

    private void setPlayState(int newState) {
        this.playState=newState;
    }


    public void pausePlay() {

        playState = PLAY_STATE_PAUSED;

        if (mTts != null) {
            mTts.pauseSpeaking();
        }
    }


    public void resumePlay() {


        playState = PLAY_STATE_PLAYING;

        if (mTts != null) {
            mTts.resumeSpeaking();
        }
    }

    public void cancelPlay() {


        playState = PLAY_STATE_STOP;
        if (mTts != null) {
            mTts.stopSpeaking();
        }

        if (playData != null) {
            playData.reset();
        }
    }

    /**
     * 获取发言人列表
     */
    public void getSpeakerList() {


    }



    public void setSpeaker(String speaker)
    {

        this.speaker = speaker;
    }






    public void setTtsApi(TtsApi ttsApi) {

        releaseTts();
        this.mTts = ttsApi;
        mTts.create(context, mTtsInitListener);

    }


    public void setTtsInfoUpdateListener(TtsInfoUpdateListener ttsInfoUpdateListener) {

        this.ttsInfoUpdateListener = ttsInfoUpdateListener;
    }

    public TtsApi getTtsApi() {
        return mTts;
    }


    /**
     * 更新语音参数
     */
    public void requestInfoUpdate() {


        if (ttsInfoUpdateListener != null) {
            ttsInfoUpdateListener.onNewTtsInfo(new TtsInfo());
        }


    }


    interface TtsInfoUpdateListener {
        void onNewTtsInfo(TtsInfo ttsInfo);
    }


    public class TtsInfo {
        boolean supportSpeakers;
        public List<TtsSpeaker> Speakers;
        boolean supportAdMoreSpeaker;
        boolean supportOnline = false;


    }

    public class TtsSpeaker {
        public String key;
        public String name;
    }


    public void runOnUiThread(Runnable runnable)
    {
        handler.post(runnable);
    }

    public void setPlayListener(SpeakListener speakListener)
    {


        this.speakListener = speakListener;
    }

}
