package com.giants3.yourreader.text;

import android.content.ComponentName;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;

import com.giants3.android.service.AbstractService;
import com.giants3.android.service.ServiceBinderHelper;
import com.xxx.reader.api.tts.SpeakListener;
import com.xxx.reader.api.tts.TtsError;
import com.xxx.reader.core.PageInfo;


/**
 * 前台阅读服务。
 */
public class BookPlayService  extends AbstractService<BookPlayService.BookPlayController> {

    private ServiceBinderHelper bookServiceBinder;


    private boolean isBookServiceBound;

    private BookPlayController bookPlayController;
    @Override
    public void onCreate() {
        super.onCreate();
        bookPlayController=new BookPlayController(this);
        if(!isBookServiceBound) {
            bookServiceBinder=new ServiceBinderHelper(this,BookService.class);
            bookServiceBinder.bindService(new ServiceBinderHelper.BindResultListener() {
                @Override
                public void onBindResult(boolean bindResult) {
                    isBookServiceBound=bindResult;
                }

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    BookService.BookReadController   bookReadController = (BookService.BookReadController) service;
                    bookPlayController.setBookReadController(bookReadController);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            });
        }

    }

    @Override
    protected BookPlayController createBinder() {
        return new BookPlayController(this);
    }

    public static class BookPlayController extends Binder
    {


        private BookPlayer bookPlayer;
        PlayData playData=null;
        PlayData dataPlayed=null;
        private BookService.BookReadController bookReadController;

        public BookPlayController(Context context)
        {
            bookPlayer=new BookPlayer(context);
            bookPlayer.setPlayListener(new SpeakListener() {
                @Override
                public void onSpeakBegin() {

                }

                @Override
                public void onBufferProgress(int progress, int beginPos, int endPos, String info) {

                }

                @Override
                public void onSpeakPaused() {

                }

                @Override
                public void onSpeakResumed() {

                }

                @Override
                public void onSpeakProgress(int progress, int beginPos, int endPos) {

                }

                @Override
                public void onSpeakCompleted() {

                }

                @Override
                public void onSpeakError(TtsError error) {






                }
            });
        }

        void setBookReadController(BookService.BookReadController bookReadController)
        {

            this.bookReadController = bookReadController;
        }

        void startSpeak()
        {


            PageInfo currentPageInfo = bookReadController.preparePageInfo.getCurrentPageInfo();

            playData=new PlayData();
            bookReadController.fillPlayData(playData,dataPlayed);
            if(playData.hasData())
            {
                bookPlayer.setDataAndPlay(playData);
                dataPlayed=playData;
            }

        }







    }


    @Override
    public void onDestroy() {
        if(isBookServiceBound)
            bookServiceBinder.unbindService();
        super.onDestroy();
    }
}
