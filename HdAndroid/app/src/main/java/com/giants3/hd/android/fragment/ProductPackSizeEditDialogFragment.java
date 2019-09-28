package com.giants3.hd.android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.giants3.android.frame.util.ToastHelper;
import com.giants3.hd.android.R;

import butterknife.Bind;

public class ProductPackSizeEditDialogFragment extends BaseDialogFragment {
    private int insideBoxQuantity;
    private int packQuantity;
    private float packLong;
    private float packWidth;
    private float packHeight;
    private OnNewPackDataListener listener;


    @Bind(R.id.inboxCount)
    EditText insideBoxQuantityText;

    @Bind(R.id.packQuantity)
    EditText packQuantityText;
    @Bind(R.id.packLong)
    EditText packLongText;
    @Bind(R.id.packWidth)
    EditText packWidthText;
    @Bind(R.id.packHeight)
    EditText packHeightText;
 @Bind(R.id.btn_confirm)
    View btn_confirm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return   inflater.inflate(R.layout.dialog_product_pack_info_edit,null);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("包装信息录入");
        insideBoxQuantityText.setText(String.valueOf(insideBoxQuantity));
        packQuantityText.setText(String.valueOf(packQuantity));
        packLongText.setText(String.valueOf(packLong));
        packWidthText.setText(String.valueOf(packWidth));
        packHeightText.setText(String.valueOf(packHeight));
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int newInBoxCount=Integer.valueOf(insideBoxQuantityText.getText().toString().trim());
                    int newPackQuantity=Integer.valueOf(packQuantityText.getText().toString().trim());
//                    float newPackLong=Float.valueOf(packLongText.getText().toString().trim());
//                    float newPackWidth =Float.valueOf(packWidthText.getText().toString().trim());
//                    float newPackHeight =Float.valueOf(packHeightText.getText().toString().trim());

                    listener.onNewPack(newInBoxCount,newPackQuantity );
                    dismiss();


                } catch (Throwable e) {
                    e.printStackTrace();
                    ToastHelper.show("数据输入异常，请检查。。。");
                }

            }
        });

    }

    public void setPackData(int insideBoxQuantity, int packQuantity, float packLong, float packWidth, float packHeight,OnNewPackDataListener listener) {

        this.insideBoxQuantity = insideBoxQuantity;
        this.packQuantity = packQuantity;
        this.packLong = packLong;
        this.packWidth = packWidth;
        this.packHeight = packHeight;
        this.listener = listener;
    }

    public interface OnNewPackDataListener
    {

         void onNewPack(int insideBoxQuantity, int packQuantity);
    }
}
