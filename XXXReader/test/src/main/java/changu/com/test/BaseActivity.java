package changu.com.test;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by davidleen29 on 2018/11/25.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        ButterKnife.bind(this);
    }
}