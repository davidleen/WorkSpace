package com.rnmap_wb.activity.home;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.giants3.android.network.ProgressListener;
import com.giants3.android.reader.domain.DefaultUseCaseHandler;
import com.giants3.android.reader.domain.GsonUtils;
import com.giants3.android.reader.domain.ResApiFactory;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.rnmap_wb.android.data.RemoteData;
import com.rnmap_wb.android.data.VersionData;
import com.vector.update_app.HttpManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;

class UpdateAppHttpUtil implements HttpManager {
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {


        // UseCaseFactory.getInstance().createPostUseCase()


    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {


        UseCaseFactory.getInstance().createPostUseCase(url, VersionData.class).execute(new DefaultUseCaseHandler<RemoteData<VersionData>>() {


            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RemoteData<VersionData> remoteData) {


                if (remoteData.isSuccess()) {

                    VersionData data = remoteData.data.get(0);
                    callBack.onResponse(GsonUtils.toJson(data));

                }

            }
        });

    }

    @Override
    public void download(@NonNull final String url, @NonNull final String path, @NonNull String fileName, @NonNull final FileCallback callback) {


        final File file = new File(path);

        if (file.exists()) {

            callback.onResponse(file);
            return;
        }


        callback.onBefore();

        new AsyncTask<Void, Long, Void>() {


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);


                if (file.exists()) callback.onResponse(file);


            }

            @Override
            protected void onProgressUpdate(Long... values) {
                super.onProgressUpdate(values);

                callback.onProgress((float)values[0]/values[1], values[1]);
            }

            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    ResApiFactory.getInstance().getResApi().download(url, path, new ProgressListener() {
                        @Override
                        public void onProgressUpdate(long current, long totalLength) {

                            publishProgress(current, totalLength);

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


}
