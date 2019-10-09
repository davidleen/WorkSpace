package com.giants3.hd.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.giants3.hd.android.R;
import com.giants3.hd.android.entity.ProductDetailSingleton;
import com.giants3.hd.android.fragment.MaterialSelectFragment;
import com.giants3.hd.android.fragment.ValueEditDialogFragment;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.GlobalData;
import com.giants3.hd.entity.Material;
import com.giants3.hd.entity.ProductPaint;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.noEntity.ProductDetail;

import butterknife.Bind;
import butterknife.OnClick;

import static com.giants3.hd.android.activity.ProductMaterialActivity.PRODUCT_MATERIAL_TYPE;

/**
 * 产品油漆清单编辑界面
 * An activity representing a single ProductListActivity detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item_work_flow_report details are presented side-by-side with a list of items
 * in a {@link ProductListActivity}.
 */
public class ProductPaintActivity extends BaseActionBarActivity  {
    public static String PRODUCT_PAINT_POSITION = "PRODUCT_PAINT_POSITION";
    private static final int REQUEST_MATERIAL_SELECT = 11;
     ProductPaint productPaint;
    String originData;

    @Bind(R.id.materialCode)
    TextView materialCode;
    @Bind(R.id.materialName)
    TextView materialName;



    @Bind(R.id.processCode)
    TextView processCode;
    @Bind(R.id.processName)
    TextView processName;
    @Bind(R.id.processPrice)
    TextView processPrice;
    @Bind(R.id.unitName)
    TextView unitName;


