package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.presenter.MaterialClassUpdateIPresenter;
import com.giants.hd.desktop.mvp.viewer.MaterialClassUpdateViewer;
import com.giants.hd.desktop.viewImpl.Panel_MaterialClassUpdate;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.MaterialClass;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;

/**
 * 材料分类更新
 */
public class MaterialClassUpdateDialog extends BaseDialog<MaterialClass> implements MaterialClassUpdateIPresenter {


    MaterialClass oldData;
    MaterialClass materialClass;
    private MaterialClassUpdateViewer viewer;


    public MaterialClassUpdateDialog(Window window) {
        this(window, null);

    }


    public MaterialClassUpdateDialog(Window window, MaterialClass materialClass) {
        super(window, "材料分类");
        setMinimumSize(new Dimension(500,600));
        viewer = new Panel_MaterialClassUpdate(this);
        setContentPane(viewer.getRoot());


        setData(materialClass);


    }

    private void setData(MaterialClass materialClass) {


        if (materialClass == null) {
            this.materialClass = new MaterialClass();
        } else {
            this.materialClass = materialClass;
        }

        oldData = GsonUtils.fromJson(GsonUtils.toJson(this.materialClass), MaterialClass.class);
        viewer.bindData(this.materialClass);
    }


    @Override
    public void save() {


        viewer.getData(materialClass);

        if (hasModifyData()) {


            UseCaseFactory.getInstance().createUpdateMaterialClassUseCase(materialClass).execute(new Subscriber<RemoteData<MaterialClass>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    viewer.hideLoadingDialog();
                    viewer.showMesssage(e.getMessage());

                }


                @Override
                public void onNext(RemoteData<MaterialClass> data) {

                    viewer.hideLoadingDialog();
                    if (data.isSuccess()) {
                        viewer.showMesssage("保存成功！");
                        final MaterialClass materialClass = data.datas.get(0);
                        setData(materialClass);
                        setResult(materialClass);
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


        int res = JOptionPane.showConfirmDialog(this, "是否删除该材料分类？（导致数据无法恢复）", "删除材料分类", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.YES_OPTION) {

            UseCaseFactory.getInstance().createDeleteMaterialClassUseCase(materialClass.id).execute(new Subscriber<RemoteData<Void>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    viewer.hideLoadingDialog();
                    viewer.showMesssage(e.getMessage());

                }


                @Override
                public void onNext(RemoteData<Void> data) {

                    viewer.hideLoadingDialog();
                    if (data.isSuccess()) {


                        viewer.showMesssage("删除成功！");
                        setResult(materialClass);
                        dispose();
                    }
                }

            });


        }

    }

    @Override
    public boolean hasModifyData() {


        final String s = GsonUtils.toJson(materialClass);
        final String oldString = GsonUtils.toJson(oldData);
   //     System.out.print( s+"===" +oldString);
        return !s.equals(oldString);
    }

    @Override
    public void close() {


        viewer.getData(materialClass);

        if (hasModifyData()) {


            int option = JOptionPane.showConfirmDialog(MaterialClassUpdateDialog.this, "数据有改动，确定关闭窗口？", " 提示", JOptionPane.OK_CANCEL_OPTION);

            if (JOptionPane.OK_OPTION == option) {
                //点击了确定按钮
                MaterialClassUpdateDialog.this.dispose();
            }

        } else {
            dispose();
        }


    }


}
