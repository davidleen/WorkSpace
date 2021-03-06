package com.giants3.android.reader.activity;

import android.content.Context;
import android.content.Intent;

import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.frame.util.ToastHelper;
import com.giants3.file.FileContentType;

import java.io.File;
import java.io.IOException;

public class FileBrowser {


    private Context context;

    public FileBrowser(Context context) {

        this.context = context;
    }


    public void openFile(File file) throws IOException {

        String contentType = FileContentType.getContentType(file);
        if (StringUtil.isEmpty(contentType)) {
            ToastHelper.show(file.getPath() + "文件类型获取失败");
            return;
        }

        switch (contentType.toLowerCase()) {
            case FileContentType.EPUB:
            case FileContentType.TEXT:{


                Intent intent = new Intent(context, TextReadActivity.class);
                intent.putExtra("filePath", file.getAbsolutePath());
                context.startActivity(intent);
            }


                break;

        }

    }
}
