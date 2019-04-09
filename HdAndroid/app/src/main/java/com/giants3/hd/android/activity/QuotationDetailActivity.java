package com.giants3.hd.android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.giants3.hd.android.R;
import com.giants3.hd.android.adapter.ItemListAdapter;
import com.giants3.hd.android.entity.TableData;
import com.giants3.hd.android.fragment.ProductDetailFragment;
import com.giants3.hd.android.fragment.QuotationDetailFragment;
import com.giants3.hd.android.fragment.ValueEditDialogFragment;
import com.giants3.hd.android.helper.AuthorityUtil;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.android.helper.ToastHelper;
import com.giants3.hd.android.mvp.quotation.QuotationDetailMVP;
import com.giants3.hd.android.mvp.quotation.QuotationDetailPresenterImpl;
import com.giants3.hd.appdata.AProduct;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.entity.QuotationXKItem;
import com.giants3.hd.entity.QuoteAuth;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.QuotationDetail;

import butterknife.Bind;

/**
 * An activity representing a single ProductListActivity detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item_work_flow_report details are presented side-by-side with a list of items
 * in a {@link ProductListActivity}.
 */
public class QuotationDetailActivity extends BaseHeadViewerActivity<QuotationDetailPresenterImpl> implements QuotationDetailMVP.Viewer {
    public static final String P_VERSION = "pVersion";
    public static final String PRODUCT_NAME = "productName";
    public static final String P_VERSION2 = "pVersion2";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static String ARG_ITEM = "PRODUCT_MATERIAL_TYPE";

    private static final int MAX_MEMO_ROW_LINE = 3;


    ItemListAdapter<QuotationItem> quotationItemItemListAdapter;
    ItemListAdapter<QuotationXKItem> xkquotationItemItemListAdapter;
    private QuotationDetailFragment.OnFragmentInteractionListener mListener;

    @Bind(R.id.listView)
    ListView listView;


    @Bind(R.id.cus_no)
    TextView cus_no;

    @Bind(R.id.qDate)
    TextView qDate;
    @Bind(R.id.validDate)
    TextView validDate;
    @Bind(R.id.moneyType)
    TextView moneyType;
    @Bind(R.id.sal)
    TextView sal;
    @Bind(R.id.memo)
    TextView memo;
    @Bind(R.id.showMore)
    View showMore;
    @Bind(R.id.more_text)
    View more_text;
    @Bind(R.id.qNumber)
    TextView qNumber;

    @Bind(R.id.check)
    public View check;
    @Bind(R.id.overdue)
    public View overdue;
    @Bind(R.id.verify)
    public View verify;
    @Bind(R.id.unVerify)
    public View unVerify;

    @Bind(R.id.verifyHint)
    public View verifyHint;


    private TableData xkQuotationTable;

    private TableData quotationTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("报价单详情");


        Quotation quotation;
        try {
            quotation = GsonUtils.fromJson(getIntent().getStringExtra(ARG_ITEM), Quotation.class);
        } catch (HdException e) {
            e.printStackTrace();
            ToastHelper.show("参数异常");
            return;
        }

        getPresenter().init(quotation);
        bindQuotation(quotation);

        xkQuotationTable = TableData.resolveData(this, R.array.table_head_xkquotation_item);
        quotationTable = TableData.resolveData(this, R.array.table_head_quotation_item);


        QuoteAuth quoteAuth = SharedPreferencesHelper.getInitData().quoteAuth;

        if (quoteAuth != null) {

            //根据权限 移除部分字段显示
            if (!quoteAuth.costVisible) {
                quotationTable.removeField("cost");
                xkQuotationTable.removeField("cost");
                xkQuotationTable.removeField("cost2");
            }
            if (!quoteAuth.fobVisible) {
                quotationTable.removeField("price");
                xkQuotationTable.removeField("price");
                xkQuotationTable.removeField("price2");
            }
        }

        if (quoteAuth != null && quoteAuth.fobEditable) {

        } else {
            xkQuotationTable.removeField("cost_price_ratio");
            xkQuotationTable.removeField("cost_price_ratio");
        }


        quotationItemItemListAdapter = new ItemListAdapter<QuotationItem>(this)
        ;
        xkquotationItemItemListAdapter = new ItemListAdapter<QuotationXKItem>(this);

        {
            ItemListAdapter.CellClickListener<QuotationItem> cellClickListener = new ItemListAdapter.CellClickListener<QuotationItem>() {
                @Override
                public boolean isCellClickable(String field) {

                    return PRODUCT_NAME.equalsIgnoreCase(field) || P_VERSION.equalsIgnoreCase(field);

                }

                @Override
                public void onCellClick(String field, QuotationItem data, int position) {
                    jumpProduct(data.productId);

                }
            };
            quotationItemItemListAdapter.setCellClickListener(cellClickListener);
        }


        {
            ItemListAdapter.CellClickListener<QuotationXKItem> xkcellClickListener = new ItemListAdapter.CellClickListener<QuotationXKItem>() {
                @Override
                public boolean isCellClickable(String field) {

                    return PRODUCT_NAME.equalsIgnoreCase(field) || P_VERSION.equalsIgnoreCase(field) || P_VERSION2.equalsIgnoreCase(field);

                }

                @Override
                public void onCellClick(String field, QuotationXKItem data, int position) {
                    long productId = 0;
                    switch (field) {
                        case PRODUCT_NAME:

                            productId = data.productId > 0 ? data.productId : data.productId2;
                            break;
                        case P_VERSION:
                            productId = data.productId;

                            break;

                        case P_VERSION2:
                            productId = data.productId2;
                            break;


                    }
                    if (productId > 0)
                        jumpProduct(productId);
                }
            };
            xkquotationItemItemListAdapter.setCellClickListener(xkcellClickListener);

        }


