package com.giants3.android.frame.util;

import android.net.Uri;
import android.content.Context;
import android.os.Build;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by davidleen29 on 2018/8/26.
 */

public class UriFileComapt {

    public static Uri fromFile(Context context,File file)
    {


        Uri uri;
        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
          //  intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        }else {
            uri=Uri.fromFile(file);
        }

        return uri;
    }
}
