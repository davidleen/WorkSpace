package com.giants.hd.desktop.interf;

/**
 * 分页数据改变监听类
 */
public interface PageListener {


    /**
     * 页面请求改变候监听
     * @param pageIndex
     * @param pageSize
     */
    public void onPageChanged(int pageIndex, int pageSize);
}
