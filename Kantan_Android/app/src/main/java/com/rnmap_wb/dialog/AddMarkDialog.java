package com.rnmap_wb.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.rnmap_wb.R;

public class AddMarkDialog extends Dialog {
    public AddMarkDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_add_mark);
    }
}
