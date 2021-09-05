package com.xxx.reader.text.page;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.WorkerThread;


import com.giants3.android.frame.util.Log;

import com.giants3.io.FileUtils;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.BuildConfig;
import com.xxx.reader.file.BufferedRandomAccessFile;
import com.xxx.reader.file.TXTReader;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** 章节文件流统一处理中心。
 * 负责打开关闭文件
 *
 * 增加缓存 ，定时关闭处理。
 */
public class TextReaderManager {
    public Map<IChapter, BufferedRandomAccessFile> cache=new HashMap();
    public Map<IChapter, Integer> refCount=new HashMap();



    private static final int MSG_CLOSE_READER=33;
    //文件流打开后， 保持缓存时间备用。 超过这个时间自动关闭。
    private static final long CLOSE_DELAY=15*1000l;
    Handler handler;
    public TextReaderManager()
    {

        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {

                int what=msg.what;
                Object o=msg.obj;
                switch (what)
                {
                    case MSG_CLOSE_READER:
                        BufferedRandomAccessFile pageTxtReader=null;
                        synchronized (cache) {
                              pageTxtReader = cache.remove(o);
                        }
                        realReleaseReader(pageTxtReader);

                }



            }
        };
    }



    private void realReleaseReader(BufferedRandomAccessFile pageTxtReader)
    {
        if(pageTxtReader!=null)
        {
            if(BuildConfig.DEBUG)
            {
                Log.e("release reader in cache:"+pageTxtReader);
            }
            FileUtils.safeClose(pageTxtReader);

        }
    }

    /**
     *
     * 获取文件流
     * @param bookChapterInfo
     * @return
     */
    @WorkerThread
    public BufferedRandomAccessFile open(IChapter bookChapterInfo)
    {
        handler.removeMessages(MSG_CLOSE_READER, bookChapterInfo);
        BufferedRandomAccessFile pageTxtReader=null;
        synchronized (cache) {
            pageTxtReader = cache.remove(bookChapterInfo);
        }

        if (pageTxtReader != null) {

            if(BuildConfig.DEBUG)
            {
                Log.e("user reader in cache:"+pageTxtReader+bookChapterInfo);
            }

            return pageTxtReader;
        }


        BufferedRandomAccessFile reader = null;
        try {
            String filePath = bookChapterInfo.getFilePath();
            reader = new BufferedRandomAccessFile(filePath,"r" );
            int code = TXTReader.regCode(filePath);
            reader.setCode(code);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return reader;


    }



    public void closeReader(IChapter bookChapterInfo,BufferedRandomAccessFile pageTxtReader)
    {

        synchronized (cache) {
            cache.put(bookChapterInfo, pageTxtReader);
        }
        handler.removeMessages(MSG_CLOSE_READER,bookChapterInfo);
        Message message = handler.obtainMessage();
        message.what=MSG_CLOSE_READER;
        message.obj=bookChapterInfo;
        handler.sendMessageDelayed(message,CLOSE_DELAY);

    }


    /**
     * 清除并关闭缓存的资源。
     */
    public void onDestroy() {


        handler.removeMessages(MSG_CLOSE_READER);
        synchronized (cache) {
            Collection<BufferedRandomAccessFile> values = cache.values();
            for (BufferedRandomAccessFile value : values) {

                realReleaseReader(value);
            }
            cache.clear();


        }

    }
}
