package com.giants.hd.desktop.viewImpl;

import com.giants3.hd.entity.Xiankang_Jiaju;

import javax.swing.*;

/**
 *
 * 咸康家具数据面板
 * Created by david on 2015/11/15.
 */
public class Panel_XK_Jiaju {



    private JTextField tf_bolijingzi;
    private JTextField tf_choutichicun;
    private JTextField tf_boliguige_kuan;
    private JTextField tf_boliguige_gao;
    private JTextField tf_caokuan;
    private JTextField tf_mucailiao;
    private JTextField tf_dalishi;
    private JTextField tf_gandu;
    private JTextField tf_caoshen;
    private JTextField tf_mupi;
    private JTextField tf_menchicun;
    private JTextField tf_chucangchicun;
    private JTextField tf_memo;
    private JPanel root;

    public void setData(   Xiankang_Jiaju data) {
        if(data==null)
            throw new IllegalArgumentException(" Xiankang_Jiaju must not be null");
        tf_bolijingzi.setText(data.getBolijingzi());
        tf_choutichicun.setText(data.getChoutichicun());
        tf_boliguige_kuan.setText(data.getBolijingziguige_kuan());
        tf_boliguige_gao.setText(data.getBolijingziguige_gao());
        tf_caokuan.setText(data.getCaokuan());
        tf_dalishi.setText(data.getDalishi());
        tf_gandu.setText(data.getGandu());
        tf_caoshen.setText(data.getCaoshen());
        tf_mucailiao.setText(data.getMucailiao());
        tf_mupi.setText(data.getMupi());
        tf_menchicun.setText(data.getMenkaikouchicun());
        tf_chucangchicun.setText(data.getChucangchicun());
        tf_memo.setText(data.getMemo());
    }

    public void getData(  Xiankang_Jiaju data) {
        if(data==null)
            throw new IllegalArgumentException(" Xiankang_Jiaju must not be null");
        data.setBolijingzi(tf_bolijingzi.getText());
        data.setChoutichicun(tf_choutichicun.getText());
        data.setBolijingziguige_kuan(tf_boliguige_kuan.getText());
        data.setBolijingziguige_gao(tf_boliguige_gao.getText());
        data.setCaokuan(tf_caokuan.getText());
        data.setDalishi(tf_dalishi.getText());
        data.setGandu(tf_gandu.getText());
        data.setCaoshen(tf_caoshen.getText());
        data.setMucailiao(tf_mucailiao.getText());
        data.setMupi(tf_mupi.getText());
        data.setMenkaikouchicun(tf_menchicun.getText());
        data.setChucangchicun(tf_chucangchicun.getText());
        data.setMemo(tf_memo.getText());
    }

    public boolean isModified( Xiankang_Jiaju data) {
        if(data==null)
            throw new IllegalArgumentException(" Xiankang_Jiaju must not be null");

        if (tf_bolijingzi.getText() != null ? !tf_bolijingzi.getText().equals(data.getBolijingzi()) : data.getBolijingzi() != null)
            return true;
        if (tf_choutichicun.getText() != null ? !tf_choutichicun.getText().equals(data.getChoutichicun()) : data.getChoutichicun() != null)
            return true;
        if (tf_boliguige_kuan.getText() != null ? !tf_boliguige_kuan.getText().equals(data.getBolijingziguige_kuan()) : data.getBolijingziguige_kuan() != null)
            return true;
        if (tf_boliguige_gao.getText() != null ? !tf_boliguige_gao.getText().equals(data.getBolijingziguige_gao()) : data.getBolijingziguige_gao() != null)
            return true;
        if (tf_caokuan.getText() != null ? !tf_caokuan.getText().equals(data.getCaokuan()) : data.getCaokuan() != null)
            return true;
        if (tf_dalishi.getText() != null ? !tf_dalishi.getText().equals(data.getDalishi()) : data.getDalishi() != null)
            return true;
        if (tf_gandu.getText() != null ? !tf_gandu.getText().equals(data.getGandu()) : data.getGandu() != null)
            return true;
        if (tf_caoshen.getText() != null ? !tf_caoshen.getText().equals(data.getCaoshen()) : data.getCaoshen() != null)
            return true;
        if (tf_mucailiao.getText() != null ? !tf_mucailiao.getText().equals(data.getMucailiao()) : data.getMucailiao() != null)
            return true;
        if (tf_mupi.getText() != null ? !tf_mupi.getText().equals(data.getMupi()) : data.getMupi() != null) return true;
        if (tf_menchicun.getText() != null ? !tf_menchicun.getText().equals(data.getMenkaikouchicun()) : data.getMenkaikouchicun() != null)
            return true;
        if (tf_chucangchicun.getText() != null ? !tf_chucangchicun.getText().equals(data.getChucangchicun()) : data.getChucangchicun() != null)
            return true;
        if (tf_memo.getText() != null ? !tf_memo.getText().equals(data.getMemo()) : data.getMemo() != null) return true;
        return false;
    }

    /**
     * 设置面板是否可见
     * @param visible
     */
    public void setVisible(boolean visible) {


        root.setVisible(visible);

    }
}
