package com.giants3.hd.android.fragment;

import android.app.Activity;
import android.content.Intent;

import com.giants3.android.frame.util.ToastHelper;
import com.giants3.hd.android.activity.ProductDetailActivity;
import com.giants3.hd.android.activity.ProductListActivity;
import com.giants3.hd.android.activity.ProductMaterialActivity;
import com.giants3.hd.android.activity.ProductPaintActivity;
import com.giants3.hd.android.activity.ProductWageActivity;
import com.giants3.hd.android.entity.ProductDetailSingleton;
import com.giants3.hd.android.events.ProductUpdateEvent;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.android.mvp.BasePresenter;
import com.giants3.hd.android.presenter.ProductDetailPresenter;
import com.giants3.hd.android.viewer.ProductDetailViewer;
import com.giants3.hd.appdata.AProduct;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.entity.Factory;
import com.giants3.hd.entity.PClass;
import com.giants3.hd.entity.Pack;
import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.GsonUtils;

import de.greenrobot.event.EventBus;
import rx.Subscriber;


/**
 * A fragment representing a single ProductListActivity detail screen.
 * This fragment is either contained in a {@link ProductListActivity}
 * in two-pane mode (on tablets) or a {@link ProductDetailActivity}
 * on handsets.
 */
public class ProductDetailPresenterImpl extends BasePresenter<ProductDetailViewer, ProductDetailModel> implements ProductDetailPresenter {


    private boolean editable = false;


    private ProductDetailSingleton productDetailSingleton;

    private ProductDetail productDetail;
    private AProduct aProduct;

    private String originData;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductDetailPresenterImpl() {
    }

    @Override
    public ProductDetailModelImpl createModel() {
        return new ProductDetailModelImpl();
    }


    private void loadProductDetail(long productId) {


        getView().showWaiting();
        UseCaseFactory.getInstance().createGetProductDetailCase(productId).execute(new Subscriber<RemoteData<ProductDetail>>() {
            @Override
            public void onCompleted() {
                getView().hideWaiting();
            }

            @Override
            public void onError(Throwable e) {
                getView().hideWaiting();
                ToastHelper.show(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<ProductDetail> remoteData) {
                if (remoteData.isSuccess()) {

                    productDetail = remoteData.datas.get(0);
                    originData = GsonUtils.toJson(productDetail);
                    getView().bindData(productDetail);
                    getView().showConceptusMaterial(productDetail);


                } else {
                    ToastHelper.show(remoteData.message);
                    if (remoteData.code == RemoteData.CODE_UNLOGIN) {
                        getView().startLoginActivity();
                    }
                }
            }
        });

    }


    //当前材料清单显示标记
    /**
     * panelIndex 白配 组装 油漆 包装
     * subIndex   0 材料  1  工资
     */
    public int panelIndex, subIndex;

    @Override
    public void onPanelForClick(int index) {

        //无改变 不响应
        if (panelIndex == index)
            return;
        panelIndex = index;
        showBaseOnIndex();

    }

    @Override
    public void onMaterialWageClick(int index) {
//无改变 不响应
        if (subIndex == index)
            return;
        subIndex = index;
        showBaseOnIndex();
    }


    @Override
    public void toEditProductDetail() {
        //调整act
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailPresenter.EXTRA_EDITABLE, true);
        ProductDetailSingleton.getInstance().setProductDetail(productDetail);
        getActivity().startActivity(intent);
    }

    public Activity getActivity() {
        return (Activity) getView().getContext();
    }

