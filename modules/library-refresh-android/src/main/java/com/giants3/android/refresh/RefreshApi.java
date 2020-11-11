package com.giants3.android.refresh;

import android.view.View;

public interface RefreshApi {
    void finishRefresh();
    void finishLoadMore(boolean success,boolean hasMore);
    void startRefresh();
    void startLoadMore();

    void setPullRefreshListener(PullRefreshListener listener);
    void setLoadMoreListener(LoadMoreListener listener);

    void setEnableLoadMore(boolean enable);
    void setEnableRefresh(boolean enable);

    void setContentView(View contentView);
    <T extends View> T getContentView();
}