        quotationItemItemListAdapter.setTableData(quotationTable);
        xkquotationItemItemListAdapter.setTableData(xkQuotationTable);


        showMore.setOnClickListener(this);


        getPresenter().loadData();


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //彈窗提示修改利潤比
                if (SharedPreferencesHelper.getInitData().quoteAuth.fobEditable && !getPresenter().isVerifiedQuotation()) {

                    float ratio = 0;
                    final Object o = parent.getItemAtPosition(position);
                    if (o instanceof QuotationXKItem) {


                        ratio = ((QuotationXKItem) o).cost_price_ratio;
                    } else {
                        ratio = ((QuotationItem) o).cost_price_ratio;
                    }

                    ValueEditDialogFragment dialogFragment = new ValueEditDialogFragment();
                    final float finalRatio = ratio;
                    dialogFragment.set("修改利润比", String.valueOf(finalRatio), new ValueEditDialogFragment.ValueChangeListener() {
                        @Override
                        public void onValueChange(String title, String oldValue, String newValue) {

                            float newRatio = 0;
                            try {
                                newRatio = Float.valueOf(newValue);
                            } catch (Throwable t) {
                            }
                            if (newRatio == 0 || newRatio > 1) {
                                ToastHelper.show("输入的利润比不正确");
                                return;
                            }
                            if (newRatio == finalRatio) {
                                ToastHelper.show("利润比没有改变");
                                return;

                            }
                            if (o instanceof QuotationXKItem) {

                                getPresenter().loadProductAndCalculatePrice((QuotationXKItem) o, newRatio, xkquotationItemItemListAdapter);

                            } else {
                                getPresenter().loadProductAndCalculatePrice((QuotationItem) o, newRatio, quotationItemItemListAdapter);
                            }


                        }
                    });
                    dialogFragment.setMultiableText(false);
                    dialogFragment.show(getSupportFragmentManager(), null);

                    return true;

                }
                return false;


            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(QuotationDetailActivity.this).setTitle("确定保存利润比修改，并将该报价单设置为已审核?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPresenter().verify();
                    }
                }).setNegativeButton("取消", null).create().show();


            }
        });

        unVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(QuotationDetailActivity.this).setTitle("是否撤销报价单的审核？（未审核的报价单可以修改，但不能导出excel）").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPresenter().unVerify();
                    }
                }).setNegativeButton("取消", null).create().show();


            }
        });


    }

    private void jumpProduct(long productId) {

        //跳转产品详情
        AProduct aProduct = new AProduct();
        aProduct.id = productId;
        //调整act
        Intent intent = new Intent(QuotationDetailActivity.this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailFragment.ARG_ITEM, GsonUtils.toJson(aProduct));
        startActivity(intent);
    }

    @Override
    protected QuotationDetailPresenterImpl onLoadPresenter() {
        return new QuotationDetailPresenterImpl();
    }

    @Override
    protected void initEventAndData(Bundle savedInstance) {

    }


    @Override
    protected View getContentView() {
        return getLayoutInflater().inflate(R.layout.activity_quotation_detail, null);
    }

    private void bindQuotation(Quotation quotation) {

        qNumber.setText(quotation.qNumber);
        qDate.setText(quotation.qDate);
        cus_no.setText(quotation.customerCode + quotation.customerName);
        sal.setText(quotation.salesman);
        validDate.setText(quotation.vDate);
        moneyType.setText(quotation.currency);

        memo.setText(quotation.memo);
        memo.setMaxLines(MAX_MEMO_ROW_LINE);
        memo.post(new Runnable() {
            @Override
            public void run() {


                int count = MAX_MEMO_ROW_LINE;
                boolean more = false;


                if (memo.getLineCount() == MAX_MEMO_ROW_LINE) {


                }


                showMore.setVisibility(more ? View.VISIBLE : View.GONE);
                more_text.setVisibility(more ? View.VISIBLE : View.GONE);


            }
        });

        if (listView.getAdapter() == null) {
            if (quotation.quotationTypeId == Quotation.QUOTATION_TYPE_XK) {
                listView.setAdapter(xkquotationItemItemListAdapter);
            } else {
                listView.setAdapter(quotationItemItemListAdapter);
            }
        }


        check.setSelected(quotation.isVerified);
        boolean isOverdueAndNotCheck = !quotation.isVerified && quotation.isOverdue();
        check.setVisibility(isOverdueAndNotCheck ? View.GONE : View.VISIBLE);
        overdue.setVisibility(isOverdueAndNotCheck ? View.VISIBLE : View.GONE);
        boolean isVerifiable = AuthorityUtil.getInstance().verifyQuotation();
        verify.setVisibility(isVerifiable && !quotation.isVerified ? View.VISIBLE : View.GONE);
        unVerify.setVisibility(isVerifiable && quotation.isVerified ? View.VISIBLE : View.GONE);

        verifyHint.setVisibility(isVerifiable && !quotation.isVerified ? View.VISIBLE : View.GONE);
    }


    @Override
    public void bindData(QuotationDetail data) {

        bindQuotation(data.quotation);

        quotationItemItemListAdapter.setDataArray(data.items);
        xkquotationItemItemListAdapter.setDataArray(data.XKItems);

    }

    @Override
    public void showUnSaveAlert() {

    }


    @Override
    public void updateListData() {

        quotationItemItemListAdapter.notifyDataSetChanged();
        xkquotationItemItemListAdapter.notifyDataSetChanged();
    }
}
