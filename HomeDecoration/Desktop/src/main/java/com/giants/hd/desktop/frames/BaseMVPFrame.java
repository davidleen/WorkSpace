package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.model.DefaultModel;
import com.giants.hd.desktop.mvp.IViewer;
import com.google.inject.Guice;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public  abstract  class BaseMVPFrame<V  extends IViewer> extends  MVPFrame< V, DefaultModel> {




    public BaseMVPFrame(String title) {
        super(title);

    }


    @Override
    public DefaultModel createModel() {
        return new DefaultModel();
    }


}