    @Override
    public void onItemAdd(int position) {


        switch (panelIndex) {


            case 0://白胚
            {

                if (subIndex == 0) {

                    int newPosition = ProductDetailSingleton.getInstance().addNewProductMaterial(ProductDetailSingleton.PRODUCT_MATERIAL_CONCEPTUS);
                    startProductMaterialEdit(ProductDetailSingleton.PRODUCT_MATERIAL_CONCEPTUS, newPosition);

                } else {
                    ProductDetailSingleton.getInstance().addNewProductWage(ProductDetailSingleton.PRODUCT_MATERIAL_CONCEPTUS, position);
                    startProductWageEdit(ProductDetailSingleton.PRODUCT_MATERIAL_CONCEPTUS, position);
                }

            }
            break;
            case 1: //组装

                if (subIndex == 0) {
                    int newPosition = ProductDetailSingleton.getInstance().addNewProductMaterial(ProductDetailSingleton.PRODUCT_MATERIAL_ASSEMBLE);
                    startProductMaterialEdit(ProductDetailSingleton.PRODUCT_MATERIAL_ASSEMBLE, newPosition);

                } else {
                    ProductDetailSingleton.getInstance().addNewProductWage(ProductDetailSingleton.PRODUCT_MATERIAL_ASSEMBLE, position);
                    startProductWageEdit(ProductDetailSingleton.PRODUCT_MATERIAL_ASSEMBLE, position);
                }


                break;
            case 2://油漆

            {

                int newPosition = ProductDetailSingleton.getInstance().addNewProductMaterial(ProductDetailSingleton.PRODUCT_MATERIAL_PAINT);
                startProductPaintEdit(newPosition);
            }

            break;
            case 3://包装

                if (subIndex == 0) {
                    int newPosition = ProductDetailSingleton.getInstance().addNewProductMaterial(ProductDetailSingleton.PRODUCT_MATERIAL_PACK);
                    startProductMaterialEdit(ProductDetailSingleton.PRODUCT_MATERIAL_PACK, newPosition);

                } else {
                    ProductDetailSingleton.getInstance().addNewProductWage(ProductDetailSingleton.PRODUCT_MATERIAL_PACK, position);
                    startProductWageEdit(ProductDetailSingleton.PRODUCT_MATERIAL_PACK, position);
                }
                break;


        }


    }


    @Override
    public void onItemModify(int position) {


        switch (panelIndex) {
            case 0://白胚


                if (subIndex == 0) {
                    startProductMaterialEdit(ProductDetailSingleton.PRODUCT_MATERIAL_CONCEPTUS, position);

                } else {
                    startProductWageEdit(ProductDetailSingleton.PRODUCT_MATERIAL_CONCEPTUS, position);
                }


                break;
            case 1: //组装


                if (subIndex == 0) {
                    startProductMaterialEdit(ProductDetailSingleton.PRODUCT_MATERIAL_ASSEMBLE, position);

                } else {
                    startProductWageEdit(ProductDetailSingleton.PRODUCT_MATERIAL_ASSEMBLE, position);
                }
                break;
            case 2://油漆

                startProductPaintEdit(position);
                break;
            case 3://包装
                if (subIndex == 0) {

                    startProductMaterialEdit(ProductDetailSingleton.PRODUCT_MATERIAL_PACK, position);
                } else {
                    startProductWageEdit(ProductDetailSingleton.PRODUCT_MATERIAL_PACK, position);
                }
                break;


        }


    }

    /**
     * 启动产品材料编辑
     *
     * @param type
     * @param position
     */
    private void startProductMaterialEdit(int type, int position) {
        Intent intent = new Intent(getActivity(), ProductMaterialActivity.class);


        intent.putExtra(ProductMaterialFragment.PRODUCT_MATERIAL_POSITION, position);
        intent.putExtra(ProductMaterialFragment.PRODUCT_MATERIAL_TYPE, type);
        getActivity().startActivityForResult(intent, REQUEST_PRODUCT_MATERIAL);
    }

    /**
     * 启动产品油漆材料编辑
     *
     * @param position
     */
    private void startProductPaintEdit(int position) {
        Intent intent = new Intent(getView().getContext(), ProductPaintActivity.class);

        intent.putExtra(ProductPaintFragment.PRODUCT_PAINT_POSITION, position);
        intent.putExtra(ProductMaterialFragment.PRODUCT_MATERIAL_TYPE, ProductDetailSingleton.PRODUCT_MATERIAL_PAINT);
        getView().startActivityForResult(intent, REQUEST_PRODUCT_PAINT);
    }

