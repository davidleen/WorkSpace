package com.giants3.hd.android.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }
}
