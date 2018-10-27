package com.giants3.hd.domain.repository;

import com.giants3.hd.noEntity.RemoteData;
import rx.Observable;

import java.io.File;
import java.util.List;

/**
 * 文件上传处理
 *
 * Created by david on 2015/10/13.
 */
public interface FileRepository {



    /**
     * 上传临时图片
     * @return
     */
    public Observable<List<String>> uploadTempFile(File[] file );


    Observable<RemoteData<Void>> syncProductPicture(String remoteResource,String filterKey,boolean shouldOverride);
}
