package com.giants3.hd.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giants3.hd.android.R;

import com.giants3.hd.android.helper.ImageLoaderFactory;
import com.giants3.android.frame.util.ToastHelper;
import com.giants3.hd.android.presenter.ProductDetailPresenter;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.app.AProduct;
import com.giants3.hd.noEntity.RemoteData;

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
public class ProductListActivity extends BaseActivity {


    private boolean mTwoPane;


    private List<AProduct> products = new ArrayList<>();

    @BindView(R.id.productlistactivity_list)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    SimpleItemRecyclerViewAdapter
            adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlistactivity_list);
        // setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }


        UseCaseFactory.getInstance().createProductListCase("", 0, 100).execute(new Subscriber<RemoteData<AProduct>>() {
            @Override
            public void onCompleted() {
                showProgress(false);


            }

            @Override
            public void onError(Throwable e) {

                showProgress(false);

                ToastHelper.show(e.getMessage());


            }

            @Override
            public void onNext(RemoteData<AProduct> aUser) {


                adapter.setData(aUser.datas);


//                ToastHelper.show("登录成功");
//
//
//                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();


            }
        });

        showProgress(true);


        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.productlistactivity_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }


    /**
     * 显示加载
     *
     * @param b
     */
    private void showProgress(boolean b) {


    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        adapter = new SimpleItemRecyclerViewAdapter(this, products);
        recyclerView.setAdapter(adapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<AProduct> mValues;

        private Context context;


        public SimpleItemRecyclerViewAdapter(Context context, List<AProduct> items) {
            this.context = context;
            mValues = items;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {


            AProduct aProduct = mValues.get(position);
            holder.mItem = aProduct;
            holder.mIdView.setText(aProduct.name);
            holder.mContentView.setText(aProduct.pVersion);


            ImageLoaderFactory.getInstance().displayImage(HttpUrl.completeUrl(aProduct.thumbnail), holder.image);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Context context = v.getContext();
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra(ProductDetailPresenter.ARG_ITEM, GsonUtils.toJson(holder.mItem));

                        context.startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public void setData(List<AProduct> data) {
            this.mValues = data;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public final ImageView image;
            public AProduct mItem;

            public ViewHolder(View view) {
                super(view);
                // ButterKnife.BindView(this, view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                image = (ImageView) view.findViewById(R.id.image);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