    /* 启动产品工资编辑
     * @param type
     * @param position
     * @param productPaint
     */
    private void startProductWageEdit(int type, int position) {

        Intent intent = new Intent(getActivity(), ProductWageActivity.class);
        intent.putExtra(ProductWageActivity.PRODUCT_WAGE_POSITION, position);
        intent.putExtra(ProductWageActivity.PRODUCT_MATERIAL_TYPE, type);
        getView().startActivityForResult(intent, REQUEST_PRODUCT_WAGE);
    }

    @Override
    public void onItemDelete(Object object, int position) {


        switch (panelIndex) {
            case 0://白胚
                if (subIndex == 0) {
                    productDetail.conceptusMaterials.remove(position);

                } else {
                    //工资
                    productDetail.conceptusWages.remove(position);

                }


                break;
            case 1: //组装
                if (subIndex == 0) {
                    productDetail.assembleMaterials.remove(position);


                } else {
                    //工资
                    productDetail.assembleWages.remove(position);


                }

                break;
            case 2://油漆

                //材料

                productDetail.paints.remove(position);

                break;
            case 3://包装
                if (subIndex == 0) {
                    productDetail.packMaterials.remove(position);

                } else {
                    //工资
                    productDetail.packWages.remove(position);


                }


                break;


        }

        ProductAnalytics.updateProductStatistics(productDetail, SharedPreferencesHelper.getInitData().globalData);

        bindData();


    }

    @Override
    public void saveProductDetail() {


        if (!productDetailSingleton.hasModifyDetail()) {

            ToastHelper.show("数据无改变");
            getActivity().finish();
            return;
        }

        getView().showWaiting("保存中...");
        //保存产品详情信息
        UseCaseFactory.getInstance().saveProductDetailCase(productDetail).execute(new Subscriber<RemoteData<ProductDetail>>() {
            @Override
            public void onCompleted() {
                getView().hideWaiting();
            }

            @Override
            public void onError(Throwable e) {
                ToastHelper.show(e.getMessage());
                getView().hideWaiting();
            }

            @Override
            public void onNext(RemoteData<ProductDetail> remoteData) {
                if (remoteData.isSuccess()) {

                    ToastHelper.show("保存成功");
                    EventBus.getDefault().post(new ProductUpdateEvent(remoteData.datas.get(0)));
                    getActivity().finish();

                } else {
                    ToastHelper.show(remoteData.message);

                }
            }
        });

    }

    @Override
    public void onUnitClick() {

        if (!editable) return;
        if (productDetail == null) return;


    getView().showFieldValueEditDailog("单位", "pUnitName", productDetail.product.pUnitName);
    }


    @Override
    public void onPVersionEdit() {

        if (!editable) return;
        if (productDetail == null) return;

    getView().showFieldValueEditDailog("配方号", "pVersion", productDetail.product.pVersion);

    }

    @Override
    public void onProductNameEdit() {
        if (!editable) return;
        if (productDetail == null) return;


        getView().showFieldValueEditDailog("产品名称", "name", productDetail.product.name);

    }

    @Override
    public void onWeightEdit() {
        if (!editable) return;
        if (productDetail == null) return;
        getView().showFieldValueEditDailog("净重KG", "weight", String.valueOf(productDetail.product.weight),Float.class);

    }

    @Override
    public void onSpecCmEdit() {


        if (!editable) return;
        if (productDetail == null) return;

        getView().showFieldValueEditDailog("产品规格cm", "specCm", productDetail.product.specCm);

    }

    @Override
    public void onPackQuantityEdit() {

        if (!editable) return;
        if (productDetail == null) return;

        getView().showPackRelateEditDialog(productDetail.product );


    }

    @Override
    public void onPackEdit() {
        if (!editable) return;
        if (productDetail == null) return;


        getView().showPickDialog("包装类型选择", "pack", SharedPreferencesHelper.getInitData().packs, productDetail.product.pack);

    }


