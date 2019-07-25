package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.ZhilingdanIPresenter;
import com.giants.hd.desktop.mvp.viewer.ZhilingdanViewer;
import com.giants.hd.desktop.viewImpl.Panel_Zhilingdan;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity_erp.Zhilingdan;
import rx.Subscriber;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *  生产流程
 *
 * Created by david on 20160303
 */
public class ZhilingdanFrame extends BaseInternalFrame implements ZhilingdanIPresenter {
    ZhilingdanViewer workFlowViewer;
    private List<Zhilingdan> datas;

    private Date today;

    public ZhilingdanFrame( ) {
        super(ModuleConstant.TITLE_ZHILINGDAN);

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//
//                readData("","1999-01-01","2999-01-01");
//            }
//        });

        today =Calendar.getInstance().getTime();




    }



    private void readData(String osName,String startDate, String endDate)
    {

        UseCaseFactory.getInstance().createSearchZhilingdanUseCase(osName,  startDate,   endDate).execute(new Subscriber<RemoteData<Zhilingdan>>() {
            @Override
            public void onCompleted() {
                workFlowViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                workFlowViewer.hideLoadingDialog();
                workFlowViewer.showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<Zhilingdan> workFlowRemoteData) {

                if(workFlowRemoteData.isSuccess())
                {


                    setData(workFlowRemoteData.datas);
                }


            }


        });
        workFlowViewer.showLoadingDialogCarefully();
    }


    public void setData(List<Zhilingdan> datas)
    {
        this.datas = datas;

        workFlowViewer.setData(datas);
    }


    @Override
    protected Container getCustomContentPane() {
        workFlowViewer =new Panel_Zhilingdan( this);
        return workFlowViewer.getRoot();
    }


    @Override
    public void search(String key, String startDateString, String endDateString) {



        readData(key,startDateString,endDateString);

    }


    @Override
    public void showAll(boolean show, boolean  caigouSelected, boolean jinhuoSelected) {

        if(datas==null) return ;


        List<Zhilingdan> result=null;

        if(show||(!caigouSelected&&!jinhuoSelected))//显示全部， 或者 都没有选  显示原样数据
        {
            result=datas;
        }else {
            result=new ArrayList<>();

            for (Zhilingdan zhilingdan : datas) {
                if (caigouSelected&&zhilingdan.isCaigouOverDue) {

                    result.add(zhilingdan);
                    continue;

                }
                if (jinhuoSelected&&zhilingdan.isJinhuoOverDue) {

                    result.add(zhilingdan);
                    continue;

                }

            }

        }



        workFlowViewer.setData(result);




    }


}
