package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.local.DownloadFileManager;
import com.giants.hd.desktop.mvp.presenter.OrderDetailIPresenter;
import com.giants3.report.jasper.ProductPaintReport;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.mvp.viewer.OrderDetailViewer;
import com.giants.hd.desktop.viewImpl.Panel_Order_Detail;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.*;
import com.giants3.hd.entity.ErpOrder;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.noEntity.ErpOrderDetail;
import com.giants3.hd.noEntity.ProductDetail;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * 订单详情界面
 * Created by davidleen29 on 2015/8/24.
 */
public class OrderDetailFrame extends BaseFrame implements OrderDetailIPresenter {

    java.util.List<String> attachStrings = new ArrayList<>();

    private ErpOrderDetail orderDetail;
    private String oldData;
    private OrderDetailViewer orderDetailViewer;

    private String os_no;

    public OrderDetailFrame(String orderNo) {
        super();
        initPanel(orderNo);

    }

    public OrderDetailFrame(ErpOrder order) {
        super();
        setTitle(order == null ? "订单详情" : "订单：" + order.os_no);
        initPanel(order.os_no);

    }

    private void initPanel(String os_no) {
        this.os_no=os_no;
        setMinimumSize(new Dimension(1080, 800));
        orderDetailViewer = new Panel_Order_Detail(this);
        setContentPane(orderDetailViewer.getRoot());


        //设置权限相关
        orderDetailViewer.setEditable(AuthorityUtil.getInstance().editOrder());
        orderDetailViewer.setPriceVisible(CacheManager.getInstance().isOrderPriceVisible());
        orderDetailViewer.setCanViewProduct(AuthorityUtil.getInstance().viewProductList());
        loadOrderDetail(os_no);
    }


    @Override
    public boolean hasModifyData() {

        if (orderDetailViewer == null || orderDetail == null) {
            return false;
        }


        String newAttachString = StringUtils.combine(attachStrings);
        if (!StringUtils.isEmpty(newAttachString))
            orderDetail.erpOrder.attaches = newAttachString;
        return !GsonUtils.toJson(orderDetail).equals(oldData);
    }

