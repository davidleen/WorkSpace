package com.giants3.android.reader.vm;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.HttpUrl;
import com.giants3.android.reader.activity.EpubBookFactory;
import com.giants3.android.reader.activity.TextBookFactory;
import com.giants3.file.FileContentType;
import com.giants3.reader.entity.Book;
import com.giants3.reader.entity.Chapter;
import com.giants3.reader.noEntity.RemoteData;
import com.xxx.reader.book.BookFactory;
import com.xxx.reader.book.IBook;
import com.xxx.reader.book.IChapter;

import java.io.File;
import java.util.List;

public class ChapterListViewModel extends BaseListViewModel<IChapter>  {


    long bookId;
    String url;




    public ChapterListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void loadData() {
        loadChapter(bookId,url);
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);


        bookId=intent.getLongExtra("bookId",0);
        url=intent.getStringExtra("bookUrl");


    }




    private void loadChapter(long bookId,String bookUrl)
    {

        if(!StringUtil.isEmpty(bookUrl))
        {

            Uri parse = Uri.parse(bookUrl);
            final String filePath = StorageUtils.getFilePath(parse.getLastPathSegment());

            String contentType = FileContentType.getContentType(new File(filePath));
            final BookFactory bookFactory;
            switch (contentType) {
                case FileContentType.EPUB: {
                    bookFactory = new EpubBookFactory();
                }
                break;
                case FileContentType.TEXT: {

                    bookFactory = new TextBookFactory();
                }
                break;
                default:
                    bookFactory = null;

            }

            if (bookFactory == null) {
                //finish();
                return;
            }

            getWaitMessage().setValue(true);
            new AsyncTask<Void, Void, IBook>() {
                @Override
                protected IBook doInBackground(Void[] objects) {

                    IBook iBook = bookFactory.create(filePath);
                    ;


                    return iBook;
                }

                @Override
                protected void onCancelled(IBook iBook) {
                    super.onCancelled(iBook);
                    getWaitMessage().setValue(false);
                }

                @Override
                protected void onPostExecute(IBook iBook) {
                    super.onPostExecute(iBook);
                    getWaitMessage().setValue(false);

                    RemoteData<IChapter> remoteData=new RemoteData<>();
                    remoteData.datas=iBook.getChapters();
                    remoteData.code=RemoteData.CODE_SUCCESS;
                    remoteData.pageIndex=0;
                    remoteData.pageCount=remoteData.datas.size();
                    remoteData.pageCount=remoteData.datas.size();
                    listResult.postValue(remoteData);



                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        }









    }

}
