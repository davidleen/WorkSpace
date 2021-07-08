package com.giants3.android.reader.vm;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.reader.activity.DownLoadBookTask;
import com.giants3.android.reader.activity.EpubBookFactory;
import com.giants3.android.reader.activity.TextBookFactory;
import com.github.mzule.activityrouter.router.Routers;
import com.xxx.reader.TextScheme;
import com.giants3.android.reader.scheme.TextSchemeLoader;
import com.giants3.file.FileContentType;
import com.xxx.reader.TextSchemeContent;
import com.xxx.reader.ThreadConst;
import com.xxx.reader.book.BookFactory;
import com.xxx.reader.book.IBook;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TextReadViewModel extends BaseViewModel {


    public final MutableLiveData<Boolean> schemeChange = new MutableLiveData<>();
    public final MutableLiveData<Boolean> dayModeLiveData = new MutableLiveData<>();

    protected final MutableLiveData<IBook> bookInfo = new MutableLiveData<>();
    public final MutableLiveData<List<TextScheme>> textSchemes = new MutableLiveData<>();
    public TextScheme[] allSettingThemes;

    public TextReadViewModel(@NonNull Application application) {
        super(application);
    }
    private String bookId;
    private String bookUrl;
    @Override
    public void handleIntent(Intent intent) {

        final String filePath = intent.getStringExtra("filePath");
          bookId = intent.getStringExtra("bookId");
          bookUrl = intent.getStringExtra("bookUrl");
        updateBook(filePath, bookId, bookUrl);

        initThemes();

    }

    public void updateBook(long bookId,final String bookName, final String bookUrl) {


        this.bookId =String.valueOf(bookId);
        this.bookUrl = bookUrl;


        Uri parse = Uri.parse(bookUrl);

        //下载url，打开书籍
        final String path = StorageUtils.getFilePath(parse.getLastPathSegment());

        if (new File(path).exists()) {
            updateBook(path, bookName, bookUrl);
        } else {
            getWaitMessage().setValue(true);
            new DownLoadBookTask(bookUrl, path) {


                @Override
                protected void onPostExecute(Boolean o) {
                    super.onPostExecute(o);
                    getWaitMessage().setValue(false);
                    if (o) {
                        updateBook(path, bookName, bookUrl);
                    }


                }
            }.execute();

        }
    }


    public void updateBook(final String filePath, String bookName, String bookUrl) {





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
                bookInfo.postValue(iBook);
                waitMessageLiveData.postValue(false);


            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initThemes() {

        new AsyncTask<Void, Void, TextScheme[]>() {
            @Override
            protected TextScheme[] doInBackground(Void... voids) {
                return TextSchemeLoader.loadAllScheme(getApplication());
            }

            @Override
            protected void onPostExecute(TextScheme[] dataArray) {

                allSettingThemes = dataArray;


            }
        }.executeOnExecutor(ThreadConst.THREAD_POOL_EXECUTOR);


    }

    public LiveData<IBook> getBookInfo() {
        return bookInfo;
    }

    public void updateScheme(TextScheme item) {
        TextSchemeContent.setTextScheme(item);
        schemeChange.postValue(true);


    }

    public void onSettingPanelShow() {

        updateSchemes();


    }

    private void updateSchemes() {
        if (allSettingThemes == null) return;
        List<TextScheme> result = new ArrayList<>();

        for (TextScheme item : allSettingThemes) {


            if (item.getStyleMode() == TextSchemeContent.getMode()) {
                result.add(item);
            }
        }


        textSchemes.postValue(result);
    }

    public void changeStyleMode() {

        boolean dayMode = TextSchemeContent.getDayMode();

        TextSchemeContent.setDayMode(!dayMode);
        updateSchemes();
        dayModeLiveData.postValue(true);


    }

    public void goChapterList() {


        Routers.open(getApplication(),"XXX://chapterList?bookUrl="+ URLEncoder.encode(bookUrl));
    }
}