    public void loadOrderDetail(String os_no) {


        UseCaseFactory.getInstance().createOrderDetailUseCase(os_no).execute(new Subscriber<ErpOrderDetail>() {
            @Override
            public void onCompleted() {
                orderDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(ErpOrderDetail orderDetail) {
                setOrderDetail(orderDetail);

            }
        });

        orderDetailViewer.showLoadingDialog();
    }


    private void setOrderDetail(ErpOrderDetail orderDetail) {
        setTitle(orderDetail == null || orderDetail.erpOrder == null ? "订单详情" : "订单：" + orderDetail.erpOrder.os_no);
        oldData = GsonUtils.toJson(orderDetail);
        this.orderDetail = orderDetail;

        orderDetailViewer.setOrderDetail(orderDetail);
        attachStrings = StringUtils.isEmpty(orderDetail.erpOrder.attaches) ? new ArrayList<String>() : (ArrayUtils.changeArrayToList(orderDetail.erpOrder.attaches.split(";")));
        orderDetailViewer.showAttachFiles(attachStrings);

    }

    /**
     * 打印产品油漆数据
     */
    @Override
    public void printPaint(final ErpOrderItem orderItem) {

        //读取产品详细

        UseCaseFactory.getInstance().createGetProductByPrdNo(orderItem.prd_name).execute(new Subscriber<ProductDetail>() {
            @Override
            public void onCompleted() {
                orderDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(final ProductDetail productDetail) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        new ProductPaintReport(productDetail, orderDetail.erpOrder, orderItem).report();
                    }
                });

            }
        });

        orderDetailViewer.showLoadingDialog();


    }


    /**
     * 显示产品分析表明细
     */
    @Override
    public void showProductDetail(ErpOrderItem orderItem) {
        UseCaseFactory.getInstance().createGetProductByPrdNo(orderItem.prd_name).execute(new Subscriber<ProductDetail>() {
            @Override
            public void onCompleted() {
                orderDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(ProductDetail productDetail) {


                HdSwingUtils.showDetailPanel(OrderDetailFrame.this, productDetail);

            }
        });
        orderDetailViewer.showLoadingDialog();
    }

    @Override
    public void save() {

        orderDetail.erpOrder.attaches = StringUtils.combine(attachStrings);
        UseCaseFactory.getInstance().saveOrderDetail(orderDetail).execute(new Subscriber<RemoteData<ErpOrderDetail>>() {
            @Override
            public void onCompleted() {
                orderDetailViewer.hideLoadingDialog();

            }

            @Override
            public void onError(Throwable e) {

                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(RemoteData<ErpOrderDetail> remoteData) {
                orderDetailViewer.hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    setOrderDetail(remoteData.datas.get(0));

                    orderDetailViewer.showMesssage("保存成功!");
                } else {

                    orderDetailViewer.showMesssage("保存失败!" + remoteData.message);
                }


            }
        });

        orderDetailViewer.showLoadingDialog("正在保存。。。");

    }

    @Override
    public void delete() {

    }

    @Override
    public void verify() {

    }


    @Override
    public void addPictureClick(File[] file) {


        //上传图片
        UseCaseFactory.getInstance().uploadTempFileUseCase(file).execute(new Subscriber<java.util.List<String>>() {
            @Override
            public void onCompleted() {
                orderDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(java.util.List<String> stringList) {
                attachStrings.addAll(stringList);
                orderDetailViewer.showAttachFiles(attachStrings);
            }
        });
        orderDetailViewer.showLoadingDialog("正在上传图片。。。");


    }


    @Override
    public void onCemaiChange(String value) {

        if (orderDetail == null || orderDetail.erpOrder == null) return;
        orderDetail.erpOrder.cemai = value;
    }

    @Override
    public void onNeihemaiChange(String value) {
        if (orderDetail == null || orderDetail.erpOrder == null) return;

        orderDetail.erpOrder.neheimai = value;

    }

    @Override
    public void onZhengmaiChange(String value) {
        if (orderDetail == null || orderDetail.erpOrder == null) return;

        orderDetail.erpOrder.zhengmai = value;

    }

    @Override
    public void onMemoChange(String value) {
        if (orderDetail == null || orderDetail.erpOrder == null) return;

        orderDetail.erpOrder.memo = value;


    }

    @Override
    public void onDeleteAttach(String url) {
        attachStrings.remove(url);
    }


    /**
     * 右唛数据改变
     *
     * @param value
     */
    @Override
    public void onYoumaiChange(String value) {

        if (orderDetail == null || orderDetail.erpOrder == null) return;
        orderDetail.erpOrder.youmai = value;

    }

    /**
     * 佐麦数据改变
     *
     * @param value
     */
    @Override
    public void onZuomaiChange(String value) {
        if (orderDetail == null || orderDetail.erpOrder == null) return;
        orderDetail.erpOrder.zuomai = value;
    }

    @Override
    public boolean isEditable() {
        return AuthorityUtil.getInstance().editOrder();
    }

    /**
     * 查看生产流程
     *
     * @param finalItem
     */
    @Override
    public void showWorkFlow(ErpOrderItem finalItem) {








    }

    /**
     * 启动生产流程跟踪
     */
    @Override
    public void startOrderTrack() {


        UseCaseFactory.getInstance().startOrderTrackUseCase(orderDetail.erpOrder.os_no).execute(new Subscriber<RemoteData<Void>>() {
            @Override
            public void onCompleted() {



            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<Void> erpOrderDetailRemoteData) {
                orderDetailViewer.hideLoadingDialog();
                if (erpOrderDetailRemoteData.isSuccess()) {
                    loadOrderDetail(orderDetail.erpOrder.os_no);
                }

            }


        });
        orderDetailViewer.showLoadingDialog();

    }

    @Override
    public void updateMaitouFile(File file) {


        UseCaseFactory.getInstance().updateMaitouFileUseCase(orderDetail.erpOrder.os_no,file).execute(new Subscriber<RemoteData<String>>() {
            @Override
            public void onCompleted() {



            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<String> remoteData) {
                orderDetailViewer.hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    orderDetail.erpOrder.maitouUrl=remoteData.datas.get(0);

                    setOrderDetail(orderDetail);


                }

            }


        });
        orderDetailViewer.showLoadingDialog();



    }


    @Override
    public void viewMaitou() {



        new SwingWorker<String ,Object>() {


            @Override
            protected String doInBackground() throws Exception {


                final String filePath = DownloadFileManager.localFilePath + "xls/" + orderDetail.erpOrder.os_no+".XLS";
                DownloadFileManager.download(HttpUrl.loadPicture(orderDetail.erpOrder.maitouUrl), filePath);
                return filePath;
            }


            @Override
            protected void done() {
                super.done();
                orderDetailViewer.hideLoadingDialog();

                try {
                    String filePath=get();
                    Desktop.getDesktop().open(new File(filePath));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }.execute();


        orderDetailViewer.showLoadingDialog("正在下载文件。。。");





    }
}



