package com.giants3.android.reader.activity;



import android.content.Intent;
import android.net.Uri;

import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.frame.util.ToastHelper;
import com.giants3.android.reader.adapter.BookListRecyclerViewAdapter;
import com.giants3.android.reader.vm.BookListViewModel;
import com.giants3.reader.entity.Book;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;
import com.xxx.reader.Url2FileMapper;

import java.io.File;

@Router(value = "bookList")
public class BookListActivity extends BaseListViewModelActivity<BookListViewModel, Book> {

//
//    @Override
//    protected Class<BookListViewModel> getViewModelClass() {
//        return BookListViewModel.class;
//    }


    @Override
    protected AbstractRecyclerAdapter createAdapter() {
        return new BookListRecyclerViewAdapter(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemClick(AbstractRecyclerAdapter adapter, Book item, int position) {



        if(!StringUtil.isEmpty(item.url))
        {
            Uri parse = Uri.parse(item.url);

            //下载url，打开书籍
            final String path= StorageUtils.getFilePath(parse.getLastPathSegment());

            if(new File(path).exists())
            {
                openBook(path);
            }
            else
            {

                new DownLoadBookTask(item.url,path)
                {




                    @Override
                    protected void onPostExecute(Boolean o) {
                        super.onPostExecute(o);
                        if(o)
                        {
                            openBook(path);
                        }





                    }
                }
                        .execute();

            }




        }else
        {
//            Intent intent=new Intent(this,ChapterListActivity.class)
//                    ;
//
//            intent.putExtra("bookid",item.id);
//            startActivity(intent);
            Routers.openForResult(this, Uri.parse("XXX://chapterList?bookId="+item.id),111);
        }









    }


    private void openBook(String path)
    {
        Intent intent = new Intent(BookListActivity.this, TextReadActivity.class);
        intent.putExtra("filePath", path);
        startActivity(intent);
    }


}
