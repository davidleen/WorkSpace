package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.ProducerValueConfig;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface ProducerValueConfigPresenter extends IPresenter {
    void save(List<ProducerValueConfig> datas);
}
