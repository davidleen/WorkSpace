package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.ItemPickDialog;
import com.giants.hd.desktop.local.DownloadFileManager;
import com.giants.hd.desktop.model.BaseListTableModel;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.QuotationPictureExportPresenter;
import com.giants.hd.desktop.mvp.viewer.QuotationPictureExportViewer;
import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.viewImpl.Panel_Quotation_Picture_Export;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Product;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 报价图片导出
 * Created by david on 2015/11/23.
 */
public class QuotationPictureExportFrame extends BaseMVPFrame<QuotationPictureExportViewer> implements QuotationPictureExportPresenter {


    List<Product> products;

    public QuotationPictureExportFrame() {
        super(ModuleConstant.TITLE_QUTOTATION_PICTURE_EXPORT);


        products = new ArrayList<>();


    }

    @Override
    protected QuotationPictureExportViewer createViewer() {
        return new Panel_Quotation_Picture_Export(this);
    }


    @Override
    public void searchProduct(String key,boolean includeCopy) {


        UseCaseFactory.getInstance().createProductByNameRandom(key, includeCopy).execute(new RemoteDataSubscriber<Product>(getViewer()) {


            @Override
            protected void handleRemoteData(RemoteData<Product> remoteData) {

                getViewer().hideLoadingDialog();
                if (remoteData.isSuccess()) {


                    if (remoteData.datas.size() <= 1) {
                        addProducts(remoteData.datas);
                    } else {


                        BaseListTableModel<Product> tableModel = new BaseListTableModel<Product>(Product.class, TableStructureUtils.getQuotationPictureModel());
                        //弹窗多选
                        ItemPickDialog<Product> itemPickDialog = new ItemPickDialog(getWindow(), "货号选择", remoteData.datas, tableModel);

                        itemPickDialog.setVisible(true);
                        List<Product> products = itemPickDialog.getResult();
                        if (products != null && products.size() > 0) {

                            addProducts(products);
                        }

                    }


                }


            }


        });
        getViewer().showLoadingDialog();

    }


    @Override
    public void exportPicture(final File directory) {


        final List<Product> newProduct = new ArrayList<>();
        newProduct.addAll(products);
        SwingWorker worker = new SwingWorker<String, Object>() {
            @Override
            protected String doInBackground() throws Exception {


                for (Product product : newProduct) {


                    String fileName = directory.getAbsolutePath() + File.separator + product.name + (StringUtils.isEmpty(product.pVersion) ? "" : ("-" + product.pVersion)) + ".JPG";
                    DownloadFileManager.download(HttpUrl.loadProductPicture(product.url), fileName);


                }

                return null;
            }

            @Override
            protected void done() {

                getViewer().hideLoadingDialog();

                String result = null;
                try {
                    result = get();
                    getViewer().showMesssage("导出图片成功，路径在：" + directory.getPath());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    getViewer().showMesssage("导出图片失败，原因：" + e.getMessage());

                } catch (ExecutionException e) {
                    e.printStackTrace();
                    getViewer().showMesssage("导出图片失败，原因：" + e.getMessage());


                }


            }


        };
        worker.execute();
        getViewer().showLoadingDialog();


    }


    @Override
    public void removeRows(int[] rows) {


        List<Product> removes=new ArrayList<>();

        int productLength=products.size();

        for(int index:rows)
        {
            if(index>=0&& index<productLength)
            removes.add(products.get(index));
        }

        products.removeAll(removes);
        getViewer().showProducts(products);

    }

    private void addProducts(List<Product> newAddProducts) {


        //去除冗余
        List<Product> removeFromOld = new ArrayList<>();

        for (Product product : products) {

            for (Product newProduct : newAddProducts) {

                if (product.id == newProduct.id) {
                    removeFromOld.add(product);
                    break;
                }
            }


        }


        products.removeAll(removeFromOld);
        products.addAll(0, newAddProducts);

        getViewer().showProducts(products);
    }


}
