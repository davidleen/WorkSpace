package com.giants3.yourreader.text;

import android.os.Binder;
import android.os.IBinder;

import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.service.AbstractService;
import com.giants3.io.FileUtils;
import com.giants3.reader.book.EpubChapter;
import com.xxx.reader.Url2FileMapper;
import com.xxx.reader.book.IBook;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.book.TextChapter;
import com.xxx.reader.core.CacheUpdateListener;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageInfo;
import com.xxx.reader.prepare.ChapterMeasureManager;
import com.xxx.reader.prepare.PrepareJob;
import com.xxx.reader.prepare.PrepareListener;
import com.xxx.reader.prepare.PreparePageInfo;
import com.xxx.reader.prepare.PrepareThread;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class BookService extends AbstractService<BookService.BookReadController> {



    /**
     * 缓存工作实现接口   测量，绘制。
     */
    PrepareJob  prepareJob;

    List<PageInfo> pageInfos=new ArrayList<>();
    BookReadController controller;
    @Override
    public void onCreate() {
        super.onCreate();
        controller=new BookReadController();
    }

    @Override
    protected BookReadController createBinder() {
        return controller;
    }

    public static class BookReadController extends Binder implements PageController, CacheUpdateListener {
        /**
         * 最大的缓存数量
         */
        public static int MAX_CACHE_SIZE = 5;
        /**
         * 章节测量分页功能处理类。
         */
        ChapterMeasureManager  chapterMeasureManager;
        PrepareThread prepareThread;
        PrepareJob prepareJob;
        private PrepareListener prepareListener;
        PreparePageInfo preparePageInfo;
        public BookReadController ()
        {

            preparePageInfo=new PreparePageInfo();
              prepareJob = new TextPrepareJob(new Url2FileMapper<IChapter>() {
                @Override
                public String map(IChapter iChapter, String url) {

                    if (iChapter instanceof EpubChapter) {


                        String filePath = StorageUtils.getFilePath(iChapter.getFilePath());
                        if (!new File(filePath).exists()) {
                            try {

                                FileUtils.writeToFile(filePath, ((EpubChapter) iChapter).getData());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return filePath;


                    } else if (iChapter instanceof TextChapter) {

                        return iChapter.getFilePath();
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
            });
            chapterMeasureManager = new ChapterMeasureManager<>(this, prepareJob, IPageTurner.HORIZENTAL);

            prepareThread = new PrepareThread(preparePageInfo,chapterMeasureManager, MAX_CACHE_SIZE);
            prepareThread.start();
        }



        public void updateDrawParam(DrawParam drawParam)
        {
            if(chapterMeasureManager!=null)
            {
                chapterMeasureManager.updateDrawParam(drawParam);
            }
        }

        public void updateBook(IBook  iBook)
        {


            chapterMeasureManager.setbook(iBook);
            updateCache();

                //step 1 检查下载
                //step 2 读取文件
                //

        }

        public boolean  canTurnNext()
        {
          return  preparePageInfo.canTurnNext();
        }
        public boolean canTurnPrevious()
        {
          return  preparePageInfo.canTurnPrevious();
        }


        public void updateCache() {
            if (prepareThread != null) {
                prepareThread.setSkip(true);
                prepareThread.interrupt();
            }
        }

        @Override
        public void turnNext() {

            preparePageInfo.turnNext();
            if (prepareListener != null) {
                prepareListener.onPagePrepared(preparePageInfo);
            }
            updateCache();


        }


        @Override
        public void turnPrevious() {
            preparePageInfo.turnPrevious();
            if (prepareListener != null) {
                prepareListener.onPagePrepared(preparePageInfo);
            }
            updateCache();

        }

        @Override
        public boolean canPageChangedNext() {
            return false;
        }

        @Override
        public boolean canPageChangedPrevious() {
            return false;
        }

        @Override
        public void setPrepareListener(PrepareListener prepareListener) {

            this.prepareListener = prepareListener;
            prepareThread.setPrepareListener(prepareListener);
        }

        public void jump(float progress) {


            preparePageInfo.jump(progress);
            if (prepareListener != null) {
                prepareListener.onPagePrepared(preparePageInfo);
            }
            updateCache();
        }

        public void previousChapter() {
            preparePageInfo.previousChapter();
            if (prepareListener != null) {
                prepareListener.onPagePrepared(preparePageInfo);
            }
            updateCache();
        }

        public void nextChapter() {

            preparePageInfo.nextChapter();
            if (prepareListener != null) {
                prepareListener.onPagePrepared(preparePageInfo);
            }
            updateCache();
        }

        public void destroy() {

            prepareThread.setPrepareListener(null);
            prepareThread.setDestroy();
            prepareThread=null;


        }
    }


    public static interface  PageController extends IBinder
    {
          boolean  canTurnNext();

          boolean canTurnPrevious();


          void turnNext();
          void turnPrevious();


         boolean canPageChangedNext();

         boolean canPageChangedPrevious();
          void setPrepareListener(PrepareListener prepareListener);

          void updateBook(IBook iBook);

    }


    @Override
    public void onDestroy() {
        controller.destroy();
        super.onDestroy();



    }
}
