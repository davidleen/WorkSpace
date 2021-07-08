package com.giants3.yourreader.text;

import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.Utils;
import com.giants3.android.service.AbstractService;
import com.giants3.io.FileUtils;
import com.giants3.reader.book.EpubChapter;
import com.xxx.reader.ThreadConst;
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
import com.xxx.reader.text.page.TextPageInfo2Typing;
import com.xxx.reader.text.page.TypeParam;
import com.xxx.reader.turnner.sim.SettingContent;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.xxx.reader.prepare.PrepareThread.MSG_NEXT;
import static com.xxx.reader.prepare.PrepareThread.MSG_PREPARE_CHAPTER;
import static com.xxx.reader.prepare.PrepareThread.MSG_PREVIOUS;


public class BookService extends AbstractService<BookService.BookReadController> {



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

        PrepareThread prepareThread;

        private PrepareListener prepareListener;
        PreparePageInfo preparePageInfo;
        private IBook iBook;

        public BookReadController ()
        {

            preparePageInfo=new PreparePageInfo();

           Handler    handler=new Handler(){


                @Override
                public void handleMessage(Message msg) {


                    switch (msg.what) {



                        case MSG_PREPARE_CHAPTER: {

                       int chapterIndex=msg.arg1;

                        IChapter iChapter=iBook.getChapter(chapterIndex);

                        if(iChapter instanceof EpubChapter)
                        {
                            String filePath = iChapter.getFilePath();
                            if (!new File(filePath).exists()) {


                                new AsyncTask (){
                                    @Override
                                    protected Object doInBackground(Object[] objects) {

                                        try {

                                            FileUtils.writeToFile(filePath, ((EpubChapter) iChapter).getData());



                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Object o) {
                                        super.onPostExecute(o);
                                        updateCache();
                                    }
                                }.executeOnExecutor(ThreadConst.THREAD_POOL_EXECUTOR);

                            }
                        }








                            break;
                        }
                        case MSG_NEXT: {
                            PageInfo measuredResult = (PageInfo) msg.obj;
                            if (measuredResult != null) {
                                measuredResult.isReady=true;
                                preparePageInfo.addLast(measuredResult);

                                if (prepareListener != null) {
                                    prepareListener.onPagePrepared(preparePageInfo);
                                }
                            }

                        }



                        break;

                        case MSG_PREVIOUS: {
                            PageInfo measuredResult = (PageInfo) msg.obj;
                            if (measuredResult != null) {

                                measuredResult.isReady=true;
                                preparePageInfo.addFirst(measuredResult);

                                if (prepareListener != null) {
                                    prepareListener.onPagePrepared(preparePageInfo);
                                }
                            }

                        }



                        break;
                    }


                }
            };
            prepareThread = new PrepareThread(preparePageInfo,  MAX_CACHE_SIZE,handler);
            prepareThread.updatePageTyping(new TextPageInfo2Typing());
            prepareThread.start();
        }



        public void updateDrawParam(DrawParam drawParam)
        {


            preparePageInfo.onSettingChange();
            prepareThread.updateTypeParam(createTypePara(drawParam));

            updateCache();
        }

        private TypeParam createTypePara(DrawParam drawParam)
        {
            TypeParam typeParam = new TypeParam();
            int[] screenWH = Utils.getScreenWH();
            typeParam.width =drawParam.width;
            typeParam.height = drawParam.height;
            typeParam.textSize = SettingContent.getInstance().getTextSize();
            typeParam.lineSpace = (int) SettingContent.getInstance().getLineSpace();
            typeParam.wordSpace = (int) SettingContent.getInstance().getWordSpace();
            typeParam.paragraphSpace = (int) SettingContent.getInstance().getParaSpace();
            typeParam.includeFontPadding = true;
            typeParam.padding = SettingContent.getInstance().getPaddings();

            return typeParam;
        }
        public void updateBook(IBook  iBook)
        {
            this.iBook = iBook;



            prepareThread.updateBook(iBook);
            preparePageInfo.resetAll();
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
          return  preparePageInfo
                  .canTurnPrevious();
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


        /**
         * 更新排版相关参数
         * @param txTtypeset
         */
        public void updateTypeset(TXTtypeset txTtypeset)
        {

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

        }

        public void jump(float progress) {


            preparePageInfo.jump(progress);
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

        public void fontSizeReduce() {

            float textSize = SettingContent.getInstance().getTextSize();
            if(textSize<12) return;
            textSize--;

            SettingContent.getInstance().setTextSize(textSize);
            preparePageInfo.onSettingChange();
            updateCache();

        }


        public void bookSideEffectChanged()
        {


            preparePageInfo.onSettingChange();
            updateCache();
        }

        public void fontSizeAdd() {

            float textSize = SettingContent.getInstance().getTextSize();
            if(textSize>60) return;
            textSize++;
            SettingContent.getInstance().setTextSize(textSize);
            preparePageInfo.onSettingChange();
            updateCache();

        }

        public void fontStyleChange() {



            preparePageInfo.onSettingChange();
            updateCache();

        }

        public void lineSpaceReduce() {

            float lineSpace = SettingContent.getInstance().getLineSpace();
            if(lineSpace<=0) return;
            lineSpace--;
            updateLineSpace(lineSpace);


        }

        public void lineSpaceAdd() {

            float lineSpace = SettingContent.getInstance().getLineSpace();
            if(lineSpace>60) return;
            lineSpace++;
            updateLineSpace(lineSpace);


        }

        public  void updateLineSpace(float lineSpace)
        {
            SettingContent.getInstance().setLineSpace(lineSpace);
            preparePageInfo.onSettingChange();
            updateCache();


        }

        public void updateWordSpace(float hGap) {
            SettingContent.getInstance().setWorkSpace(hGap);
            preparePageInfo.onSettingChange();
            updateCache();
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
