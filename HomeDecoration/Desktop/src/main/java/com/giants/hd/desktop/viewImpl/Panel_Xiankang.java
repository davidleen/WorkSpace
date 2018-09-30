package com.giants.hd.desktop.viewImpl;

import com.giants3.hd.entity.Xiankang;
import com.giants3.hd.entity.Xiankang_Dengju;
import com.giants3.hd.entity.Xiankang_Jiaju;
import com.giants3.hd.entity.Xiankang_Jingza;

import javax.swing.*;

/**
 * 咸康汇总面板
 */
public class Panel_Xiankang {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private Panel_XK_JIngza panel_xk_jingza;
    private JTextField tf_qitahuohao;
    private JComboBox cb_jiaquan;
    private JTextField tf_caizhibaifenbi;
    private Panel_XK_Jiaju panel_xk_jiaju;
    private Panel_XK_Dengju panel_xk_dengju;
    Xiankang xiankang;

    public void setData(Xiankang data) {

        this.xiankang=data;

        if(data!=null) {

            if (data.getXiankang_jingza() == null) {
                data.setXiankang_jingza(new Xiankang_Jingza());
            }
            panel_xk_jingza.setData(data.getXiankang_jingza());

            if (data.getXiankang_jiaju() == null) {
                data.setXiankang_jiaju(new Xiankang_Jiaju());
            }
            panel_xk_jiaju.setData(data.getXiankang_jiaju());



            if (data.getXiankang_dengju() == null) {
                data.setXiankang_dengju(new Xiankang_Dengju());
            }
            panel_xk_dengju.setData(data.getXiankang_dengju());

            cb_jiaquan.setSelectedItem(data.getJiaquan());
            tf_caizhibaifenbi.setText(data.getCaizhibaifenbi());
            tf_qitahuohao.setText(data.getQitahuohao());
        }
    }

    public Xiankang getData() {
        if(xiankang==null)
        {
            xiankang=new Xiankang();
        }

        if(xiankang.xiankang_jingza==null)
        {
            xiankang.xiankang_jingza=new Xiankang_Jingza();
        }
        panel_xk_jingza.getData(xiankang.xiankang_jingza) ;
        if(xiankang.xiankang_jiaju==null)
        {
            xiankang.xiankang_jiaju=new Xiankang_Jiaju();
        }
        panel_xk_jiaju.getData(xiankang.getXiankang_jiaju());
        if(xiankang.xiankang_dengju==null)
        {
            xiankang.xiankang_dengju=new Xiankang_Dengju();
        }
        panel_xk_dengju.getData(xiankang.getXiankang_dengju());


        xiankang.setQitahuohao(tf_qitahuohao.getText().trim());
        xiankang.setCaizhibaifenbi(tf_caizhibaifenbi.getText().trim());
        xiankang.setJiaquan(cb_jiaquan.getSelectedItem().toString());

        return xiankang;

    }

    public boolean isModified(Xiankang data) {
        if( panel_xk_jingza.isModified(data.getXiankang_jingza()))
            return true;
        if( panel_xk_jiaju.isModified(data.getXiankang_jiaju()))
            return true;
        if( panel_xk_dengju.isModified(data.getXiankang_dengju()))
            return true;
        return false;

}


    /**
     * 设置面板是否可见
     * @param visible
     */
    public void setVisible(boolean visible) {

        panel1.setVisible(visible);
        panel_xk_jingza.setVisible(visible);
        panel_xk_jiaju.setVisible(visible);


    }
}
