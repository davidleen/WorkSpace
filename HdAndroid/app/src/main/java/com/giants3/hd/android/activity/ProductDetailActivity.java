package com.giants3.hd.android.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.frame.util.ToastHelper;
import com.giants3.android.frame.util.Utils;
import com.giants3.hd.android.R;
import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.entity.TableData;
import com.giants3.hd.android.events.LoginSuccessEvent;

import com.giants3.hd.android.fragment.ItemPickDialogFragment;
import com.giants3.hd.android.fragment.ProductDetailPresenterImpl;
import com.giants3.hd.android.fragment.ProductPackSizeEditDialogFragment;
import com.giants3.hd.android.fragment.ValueEditDialogFragment;
import com.giants3.hd.android.helper.AuthorityUtil;
import com.giants3.hd.android.helper.ImageLoaderFactory;
import com.giants3.hd.android.helper.ImageViewerHelper;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.android.presenter.ProductDetailPresenter;
import com.giants3.hd.android.viewer.ProductDetailViewer;
import com.giants3.hd.android.widget.ExpandableHeightListView;
import com.giants3.hd.appdata.AProduct;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.Factory;
import com.giants3.hd.entity.Flow;
import com.giants3.hd.entity.Pack;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.ProductMaterial;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.giants3.hd.android.presenter.ProductDetailPresenter.ARG_ITEM;
import static com.giants3.hd.android.presenter.ProductDetailPresenter.REQUEST_PRODUCT_MATERIAL;
import static com.giants3.hd.android.presenter.ProductDetailPresenter.REQUEST_PRODUCT_PAINT;
import static com.giants3.hd.android.presenter.ProductDetailPresenter.REQUEST_PRODUCT_WAGE;


public class ProductDetailActivity extends BaseHeadViewerActivity<ProductDetailPresenter> implements ProductDetailViewer, View.OnClickListener {


    private static final int MAX_MEMO_ROW_LINE = 3;


    @Bind(R.id.photo)
    ImageView photo;


    @Bind(R.id.loading)
    View loading;


    @Bind(R.id.pversion)
    TextView pversion;
    @Bind(R.id.name)
    TextView name;


    @Bind(R.id.unit)
    TextView unit;

