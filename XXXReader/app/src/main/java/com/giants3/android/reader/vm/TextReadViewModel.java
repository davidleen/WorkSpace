package com.giants3.android.reader.vm;

import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.giants3.android.reader.activity.EpubBookFactory;
import com.giants3.android.reader.activity.TextBookFactory;
import com.giants3.file.FileContentType;
import com.xxx.reader.book.BookFactory;
import com.xxx.reader.book.IBook;

import java.io.File;

public class TextReadViewModel extends BaseViewModel {



    protected final MutableLiveData<IBook> bookInfo = new MutableLiveData<>();
    @Override
    public void handleIntent(Intent intent) {

        final String filePath = intent.getStringExtra("filePath");
        String bookId = intent.getStringExtra("bookId");
        String bookUrl = intent.getStringExtra("bookUrl");



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
            //finish();
            return;
        }

        getWaitMessage().setValue(true);
        new AsyncTask<Void, Void, IBook>() {
            @Override
            protected IBook doInBackground(Void[] objects) {

                IBook iBook =   bookFactory.create(filePath);;


                return iBook;
            }

            @Override
            protected void onPostExecute(IBook iBook) {
                super.onPostExecute(iBook);
                getWaitMessage().setValue(false);
                bookInfo.postValue(iBook);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public LiveData<IBook> getBookInfo()
    {
        return bookInfo;
    }
}
