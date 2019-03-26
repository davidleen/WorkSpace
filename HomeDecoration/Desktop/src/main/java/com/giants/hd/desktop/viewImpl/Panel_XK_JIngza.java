package com.giants.hd.desktop.viewImpl;

import com.giants3.hd.entity.Xiankang_Jingza;

import javax.swing.*;

/**
 * 咸康产品信息面板
 */
public class Panel_XK_JIngza {
    private JPanel panel1;
    private JTextField jingzi_gao;
    private JTextField jingzi_kuan;
    private JTextField caokuan;
    private JTextField caoshen;
    private JTextField guaju;
    private JTextField huangui_gao;
    private JTextField huagui_kuan;
    private JTextField boliguige_gao;
    private JTextField boliguige_kuan;
    private JTextField caizhi;
    private JTextField biankuang;
    private JTextField mobian;
    private JTextField beizhu;

    private JTextField huaxinbianhao;
    private JTextField huaxinchangshang;
    private JTextField huaxinxiaoguo;
    private JTextField beikuanchicun;


    public void setData(Xiankang_Jingza  data) {









        if(data==null) return;
        jingzi_gao.setText(data.getJingzi_gao());
        jingzi_kuan.setText(data.getJingzi_kuan());
        beizhu.setText(data.getBeizhu());
        caokuan.setText(data.getCaokuan());
        caoshen.setText(data.getCaoshen());
        guaju.setText(data.getGuaju());
        huangui_gao.setText(data.getHuangui_gao());
        huagui_kuan.setText(data.getHuangui_kuan());
        boliguige_gao.setText(data.getBoliguige_gao());
        boliguige_kuan.setText(data.getBoliguige_kuan());
        caizhi.setText(data.getCaizhi());
        biankuang.setText(data.getBiankuang());
        mobian.setText(data.getMobian());
        beikuanchicun.setText(data.getBeikuanchicun());

        huaxinbianhao.setText(data.getHuaxinbianhao());
        huaxinchangshang.setText(data.getHuaxinchangshang());
        huaxinxiaoguo.setText(data.getHuaxinxiaoguo());
    }

    public void getData( Xiankang_Jingza data) {

        if(data==null)
            throw new IllegalArgumentException(" Xiankang_Jingza must not be null");


        data.setJingzi_gao(jingzi_gao.getText());
        data.setJingzi_kuan(jingzi_kuan.getText());
        data.setBeizhu(beizhu.getText());
        data.setCaokuan(caokuan.getText());
        data.setCaoshen(caoshen.getText());
        data.setGuaju(guaju.getText());
        data.setHuangui_gao(huangui_gao.getText());
        data.setHuangui_kuan(huagui_kuan.getText());
        data.setBoliguige_gao(boliguige_gao.getText());
        data.setBoliguige_kuan(boliguige_kuan.getText());
        data.setCaizhi(caizhi.getText());
        data.setBiankuang(biankuang.getText());
        data.setMobian(mobian.getText());
        data.setBeikuanchicun(beikuanchicun.getText());

        data.setHuaxinbianhao(huaxinbianhao.getText());
        data.setHuaxinchangshang(huaxinchangshang.getText());
        data.setHuaxinxiaoguo(huaxinxiaoguo.getText());

    }

    public boolean isModified(Xiankang_Jingza data) {
        if (jingzi_gao.getText() != null ? !jingzi_gao.getText().equals(data.getJingzi_gao()) : data.getJingzi_gao() != null)
            return true;
        if (jingzi_kuan.getText() != null ? !jingzi_kuan.getText().equals(data.getJingzi_kuan()) : data.getJingzi_kuan() != null)
            return true;
        if (beizhu.getText() != null ? !beizhu.getText().equals(data.getBeizhu()) : data.getBeizhu() != null)
            return true;
        if (caokuan.getText() != null ? !caokuan.getText().equals(data.getCaokuan()) : data.getCaokuan() != null)
            return true;
        if (caoshen.getText() != null ? !caoshen.getText().equals(data.getCaoshen()) : data.getCaoshen() != null)
            return true;
        if (guaju.getText() != null ? !guaju.getText().equals(data.getGuaju()) : data.getGuaju() != null) return true;
        if (huangui_gao.getText() != null ? !huangui_gao.getText().equals(data.getHuangui_gao()) : data.getHuangui_gao() != null)
            return true;
        if (huagui_kuan.getText() != null ? !huagui_kuan.getText().equals(data.getHuangui_kuan()) : data.getHuangui_kuan() != null)
            return true;
        if (boliguige_gao.getText() != null ? !boliguige_gao.getText().equals(data.getBoliguige_gao()) : data.getBoliguige_gao() != null)
            return true;
        if (boliguige_kuan.getText() != null ? !boliguige_kuan.getText().equals(data.getBoliguige_kuan()) : data.getBoliguige_kuan() != null)
            return true;
        if (caizhi.getText() != null ? !caizhi.getText().equals(data.getCaizhi()) : data.getCaizhi() != null)
            return true;
        if (biankuang.getText() != null ? !biankuang.getText().equals(data.getBiankuang()) : data.getBiankuang() != null)
            return true;
        if (mobian.getText() != null ? !mobian.getText().equals(data.getMobian()) : data.getMobian() != null)
            return true;

        if (huaxinbianhao.getText() != null ? !huaxinbianhao.getText().equals(data.getHuaxinbianhao()) : data.getHuaxinbianhao() != null)
            return true;
        if (huaxinchangshang.getText() != null ? !huaxinchangshang.getText().equals(data.getHuaxinchangshang()) : data.getHuaxinchangshang() != null)
            return true;
        if (huaxinxiaoguo.getText() != null ? !huaxinxiaoguo.getText().equals(data.getHuaxinxiaoguo()) : data.getHuaxinxiaoguo() != null)
            return true;
        return false;
    }


    /**
     * 设置面板是否可见
     * @param visible
     */
    public void setVisible(boolean visible) {


        panel1.setVisible(visible);

    }
}
