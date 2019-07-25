package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.productprocess.ProductProcessUpdatePresenter;
import com.giants.hd.desktop.mvp.productprocess.ProductProcessUpdateViewer;
import com.giants.hd.desktop.viewImpl.Panel_ProductProcessUpdate;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.GsonUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 材料分类更新
 */
public class ProductProcessUpdateDialog extends BaseDialog<ProductProcess> implements ProductProcessUpdatePresenter {


    ProductProcess oldData;
    ProductProcess productProcess;
    private ProductProcessUpdateViewer viewer;


    public ProductProcessUpdateDialog(Window window) {
        this(window, null);

    }


    public ProductProcessUpdateDialog(Window window, ProductProcess materialClass) {
        super(window, materialClass == null ? "添加工序" : "工序详情");
        setMinimumSize(new Dimension(500, 600));
        viewer = new Panel_ProductProcessUpdate(this);
        setContentPane(viewer.getRoot());


        setData(materialClass);


    }

    private void setData(ProductProcess materialClass) {


        if (materialClass == null) {
            this.productProcess = new ProductProcess();
        } else {
            this.productProcess = materialClass;
        }

        oldData = GsonUtils.fromJson(GsonUtils.toJson(this.productProcess), ProductProcess.class);
        viewer.bindData(this.productProcess);
    }


    @Override
    public void save() {


        if (hasModifyData()) {


            UseCaseFactory.getInstance().createPostUseCase(HttpUrl.updateProductProcess(), productProcess, ProductProcess.class).execute(new RemoteDataSubscriber<ProductProcess>(viewer) {


                @Override
                protected void handleRemoteData(RemoteData<ProductProcess> data) {

                    if (data.isSuccess()) {
                        viewer.showMesssage("保存成功！");
                        final ProductProcess productProcess = data.datas.get(0);
                        setData(productProcess);
                        setResult(productProcess);
                    }

                }


            });


            viewer.showLoadingDialog();
        } else {


            viewer.showMesssage("数据无改变");

        }

    }

    @Override
    public void delete() {


        int res = JOptionPane.showConfirmDialog(this, "是否删除该工序？（导致数据无法恢复）", "删除", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.YES_OPTION) {

            UseCaseFactory.getInstance().createDeleteUseCase(HttpUrl.deleteProductProcess(productProcess.id), Void.class).execute(new RemoteDataSubscriber<Void>(viewer) {
                @Override
                protected void handleRemoteData(RemoteData<Void> data) {
                    if (data.isSuccess()) {
                        viewer.showMesssage("删除成功！");
                        setResult(productProcess);
                        dispose();
                    }
                }
            });


        }

    }

    @Override
    public void updateName(String name) {
        productProcess.name=name;

    }

    @Override
    public void updateMemo(String memo) {

        productProcess.memo=memo;
    }

    @Override
    public void updateCode(String code) {

        productProcess.code=code;
    }

    @Override
    public boolean hasModifyData() {


        final String s = GsonUtils.toJson(productProcess);
        final String oldString = GsonUtils.toJson(oldData);
        //     System.out.print( s+"===" +oldString);
        return !s.equals(oldString);
    }

    @Override
    public void close() {




        if (hasModifyData()) {


            int option = JOptionPane.showConfirmDialog(ProductProcessUpdateDialog.this, "数据有改动，确定关闭窗口？", " 提示", JOptionPane.OK_CANCEL_OPTION);

            if (JOptionPane.OK_OPTION == option) {
                //点击了确定按钮
                ProductProcessUpdateDialog.this.dispose();
            }

        } else {
            dispose();
        }


    }


}
