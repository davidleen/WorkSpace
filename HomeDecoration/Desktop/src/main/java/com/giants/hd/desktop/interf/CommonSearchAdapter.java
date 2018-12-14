package com.giants.hd.desktop.interf;

/**
 * Created by davidleen29 on 2018/11/12.
 */
public abstract class CommonSearchAdapter<T> implements CommonSearch<T> {

    protected java.util.Map<String, Object> param;

    @Override
    public void setAdditionalParam(java.util.Map<String, Object> param) {

        this.param = param;
    }

}
