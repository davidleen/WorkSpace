package com.giants3.hd.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.giants3.hd.android.R;
import com.giants3.hd.android.entity.ProductDetailSingleton;
import com.giants3.hd.android.fragment.ProcessSelectFragment;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.entity.ProductWage;
import com.giants3.hd.exception.HdException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 产品清单工资编辑界面
 * An activity representing a single ProductListActivity detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item_work_flow_report details are presented side-by-side with a list of items
 * in a {@link ProductListActivity}.
 */
public class ProductWageActivity extends BaseActionBarActivity {
    private static final int REQUEST_PROCESS_SELECT = 12;

        public static final String EXTRA_PRODUCT_WAGE = "EXTRA_PRODUCT_WAGE";
    public static String PRODUCT_MATERIAL_TYPE = "PRODUCT_MATERIAL_TYPE";
   public static String PRODUCT_WAGE_POSITION = "PRODUCT_WAGE_POSITION";

    public static final int PRODUCT_MATERIAL_CONCEPTUS = 1;
        public static final int PRODUCT_MATERIAL_ASSEMBLE = 2;
    public static final int PRODUCT_MATERIAL_PACK = 3;
    public  static final int PRODUCT_WAGE_CONCEPTUS=2;
    ProductWage productWage;


    @Bind(R.id.processCode)
    TextView processCode;
    @Bind(R.id.processName)
    TextView processName;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.amount)
    TextView amount;
    @Bind(R.id.memo)
    TextView memo;
    @Bind(R.id.selectWage)
    View selectWage;
    @Bind(R.id.selectWage2)
    View selectWage2;

    @Bind(R.id.goBack)
    View goBack;


    private ProductWage originData;
    private int productMaterialType;
    private int wagePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Show the Up button in the action bar.


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

              productMaterialType = getIntent().getIntExtra(ProductWageActivity.PRODUCT_MATERIAL_TYPE, ProductWageActivity.PRODUCT_MATERIAL_CONCEPTUS);

              wagePosition = getIntent().getIntExtra(ProductWageActivity.PRODUCT_WAGE_POSITION, 0);
            productWage = ProductDetailSingleton.getInstance().getProductWage(productMaterialType, wagePosition);
            originData=GsonUtils.fromJson(GsonUtils.toJson(productWage),ProductWage.class);

        }


        setTitle("产品工资编辑");
        bindData(productWage);
    }


    @OnClick(R.id.goBack)
    public void goBack()
    {
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

        ProductDetailSingleton.getInstance().deleteProductWage(productMaterialType,wagePosition);
        productWage=null;
        finish();



    }

    @OnClick({R.id.selectWage, R.id.selectWage2})
    public void onProcessSelect(View v) {

        Intent intent = new Intent(this, ProductProcessSelectActivity.class);

        if (v.getId() == R.id.selectWage) {
            intent.putExtra(ProcessSelectFragment.ARG_PARAM_CODE, productWage.processCode);
        } else {

            intent.putExtra(ProcessSelectFragment.ARG_PARAM_NAME, productWage.processName);
        }


        startActivityForResult(intent, REQUEST_PROCESS_SELECT);
    }

    private void bindData(ProductWage productWage) {

        price.removeTextChangedListener(priceTextWatcher);
        memo.removeTextChangedListener(memoTextWatcher);
        processCode.setText(productWage.processCode);
        processName.setText(productWage.processName);
        price.setText(String.valueOf(productWage.price));
        amount.setText(String.valueOf(productWage.amount));
        memo.setText(productWage.memo == null ? "" : productWage.memo);


        price.addTextChangedListener(priceTextWatcher);
        memo.addTextChangedListener(memoTextWatcher);
    }

    private TextWatcher priceTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            Float price = null;
            try {
                price = Float.valueOf(s.toString().trim());

                productWage.setPrice(price);
                productWage.setAmount(price);
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    ;

    private TextWatcher memoTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            productWage.setMemo(s.toString().trim());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void finish() {



        if(!GsonUtils.toJson(productWage).equalsIgnoreCase(GsonUtils.toJson(originData)))
        {
            setResult(RESULT_OK);
        }
        super.finish();
    }

    @Override
    protected View getContentView() {
        return getLayoutInflater().inflate(R.layout.fragment_product_wage, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {

            case REQUEST_PROCESS_SELECT:


                    ProductProcess productProcess = GsonUtils.fromJson(data.getExtras().getString(ProductProcessSelectActivity.EXTRA_PRODUCT_PROCESS), ProductProcess.class);
                    productWage.setProductProcess(productProcess);
                    bindData(productWage);


                break;
        }


    }

}
