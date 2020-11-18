package com.giants3.hd.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.giants3.hd.android.R;
import com.giants3.hd.android.entity.ProductDetailSingleton;
import com.giants3.hd.android.fragment.MaterialSelectFragment;
import com.giants3.hd.android.fragment.ValueEditDialogFragment;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.entity.Material;
import com.giants3.hd.entity.PackMaterialClass;
import com.giants3.hd.entity.PackMaterialPosition;
import com.giants3.hd.entity.PackMaterialType;
import com.giants3.hd.entity.ProductMaterial;
import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.GsonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 产品材料清单编辑界面
 * An activity representing a single ProductListActivity detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item_work_flow_report details are presented side-by-side with a list of items
 * in a {@link ProductListActivity}.
 */
public class ProductMaterialActivity extends BaseActionBarActivity {

    private static final int REQUEST_MATERIAL_SELECT = 11;


    public static String PRODUCT_MATERIAL_TYPE = "PRODUCT_MATERIAL_TYPE";
    public static String PRODUCT_MATERIAL_POSITION = "PRODUCT_PAINT_POSITION";
     @BindView(R.id.materialCode)
    TextView materialCode;
     @BindView(R.id.materialName)
    TextView materialName;
     @BindView(R.id.quantity)
    TextView quantity;
     @BindView(R.id.mLong)
    TextView mLong;
     @BindView(R.id.mWidth)
    TextView mWidth;
     @BindView(R.id.mHeight)
    TextView mHeight;
     @BindView(R.id.wLong)
    TextView wLong;
     @BindView(R.id.wWidth)
    TextView wWidth;
     @BindView(R.id.wHeight)
    TextView wHeight;
     @BindView(R.id.quota)
    TextView quota;
     @BindView(R.id.unit)
    TextView unit;
     @BindView(R.id.available)
    TextView available;
     @BindView(R.id.type)
    TextView type;
     @BindView(R.id.price)
    TextView price;
     @BindView(R.id.amount)
    TextView amount;
     @BindView(R.id.memo)
    TextView memo;
     @BindView(R.id.selectMaterial)
    View selectMaterial;
     @BindView(R.id.selectMaterial2)
    View selectMaterial2;

     @BindView(R.id.goBack)
    View goBack;


     @BindView(R.id.packMaterialClass)
    Spinner packMaterialClass;

     @BindView(R.id.packMaterialType)
    Spinner packMaterialType;
     @BindView(R.id.packMaterialPosition)
    Spinner packMaterialPositionSpinner;


     @BindView(R.id.ll_packMaterialClass)
    View ll_packMaterialClass;

     @BindView(R.id.ll_packMaterialType)
    View ll_packMaterialType;
     @BindView(R.id.ll_packMaterialPosition)
    View ll_packMaterialPosition;



