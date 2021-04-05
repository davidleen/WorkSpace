package com.giants3.android.reader.window;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;

import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.frame.util.AndroidCompatUtils;
import com.giants3.android.kit.AbsPopupWindow;
import com.giants3.android.kit.DownUpPopupWindow;
import com.giants3.android.reader.activity.BaseViewModelActivity;
import com.giants3.android.reader.adapter.TypefaceAdapter;
import com.giants3.android.reader.databinding.PopTypeFaceBinding;
import com.giants3.android.reader.scheme.TypefaceEntity;
import com.giants3.android.reader.vm.TypesetViewModel;
import com.xxx.reader.turnner.sim.SettingContent;

/**
 * 排版弹窗
 */
public class TypesetPopupWindow extends DownUpPopupWindow<AbsPopupWindow.SimpleViewHolder> {


    PopTypeFaceBinding binding;
    TypefaceAdapter adapter;

    public TypesetPopupWindow(final AppCompatActivity activity, final TypesetUpdateListener listener) {
        super(activity);

        binding.typeface.setLayoutManager(new GridLayoutManager(activity, 6));
        adapter = new TypefaceAdapter(activity);
        binding.typeface.setAdapter(adapter);
        updateLineSpace();
        updateWordSpace();
        TypesetViewModel typefaceViewModel = createViewModel(TypesetViewModel.class);
        typefaceViewModel.loadData();


        binding.lineSpaceReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float lineSpace = SettingContent.getInstance().getLineSpace();
                if (lineSpace <= 0) return;
                lineSpace--;
                listener.updateVGap(lineSpace);
                updateLineSpace();

            }
        });
        binding.lineSpaceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float lineSpace = SettingContent.getInstance().getLineSpace();
                if (lineSpace > 60) return;
                lineSpace++;
                listener.updateVGap(lineSpace);
                updateLineSpace();

            }
        });

        binding.wordSpaceReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float wordSpace = SettingContent.getInstance().getWordSpace();
                if (wordSpace <= 0) return;
                wordSpace--;
                listener.updateHGap(wordSpace);
                updateWordSpace();

            }
        });
        binding.wordSpaceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float wordSpace = SettingContent.getInstance().getWordSpace();
                if (wordSpace > 60) return;
                wordSpace++;
                listener.updateHGap(wordSpace);
                updateWordSpace();

            }
        });

        typefaceViewModel.typefaces.observe(activity, new Observer<TypefaceEntity[]>() {
            @Override
            public void onChanged(TypefaceEntity[] typefaces) {

                adapter.setDataArray(typefaces);

            }
        });
        adapter.setOnItemClickListener(new AbstractRecyclerAdapter.OnItemClickListener<TypefaceEntity>() {
            @Override
            public void onItemClick(TypefaceEntity item, int position) {

                listener.onNewTypeface(item);
                adapter.setSelectItem(item);
                adapter.notifyDataSetChanged();

            }
        });
        binding.btnTypeface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.goMoreTypefaces();
                dismiss();

            }
        });

    }

    private void updateLineSpace() {

        binding.lineSpace.setText(String.valueOf(SettingContent.getInstance().getLineSpace()));
    } private void updateWordSpace() {

        binding.wordSpace.setText(String.valueOf(SettingContent.getInstance().getWordSpace()));
    }



    @Override
    protected SimpleViewHolder createViewHolder(Context context) {
        binding = PopTypeFaceBinding.inflate(LayoutInflater.from(context));
        return new SimpleViewHolder(binding.getRoot());
    }

    protected <T extends ViewModel> T createViewModel(Class<T> modelClass) {
        Activity activity = AndroidCompatUtils.findActivityFromContext(mContext);
        if (activity instanceof BaseViewModelActivity) {

            return ((BaseViewModelActivity) activity).getViewModelProvider().get(modelClass);

        }
        return null;
    }


    public interface TypesetUpdateListener {
        void onNewTypeface(TypefaceEntity typeface);

        void updateVGap(float vGap);

        void updateHGap(float hGap);

        void updateTextStyle(Paint.Style style);

        void goMoreTypefaces();

    }

}
