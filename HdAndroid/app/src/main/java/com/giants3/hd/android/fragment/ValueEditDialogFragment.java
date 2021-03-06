package com.giants3.hd.android.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.giants3.hd.android.R;
import com.giants3.hd.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by davidleen29 on 2016/6/13.
 */
public class ValueEditDialogFragment<T extends Serializable> extends DialogFragment {


    private Class valueType;

    private String titleString;
    private String oldValue;
    private ValueChangeListener listener;

    private boolean isMutiableText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       return   inflater.inflate(R.layout.dialog_value_edit,null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView oldValueTextView= (TextView) view.findViewById(R.id.oldValue);
        oldValueTextView.setText(oldValue);
        final EditText newValueTextView=   view.findViewById(R.id.newValue);
        if(!StringUtils.isEmpty(oldValue)) {
            newValueTextView.setText(oldValue);
            newValueTextView.setSelection(oldValue.length());
        }

        if(valueType!=null) {

            if(valueType==Float.class||valueType==Double.class||valueType==Integer.class)
            {
                newValueTextView.setRawInputType(InputType.TYPE_CLASS_NUMBER);;
                newValueTextView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            }else
            {}

        }


        if(isMutiableText)
        {
            newValueTextView.setMinLines(4);

            newValueTextView.setGravity(Gravity.LEFT|Gravity.TOP);
        }else
        {
            newValueTextView.setMinLines(1);
            newValueTextView.setGravity(Gravity.CENTER );
        }
        getDialog().setTitle(titleString);
        View save=view.findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String value=newValueTextView.getText().toString();

               if(listener!=null)
               {
                   listener.onValueChange(titleString, oldValue,value);
               }


                dismiss();


            }
        });
    }

    public void set(String titleString,String oldValue,ValueChangeListener listener)
    {

        set(titleString,oldValue,null,listener);
    }

    public void set(String titleString,String oldValue,Class<?> valueType,ValueChangeListener listener) {
        this.titleString = titleString;
        this.oldValue = oldValue;
        this.listener = listener;
        this.valueType=valueType;
    }

    public void set(String titleString, List<Object> datas , ValueChangeListener listener) {
        this.titleString = titleString;
        this.oldValue = oldValue;
        this.listener = listener;
    }

    /**
     * 是否多行文本
     * @param b
     */
    public void setMultiableText(boolean b) {

        this.isMutiableText=b;


    }

    public interface  ValueChangeListener
    {public void onValueChange(String title,String oldValue,String newValue);}

}
