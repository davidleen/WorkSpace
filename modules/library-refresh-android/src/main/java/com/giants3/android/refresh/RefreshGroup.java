package com.giants3.android.refresh;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class RefreshGroup extends FrameLayout implements  RefreshApi {
    private static final int delay=1000;

    private SmartRefreshLayout smartRefreshLayout;
    private PullRefreshListener pullRefreshListener;
    private LoadMoreListener loadMoreListener;

    private View refreshContent;
    public RefreshGroup(@NonNull Context context) {
        this(context,null);
    }

    public RefreshGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        removeAllViews();
        smartRefreshLayout = (SmartRefreshLayout) LayoutInflater.from(context).inflate(R.layout.view_refresh_group, null);
        refreshContent=smartRefreshLayout.findViewById(R.id.refresh_content);
        addView(smartRefreshLayout);




    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RefreshGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    @Override
    public void finishRefresh() {

        smartRefreshLayout.finishRefresh(delay);

    }

    @Override
    public void finishLoadMore(boolean success,boolean hasMore) {
        smartRefreshLayout.finishLoadMore(delay,success,!hasMore);
    }

    @Override
    public void startRefresh() {

        smartRefreshLayout.autoRefresh();

    }

    @Override
    public void startLoadMore() {
        smartRefreshLayout.autoLoadMore();
    }

    @Override
    public void setPullRefreshListener(PullRefreshListener listener) {

        pullRefreshListener = listener;
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (pullRefreshListener!=null) {

                    pullRefreshListener.onPullToRefresh();
                }
            }

             
        });
    }

    @Override
    public void setLoadMoreListener(LoadMoreListener listener) {

        loadMoreListener = listener;
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (loadMoreListener!=null) {

                    loadMoreListener.onDragToLoadMore();
                }
            }
        });


    }

    @Override
    public void setEnableLoadMore(boolean enableLoadMore) {

        smartRefreshLayout.setEnableLoadMore(enableLoadMore);
    }

    @Override
    public void setEnableRefresh(boolean enableRefresh) {

        smartRefreshLayout.setEnableRefresh(enableRefresh);


    }

    @Override
    public void setContentView(View contentView) {
        refreshContent=contentView;
        smartRefreshLayout.setRefreshContent(contentView);

    }

    @Override
    public <T extends View> T getContentView() {
        return (T) refreshContent;
    }
}
