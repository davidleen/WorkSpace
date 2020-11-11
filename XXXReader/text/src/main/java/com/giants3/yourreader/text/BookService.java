package com.giants3.yourreader.text;

import android.os.Binder;
import android.os.IBinder;

import com.giants3.android.service.AbstractService;
import com.xxx.reader.book.IBook;
import com.xxx.reader.core.PageInfo;
import com.xxx.reader.prepare.ChapterMeasureManager;
import com.xxx.reader.prepare.PrepareJob;
import com.xxx.reader.prepare.PrepareListener;
import com.xxx.reader.prepare.PrepareThread;

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

    public static class BookReadController extends Binder implements PageController
    {
        /**
         * 最大的缓存数量
         */
        public static int MAX_CACHE_SIZE = 5;
        /**
         * 章节测量分页功能处理类。
         */
        ChapterMeasureManager  chapterMeasureManager;
        PrepareThread prepareThread;
        public BookReadController ()
        {

            prepareThread = new PrepareThread(this, MAX_CACHE_SIZE);
            prepareThread.start();
        }



        public void updateBook(IBook  iBook)
        {


            chapterMeasureManager.setbook(iBook);
            updateCache();

                //step 1 检查下载
                //step 2 读取文件
                //

        }

        public void updateCache() {
            if (prepareThread != null) {
                prepareThread.setSkip(true);
                prepareThread.interrupt();
            }
        }

        @Override
        public void turnNext() {

        }

        @Override
        public void turnPrevious() {

        }

        @Override
        public void setPrepareListener(PrepareListener prepareListener) {

        }
    }


    public static interface  PageController extends IBinder
    {


          void turnNext();
          void turnPrevious();


          void setPrepareListener(PrepareListener prepareListener);

          void updateBook(IBook iBook);

    }


}
