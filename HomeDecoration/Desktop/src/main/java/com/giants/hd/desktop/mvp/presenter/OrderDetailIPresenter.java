package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.ErpOrderItem;

import java.io.File;

/**
 *
 * 订单详情展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderDetailIPresenter extends IPresenter {


    void save();
    public void delete();
    public void verify();

    void addPictureClick(File[] file);

    /**
     * 侧唛数据改变
     * @param value
     */
    void onCemaiChange(String value);
    /**
     * 内盒数据改变
     * @param value
     */
    void onNeihemaiChange(String value);

    /**
     * 正唛数据改变
     * @param value
     */
    void onZhengmaiChange(String value);

    /**
     * 备注数据改变
     * @param value
     */
    void onMemoChange(String value);

    /**
     * 删除附件
     * @param url
     */
    void onDeleteAttach(String url);


    /**
     * 打印订单明细数据
     * @param orderItem
     */
    void printPaint(ErpOrderItem orderItem);

    void showProductDetail(ErpOrderItem orderItem);

    /**
     * 右唛数据改变
     * @param value
     */
    void onYoumaiChange(String value);

    /**
     * 佐麦数据改变
     * @param trim
     */
    void onZuomaiChange(String trim);

    boolean isEditable();

    /**
     * 查看生产流程
     * @param finalItem
     */
    void showWorkFlow(ErpOrderItem finalItem);


    /**
     * 启动生产流程跟踪
     */
    void startOrderTrack();

    void updateMaitouFile(File file);

    void viewMaitou();
}
