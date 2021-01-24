package com.giants3.android.reader.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.reader.databinding.ActivityTextReaderBinding;
import com.giants3.android.reader.entity.User;
import com.giants3.android.reader.vm.TextReadViewModel;
import com.giants3.android.reader.vm.UserModel;
import com.giants3.file.FileContentType;
import com.giants3.io.FileUtils;
import com.giants3.reader.book.EpubChapter;
import com.giants3.yourreader.text.BookService;
import com.giants3.yourreader.text.TextPageBitmap;
import com.giants3.yourreader.text.TextPageInfo;
import com.giants3.yourreader.text.TextPrepareJob;
import com.xxx.reader.ReaderView;
import com.xxx.reader.Url2FileMapper;
import com.xxx.reader.Utils;
import com.xxx.reader.book.BookFactory;
import com.xxx.reader.book.IBook;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.book.TextChapter;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageInfo;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.prepare.DrawLayer;
import com.xxx.reader.prepare.PageBitmapCreator;
import com.xxx.reader.prepare.PagePlayer;
import com.xxx.reader.prepare.PagePlayerBuilder;
import com.xxx.reader.prepare.PrepareJob;
import com.xxx.reader.prepare.PrepareListener;
import com.xxx.reader.prepare.PreparePageInfo;
import com.xxx.reader.turnner.SimulatePageTurner;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;


public class TextReadActivity extends BaseViewModelActivity<ActivityTextReaderBinding,TextReadViewModel> implements PrepareListener {
    BookService.BookReadController bookReadController;
      PagePlayer<IChapter, TextPageInfo, DrawParam, TextPageBitmap> prepareLayer ;

    private IBook iBook;
    ServiceConnection conn ;
    DrawParam drawParam;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init( );
        getViewModel().getBookInfo().observe(this, new Observer<IBook>() {


            @Override
            public void onChanged(IBook iBook) {
                TextReadActivity.this.iBook = iBook;


                if (bookReadController!=null)
                {
                    bookReadController.updateBook(iBook);
                }

            }
        });

        Intent intent=new Intent(this, BookService.class);
          conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                bookReadController = (BookService.BookReadController) service;
                if(drawParam!=null)
                    bookReadController.updateDrawParam(drawParam);
                bookReadController.setPrepareListener(TextReadActivity.this);
                if (iBook != null)
                    bookReadController.updateBook(iBook);
//                getViewModel().getBookInfo().observe(TextReadActivity.this, new Observer<IBook>() {
//                    @Override
//                    public void onChanged(IBook iBook) {
//
//
//
//                        if (iBook != null)
//                            bookReadController.updateBook(iBook);
//                    }
//                });

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {




            }
        };
        bindService(intent, conn, Service.BIND_AUTO_CREATE);



    }

    @Override
    protected Class<TextReadViewModel> getViewModelClass() {
        return TextReadViewModel.class;
    }

    @Override
    protected ActivityTextReaderBinding createViewBinding() {
        return ActivityTextReaderBinding.inflate(getLayoutInflater());
    }

    private void init( ) {



        getViewBinding().panelSetting.setVisibility(View.GONE);
        getViewBinding().panelSetting.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getViewBinding().panelSetting.setVisibility(View.GONE);

            }
        });

        getViewBinding().progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser)
                {
                    bookReadController.jump(progress/100f);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getViewBinding().next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookReadController.nextChapter();
            }
        });
 getViewBinding().previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookReadController.previousChapter();
            }
        });



        SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
         sharedPreferences.edit().putLong("lastcliptime", Calendar.getInstance().getTimeInMillis()).apply();

        final Context context = this;
        final int[] wh = Utils.getScreenDimension(this);
        PagePlayerBuilder<IChapter, TextPageInfo, DrawParam, TextPageBitmap> builder = new PagePlayerBuilder();
        final ReaderView readerView = getViewBinding().reader;

        builder.setCreator(new PageBitmapCreator<TextPageBitmap>() {
            @Override
            public TextPageBitmap create() {
                return new TextPageBitmap(context, wh[0], wh[1], readerView);
            }
        });
        builder.setiDrawable(readerView);





        prepareLayer = builder.createPrepareLayer();

       drawParam= new DrawParam();
        drawParam.width = wh[0] ;
        drawParam.height = wh[1] ;
        prepareLayer.updateDrawParam(drawParam);
        readerView.setOnSizeChangeListener(new ReaderView.onSizeChangeLister(){
            @Override
            public void onSizeChanged(int width, int height) {
                DrawParam drawParam = new DrawParam();
                drawParam.width = width ;
                drawParam.height =height;
                prepareLayer.updateDrawParam(drawParam);
                TextReadActivity.this.drawParam=drawParam;
                if(bookReadController!=null)
                    bookReadController.updateDrawParam(drawParam);
            }
        });

        DrawLayer drawLayer = new DrawLayer(this, prepareLayer, readerView);
        drawLayer.setClickListener(new DrawLayer.MenuClickListener() {
            @Override
            public void onMenuAreaClick() {
                Log.e("~~~~~~~~~~~~~~~~~~");

                getViewBinding().panelSetting.setVisibility(View.VISIBLE);


            }
        });
        PageSwitchListener pageSwitchListener = new PageSwitchListener() {
            @Override
            public boolean canPageChanged(int direction) {
                if (direction == PageSwitchListener.TURN_PREVIOUS)
                return bookReadController.canTurnPrevious();
                return bookReadController.canTurnNext();
            }

            @Override
            public void beforePageChanged(int direction) {


            }

            @Override
            public void afterPageChanged(int direction) {




                Log.e("afterPageChanged:" + direction);
                if (direction == PageSwitchListener.TURN_NEXT) {

                    prepareLayer.turnNext();
                    bookReadController.turnNext();
                } else {
                    prepareLayer.turnPrevious();
                    bookReadController.turnPrevious();
                }

            }

            @Override
            public void onPageTurnFail(int turnMoveDirection) {

            }
        };


        IPageTurner pageTurner = null;
      //  pageTurner = new ScrollPageTurner(this, pageSwitchListener, activityTextReaderBinding.reader, prepareLayer);
//        pageTurner=new SimPageTurner(this,pageSwitchListener,activityTextReaderBinding.reader,prepareLayer);
     pageTurner=new SimulatePageTurner(this,pageSwitchListener, readerView,prepareLayer);
        //   pageTurner = new SlidePageTurner(this, pageSwitchListener, activityTextReaderBinding.reader, prepareLayer);

        drawLayer.setPageTurner(pageTurner);
        readerView.setDrawLayer(drawLayer);




    }

    @Override
    protected void onDestroy() {
        if (prepareLayer!=null) {
            prepareLayer.onDestroy();
        }
        if(conn!=null)
            unbindService(conn);


        super.onDestroy();
    }

    @Override
    public void onPagePrepared(PreparePageInfo preparePageInfo) {

        if(preparePageInfo!=null) {
            PageInfo currentPageInfo = preparePageInfo.getCurrentPageInfo();
            if(currentPageInfo==null||currentPageInfo.pageCount==0)
            {
                getViewBinding().progress.setProgress(0);
            }else
            {

                getViewBinding().progress.setProgress(currentPageInfo.pageIndex*100/currentPageInfo.pageCount);
            }
            prepareLayer.onPagePrepared(preparePageInfo);

        }

    }
}
