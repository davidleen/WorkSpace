package changu.com.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.giants3.android.reader.domain.UseCaseFactory;
import com.giants3.android.reader.domain.UseCaseHandler;
import com.giants3.reader.entity.Book;
import com.giants3.reader.noEntity.ComicChapterInfo;
import com.giants3.reader.noEntity.RemoteData;

import butterknife.Bind;

public class Main2Activity extends BaseActivity {

    @Bind(R.id.list)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final BookListAdapter adapter = new BookListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Book book=adapter.getItem(position);

                Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                intent.putExtra(MainActivity.KEY_BOOK_ID,book.id);
                startActivity(intent);



            }
        });
        UseCaseFactory.getInstance().createGetBookListUseCase(HttpUrl.getComicBookList()).execute(new UseCaseHandler<RemoteData<Book>>() {
            @Override
            public void onCompleted() {


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RemoteData<Book> remoteData) {

                if (remoteData.isSuccess()) {

                    adapter.setDataArray(remoteData.datas);

                }

            }
        });
    }
}
