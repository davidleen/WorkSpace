package com.giants3.android.router;
import android.content.Context;
public interface RouterCallBack {



       void onSuccess(Context context,String url);
       void onFail(Context context,String url,String message);

}
