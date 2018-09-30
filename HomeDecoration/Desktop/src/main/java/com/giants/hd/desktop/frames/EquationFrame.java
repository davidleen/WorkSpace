package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.EquationIPresenter;
import com.giants.hd.desktop.mvp.viewer.EquationViewer;
import com.giants.hd.desktop.viewImpl.Panel_Equation;
import com.giants3.hd.noEntity.ModuleConstant;

import javax.swing.*;
import java.awt.*;

/**
 *
 * 计算公式备注
 * Created by david on 2015/11/23.
 */
public class EquationFrame extends BaseInternalFrame implements EquationIPresenter {
    EquationViewer equationViewer;
    public EquationFrame( ) {
        super(ModuleConstant.TITLE_EQUATION);


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                search( );
            }
        });

    }

    @Override
    protected Container getCustomContentPane() {

        equationViewer =new Panel_Equation(this);
        return equationViewer.getRoot();
    }

    @Override
    public void search( ) {





//        UseCaseFactory.getInstance().createGetEquations(key,salesId,pageIndex,pageSize).execute(new Subscriber<RemoteData<ErpOrder>>() {
//            @Override
//            public void onCompleted() {
//                equationViewer.hideLoadingDialog();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                equationViewer.hideLoadingDialog();
//                equationViewer.showMesssage(e.getMessage());
//            }
//
//            @Override
//            public void onNext(RemoteData<ErpOrder> erpOrderRemoteData) {
//
//                equationViewer.setData(erpOrderRemoteData);
//            }
//        });
//
//        equationViewer.showLoadingDialog();


    }




}