    public void bindData() {

        switch (panelIndex) {
            case 0://白胚
                if (subIndex == 0) {

                    getView().showConceptusMaterial(productDetail);
                } else {

                    getView().showConceptusWage(productDetail);
                }
                getView().bindData(productDetail);


                break;
            case 1: //组装
                if (subIndex == 0) {
                    getView().showAssembleMaterial(productDetail);
                } else {

                    getView().showAssembleWage(productDetail);

                }
                getView().bindData(productDetail);
                break;
            case 2://油漆

                //材料 `
                getView().showPaintMaterialWage(productDetail);
                // productDetail.updateProductStatistics();
                getView().bindData(productDetail);
                break;
            case 3://包装
                if (subIndex == 0) {

                    getView().showPackMaterial(productDetail);
                } else {

                    getView().showPackWage(productDetail);

                }

                getView().bindData(productDetail);
                break;


        }

    }

    private void showBaseOnIndex() {


        switch (panelIndex) {

            case 0:
                if (subIndex == 0) {
                    getView().showConceptusMaterial(productDetail);
                } else {
                    getView().showConceptusWage(productDetail);
                }

                break;

            case 1:
                ;
                if (subIndex == 0) {
                    getView().showAssembleMaterial(productDetail);
                } else {
                    getView().showAssembleWage(productDetail);
                }
                break;
            case 2:
                ;
                getView().showPaintMaterialWage(productDetail);
                break;
            case 3:
                if (subIndex == 0) {
                    getView().showPackMaterial(productDetail);
                } else {
                    getView().showPackWage(productDetail);
                }
                break;

        }

    }


    public void onEvent(ProductUpdateEvent event) {


        productDetail = event.productDetail;
        if (getView() != null) {
            getView().bindData(productDetail);
            bindData();
        }
        ProductDetailSingleton.getInstance().setProductDetail(null);
    }


    @Override
    public void start() {


        if (!editable)
            loadProductDetail(aProduct == null ? 0 : aProduct.id);
        else {

            productDetail = ProductDetailSingleton.getInstance().getProductDetail();
            originData = GsonUtils.toJson(productDetail);
            getModel().setProductDetail(productDetail);
            getView().bindData(this.productDetail);
            getView().showConceptusMaterial(this.productDetail);
        }
    }

    public void setInitData(boolean editable, AProduct aProduct) {
        this.editable = editable;
        this.aProduct = aProduct;
    }


    @Override
    public void updateFieldData(String field, String oldValue, String newValue) {
        switch (field) {
            case "pUnitName":
                productDetail.product.setpUnitName(newValue);

                break;

            case "packQuantity":
                try {

                    int newV = Integer.valueOf(newValue.trim());

                    getModel().setPackQuantity(newV);

                } catch (Throwable t) {
                    t.printStackTrace();
                }
                break;
            case "specCm":


                getModel().setSpecCm(newValue);


                break;

            case "weight":

                try {

                    float newWeight = Float.valueOf(newValue.trim());
                    getModel().setProductWeight(newWeight);

                } catch (Throwable t) {
                }


                break;
            case "pVersion":


                getModel().setProductPVersion(newValue);


                break;
            case "name":

                try {
                    getModel().setProductName(newValue);

                } catch (Throwable t) {
                }


                break;
        }
        bindData();
    }


    public boolean needNoSaveNotice() {

        if (!editable) return false;


        if (originData == null) {
            return false;
        }

        if (originData.equals(GsonUtils.toJson(productDetail)))

            return false;
        return true;


    }

    @Override
    public void onFactoryClick() {


        getView().showPickDialog(
                "厂家选择", "factory", SharedPreferencesHelper.getInitData().factories, null);

    }

    @Override
    public void onPClassEdit() {

        getView().showPickDialog(
                "类型选择", "pClassId", SharedPreferencesHelper.getInitData().pClasses, null);

    }

    @Override
    public void setNewPackData(int insideBoxQuantity, int packQuantity ) {
        getModel().setNewPackData(  insideBoxQuantity,   packQuantity );
        bindData();
    }

    @Override
    public <T> void setNewSelectItem(String field, T newValue) {


        switch (field) {
            case "pack":
                Pack pack = (Pack) newValue;
                getModel().setNewPack(pack);
                break;
            case "factory":
                Factory factory = (Factory) newValue;
                getModel().setFactory(factory);
                break;
            case "pClassId":
                PClass pClass = (PClass) newValue;
                getModel().setNewPClass(pClass);
                break;
        }

        bindData();
    }
}
