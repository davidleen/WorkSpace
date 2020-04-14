package com.giants3.android.reader.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.R;
import com.giants3.file.FileContentType;
import com.giants3.io.FileUtils;
import com.giants3.reader.book.EpubChapter;
import com.giants3.tool.zip.ZipJNIInterface;
import com.giants3.yourreader.text.TextPageBitmap;
import com.giants3.yourreader.text.TextPageInfo;
import com.giants3.yourreader.text.TextPrepareJob;
import com.xxx.reader.ReaderView;
import com.xxx.reader.Url2FileMapper;
import com.xxx.reader.Utils;
import com.xxx.reader.book.BookFactory;
import com.xxx.reader.book.IBook;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.book.TextBook;
import com.xxx.reader.book.TextChapter;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.prepare.DrawLayer;
import com.xxx.reader.prepare.PageBitmapCreator;
import com.xxx.reader.prepare.PagePlayer;
import com.xxx.reader.prepare.PagePlayerBuilder;
import com.xxx.reader.turnner.ScrollPageTurner;
import com.xxx.reader.turnner.SimulatePageTurner;
import com.xxx.reader.turnner.sim.SimPageTurner;
import com.xxx.reader.turnner.slide.SlidePageTurner;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;

public class TextReadActivity extends BaseActivity {
    @Bind(R.id.reader)
    ReaderView readerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reader);

        handleFile(getIntent());
    }

    private void handleFile(Intent intent) {

        final String filePath = intent.getStringExtra("filePath");
        String bookId = intent.getStringExtra("bookId");
        String bookUrl = intent.getStringExtra("bookUrl");




        SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
         sharedPreferences.edit().putLong("lastcliptime", Calendar.getInstance().getTimeInMillis()).apply();

        final Context context = this;
        final int[] wh = Utils.getScreenDimension(this);
        PagePlayerBuilder<IChapter, TextPageInfo, DrawParam, TextPageBitmap> builder = new PagePlayerBuilder();
        builder.setCreator(new PageBitmapCreator<TextPageBitmap>() {
            @Override
            public TextPageBitmap create() {
                return new TextPageBitmap(context, wh[0], wh[1], readerView);
            }
        });
        builder.setiDrawable(readerView);

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



        final PagePlayer<IChapter, TextPageInfo, DrawParam, TextPageBitmap> prepareLayer = builder.createPrepareLayer();
        DrawParam drawParam = new DrawParam();
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
            }
        });

        DrawLayer drawLayer = new DrawLayer(this, prepareLayer, readerView);

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


               // Log.e("afterPageChanged:" + direction);
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
      //  pageTurner = new ScrollPageTurner(this, pageSwitchListener, readerView, prepareLayer);
//        pageTurner=new SimPageTurner(this,pageSwitchListener,readerView,prepareLayer);
     pageTurner=new SimulatePageTurner(this,pageSwitchListener,readerView,prepareLayer);
        //   pageTurner = new SlidePageTurner(this, pageSwitchListener, readerView, prepareLayer);

        drawLayer.setPageTurner(pageTurner);
        readerView.setDrawLayer(drawLayer);
        String contentType = FileContentType.getContentType(new File(filePath));
        final BookFactory bookFactory;
        switch (contentType) {
            case FileContentType.EPUB: {
                bookFactory = new EpubBookFactory();
            }
            break;
            case FileContentType.TEXT:
            {

                bookFactory=new TextBookFactory();
            }break;
            default:
                bookFactory = null;

        }

        if(bookFactory==null){
            finish();
            return;
        }
        new AsyncTask<Void, Void, IBook>() {
            @Override
            protected IBook doInBackground(Void[] objects) {

                IBook iBook =   bookFactory.create(filePath);;


                return iBook;
            }

            @Override
            protected void onPostExecute(IBook iBook) {
                super.onPostExecute(iBook);

                if (iBook != null)
                    prepareLayer.updateBook(iBook);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

}
