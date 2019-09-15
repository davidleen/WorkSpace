package com.giants3.hd.android.presenter;

import com.giants3.hd.android.mvp.NewPresenter;
import com.giants3.hd.android.viewer.ProductDetailViewer;
import com.giants3.hd.appdata.AProduct;
import com.giants3.hd.entity.Factory;
import com.giants3.hd.entity.Pack;

/**
 * Created by david on 2016/4/13.
 */
public interface ProductDetailPresenter  extends NewPresenter<ProductDetailViewer> {
    public static final int REQUEST_PRODUCT_MATERIAL = 113;
    public static final int REQUEST_PRODUCT_WAGE = 114;
    public static final int REQUEST_PRODUCT_PAINT = 115;
    /**
     * The fragment argument representing the item_work_flow_report ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "item_work_flow_report";
    public static final String EXTRA_EDITABLE = "EXTRA_EDITABLE";
    /**
     * 白胚 组装 油漆 包装 模板四选一 点击事件
     * @param index
     */
    public void onPanelForClick( int index);


    /**
     * 工资材料 面板点击
     * @param index
     */
    public void onMaterialWageClick(int index);

    /**
     * 编辑点击
     */
   public  void toEditProductDetail();

    public void onItemAdd(int position);
    public void onItemModify( int position);
    public void onItemDelete(Object object,int position);

   public  void saveProductDetail();

    void onUnitClick();

    void onPVersionEdit();

    void onProductNameEdit();

    void onWeightEdit();

    void onSpecCmEdit();

    void onPackQuantityEdit();

    /**
     * 包装类型点击
     */
    void onPackEdit();

    void onFactoryClick();

    void bindData();

    boolean needNoSaveNotice();

    void setInitData(boolean editable, AProduct aProduct);

    void updateFieldData(String field, String oldValue, String newValue);





   <T> void setNewSelectItem(String field, T newValue);

    void onPClassEdit();

    void setNewPackData(int insideBoxQuantity, int packQuantity, float packLong, float packWidth, float packHeight);
}
