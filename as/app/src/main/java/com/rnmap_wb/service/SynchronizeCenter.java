package com.rnmap_wb.service;

import android.os.AsyncTask;

import com.giants3.android.frame.util.FileUtils;
import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.domain.DefaultUseCaseHandler;
import com.giants3.android.reader.domain.GsonUtils;
import com.giants3.android.reader.domain.RemoteDataParser;
import com.giants3.android.reader.domain.ResApiFactory;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.google.gson.reflect.TypeToken;
import com.rnmap_wb.MainApplication;
import com.rnmap_wb.android.data.LoginResult;
import com.rnmap_wb.android.data.RemoteData;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.android.data.UploadFileResult;
import com.rnmap_wb.entity.MapElement;
import com.rnmap_wb.entity.PostElements;
import com.rnmap_wb.url.HttpUrl;
import com.rnmap_wb.utils.SessionManager;
import com.rnmap_wb.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 同步处理中心
 */
public class SynchronizeCenter {


    public static final String MAP_ELEMENTS_JSON = "mapElements.json";
    public static final String TASK_FEEDBACK = "feedback";
    public static final String UPDATE = "UPDATE";
    public static final String REGEX = ";";

    public static void synchronize() {


        LoginResult loginResult = SessionManager.getLoginUser(MainApplication.baseContext);
        if (loginResult == null) return;
        String filePath = StorageUtils.getFilePath(loginResult.id);
        File file = new File(filePath);
        if (file.exists() && file.isDirectory()) {

            for (File taskFile : file.listFiles()) {

                if (taskFile.exists() && taskFile.isDirectory()) {
                    File updateFile = new File(taskFile, UPDATE);

                    //检查是否存在update文件
                    if (updateFile.exists()) {
                        //同步更新文件。
                        String taskId = taskFile.getName();
                        File mapElementsFile = new File(taskFile, MAP_ELEMENTS_JSON);
                        File feedbackFile=new File(taskFile, TASK_FEEDBACK);
                        if (mapElementsFile.exists()) {

                            String mapElementFile = mapElementsFile.getAbsolutePath();
                            String mapElementsString = FileUtils.readStringFromFile(mapElementFile);
                            List<MapElement> mapElements = GsonUtils.fromJson(mapElementsString, new TypeToken<List<MapElement>>() {
                            }.getType());
                            if (mapElements == null) return;

                            String feedback=FileUtils.readStringFromFile(feedbackFile.getAbsolutePath());

                            PostElements postElements = new PostElements();
                            postElements.taskId = taskId;
                            postElements.feedback = feedback;
                            postElements.flags = mapElements;

                            synchronizedPictureIfNeed(postElements, mapElementFile, updateFile.getAbsolutePath());


                        }


                    }


                }


            }

        }


    }


    private static void synchronizedPictureIfNeed(final PostElements postElements, final String mapElementFilePath, final String uploadFilePath) {


        final List<MapElement> mapElements = postElements.flags;
        new AsyncTask<Void, Void, List<MapElement>>() {
            @Override
            protected List<MapElement> doInBackground(Void... voids) {

                boolean doUpload = false;
                for (MapElement mapElement : mapElements) {

                    if (StringUtil.isEmpty(mapElement.filePath)) continue;

                    String[] filePaths = mapElement.filePath.split(REGEX);
                    if (filePaths != null && filePaths.length > 0) {


                        String[] urls = new String[filePaths.length];
                        String[] oldUrl = mapElement.picture == null ? null : mapElement.picture.split(REGEX);
                        if (oldUrl != null)
                            for (int i = 0; i < oldUrl.length; i++) {
                                urls[i] = oldUrl[i];
                            }

                        RemoteDataParser<UploadFileResult> parser = new RemoteDataParser<>(UploadFileResult.class);

                        for (int i = 0; i < filePaths.length; i++) {
                            if (!StringUtil.isEmpty(urls[i])) continue;
                            doUpload = true;
                            String filePath = filePaths[i];
                            File file = new File(filePath);
                            String result = null;
                            try {
                                result = ResApiFactory.getInstance().getResApi().uploadFile(HttpUrl.getUploadFileUrl(), "file", file);
                                RemoteData<UploadFileResult> remoteData = parser.parser(result);
                                if (remoteData != null && remoteData.isSuccess() && remoteData.data.size() > 0) {
                                    urls[i] = remoteData.data.get(0).httpPath;

                                } else {
                                    //uploadFail
                                    return null;
                                }

                            } catch (IOException e) {

                                //uploadFail
                                e.printStackTrace();
                                return null;
                            }


                        }


                        mapElement.picture = StringUtil.toString(REGEX, urls);


                    } else {
                        mapElement.picture = "";
                    }


                }

                if (doUpload) {

                    try {
                        FileUtils.writeStringToFile(GsonUtils.toJson(mapElements), mapElementFilePath);
                    } catch (Exception e) {
                        Log.e(e);
                        return null;
                    }
                }


                return mapElements;
            }

            @Override
            protected void onPostExecute(List<MapElement> elements) {

                if (elements != null) {


                    postJson(postElements, uploadFilePath);
                }


            }
        }
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }


    private static void postJson(PostElements postElements, final String updateFilePath) {


        UseCaseFactory.getInstance().createPostJsonUseCase(HttpUrl.getSynchronizeFlags(), GsonUtils.toJson(postElements), Void.class).execute(new DefaultUseCaseHandler<RemoteData<Void>>() {


            @Override
            public void onError(Throwable e) {

                Log.e(e);
            }

            @Override
            public void onNext(RemoteData<Void> remoteData) {


                if (remoteData.isSuccess()) {

                    //同步成功删除 删除更新标志文件
                    new File(updateFilePath).delete();

                } else {
                    Log.e(remoteData.errmsg);
                }


            }
        });


    }

    public static String getElementsFilePath(Task task) {


        return getTaskRelateFilePath(task,MAP_ELEMENTS_JSON);

    }

    public static String getTaskFeedbackFilePath(Task task) {


        return getTaskRelateFilePath(task,TASK_FEEDBACK);

    }

    /**
     * 是否处于待更新状态
     * @param task
     * @return
     */
    public static String getTaskUpdateStateFilePath(Task task) {


        return getTaskRelateFilePath(task,UPDATE);
    }

    private static String getTaskRelateFilePath(Task task,String fileName)
    {
        //查找本地文件  是否存在标记文件
        LoginResult loginResult = SessionManager.getLoginUser(MainApplication.baseContext);
        if(loginResult==null) return "";

        String filePath = StorageUtils.getFilePath(loginResult.id + File.separator + task.id + File.separator +fileName);

        return filePath;
    }


    public static boolean waitForFeedBack(Task task)
    {

        return new File(getTaskUpdateStateFilePath(task)).exists();
    }


    public static String getKmlFilePath(Task task)
    {
            return      StorageUtils.getFilePath(task.id+task.dir_id + ".kml");

    }
}
