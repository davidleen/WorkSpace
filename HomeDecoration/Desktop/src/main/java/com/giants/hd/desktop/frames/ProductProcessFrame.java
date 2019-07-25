package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.ProductProcessUpdateDialog;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.productprocess.ProductProcessPresenter;
import com.giants.hd.desktop.mvp.productprocess.ProductProcessViewer;
import com.giants.hd.desktop.viewImpl.Panel_ProductProcess;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;

/**
 * 工序对话框
 */
public class ProductProcessFrame extends BaseMVPFrame<ProductProcessViewer> implements ProductProcessPresenter {


    public ProductProcessFrame() {

        super(ModuleConstant.TITLE_PROCESS);
        init();
        loadData();
    }


    private void loadData() {


        UseCaseFactory.getInstance().createGetUseCase(HttpUrl.loadProductProcess(), ProductProcess.class).execute(new RemoteDataSubscriber<ProductProcess>(getViewer()) {


            @Override
            protected void handleRemoteData(RemoteData<ProductProcess> data) {

                viewer.setData(data.datas);
            }


        });
        getViewer().showLoadingDialog();
    }



    @Override
    public  void addOne()
    {
        ProductProcessUpdateDialog dialog = new ProductProcessUpdateDialog(this.getWindow());
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        ProductProcess productProcess = dialog.getResult();
        if (productProcess != null) {
            //重新加载
            loadData();

        }
    }

    @Override
    public void updateItem(ProductProcess item) {
        ProductProcessUpdateDialog dialog = new ProductProcessUpdateDialog(this.getWindow(),item);
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        ProductProcess productProcess = dialog.getResult();
        if (productProcess != null) {
            //重新加载
            loadData();

        }
    }


    //    @Override
//    protected RemoteData<ProductProcess> saveData(List<ProductProcess> datas) throws HdException {
//      return   apiManager.saveProductProcess(datas);
//    }


    protected void init() {


//        TableMouseAdapter adapter=new TableMouseAdapter(new TablePopMenu.TableMenuLister() {
//            @Override
//            public void onTableMenuClick(int index, BaseTableModel tableModel, int[] rowIndex) {
//
//                switch (index) {
//
//
//                    case TablePopMenu.ITEM_INSERT:
//
//                        tableModel.addNewRow(rowIndex[0]);
//
//                        break;
//                    case TablePopMenu.ITEM_DELETE:
//
//
//
//
//                        tableModel.deleteRows(rowIndex);
//                        break;
//                }
//
//
//
//            }
//        });


//        panel_productProcess.jt_process.addMouseListener(adapter);


    }


    @Override
    protected ProductProcessViewer createViewer() {
        return new Panel_ProductProcess(this);
    }
}
