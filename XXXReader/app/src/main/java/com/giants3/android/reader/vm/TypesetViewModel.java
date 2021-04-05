package com.giants3.android.reader.vm;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.giants3.android.reader.scheme.TextSchemeLoader;
import com.giants3.android.reader.scheme.TypefaceEntity;
import com.giants3.android.reader.scheme.TypefaceLoader;
import com.xxx.reader.TextScheme;
import com.xxx.reader.ThreadConst;

import java.util.ArrayList;
import java.util.List;

public class TypesetViewModel extends  BaseViewModel  {
    public TypesetViewModel(@NonNull Application application) {
        super(application);
    }




    public final MutableLiveData< TypefaceEntity[]> typefaces = new MutableLiveData<>();




    public void loadTypefaces()
    {






        new AsyncTask<Void, Void, TypefaceEntity[]>() {
            @Override
            protected TypefaceEntity[] doInBackground(Void... voids) {
                return TypefaceLoader.loadAllScheme(getApplication());
            }

            @Override
            protected void onPostExecute(TypefaceEntity[] typefaceEntities) {
                typefaces.postValue(typefaceEntities);
            }
        }.executeOnExecutor(ThreadConst.THREAD_POOL_EXECUTOR);







    }

    public void loadData() {

        loadTypefaces();







    }


}
