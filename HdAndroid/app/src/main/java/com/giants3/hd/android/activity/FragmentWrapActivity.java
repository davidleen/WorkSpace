package com.giants3.hd.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.giants3.android.frame.util.ToastHelper;
import com.giants3.hd.android.R;
import com.giants3.hd.android.helper.ImageLoaderFactory;
import com.giants3.hd.android.mvp.AndroidRouter;
import com.giants3.hd.android.presenter.ProductDetailPresenter;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.app.AProduct;
import com.giants3.hd.noEntity.RemoteData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ProductDetailActivity} representing
 * item_work_flow_report details. On tablets, the activity presents the list of items and
 * item_work_flow_report details side-by-side using two vertical panes.
 */
public class FragmentWrapActivity extends BaseActionBarActivity {


    private static final String PARA_FRAGMENT_CLASS="PARA_FRAGMENT_CLASS";


    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String FragmentClassName=getIntent().getStringExtra(PARA_FRAGMENT_CLASS);
        Fragment fragment= null;
        try {
            fragment = (Fragment) Class.forName(FragmentClassName).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(fragment==null)
        {
            finish();
            return;
        }

        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    @Override
    protected View getContentView() {
        return View.inflate(this,R.layout.activity_fragment_wrap,null);
    }


    public static  void start(AndroidRouter router,  Class fragmentClass, Bundle bundle,int requestCode)
    {


        Intent intent=new Intent(router.getContext(),FragmentWrapActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(PARA_FRAGMENT_CLASS,fragmentClass.getName());
        router.startActivityForResult(intent,requestCode);


    }
}
