package changu.com.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xxx.reader.ReaderView;
import com.xxx.reader.Utils;
import com.xxx.reader.comic.ComicPrepareLayer;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.download.DownloadListener;
import com.xxx.reader.empty.SimpleBitmapProvider;
import com.xxx.reader.prepare.Cancelable;
import com.xxx.reader.prepare.DrawLayer;
import com.xxx.reader.prepare.PagePlayer;
import com.xxx.reader.text.layout.BitmapProvider;
import com.xxx.reader.turnner.ScrollPageTurner;
import com.xxx.reader.turnner.sim.SimPageTurner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.init(getApplicationContext());
        ReaderView readerView= (ReaderView) findViewById(R.id.reader);
        int[] wh= Utils.getScreenDimension(this);




        PagePlayer comicPagePlayer =  ComicPrepareLayer.createComicPrepareLayer(this, wh[0], wh[1], readerView, new DownloadListener() {
            @Override
            public void startDownload(String url, String filePath, Cancelable cancelable, NotifyListener notifyListener) {

            }
        });

        //SimpleBitmapProvider provider=new SimpleBitmapProvider(this,wh[0],wh[1]);
      BitmapProvider provider= comicPagePlayer;

        DrawLayer drawLayer = new DrawLayer(this,comicPagePlayer,readerView);

        PageSwitchListener pageSwitchListener=new PageSwitchListener() {
            @Override
            public boolean canPageChanged(int direction) {
                return true;
            }

            @Override
            public void beforePageChanged(int direction) {

            }

            @Override
            public void afterPageChanged(int direction) {

            }

            @Override
            public void onPageTurnFail(int turnMoveDirection) {

            }
        };
        IPageTurner pageTurner=null;
       //  pageTurner=new ScrollPageTurner(this,pageSwitchListener,readerView,provider);
         pageTurner=new SimPageTurner(this,pageSwitchListener,readerView,provider);
        drawLayer.setPageTurner(pageTurner);
        readerView.setDrawLayer(drawLayer);



    }
}
