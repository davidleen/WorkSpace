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
public class MaterialPictureDialog extends UploadPictureDialog {
    @Inject
    ApiManager apiManager;
    public MaterialPictureDialog(Window window) {
        super(window);
        setTitle("材料图片管理");
    }


    @Override
    protected RemoteData<Void> uploadPicture(File file, boolean doesOverride) throws HdException {

        return apiManager.uploadMaterialPicture(file, doesOverride);

    }

    @Override
    protected RemoteData<Void> syncPicture() throws HdException {

        return apiManager.syncMaterialPhoto();
    }
}
