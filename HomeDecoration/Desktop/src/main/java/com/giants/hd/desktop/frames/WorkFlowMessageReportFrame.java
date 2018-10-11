package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.WorkFlowMessageReportPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowMessageReportViewer;
import com.giants.hd.desktop.reports.ExcelReportTaskUseCase;
import com.giants.hd.desktop.reports.excels.ReporterHelper;
import com.giants.hd.desktop.reports.excels.TableToExcelReporter;
import com.giants.hd.desktop.utils.FileChooserHelper;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Message_Report;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.DateFormats;

import javax.swing.*;
import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * 流程工作人配置表
 * Created by david on 2015/11/23.
 */
public class WorkFlowMessageReportFrame extends BaseMVPFrame<WorkFlowMessageReportViewer> implements WorkFlowMessageReportPresenter {


    private List<WorkFlowMessage> mDatas;

    public WorkFlowMessageReportFrame() {

        super(ModuleConstant.TITLE_UNHANDLE_MESSAGE_REPORT);




    }



    private void setData(List<WorkFlowMessage> datas) {
        mDatas = datas;
        getViewer().bindData(datas);
    }



    @Override
    protected WorkFlowMessageReportViewer createViewer() {
        return new Panel_Work_Flow_Message_Report(this);
    }


    @Override
    public void exportExcel(List<TableField> fieldList) {



        if(mDatas==null||mDatas.size()==0) {
            getViewer().showMesssage("无数据可导出");
            return ;
        }
        //选择excel 文件
        final File file = FileChooserHelper.chooseFile(JFileChooser.DIRECTORIES_ONLY, false,null);
        if(file==null) return;
        final String fileName = "未处理消息报表-" + DateFormats.FORMAT_YYYY_MM_DD_HH_MM_SS_LOG.format(Calendar.getInstance().getTime()) + ".xls";
        TableToExcelReporter tableToExcelReporter=new TableToExcelReporter(fileName,fieldList );
        new ExcelReportTaskUseCase<>(tableToExcelReporter,mDatas,file.getAbsolutePath()).execute(new RemoteDataSubscriber<Void>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData data) {
                ReporterHelper.openFileHint(getWindow(),new File(file,fileName));
            }
        });



        getViewer().showLoadingDialog();

//
//        System.out.println(new File(file,fileName));
//        try {
//            tableToExcelReporter.report(mDatas, file.getAbsolutePath());
//
//            ReporterHelper.openFileHint(getWindow(),new File(file,fileName));
//
//        } catch (Throwable e) {
//
//            e.printStackTrace();
//            getViewer().showError(e.getMessage());
//        }







    }


    @Override
    public void search(String dateStart, String dateEnd, boolean unhandle, boolean overdue) {
        UseCaseFactory.getInstance().createGetWorkFlowMessageReportUseCase(  dateStart,   dateEnd,   unhandle,   overdue).execute(new RemoteDataSubscriber<WorkFlowMessage>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData<WorkFlowMessage> data) {
                setData(data.datas);
            }
        });


        getViewer().showLoadingDialog();
    }
}
