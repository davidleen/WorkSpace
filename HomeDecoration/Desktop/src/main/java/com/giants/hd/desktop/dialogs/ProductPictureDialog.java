package com.giants.hd.desktop.dialogs;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;

import java.awt.*;
import java.io.File;

/**
 * Created by davidleen29 on 2015/8/24.
 */
public class ProductPictureDialog extends UploadPictureDialog {
    @Inject
    ApiManager apiManager;
    public ProductPictureDialog(Window window) {
        super(window);
        setTitle("产品图片管理");
        uploadPicture.setText("上传图片并同步");
        syncPicture.setText("所有产品图片同步");
        asyncRelate.setVisible(true);
        asyncRelateMessage.setVisible(true);
    }


    @Override
    protected RemoteData<Void> uploadPicture(File file, boolean doesOverride) throws HdException {

        return apiManager.uploadProductPicture(file,doesOverride);

    }

    @Override
    protected RemoteData<Void> syncPicture() throws HdException {
        return apiManager.syncProductPhoto();
    }

    @Override
    protected RemoteData<Void> syncRelatePicture() throws HdException {

        return apiManager.syncRelateProductPicture();


    }
}