    @Bind(R.id.pClass)
    TextView pClass;
    @Bind(R.id.pack)
    TextView pack;
    @Bind(R.id.weight)
    TextView weight;
    @Bind(R.id.factory)
    TextView factory;
    @Bind(R.id.specCm)
    TextView specCm;
    @Bind(R.id.packSize)
    TextView packSize;
    @Bind(R.id.price_panel)
    View price_panel;
    @Bind(R.id.panel_cost_wage_sum)
    View panel_cost_wage_sum;
    @Bind(R.id.fob)
    TextView fob;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.cost)
    TextView cost;
    @Bind(R.id.conceptusCost)
    TextView conceptusCost;
    @Bind(R.id.conceptusWage)
    TextView conceptusWage;
    @Bind(R.id.assembleCost)
    TextView assembleCost;
    @Bind(R.id.assembleWage)
    TextView assembleWage;
    @Bind(R.id.paintCost)
    TextView paintCost;
    @Bind(R.id.paintWage)
    TextView paintWage;
    @Bind(R.id.packCost)
    TextView packCost;
    @Bind(R.id.packWage)
    TextView packWage;
    @Bind(R.id.memo)
    TextView memo;


    @Bind(R.id.panel_conceptus)
    TextView panel_conceptus;
    @Bind(R.id.panel_assemble)
    TextView panel_assemble;
    @Bind(R.id.panel_paint)
    TextView panel_paint;
    @Bind(R.id.panel_pack)
    TextView panel_pack;

    @Bind(R.id.panel_material_wage)
    View panel_material_wage;

    @Bind(R.id.segment_material)
    View segment_material;

    @Bind(R.id.segment_wage)
    View segment_wage;

    //四合一控件 用作选中状态控制
    @Bind({R.id.panel_conceptus, R.id.panel_assemble, R.id.panel_paint, R.id.panel_pack})
    View[] panels;
    //二合一 材料工资  选中状态控制
    @Bind({R.id.segment_material, R.id.segment_wage})
    View[] materialWage;
    @Bind(R.id.product_item_list)
    ExpandableHeightListView listView;


    @Bind(R.id.showMore)
    View showMore;

    @Bind(R.id.control)
    View control;
    @Bind(R.id.edit)
    View edit;
    @Bind(R.id.save)
    View save;
    @Bind(R.id.table_operate)
    View table_operate;
    @Bind(R.id.table_add)
    TextView table_add;
    @Bind(R.id.table_modify)
    TextView table_modify;
    @Bind(R.id.table_delete)
    TextView table_delete;


    boolean editable;


    ItemListAdapter adapter;


    //表格模型 对应的数据结构
    private TableData productMaterialTableData;
    private TableData productWageTableData;
    private TableData productPaintTableData;
    private TableData productPackMaterialTableData;

    private ProductDetail productDetail;


    @Override
    public View getContentView() {
        return View.inflate(this, R.layout.fragment_product_detail, null);
    }

    @Override
    public void initEventAndData(Bundle saveInstance) {

        editable = getIntent().getBooleanExtra(ProductDetailPresenter.EXTRA_EDITABLE, false);
        AProduct aProduct = GsonUtils.fromJson(getIntent().getStringExtra(ARG_ITEM), AProduct.class);
        getPresenter().setInitData(editable, aProduct);


        productMaterialTableData = TableData.resolveData(this, R.array.table_head_product_material_item);
        productWageTableData = TableData.resolveData(this, R.array.table_head_product_wage_item);
        productPaintTableData = TableData.resolveData(this, R.array.table_head_product_paint_item);
        productPackMaterialTableData = TableData.resolveData(this, R.array.table_head_product_pack_material_item);
        adapter = new ItemListAdapter(this) {
            @Override
            public Object getData(String field, Object object) {

                ProductMaterial productMaterial = null;

                boolean viewPriceable = AuthorityUtil.getInstance().canViewPrice(ModuleConstant.NAME_PRODUCT);
                if (!viewPriceable) {
                    switch (field) {
                        case "amount":
                        case "price":
                        case "cost":
                        case "processPrice":
                            return "***";

                    }

                }


                if (object instanceof ProductMaterial) {
                    productMaterial = (ProductMaterial) object;
                }


                if (productDetail != null && productMaterial != null && "amount".equals(field) && productMaterial.flowId == Flow.FLOW_PACK) {
                    return FloatHelper.scale(productMaterial.amount / Math.max(productDetail.product.packQuantity, 1));
                } else
                    return super.getData(field, object);
            }
        };


        edit.setOnClickListener(this);
        save.setOnClickListener(this);
        panel_paint.setOnClickListener(this);
        panel_pack.setOnClickListener(this);
        panel_conceptus.setOnClickListener(this);
        panel_assemble.setOnClickListener(this);

        segment_material.setOnClickListener(this);

        segment_wage.setOnClickListener(this);
        showMore.setOnClickListener(this);
        table_add.setOnClickListener(this);
        table_modify.setOnClickListener(this);
        table_delete.setOnClickListener(this);

        listView.setAdapter(adapter);
        listView.setExpanded(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  listView.setSelection(position);

                ((AbstractAdapter) parent.getAdapter()).setSelectedPosition(position);
                ((AbstractAdapter) parent.getAdapter()).notifyDataSetChanged();


                if (editable) {

                    getPresenter().onItemModify(position - 1);
                }

            }
        });
        applyEditStateToView(name);
        applyEditStateToView(pversion);
        applyEditStateToView(unit);
        applyEditStateToView(pClass);
        applyEditStateToView(pack);
        applyEditStateToView(weight);
        applyEditStateToView(factory);
        applyEditStateToView(specCm);
        applyEditStateToView(packSize);
        adapter.setRowHeight(40);
        photo.setOnClickListener(this);
    }

    @Override
    public void bindData(ProductDetail productDetail) {

        this.productDetail = productDetail;

        control.setVisibility(AuthorityUtil.getInstance().editProduct() ? View.VISIBLE : View.GONE);
        edit.setVisibility(!editable ? View.VISIBLE : View.GONE);
        save.setVisibility(editable ? View.VISIBLE : View.GONE);

        table_operate.setVisibility(editable ? View.VISIBLE : View.GONE);


        final Product product = productDetail.product;
        ImageLoaderFactory.getInstance().displayImage(HttpUrl.completeUrl(product.thumbnail), photo);
        photo.setTag(product.url);
        name.setText(product.name);


        //pversion.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.ic_menu_edit,0);
        pversion.setText(product.pVersion);

        unit.setText(product.pUnitName);

        pClass.setText(product.pClassName);

        pack.setText(product.pack.getName());
        weight.setText(String.valueOf(product.weight));

        factory.setText(String.valueOf(product.factoryName));

        specCm.setText(String.valueOf(product.specCm));

        String packString = product.insideBoxQuantity + "/" + product.packQuantity + "/" + product.packLong + "*" + product.packWidth + "*" + product.packHeight;

        packSize.setText(String.valueOf(packString));


        fob.setText(String.valueOf(product.fob));
        price.setText(String.valueOf(product.price));
        cost.setText(String.valueOf(product.cost));

        boolean viewPriceable = AuthorityUtil.getInstance().canViewPrice(ModuleConstant.NAME_PRODUCT);

        price_panel.setVisibility(viewPriceable ? View.VISIBLE : View.GONE);
        panel_cost_wage_sum.setVisibility(viewPriceable ? View.VISIBLE : View.GONE);


        //白胚组装油漆包装成本工资

        conceptusCost.setText(addUnderLineStringForTextView(String.valueOf(product.conceptusCost)));
        conceptusWage.setText(addUnderLineStringForTextView(String.valueOf(product.conceptusWage)));
        assembleCost.setText(addUnderLineStringForTextView(String.valueOf(product.assembleCost)));
        assembleWage.setText(addUnderLineStringForTextView(String.valueOf(product.assembleWage)));
        paintCost.setText(addUnderLineStringForTextView(String.valueOf(product.paintCost)));
        paintWage.setText(addUnderLineStringForTextView(String.valueOf(product.paintWage)));
        packCost.setText(addUnderLineStringForTextView(String.valueOf(product.packCost)));
        packWage.setText(addUnderLineStringForTextView(String.valueOf(product.packWage)));

        memo.setText(String.valueOf(product.memo));
        memo.setMaxLines(MAX_MEMO_ROW_LINE);
        showMore.setVisibility(View.VISIBLE);
        memo.post(new Runnable() {
            @Override
            public void run() {


                int count = MAX_MEMO_ROW_LINE;
                boolean more = false;


                if (memo.getLineCount() > MAX_MEMO_ROW_LINE) {

                    int width = memo.getLayout().getLineEnd(count - 1);

                    more = product.memo.length() > width;


                }


                showMore.setVisibility(more ? View.VISIBLE : View.GONE);


            }
        });


    }


    @Override
    public void showConceptusMaterial(ProductDetail productDetail) {

        setPanelSelected(panel_conceptus);
        setMaterialWageSelected(segment_material);
        panel_material_wage.setVisibility(View.VISIBLE);
        adapter.setTableData(productMaterialTableData);
        setData(productDetail.conceptusMaterials);
        listView.setAdapter(adapter);
    }

    @Override
    public void showConceptusWage(ProductDetail productDetail) {
        setPanelSelected(panel_conceptus);
        setMaterialWageSelected(segment_wage);
        panel_material_wage.setVisibility(View.VISIBLE);
        adapter.setTableData(productWageTableData);
        setData(productDetail.conceptusWages);
        listView.setAdapter(adapter);
    }

    @Override
    public void showAssembleMaterial(ProductDetail productDetail) {


        setPanelSelected(panel_assemble);
        setMaterialWageSelected(segment_material);
        panel_material_wage.setVisibility(View.VISIBLE);

        adapter.setTableData(productMaterialTableData);
        setData(productDetail.assembleMaterials);
        listView.setAdapter(adapter);

    }

    @Override
    public void showAssembleWage(ProductDetail productDetail) {
        setPanelSelected(panel_assemble);
        setMaterialWageSelected(segment_wage);
        panel_material_wage.setVisibility(View.VISIBLE);


        adapter.setTableData(productWageTableData);
        setData(productDetail.assembleWages);
        listView.setAdapter(adapter);
    }

    @Override
    public void showPaintMaterialWage(ProductDetail productDetail) {
        setPanelSelected(panel_paint);
        panel_material_wage.setVisibility(View.GONE);

        adapter.setTableData(productPaintTableData);
        setData(productDetail.paints);
        listView.setAdapter(adapter);
    }

    @Override
    public void showPackMaterial(ProductDetail productDetail) {

        setPanelSelected(panel_pack);
        setMaterialWageSelected(segment_material);
        panel_material_wage.setVisibility(View.VISIBLE);

        adapter.setTableData(productPackMaterialTableData);
        setData(productDetail.packMaterials);
        listView.setAdapter(adapter);
    }

    @Override
    public void showPackWage(ProductDetail productDetail) {
        setPanelSelected(panel_pack);
        setMaterialWageSelected(segment_wage);
        panel_material_wage.setVisibility(View.VISIBLE);

        adapter.setTableData(productWageTableData);
        setData(productDetail.packWages);
        listView.setAdapter(adapter);
    }

    @Override
    public void showFieldValueEditDailog(String title, final String field, String oldValue) {
        ValueEditDialogFragment dialogFragment = new ValueEditDialogFragment();
        dialogFragment.set(title,oldValue, new ValueEditDialogFragment.ValueChangeListener() {
            @Override
            public void onValueChange(String title, String oldValue, String newValue) {
                try {

                    getPresenter().updateFieldData(field,oldValue,newValue);

                } catch (Throwable t) {

                }


            }
        });
        dialogFragment.show( getSupportFragmentManager(), null);
    }


    @Override
    public void showPackRelateEditDialog(Product product) {


        ProductPackSizeEditDialogFragment dialogFragment=new ProductPackSizeEditDialogFragment();
        dialogFragment.setPackData(product.insideBoxQuantity, product.packQuantity, product.packLong, product.packWidth, product.packHeight, new ProductPackSizeEditDialogFragment.OnNewPackDataListener() {
            @Override
            public void onNewPack(int insideBoxQuantity, int packQuantity, float packLong, float packWidth, float packHeight) {
                getPresenter().setNewPackData(  insideBoxQuantity,   packQuantity,   packLong,   packWidth,   packHeight);
            }
        });
        dialogFragment.show(getSupportFragmentManager(),null);

    }

    public <T> void showPickDialog(String title , final String field, List<T> items, T preItem)
    {
        ItemPickDialogFragment<T> dialogFragment = new ItemPickDialogFragment<T>();
        dialogFragment.set(title, items, preItem, new ItemPickDialogFragment.ValueChangeListener<T>() {
            @Override
            public void onValueChange(String title, T oldValue, T newValue) {

                getPresenter().setNewSelectItem(field,newValue);

            }
        });
        dialogFragment.show(getSupportFragmentManager(), null);

    }

    List<Object> objects = new ArrayList<>(100);

    private void setData(List<?> datas) {

        objects.clear();
        for (Object object
                : datas
        ) {
            objects.add(object);
        }

        adapter.setDataArray(objects);
    }


    private CharSequence addUnderLineStringForTextView(String underLine) {
        return Html.fromHtml("<u>" + underLine + "</u>");

    }


    ProgressDialog progressDialog;


    @Override
    protected ProductDetailPresenter onLoadPresenter() {
        return new ProductDetailPresenterImpl();
    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (v.getId()) {


            case R.id.showMore:
                memo.setMaxLines(Integer.MAX_VALUE);
                showMore.setVisibility(View.GONE);

                break;

            case R.id.panel_conceptus:
            case R.id.panel_assemble:
            case R.id.panel_pack:
            case R.id.panel_paint: {
                //找出点击index
                int index = -1;
                for (int i = 0; i < panels.length; i++) {
                    if (v == panels[i]) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    getPresenter().onPanelForClick(index);
                }


            }
            break;

            case R.id.segment_material:

            case R.id.segment_wage:
                getPresenter().onMaterialWageClick(id == R.id.segment_material ? 0 : 1);


                break;
            case R.id.photo:

                String url = (String) v.getTag();
                if (StringUtils.isEmpty(url)) return;
                ImageViewerHelper.view(this, url);


                break;

            case R.id.edit:
                //打开编辑
                getPresenter().toEditProductDetail();
                break;

            case R.id.save:
                //打开编辑
                getPresenter().saveProductDetail();


                break;


            case R.id.table_delete:
                if (adapter.getSelectedPosition() > 0 && adapter.getCount() > adapter.getSelectedPosition())
                    getPresenter().onItemDelete(adapter.getItem(adapter.getSelectedPosition()), adapter.getSelectedPosition() - 1);
                else
                    ToastHelper.show("请选择一条记录进行删除");
                break;

            case R.id.table_modify:
                if (adapter.getSelectedPosition() > 0 && adapter.getCount() > adapter.getSelectedPosition())
                    getPresenter().onItemModify(adapter.getSelectedPosition() - 1);
                else {

                    ToastHelper.show("请选择一条记录进行修改");
                }
                break;

            case R.id.table_add:
                int selectPosition = 0;
                if (adapter.getSelectedPosition() > 0 && adapter.getCount() > adapter.getSelectedPosition())
                    selectPosition = adapter.getSelectedPosition() - 1;
                getPresenter().onItemAdd(selectPosition);
                break;

        }
    }

    /**
     * 设置面板选中
     *
     * @param v
     */
    private void setPanelSelected(View v) {
        for (View view : panels) {

            view.setSelected(v == view);
        }
    }

    private void setMaterialWageSelected(View v) {
        for (View view : materialWage) {
            view.setSelected(v == view);
        }
    }


    /**
     * 对文本编辑框添加上编辑效果
     *
     * @param v
     */
    private void applyEditStateToView(TextView v) {

        if (!editable) return;
        v.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_menu_edit, 0);
        v.setBackgroundResource(R.drawable.bg_gray_input_selector);
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.width = lp.width + Utils.dp2px(50);
        v.setLayoutParams(lp);
    }


    @OnClick(R.id.unit)
    public void onUnitClick(View v) {

        getPresenter().onUnitClick();

    }

    @OnClick(R.id.factory)
    public void onFactoryClick(View v) {

        getPresenter().onFactoryClick();

    }

    @OnClick(R.id.pversion)
    public void onPVersionClick(View v) {


        getPresenter().onPVersionEdit();

    }


    @OnClick(R.id.name)
    public void onProductNameClick(View v) {


        getPresenter().onProductNameEdit();

    }


    @OnClick(R.id.weight)
    public void onWeightClick(View v) {

        getPresenter().onWeightEdit();

    }


    @OnClick(R.id.specCm)
    public void onSpecCmClick(View v) {

        getPresenter().onSpecCmEdit();

    }


    @OnClick(R.id.packSize)
    public void onPackSizeClick(View v) {
        getPresenter().onPackQuantityEdit();

    }

    @OnClick(R.id.pack)
    public void onPackClick(View v) {
        getPresenter().onPackEdit();

    }
    @OnClick(R.id.pClass)
    public void onPClassClick(View v) {
        getPresenter().onPClassEdit();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_PRODUCT_MATERIAL:


                getPresenter().bindData();
                break;

            case REQUEST_PRODUCT_WAGE:

                getPresenter().bindData();
                break;
            case REQUEST_PRODUCT_PAINT
                    :

                getPresenter().bindData();
                break;
        }
    }
    public void onEvent(LoginSuccessEvent event) {

        onLoginRefresh();

    }

    @Override
    public void onBackPressed() {

        if(getPresenter().needNoSaveNotice())
        {
            AlertDialog alertDialog =new AlertDialog.Builder(this).setMessage("当前录入数据未保存，是否退出？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                }
            }).create();

            alertDialog.show();


        }else
            super.onBackPressed();
    }

}