    @Bind(R.id.ingredientRatio)
    TextView ingredientRatio;
    @Bind(R.id.quantity)
    TextView quantity;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.cost)
    TextView cost;
    @Bind(R.id.materialQuantity)
    TextView materialQuantity;

    @Bind(R.id.ingredientQuantity)
    TextView ingredientQuantity;

    @Bind(R.id.memo)
    TextView memo;


    @Bind(R.id.goBack)
    View goBack;

    @Bind(R.id.delete)
    View delete;
    private int position = -1;
    private int materialType = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);








        setTitle("产品油漆清单编辑");

        initView();

        materialType=getIntent().getIntExtra(ProductMaterialActivity.PRODUCT_MATERIAL_TYPE, ProductDetailSingleton.PRODUCT_MATERIAL_PAINT);
        position=getIntent().getIntExtra(PRODUCT_PAINT_POSITION,0);

        productPaint=   ProductDetailSingleton.getInstance().getProductPaint(position);
        originData= GsonUtils.toJson(productPaint);



        bindData(productPaint);
    }
    private void initView() {


    }
    @OnClick({R.id.selectMaterial, R.id.selectMaterial2})
    public void onMaterialSelect(View v) {

        Intent intent = new Intent(this, MaterialSelectActivity.class);

        if(v.getId()==R.id.selectMaterial)
        {
            intent.putExtra(MaterialSelectFragment.ARG_PARAM_CODE, productPaint.materialCode);
        }else {

            intent.putExtra(MaterialSelectFragment.ARG_PARAM_NAME, productPaint.materialName);
        }



        startActivityForResult(intent, REQUEST_MATERIAL_SELECT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {

            case REQUEST_MATERIAL_SELECT:

                try {
                    Material material = GsonUtils.fromJson(data.getExtras().getString(MaterialSelectActivity.EXTRA_MATERIAL), Material.class);


                    ProductDetailSingleton.getInstance().setMaterialToProductPaint(productPaint,material);
                    bindData(productPaint);


                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
        }


    }

    private void bindData(ProductPaint productPaint) {


        materialCode.setText(productPaint.materialCode);
        materialName.setText(productPaint.materialName);
        quantity.setText(String.valueOf(productPaint.quantity));


        processCode.setText(productPaint.processCode);
        processName.setText(productPaint.processName);
        processPrice.setText(String.valueOf(productPaint.processPrice));



        unitName.setText(productPaint.unitName);

        ingredientRatio.setText(String.valueOf(productPaint.ingredientRatio));


        quantity.setText(String.valueOf(productPaint.quantity));

        price.setText(String.valueOf(productPaint.price));

        cost.setText(String.valueOf(productPaint.cost));

        materialQuantity.setText(String.valueOf(productPaint.cost));

        ingredientQuantity.setText(String.valueOf(productPaint.ingredientQuantity));





        memo.setText(productPaint.memo);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected View getContentView() {
        return getLayoutInflater().inflate(R.layout.activity_product_paint,null);
    }



    @OnClick(R.id.processCode)
    public void onProcessCodeClick(View view) {


        ValueEditDialogFragment dialogFragment=new ValueEditDialogFragment();
        dialogFragment.set("工序代码", String.valueOf(productPaint.processCode), new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {

                    productPaint.setProcessCode(newValue);
                    bindData(productPaint);
                }catch (Throwable t )
                {

                }


            }
        });
        dialogFragment.show( getSupportFragmentManager(),null);


    }

    @OnClick(R.id.processName)
    public void onProcessNameClick(View view) {


        ValueEditDialogFragment dialogFragment=new ValueEditDialogFragment();
        dialogFragment.set("工序名称",  productPaint.processName, new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {

                    productPaint.setProcessName(newValue);


                    //指定的工序特殊处理;
                    if(productPaint.processName!=null&&productPaint.processName.contains(ProductProcess.XISHUA) )
                    {
                        updateQuantityOfIngredient();

                    }
                    bindData(productPaint);
                }catch (Throwable t )
                {

                }


            }
        });
        dialogFragment.show( getSupportFragmentManager(),null);


    }
    @OnClick(R.id.processPrice)
    public void onProcessPriceClick(View view) {


        ValueEditDialogFragment dialogFragment=new ValueEditDialogFragment();
        dialogFragment.set("工价", String.valueOf(productPaint.processPrice),Float.class, new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {
                    float newValueFloat = Float.valueOf(newValue);
                    productPaint.setProcessPrice(newValueFloat);
                    bindData(productPaint);
                }catch (Throwable t )
                {

                }


            }
        });
        dialogFragment.show( getSupportFragmentManager(),null);


    }

    @OnClick(R.id.quantity)
    public void onuantityClick(View view) {


        ValueEditDialogFragment dialogFragment=new ValueEditDialogFragment();
        dialogFragment.set("用量", String.valueOf(productPaint.quantity),Float.class, new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {
                    float newValueFloat = Float.valueOf(newValue);
                    productPaint.quantity=newValueFloat;                    ;

                    ProductAnalytics.updateProductPaintPriceAndCostAndQuantity(productPaint,SharedPreferencesHelper.getInitData().globalData);
                    updateQuantityOfIngredient();
                    bindData(productPaint);
                }catch (Throwable t )
                {

                }


            }
        });
        dialogFragment.show( getSupportFragmentManager(),null);


    }

    @OnClick(R.id.ingredientRatio)
    public void onIngredientRatioClick(View view) {


        ValueEditDialogFragment dialogFragment=new ValueEditDialogFragment();
        dialogFragment.set("配料比例", String.valueOf(productPaint.ingredientRatio),Float.class, new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {
                    float newValueFloat = Float.valueOf(newValue);
                    productPaint.ingredientRatio=newValueFloat;                    ;
                    ProductAnalytics.updateProductPaintPriceAndCostAndQuantity(productPaint,SharedPreferencesHelper.getInitData().globalData);

                    updateQuantityOfIngredient();
                    bindData(productPaint);
                }catch (Throwable t )
                {

                }


            }
        });
        dialogFragment.show( getSupportFragmentManager(),null);


    }
    @OnClick(R.id.memo)
    public void onMemoClick(View view) {


        ValueEditDialogFragment dialogFragment=new ValueEditDialogFragment();
        dialogFragment.set("备注", String.valueOf(productPaint.memo), new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {
                    productPaint.setMemo(newValue);
                    bindData(productPaint);
                }catch (Throwable t )
                {

                }


            }
        });
        dialogFragment.show(getSupportFragmentManager(),null);


    }

    /**
     * 更新
     */
    public void updateQuantityOfIngredient()
    {



        ProductDetailSingleton.getInstance().updateQuantityOfIngredient();

    }
    @OnClick(R.id.goBack)
    public void goBack(View v) {

        finish();

    }

    @OnClick(R.id.delete)
    public void deleteItem()
    {

//        new AlertDialog.Builder(this).setMessage("有新版本apk，是否下载安装？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startDownLoadApk(fileInfo);
//
//            }
//        }).create().show();

        ProductDetailSingleton.getInstance().deleteProductMaterial(materialType,position);
        productPaint=null;
        finish();



    }

    @Override
    public void finish() {



        if(!com.giants3.hd.data.utils.GsonUtils.toJson(productPaint).equalsIgnoreCase(originData))
        {
            setResult(RESULT_OK);
        }
        super.finish();
    }
}
