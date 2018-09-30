package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.WorkFlowConfigDialog;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.mvp.presenter.ProductDetailIPresenter;
import com.giants.hd.desktop.viewImpl.BasePanel;
import com.giants.hd.desktop.viewImpl.Panel_ProductDetail;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.ProductDelete;
import com.giants3.hd.noEntity.ProductDetail;
import com.google.inject.Inject;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

/**
 *  产品详细模块
 */
public class ProductDetailFrame extends BaseFrame implements ProductDetailIPresenter {



    @Inject
    ApiManager apiManager;
    @Inject
    Panel_ProductDetail panel_productDetail;
    ProductDelete productDelete =null;



    java.util.List<String> attachStrings = new ArrayList<>();


    private BasePanel.PanelAdapter adapter=new ProductDetailAdapter();
   public   ProductDetailFrame(ProductDetail productDetail )
    {
        this(productDetail, null);


    }

    public   ProductDetailFrame(ProductDetail productDetail,ProductDelete productDelete )
    {


        super();
        this.productDelete =productDelete;


        setTitle("产品详情[" + (productDetail.product == null ? "新增" : ("货号：" + productDetail.product.getName() + "---版本号：" + productDetail.product.getpVersion())) + "]" + (productDelete!=null ? "    [已删除]   " : ""));

        panel_productDetail.setPresenter(this);
        init( );

        panel_productDetail.setProductDetail(productDetail,productDelete);
    }



    public void init()
    {






        panel_productDetail.setListener(adapter);
        setContentPane(panel_productDetail.getRoot());
        setMinimumSize(new Dimension(1024, 600));
        pack();





    }

    @Override
    public boolean hasModifyData() {
        if(panel_productDetail==null||panel_productDetail.productDetail==null|| productDelete!=null)
        {

            return false;
        }

       return panel_productDetail.isModified() ;
    }

    /**
     * 显示产品详情
     * @param product
     * @Param component
     *
     */
    public      ProductDetailFrame(final Product product  )
    {

        super("产品详情[" + (product == null ? "新增" : ("货号：" + product.getName() + "---版本号：" + product.getpVersion())) + "]");
        panel_productDetail.setPresenter(this);
        init();
        if(product==null)
        {

            panel_productDetail.setProductDetail(null);

        }
         else
        {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    loadProductDetail(product);
                }
            });

        }
    }

    @Override
    public void addPackagePicture(File[] selectedFiles) {


        //上传图片
        UseCaseFactory.getInstance().uploadTempFileUseCase(selectedFiles).execute(new Subscriber<java.util.List<String>>() {
            @Override
            public void onCompleted() {
                panel_productDetail.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panel_productDetail.hideLoadingDialog();
                panel_productDetail.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(java.util.List<String> stringList) {
                attachStrings.addAll(stringList);
                panel_productDetail.showPackAttachFiles(attachStrings);
            }
        });
        panel_productDetail.showLoadingDialog("正在上传图片。。。");



    }

    @Override
    public void save( final ProductDetail productDetail) {
        new HdSwingWorker<ProductDetail, Object>(this.getOwner()) {
            @Override
            protected RemoteData<ProductDetail> doInBackground() throws Exception {

                return apiManager.saveProduct(productDetail);
            }

            @Override
            public void onResult(RemoteData<ProductDetail> data) {
              //  panel_productDetail.hideLoadingDialog();
                if (data.isSuccess()) {
                    // 显示保存成功
                    panel_productDetail.showMesssage("数据保存成功!");

                    panel_productDetail.setProductDetail(data.datas.get(0));


                } else {
                    panel_productDetail.showMesssage(data.message);

                }
            }
        }.go();

      //  panel_productDetail.showLoadingDialog("正在提交保存。。。·");


    }


    @Override
    public void showProductWorkFlow() {




        if(panel_productDetail.getData().product.id<=0)
        {
            panel_productDetail.showMesssage("产品未建立，请先保存");
            return;
        }


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Product product=panel_productDetail.getData().product;
                  WorkFlowConfigDialog workFlowProductFrame=new WorkFlowConfigDialog(ProductDetailFrame.this,product.id,product.name+ (StringUtils.isEmpty(product.pVersion)?"":("-"+product.pVersion)));


                workFlowProductFrame.setVisible(true);
            }
        });








    }

    private class ProductDetailAdapter extends BasePanel.PanelAdapter
    {

        @Override
        public void save() {


        }

        @Override
        public void delete() {



            if(productDelete!=null) return;


            final ProductDetail detail=panel_productDetail.getData();

            if(detail.product.id<=0)
            {

                JOptionPane.showMessageDialog(ProductDetailFrame.this, "产品数据未建立，请先保存");
                return;

            }



            int res=   JOptionPane.showConfirmDialog(ProductDetailFrame.this, "是否删除产品？（导致数据无法恢复）", "删除产品", JOptionPane.OK_CANCEL_OPTION);
            if(res==JOptionPane.YES_OPTION)
            {
                new HdSwingWorker<Void,Void>(ProductDetailFrame.this)
                {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {

                        return     apiManager.deleteProductLogic(detail.product.id);

                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {

                        if(data.isSuccess())
                        {

                            JOptionPane.showMessageDialog(ProductDetailFrame.this,"删除成功！");

                            ProductDetailFrame.this.dispose();



                        }else
                        {
                            JOptionPane.showMessageDialog(ProductDetailFrame.this,data.message);
                        }

                    }
                }.go();



            }


        }

        @Override
        public void close() {
            setVisible(false);
            dispose();
        }


    }





    /**
     * 加载产品详情信息
     */
    private void loadProductDetail(final Product product) {


        new HdSwingWorker<ProductDetail, Long>(this) {
            @Override
            protected RemoteData<ProductDetail> doInBackground() throws Exception {
                return apiManager.loadProductDetail(product.id);
            }

            @Override
            public void onResult(RemoteData<ProductDetail> data) {

                if(data.isSuccess()) {

                    ProductDetail detail = data.datas.get(0);

                    panel_productDetail.setProductDetail(detail);
                }else
                {

                    JOptionPane.showMessageDialog(ProductDetailFrame.this,data.message);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            adapter.close();
                        }
                    });

                }
            }
        }.go();


    }


    /**
     * 修复缩略图
     */
    @Override
    public void correctThumbnail(long productId) {



        //上传图片
        UseCaseFactory.getInstance().createCorrectThumbnaiUseCase( productId).execute(new Subscriber<RemoteData<Void>>() {
            @Override
            public void onCompleted() {
                panel_productDetail.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panel_productDetail.hideLoadingDialog();
                panel_productDetail.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<Void> data) {

                if(data.isSuccess())
                panel_productDetail.showMesssage("图片修复成功");
                else
                {
                    panel_productDetail.showMesssage(data.message);
                }
            }
        });
        panel_productDetail.showLoadingDialog("处理中。。。");






    }
}
