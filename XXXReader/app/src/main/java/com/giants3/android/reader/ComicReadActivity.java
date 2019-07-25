package com.giants3.android.reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.ToastHelper;
import com.giants3.android.reader.activity.BaseActivity;
import com.giants3.android.reader.adapter.ComicBook;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.giants3.android.reader.domain.UseCaseHandler;
import com.giants3.reader.noEntity.ComicChapterInfo;
import com.giants3.reader.noEntity.RemoteData;
import com.xxx.reader.ReaderView;
import com.xxx.reader.Utils;
import com.xxx.reader.comic.ComicPrepareLayer;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.download.DownloadListener;
import com.xxx.reader.prepare.Cancelable;
import com.xxx.reader.prepare.DrawLayer;
import com.xxx.reader.prepare.PagePlayer;
import com.xxx.reader.text.layout.BitmapProvider;
import com.xxx.reader.turnner.ScrollPageTurner;
import com.xxx.reader.turnner.sim.SimPageTurner;
import com.xxx.reader.turnner.slide.SlidePageTurner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComicReadActivity extends BaseActivity {

    public static final String KEY_BOOK_ID = "KEY_BOOK_ID";
    public static final String KEY_BOOK = "KEY_BOOK";

    DrawLayer drawLayer;
    ReaderView readerView;
    PageSwitchListener pageSwitchListener;
    BitmapProvider provider;
    public static final String KEY_FILE_PATH = "KEY_FILE_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        readerView = (ReaderView) findViewById(R.id.reader);
        int[] wh = Utils.getScreenDimension(this);


        final PagePlayer comicPagePlayer = ComicPrepareLayer.createComicPrepareLayer(this, wh[0], wh[1], readerView, new DownloadListener() {
            Map<String, List<NotifyListener>> notifyListenerMap = new HashMap<>();

            @Override
            public synchronized void startDownload(final String url, final String filePath, Cancelable cancelable, final NotifyListener notifyListener) {


                List<NotifyListener> notifyListeners = notifyListenerMap.get(url);
                if (notifyListeners == null
                ) {
                    notifyListeners = new ArrayList<>();
                    notifyListenerMap.put(url, notifyListeners);
                }

                notifyListeners.add(notifyListener);
                if (notifyListeners.size() > 1) {
                    return;
                }


                UseCaseFactory.getInstance().createDownloadUseCase(url, filePath).execute(new UseCaseHandler() {
                    @Override
                    public void onError(Throwable e) {


                        List<NotifyListener> notifyListeners = notifyListenerMap.remove(url);
                        for (NotifyListener notifyListener : notifyListeners) {
                            notifyListener.onError(url, filePath);
                        }
                    }

                    @Override
                    public void onNext(Object object) {


                        List<NotifyListener> notifyListeners = notifyListenerMap.remove(url);
                        for (NotifyListener notifyListener : notifyListeners) {
                            notifyListener.onComplete(url, filePath);
                        }


                    }

                    @Override
                    public void onCompleted() {

                    }
                });


                Log.e("download :" + url + ",filePath:" + filePath);

            }
        });


        DrawParam drawParam = new DrawParam();
        drawParam.width = wh[0];
        drawParam.height = wh[1];
        comicPagePlayer.updateDrawParam(drawParam);
        //SimpleBitmapProvider provider=new SimpleBitmapProvider(this,wh[0],wh[1]);
        provider = comicPagePlayer;

        drawLayer = new DrawLayer(this, comicPagePlayer, readerView);

        pageSwitchListener = new PageSwitchListener() {
            @Override
            public boolean canPageChanged(int direction) {
                return true;
            }

            @Override
            public void beforePageChanged(int direction) {


            }

            @Override
            public void afterPageChanged(int direction) {


                Log.e("afterPageChanged:" + direction);
                if (direction == PageSwitchListener.TURN_NEXT) {

                    comicPagePlayer.turnNext();
                } else {
                    comicPagePlayer.turnPrevious();
                }

            }

            @Override
            public void onPageTurnFail(int turnMoveDirection) {

            }
        };
        IPageTurner pageTurner = null;
        //  pageTurner=new ScrollPageTurner(this,pageSwitchListener,readerView,provider);
        pageTurner = new SimPageTurner(this, pageSwitchListener, readerView, provider);
        // pageTurner = new SlidePageTurner(this, pageSwitchListener, readerView, provider);

        drawLayer.setPageTurner(pageTurner);
        readerView.setDrawLayer(drawLayer);


        long bookId = getIntent().getLongExtra(KEY_BOOK_ID, 0);
        if (bookId == 0) {


        } else {

            UseCaseFactory.getInstance().createUseCase(HttpUrl.getBookCategoryInfo(bookId), ComicChapterInfo.class).execute(new UseCaseHandler<RemoteData<ComicChapterInfo>>() {
                @Override
                public void onCompleted() {


                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(RemoteData<ComicChapterInfo> remoteData) {

                    if (remoteData.isSuccess()) {


                        comicPagePlayer.updateBook(new ComicBook(remoteData.datas));
                    }

                }
            });

        }






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_work_flow_report clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {


            case R.id.slide:

                drawLayer.setPageTurner(new SlidePageTurner(this, pageSwitchListener, readerView, provider));

                break;
            case R.id.simulate:
                drawLayer.setPageTurner(new SimPageTurner(this, pageSwitchListener, readerView, provider));
                break;
            case R.id.scroll:
                drawLayer.setPageTurner(new ScrollPageTurner(this, pageSwitchListener, readerView, provider));
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
