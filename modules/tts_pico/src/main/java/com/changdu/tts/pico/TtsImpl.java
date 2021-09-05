package com.changdu.tts.pico;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xxx.reader.api.tts.InitListener;
import com.xxx.reader.api.tts.SpeakListener;
import com.xxx.reader.api.tts.TtsApi;
import com.xxx.reader.api.tts.TtsError;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * pico语音合成实现类
 */
public class TtsImpl implements TtsApi {
    private static final String TAG = "PICO_TTS_IMPL";
    private TextToSpeech tts;
    private String content;
    private SpeakListener ttsListener;

    private Handler handler;
    private Locale locale;

    public TtsImpl(Context context) {

        handler = new Handler(Looper.getMainLooper());


    }




    private Runnable onSpeakProgress;

    @Override
    public void create(final Context context, final InitListener listener) {
//        Intent  intent = new Intent();
//        intent.setAction("com.android.settings.TTS_SETTINGS");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

//
        initTts(context,null,listener,false);


    }

    @Override
    public void setLocal(Locale locale) {
        this.locale=locale;
    }

    private void initTts(final Context context, String engine, final InitListener listener, final boolean hasRetry)
    {


        destroyTts(); 
        tts = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int languageAvailable = tts.setLanguage(locale);
                    if (languageAvailable == TextToSpeech.LANG_MISSING_DATA ||languageAvailable==TextToSpeech.LANG_NOT_SUPPORTED ){


                        Intent installIntent = new Intent();
                        installIntent.setAction(
                                TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(installIntent);
                        if (listener != null) {
                            listener.onError(new TtsError(TtsError.CODE_ERROR_COMPONENT_NOT_INSTALLED,context.getResources().getString(R.string.tip_tts_data_miss)));
                        }






                    }else {
                        List<TextToSpeech.EngineInfo> engines = tts.getEngines();
                        if(  languageAvailable == TextToSpeech.LANG_NOT_SUPPORTED){

                            String newEngine=null;
                            if(!hasRetry&&engines.size()>1)
                            {
                                for (TextToSpeech.EngineInfo engineInfo:engines)
                                {
                                    if(!engineInfo.name.equals(tts.getDefaultEngine()))
                                    {

                                        newEngine=engineInfo.name;
                                        break;
                                    }
                                }



                            }
                            if(newEngine==null) {
                                if (listener != null)
                                    listener.onError(new TtsError(TtsError.CODE_ERROR_NOT_SUPPORT,context.getResources().getString(R.string.no_feature)));
                            }else
                            {
                                initTts(context,newEngine,listener,true);
                            }

                        }else
                        {


//
//
//                            List<TextToSpeech.EngineInfo> engines = engines1;
//                            // int i = tts.setEngineByPackageName("com.iflytek.vflynote");

                            initListeners();


//                            //记录当前使用的语音引擎
//                            context.getSharedPreferences("setting",Context.MODE_PRIVATE).edit().putString("last_working_tts_engine",tts.getDefaultEngine()).apply();

                            listener.onSuccess( );
                        }
                    }


                } else {



                    if (listener != null)
                        listener.onError(new TtsError(TtsError.CODE_ERROR_NOT_SUPPORT,context.getResources().getString(R.string.no_feature)));


                }


            }

        },engine );
    }


    private void initListeners()
    {

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
                super.onRangeStart(utteranceId, start, end, frame);

            }

            @Override
            public void onBeginSynthesis(String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) {
                super.onBeginSynthesis(utteranceId, sampleRateInHz, audioFormat, channelCount);

            }

            @Override
            public void onAudioAvailable(String utteranceId, byte[] audio) {
                super.onAudioAvailable(utteranceId, audio);
            }

            @Override
            public void onStart(final String utteranceId) {

                String[] split = utteranceId.split("_");
                if (split.length > 2) {
                    int start = Integer.valueOf(split[0]);
                    int end = Integer.valueOf(split[1]);
                    int length = Integer.valueOf(split[2]);

                    handleSpeakProgress(utteranceId,start ,end,length);
                }


            }

            @Override
            public void onDone(final String utteranceId) {


                boolean lastSentence =false;
                String[] split = utteranceId.split("_");
                if (split.length > 2) {
                    int start = Integer.valueOf(split[0]);
                    int end = Integer.valueOf(split[1]);
                    int length = Integer.valueOf(split[2]);
                    if(end+1>=length)
                    {
                        lastSentence=true;
                    }else
                    {

                        sentenceIndex=end+1;
                        //handleSpeakProgress(utteranceId,end+1,end+1,length);

                    }


                }


                if(lastSentence) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ttsListener.onSpeakCompleted();
                        }
                    });
                }
                else{


                }


            }

            @Override
            public void onError(final String utteranceId) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ttsListener.onSpeakError(new TtsError(TtsError.CODE_ERROR_ENGINE_CALL_FAIL, utteranceId));
                    }
                });

            }
        });
    }

    private void handleSpeakProgress(final String utteranceId, final int start, final int end, final int length) {

        Runnable      onSpeakProgress = new Runnable() {
            @Override
            public void run() {

                int progress = (start+end)/2 * 100 / length;
//                Log.e("utteranceId:" + utteranceId + ",progress:" + progress + ",start:" + start + ",end:" + end + ",content.length():" + content.length());


                ttsListener.onSpeakProgress(progress, start, end);


            }
        };
        if(Looper.getMainLooper()==Looper.myLooper())
        {
            onSpeakProgress.run();

        }else
        {
            handler.post(onSpeakProgress);
        }


    }

    @Override
    public void setSpeaker(String speaker) {

        //本地引擎的voice值都是不可靠，没有意义。引导用户去本地引擎设置里面设置。
//        //do nothing
//        Set<Voice> voices = tts.getVoices();
//        for (Voice voice: voices)
//       {
//           if(voice.getName().equals(speaker))
//           {
//               int code = tts.setVoice(voice);
//               Log.e("code:"+code);
//               if(code==TextToSpeech.SUCCESS)
//               {
//
//
//               }
//               break;
//           }
//       }

    }

    /**
     * speed 取值   1.0表示正常速度 对应 50
     * @param speakSpeed
     */
    @Override
    public void setSpeakSpeed(String speakSpeed) {

        //本地引擎的速度 由系统控制设置， 应用中无设置意义

//        try {
//            Float speechRate = Float.valueOf(speakSpeed)/50;
//            tts.setSpeechRate(speechRate);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }

    }

    /**
     * picch 取值   1.0表示正常状态 对应 50
     * @param speakPitch
     */
    @Override
    public void setSpeakPitch(String speakPitch) {

        //本地引擎的语调 由系统控制设置， 应用中无设置意义
//        Float pitch = Float.valueOf(speakPitch)/50;
//        tts.setPitch(pitch);


    }

    @Override
    public void setOffLine(boolean offLine) {


    }



    @Override
    public Map<String, String> getSpeakers(boolean offLine) {

        //本地引擎的voice值都是不可靠，没有意义。引导用户去本地引擎设置里面设置。
        return null;
//        Set<Voice> voices = tts.getVoices();
//        Map<String,String> map=new HashMap<>();
//        for (Voice voice:voices)
//        {
//         map.put(    voice.getName()+"name",voice.getName());
//        }
//        return map;

    }

    @Override
    public ComponentInfo getComponentInfo() {
        return null;
    }

    @Override
    public boolean isServiceInstalled() {
        return true;
    }

    @Override
    public void requestAddMoreSpeaker(Context context) {

    }

    @Override
    public boolean isSupportAddSpeaker(boolean offline) {
        return false;
    }

    @Override
    public boolean isSupportLocal() {
        return true;
    }

    @Override
    public void stopSpeaking() {
        if (tts != null) {
            tts.stop();
        }

    }

    @Override
    public void destroy() {
        stopSpeaking();
        destroyTts();

    }

    private void destroyTts()
    {
        if(tts!=null) {
            try {
                tts.shutdown();
                tts.setOnUtteranceProgressListener(null);
                tts = null;
            } catch (Throwable t) {
            }
        }
    }


    private int sentenceIndex;

    /**
     * 断句功能，返回指定位置所在句子的起始和结尾
     *
     * @param index       index[0]句子开始 index[1] 句子结束
     * @param commaEnable commaEnable 时候允许用 逗号 断句
     * @return
     */

    public static int[] getSentence(int index, String data, boolean commaEnable) {
        int[] sentence = new int[]{0, 0};

        int begin = 0;
        for (; begin < data.length(); begin++) {
            if (data.charAt(begin) != '\ufeff' && data.charAt(begin) != ' ' && data.charAt(begin) != '　') {
                break;
            }
        }

        if (data.length() <= 14) {
            sentence[1] = data.length() - 1;
            sentence[0] = begin;
            if (sentence[0] == -1) {
                sentence[0] = 0;
            }
        } else {
            sentence[0] = begin;
            int i = 0;
            // 特殊字符串排列有要求，同样的位置，排在后面的字符串有更高优先级
            String[] check;
            if (!commaEnable) {
                check = new String[]{". ", "。", "。\"", "。”", "!", "!\"", "!”", "！", "！\"", "！”", "?", "?\"", "?”", "？", "？”", "？\"", ";", "；", "..."};
            } else {
                check = new String[]{", ", "，", "， ", "\n", "\r", ". ", "。", "。\"", "。”", "!", "!\"", "!”", "！", "！\"", "！”", "?", "?\"", "?”", "？", "？”", "？\"", ";", "；", "..."};
            }

            for (int j = 0; index > 1 && j < check.length; j++) {
                i = data.lastIndexOf(check[j], index - 1);
                if (i != -1 && i + check[j].length() >= sentence[0]) {
                    sentence[0] = i + check[j].length();
                }
            }

            sentence[1] = data.length() - 1;
            for (int j = 0; j < check.length; j++) {
                i = data.indexOf(check[j], index);
                if (i != -1 && i <= sentence[1]) {
                    sentence[1] = i + check[j].length() - 1;
                }
            }
        }

        while (sentence[1] > 0) {
            if (data.charAt(sentence[1]) == '\r' || data.charAt(sentence[1]) == '\n') {
                sentence[1] -= 1;
            } else {
                break;
            }
        }

        return sentence;
    }

    @Override
    public int startSpeaking(String content, final SpeakListener listener) {
        this.content = content;
        this.ttsListener = listener;
        sentenceIndex=0;
        setDataAndPlay(content,0);


        return TtsError.CODE_SUCCESS;


//        if (TextUtils.isEmpty(content.trim())){ //兼容莫名其妙有空串的问题
//            content =".";
//        }
//        return tts.speak(content, TextToSpeech.QUEUE_ADD, params);

    }

    private boolean setDataAndPlay(String content,int beginIndex) {
        HashMap<String, String> params = new HashMap<>();
        int length = content.trim().length();
        boolean isLast = false;
        boolean hasDoSpeak=false;
        while (!isLast) {
            int[] sentence = getSentence(beginIndex, content, false);

            int start=sentence[0];
            int end=sentence[1];


            try {
                beginIndex = sentence[1] + 1;
                if(beginIndex>=length) isLast=true;

                String data = content.substring(sentence[0], sentence[1]);
                if (isEmpty(data)) {
                    Log.e(TAG,"error :   nodata "+content+",sentence[0]:"+sentence[0]+",sentence[1]:"+sentence[1]+",length:"+length+",beginIndex:"+beginIndex);
                    continue;
                }

                String id = start + "_" + end+"_"+length;
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,   id);


//                Log.e("==================length=" + length + "=======index=" + sentenceIndex + "=======id=" + params.get(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID) + "======================content=" + data);
                if (TextUtils.isEmpty(data.trim())) { //兼容莫名其妙有空串的问题
                    data = ".";
                }
                hasDoSpeak=true;
                tts.speak(data, TextToSpeech.QUEUE_ADD, params);
            } catch (Throwable e) {
                e.printStackTrace();
                beginIndex = sentence[1] + 1;
                hasDoSpeak=true;
                tts.speak(".", TextToSpeech.QUEUE_ADD, params);
            }
        }
//        if (!hasDoSpeak)
//        {
//            Log.e("xxxxxxxxxxxxxxxxxxxxxxxxxx====empty  sentence!!!!!!!! "+content+",beginIndex:"+beginIndex);
//        }
        return hasDoSpeak;

    }

    private boolean isEmpty(String data) {


        return data==null||data.trim().equals("");
    }

    @Override
    public void pauseSpeaking() {

        if (tts != null) {
            if (tts.isSpeaking()) {
                tts.stop();
            }
        }

    }

    @Override
    public void resumeSpeaking() {


            if(!isEmpty(content)) {
                boolean b = setDataAndPlay(content, sentenceIndex);
                if(!b&&ttsListener!=null)
                {
                   // Log.e("xxxxxxxxxxxxxxxxxxxxxxxxxx====resume but end sentence!!!!!!!! "+content+",sentenceIndex:"+sentenceIndex);
                    ttsListener.onSpeakCompleted();

                }
            }




    }


}
