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
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.prepare.DrawLayer;
import com.xxx.reader.prepare.PageBitmapCreator;
import com.xxx.reader.prepare.PagePlayer;
import com.xxx.reader.prepare.PagePlayerBuilder;
import com.xxx.reader.turnner.SimulatePageTurner;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;


public class TextReadActivity extends BaseViewModelActivity<ActivityTextReaderBinding,TextReadViewModel> {
    BookService.BookReadController bookReadController;
      PagePlayer<IChapter, TextPageInfo, DrawParam, TextPageBitmap> prepareLayer ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init( );
        getViewModel().getBookInfo().observe(this, new Observer<IBook>() {
            @Override
            public void onChanged(IBook iBook) {



                if (iBook != null)
                    prepareLayer.updateBook(iBook);
            }
        });

        Intent intent=new Intent(this, BookService.class);
        bindService(intent,new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                bookReadController= (BookService.BookReadController) service;
                bookReadController.setPrepareListener(prepareLayer);

                getViewModel().getBookInfo().observe(TextReadActivity.this, new Observer<IBook>() {
                    @Override
                    public void onChanged(IBook iBook) {



                        if (iBook != null)
                            bookReadController.updateBook(iBook);
                    }
                });

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Service.BIND_AUTO_CREATE);



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







        SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
         sharedPreferences.edit().putLong("lastcliptime", Calendar.getInstance().getTimeInMillis()).apply();

        final Context context = this;
        final int[] wh = Utils.getScreenDimension(this);
        PagePlayerBuilder<IChapter, TextPageInfo, DrawParam, TextPageBitmap> builder = new PagePlayerBuilder();
        builder.setCreator(new PageBitmapCreator<TextPageBitmap>() {
            @Override
            public TextPageBitmap create() {
                return new TextPageBitmap(context, wh[0], wh[1], getViewBinding().reader);
            }
        });
        builder.setiDrawable(getViewBinding().reader);

        builder.setPrepareJob(new TextPrepareJob(new Url2FileMapper<IChapter>() {
            @Override
            public String map(IChapter iChapter, String url) {

                if (iChapter instanceof EpubChapter) {


                    String filePath=StorageUtils.getFilePath(iChapter.getFilePath());
                    if(!new File( filePath).exists()) {
                        try {

                            FileUtils.writeToFile(filePath, ((EpubChapter) iChapter).getData());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return filePath;


                }else  if (iChapter instanceof TextChapter)
                {

                    return  iChapter.getFilePath();
                }



                String fileName = String.valueOf(url.hashCode());
                try {
                    fileName = URLDecoder.decode(url.substring(url.lastIndexOf("/")), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return StorageUtils.getFilePath(fileName);
            }

            @Override
            public String map(String chapterName) {
                return null;
            }
        }));



        prepareLayer = builder.createPrepareLayer();
        DrawParam drawParam = new DrawParam();
        drawParam.width = wh[0] ;
        drawParam.height = wh[1] ;
        prepareLayer.updateDrawParam(drawParam);
        getViewBinding().reader.setOnSizeChangeListener(new ReaderView.onSizeChangeLister(){
            @Override
            public void onSizeChanged(int width, int height) {
                DrawParam drawParam = new DrawParam();
                drawParam.width = width ;
                drawParam.height =height;
                prepareLayer.updateDrawParam(drawParam);
            }
        });

        DrawLayer drawLayer = new DrawLayer(this, prepareLayer, getViewBinding().reader);

        PageSwitchListener pageSwitchListener = new PageSwitchListener() {
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
                    prepareLayer.turnNext();
                } else {
                    prepareLayer.turnPrevious();
                }

            }

            @Override
            public void onPageTurnFail(int turnMoveDirection) {

            }
        };


        IPageTurner pageTurner = null;
      //  pageTurner = new ScrollPageTurner(this, pageSwitchListener, activityTextReaderBinding.reader, prepareLayer);
//        pageTurner=new SimPageTurner(this,pageSwitchListener,activityTextReaderBinding.reader,prepareLayer);
     pageTurner=new SimulatePageTurner(this,pageSwitchListener,getViewBinding().reader,prepareLayer);
        //   pageTurner = new SlidePageTurner(this, pageSwitchListener, activityTextReaderBinding.reader, prepareLayer);

        drawLayer.setPageTurner(pageTurner);
        getViewBinding().reader.setDrawLayer(drawLayer);




    }

    @Override
    protected void onDestroy() {
        if (prepareLayer!=null) {
            prepareLayer.onDestroy();
        }


        super.onDestroy();
    }
}