    private int materialType;
    private int position;
    private ProductMaterial productMaterial;
    private String originData ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);








        materialType =    getIntent().getIntExtra(PRODUCT_MATERIAL_TYPE, ProductDetailSingleton.PRODUCT_MATERIAL_CONCEPTUS);
        position =    getIntent().getIntExtra(PRODUCT_MATERIAL_POSITION,0);


        productMaterial =ProductDetailSingleton.getInstance().getProductMaterial(materialType,position);

       originData= com.giants3.hd.data.utils.GsonUtils.toJson(productMaterial);


        setTitle("材料清单编辑");
        initView();
        bindData(productMaterial);
    }


    @Override
    protected View getContentView() {
        return getLayoutInflater().inflate(R.layout.activity_product_material, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {

            case REQUEST_MATERIAL_SELECT:

                try {
                    Material material = GsonUtils.fromJson(data.getExtras().getString(MaterialSelectActivity.EXTRA_MATERIAL), Material.class);

                    ProductAnalytics.setMaterialToProductPaint(productMaterial,material);
                    ProductDetailSingleton.getInstance().updateProductStatistics();
                    bindData(productMaterial);


                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
        }


    }
    private void initView() {
        ll_packMaterialClass.setVisibility(materialType == ProductDetailSingleton.PRODUCT_MATERIAL_PACK ? View.VISIBLE : View.GONE);
        ll_packMaterialPosition.setVisibility(materialType == ProductDetailSingleton.PRODUCT_MATERIAL_PACK ? View.VISIBLE : View.GONE);
        ll_packMaterialType.setVisibility(materialType == ProductDetailSingleton.PRODUCT_MATERIAL_PACK ? View.VISIBLE : View.GONE);
        {
            // 建立Adapter并且绑定数据源
            List<PackMaterialClass> mItems = SharedPreferencesHelper.getInitData().packMaterialClasses;
            PackMaterialClass[] arrayItems = new PackMaterialClass[mItems.size()+1];
            for(int i=1;i<arrayItems.length;i++)
            {
                arrayItems[i ]= mItems.get(i-1);
            }
            arrayItems[0]=new PackMaterialClass();
            ArrayAdapter<PackMaterialClass> _Adapter = new ArrayAdapter<PackMaterialClass>(this, android.R.layout.simple_spinner_item, arrayItems);
            packMaterialClass.setAdapter(_Adapter);
        }

        {
            // 建立Adapter并且绑定数据源
            List<PackMaterialType> typeItems = SharedPreferencesHelper.getInitData().packMaterialTypes;
            PackMaterialType[] arrayTypeItems = new PackMaterialType[typeItems.size()+1];
            for(int i=1;i<arrayTypeItems.length;i++)
            {
                arrayTypeItems[i ]= typeItems.get(i-1);
            }
            arrayTypeItems[0]=new PackMaterialType();
            ArrayAdapter<PackMaterialType> _TypeAdapter = new ArrayAdapter<PackMaterialType>(this, android.R.layout.simple_spinner_item, arrayTypeItems);
            packMaterialType.setAdapter(_TypeAdapter);
        }
        {
            // 建立Adapter并且绑定数据源
            List<PackMaterialPosition> positionItems = SharedPreferencesHelper.getInitData().packMaterialPositions;
            PackMaterialPosition[] arrayPositionItems = new PackMaterialPosition[positionItems.size()+1];
            for(int i=1;i<arrayPositionItems.length;i++)
            {
                arrayPositionItems[i ]= positionItems.get(i-1);
            }
            arrayPositionItems[0]=new PackMaterialPosition();

            ArrayAdapter<PackMaterialPosition> _PositionAdapter = new ArrayAdapter<PackMaterialPosition>(this, android.R.layout.simple_spinner_item, arrayPositionItems);
            packMaterialPositionSpinner.setAdapter(_PositionAdapter);
        }
    }

    @OnClick({R.id.selectMaterial, R.id.selectMaterial2})
    public void onMaterialSelect(View v) {

        Intent intent = new Intent(this, MaterialSelectActivity.class);

        if (v.getId() == R.id.selectMaterial) {
            intent.putExtra(MaterialSelectFragment.ARG_PARAM_CODE, productMaterial.materialCode);
        } else {

            intent.putExtra(MaterialSelectFragment.ARG_PARAM_NAME, productMaterial.materialName);
        }


        startActivityForResult(intent, REQUEST_MATERIAL_SELECT);
    }


    @OnClick(R.id.available)
    public void onAvailableClick(View view) {


        ValueEditDialogFragment dialogFragment = new ValueEditDialogFragment();
        dialogFragment.set("利用率", String.valueOf(productMaterial.available),Float.class, new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {
                    float newAvailable = Float.valueOf(newValue);
                    productMaterial.setAvailable(newAvailable);
                    ProductAnalytics.update(productMaterial);
                    ProductDetailSingleton.getInstance().updateProductStatistics();
                    bindData(productMaterial);
                } catch (Throwable t) {

                }


            }
        });
        dialogFragment.show(this.getSupportFragmentManager(), null);

    }

    @OnClick(R.id.quantity)
    public void onQuantityClick(View view) {


        ValueEditDialogFragment dialogFragment = new ValueEditDialogFragment();
        dialogFragment.set("数量", String.valueOf(productMaterial.quantity),Integer.class, new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {
                    float newValueFloat = Float.valueOf(newValue);
                    productMaterial.setQuantity(newValueFloat);
                    ProductAnalytics.update(productMaterial);
                    ProductDetailSingleton.getInstance().updateProductStatistics();
                    bindData(productMaterial);
                } catch (Throwable t) {

                }


            }
        });
        dialogFragment.show(this.getSupportFragmentManager(), null);


    }

    @OnClick(R.id.mLong)
    public void onLongClick(View view) {


        ValueEditDialogFragment dialogFragment = new ValueEditDialogFragment();
        dialogFragment.set("长", String.valueOf(productMaterial.pLong),Float.class, new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {
                    float newValueFloat = Float.valueOf(newValue);
                    productMaterial.setpLong(newValueFloat);
                    ProductAnalytics.update(productMaterial);
                    ProductDetailSingleton.getInstance().updateProductStatistics();
                    bindData(productMaterial);
                } catch (Throwable t) {

                }


            }
        });
        dialogFragment.show(this.getSupportFragmentManager(), null);


    }

    @OnClick(R.id.mWidth)
    public void onWidthClick(View view) {


        ValueEditDialogFragment dialogFragment = new ValueEditDialogFragment();
        dialogFragment.set("宽", String.valueOf(productMaterial.pWidth),Float.class, new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {
                    float newValueFloat = Float.valueOf(newValue);
                    productMaterial.setpWidth(newValueFloat);
                    ProductAnalytics.update(productMaterial);
                    ProductDetailSingleton.getInstance().updateProductStatistics();
                    bindData(productMaterial);
                } catch (Throwable t) {

                }


            }
        });
        dialogFragment.show(this.getSupportFragmentManager(), null);


    }

    @OnClick(R.id.mHeight)
    public void onHeightClick(View view) {


        ValueEditDialogFragment dialogFragment = new ValueEditDialogFragment();
        dialogFragment.set("高", String.valueOf(productMaterial.pHeight),Float.class, new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {
                    float newValueFloat = Float.valueOf(newValue);
                    productMaterial.setpHeight(newValueFloat);
                    ProductAnalytics.update(productMaterial);
                    ProductDetailSingleton.getInstance().updateProductStatistics();
                    bindData(productMaterial);
                } catch (Throwable t) {

                }


            }
        });
        dialogFragment.show(this.getSupportFragmentManager(), null);


    }

    @OnClick(R.id.memo)
    public void onMemoClick(View view) {


        ValueEditDialogFragment dialogFragment = new ValueEditDialogFragment();
        dialogFragment.set("分件备注", String.valueOf(productMaterial.memo), new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {
                    productMaterial.setMemo(newValue);
                    bindData(productMaterial);
                } catch (Throwable t) {

                }


            }
        });
        dialogFragment.show(this.getSupportFragmentManager(), null);


    }

    private void bindData(final ProductMaterial productMaterial) {


        materialCode.setText(productMaterial.materialCode);
        materialName.setText(productMaterial.materialName);
        quantity.setText(String.valueOf(productMaterial.quantity));
        mLong.setText(String.valueOf(productMaterial.pLong));
        mWidth.setText(String.valueOf(productMaterial.pWidth));
        mHeight.setText(String.valueOf(productMaterial.pHeight));
        wLong.setText(String.valueOf(productMaterial.wLong));
        wWidth.setText(String.valueOf(productMaterial.wWidth));
        wHeight.setText(String.valueOf(productMaterial.wHeight));
        quota.setText(String.valueOf(productMaterial.quota));
        unit.setText(productMaterial.unitName);
        available.setText(String.valueOf(productMaterial.available));
        type.setText(String.valueOf(productMaterial.type));
        price.setText(String.valueOf(productMaterial.price));


        if (materialType == ProductDetailSingleton.PRODUCT_MATERIAL_PACK) {
            int packQuantity = Math.max(1, ProductDetailSingleton.getInstance().getProductDetail().product.packQuantity);
            amount.setText(String.valueOf(FloatHelper.scale(productMaterial.amount / packQuantity)));
        } else {

            amount.setText(String.valueOf(productMaterial.amount));
        }

        memo.setText(productMaterial.memo);


        {
            packMaterialClass.setOnItemSelectedListener(null);
            int position = -1;
            if (productMaterial.packMaterialClass != null) {
                int size = SharedPreferencesHelper.getInitData().packMaterialClasses.size();
                for (int i = 0; i < size; i++) {
                    PackMaterialClass packMaterialClass = SharedPreferencesHelper.getInitData().packMaterialClasses.get(i);
                    if (packMaterialClass.equals(productMaterial.packMaterialClass)) {
                        position = i;
                        break;
                    }
                }
            }

            packMaterialClass.setSelection(position+1,false);

            packMaterialClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    ProductAnalytics.setPackMaterialClass(productMaterial,(PackMaterialClass) parent.getItemAtPosition(position));

                    ProductAnalytics.update(productMaterial);

                    ProductDetailSingleton.getInstance().updateProductPackRelateData( );
                    ProductDetailSingleton.getInstance().updatePackDataOnPackMaterialClass(productMaterial );
                    ProductDetailSingleton.getInstance().updateProductStatistics();

                    bindData(productMaterial);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


        {
            packMaterialPositionSpinner.setOnItemSelectedListener(null);
            int position = -1;
            if (productMaterial.packMaterialPosition != null) {
                int size = SharedPreferencesHelper.getInitData().packMaterialPositions.size();
                for (int i = 0; i < size; i++) {
                    PackMaterialPosition packMaterialPosition = SharedPreferencesHelper.getInitData().packMaterialPositions.get(i);
                    if (packMaterialPosition.equals(productMaterial.packMaterialPosition)) {
                        position = i;
                        break;
                    }
                }
            }

            packMaterialPositionSpinner.setSelection(position+1,false);
            packMaterialPositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    productMaterial.setPackMaterialPosition((PackMaterialPosition) parent.getItemAtPosition(position));

                    ProductDetailSingleton.getInstance().updateProductPackRelateData( );

                    ProductDetailSingleton.getInstance().updatePackDataOnPackMaterialClass( productMaterial);
                    ProductDetailSingleton.getInstance().updateProductStatistics();


                    bindData(productMaterial);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        {
            packMaterialType.setOnItemSelectedListener(null);
            int position = -1;
            if (productMaterial.packMaterialType != null) {
                int size = SharedPreferencesHelper.getInitData().packMaterialTypes.size();
                for (int i = 0; i < size; i++) {
                    PackMaterialType packMaterialType = SharedPreferencesHelper.getInitData().packMaterialTypes.get(i);
                    if (packMaterialType.equals(productMaterial.packMaterialType)) {
                        position = i;
                        break;
                    }
                }
            }

            packMaterialType.setSelection(position+1,false);
            packMaterialType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    productMaterial.setPackMaterialType((PackMaterialType) parent.getItemAtPosition(position));
                    ProductDetail productDetail = ProductDetailSingleton.getInstance().getProductDetail();


                    ProductDetailSingleton.getInstance().updatePackDataOnPackMaterialClass( productMaterial);
                    ProductDetailSingleton.getInstance().updateProductStatistics();
                    bindData(productMaterial);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


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
        productMaterial=null;
        finish();



    }

    @Override
    public void finish() {



        if(!com.giants3.hd.data.utils.GsonUtils.toJson(productMaterial).equalsIgnoreCase(originData))
        {
            setResult(RESULT_OK);
        }
        super.finish();
    }
}
