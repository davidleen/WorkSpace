package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.ProducerValueConfigPresenter;
import com.giants.hd.desktop.mvp.viewer.ProducerValueConfigViewer;
import com.giants.hd.desktop.mvp.viewer.WorkFlowMessageReportViewer;
import com.giants.hd.desktop.reports.ExcelReportTaskUseCase;
import com.giants.hd.desktop.reports.excels.ReporterHelper;
import com.giants.hd.desktop.reports.excels.TableToExcelReporter;
import com.giants.hd.desktop.utils.FileChooserHelper;
import com.giants.hd.desktop.viewImpl.Panel_ProducerValueConfig;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Message_Report;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.ProducerValueConfig;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.GsonUtils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 流程工作人配置表
 * Created by david on 2015/11/23.
 */
public class ProducerValueConfigFrame extends BaseMVPFrame<ProducerValueConfigViewer> implements ProducerValueConfigPresenter {


    private String originData;

    public ProducerValueConfigFrame() {
        super(ModuleConstant.TITLE_PRODUCER_VALUE_CONFIG);
        loadData();
    }


    @Override
    protected ProducerValueConfigViewer createViewer() {
        return new Panel_ProducerValueConfig(this);
    }

    @Override
    public void save(List<ProducerValueConfig> datas) {

        if (GsonUtils.toJson(datas).equals(originData)) {


            getViewer().showMesssage("数据无改变。");
            return;
        }


        List<ProducerValueConfig> updateDatas = new ArrayList<>();
        for (ProducerValueConfig config : datas) {
            if (config.id > 0 || config.maxOutputValue > 0 || config.minOutputValue > 0||config.workerNum>0) {
                updateDatas.add(config);
            }
        }

        UseCaseFactory.getInstance().createPostJsonUseCase(HttpUrl.saveProducerValueConfig(), GsonUtils.toJson(updateDatas), ProducerValueConfig.class).execute(new RemoteDataSubscriber<ProducerValueConfig>(getViewer()) {


            @Override
            protected void handleRemoteData(RemoteData<ProducerValueConfig> data) {
                originData = GsonUtils.toJson(data.datas);
                getViewer().bindData(data.datas);
                getViewer().showMesssage("保存成功");
                getViewer().completeEdit();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getViewer().completeEdit();
            }
        });
        getViewer().showLoadingDialog();


    }

    @Override
    public boolean hasModifyData() {

        if (originData == null) return false;
        return getViewer().hasModify(originData);

    }

    private void loadData() {


        UseCaseFactory.getInstance().createGetUseCase(HttpUrl.loadProducerValueConfig(), ProducerValueConfig.class).execute(new RemoteDataSubscriber<ProducerValueConfig>(getViewer()) {


            @Override
            protected void handleRemoteData(RemoteData<ProducerValueConfig> data) {
                originData = GsonUtils.toJson(data.datas);
                viewer.bindData(data.datas);
            }


        });
        getViewer().showLoadingDialog();
    }
}
