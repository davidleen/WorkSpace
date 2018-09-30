package com.giants.hd.desktop.viewImpl;

import com.giants3.hd.entity.Xiankang_Dengju;

import javax.swing.*;

/**
 * Created by david on 2015/11/16.
 */
public class Panel_XK_Dengju {
    private JPanel root;
    private JComboBox cb_dengyongtu;
    private JComboBox cb_dengtichangshang;
    private JTextField tf_dengdikuan;
    private JTextField tf_dengdichang;
    private JTextField tf_feiniuhao;
    private JTextField tf_feiniukuan;
    private JTextField tf_feiniuchang;
    private JTextField tf_feiniugao;
    private JComboBox cb_washu;
    private JComboBox cb_dengpaoleixing;
    private JComboBox cb_dengtouleixing;
    private JComboBox cb_dengtoukaiguanleixing;
    private JTextField tf_memo;
    private JTextField tf_dengzhaobuliao;
    private JTextField tf_dengzhaochicun;
    private JTextField tf_dengzhaobaozhuang;
    private JComboBox cb_approval;
    private JComboBox cb_kdyn;
    private JComboBox cb_8dtt;
    private JComboBox cb_hafujiayanse;
    private JTextField tf_hafujia;
    private JTextField tf_dengzhaochangshang;
    private JTextField tf_caizhi;

    public void setData(Xiankang_Dengju data) {


        if (data==null) throw new IllegalArgumentException(" Xiankang_Dengju must not  be null");
        tf_dengdikuan.setText(data.getDengdikuan());
        tf_dengdichang.setText(data.getDengdichang());
        tf_feiniuhao.setText(data.getFeiniuhao());
        tf_feiniukuan.setText(data.getFeiniukuan());
        tf_feiniuchang.setText(data.getFeiniuchang());
        tf_feiniugao.setText(data.getFeiniugao());
        tf_memo.setText(data.getMemo());
        tf_dengzhaobuliao.setText(data.getDengzhaobuliao());
        tf_dengzhaochicun.setText(data.getDengzhaochicun());
        tf_dengzhaobaozhuang.setText(data.getDengzhaobaozhuang());
        tf_hafujia.setText(data.getHafujia());
        tf_dengzhaochangshang.setText(data.getDengzhaochangshang());
        tf_caizhi.setText(data.getCaizhi());




        cb_8dtt.setSelectedItem(data.get_8dtt());
        cb_approval.setSelectedItem(data.getULApproval());

        cb_dengpaoleixing.setSelectedItem(data.getDengpaoleixing());

        cb_dengtichangshang.setSelectedItem(data.getDengtichangshang());

        cb_dengtoukaiguanleixing.setSelectedItem(data.getDengtoukaiguangleixing());

        cb_dengtouleixing.setSelectedItem(data.getDengtouleixing());

        cb_dengyongtu.setSelectedItem(data.getDengyongtu());

        cb_hafujiayanse.setSelectedItem(data.getHafujiayanse());

        cb_kdyn.setSelectedItem(data.getKdyn());
        cb_washu.setSelectedItem(data.getWashu());


    }

    public void getData(Xiankang_Dengju data) {

        if (data==null) throw new IllegalArgumentException(" Xiankang_Dengju must not  be null");

        data.setDengdikuan(tf_dengdikuan.getText());
        data.setDengdichang(tf_dengdichang.getText());
        data.setFeiniuhao(tf_feiniuhao.getText());
        data.setFeiniukuan(tf_feiniukuan.getText());
        data.setFeiniuchang(tf_feiniuchang.getText());
        data.setFeiniugao(tf_feiniugao.getText());
        data.setMemo(tf_memo.getText());
        data.setDengzhaobuliao(tf_dengzhaobuliao.getText());
        data.setDengzhaochicun(tf_dengzhaochicun.getText());
        data.setDengzhaobaozhuang(tf_dengzhaobaozhuang.getText());
        data.setHafujia(tf_hafujia.getText());
        data.setDengzhaochangshang(tf_dengzhaochangshang.getText());
        data.setCaizhi(tf_caizhi.getText());


        data.set_8dtt(getCbItemValue(cb_8dtt));
        data.setULApproval(getCbItemValue(cb_approval));
        data.setDengpaoleixing(getCbItemValue(cb_dengpaoleixing));
        data.setDengtichangshang(getCbItemValue(cb_dengtichangshang));
        data.setDengtoukaiguangleixing(getCbItemValue(cb_dengtoukaiguanleixing));
        data.setDengtouleixing(getCbItemValue(cb_dengtouleixing));
        data.setDengyongtu(getCbItemValue(cb_dengyongtu));
        data.setHafujiayanse(getCbItemValue(cb_hafujiayanse));
        data.setKdyn(getCbItemValue(cb_kdyn));
        data.setWashu(getCbItemValue(cb_washu));

    }


    private String getCbItemValue(JComboBox jComboBox)
    {

        if(jComboBox.getSelectedItem()==null)
            return "";
        else return jComboBox.getSelectedItem().toString().trim();


    }

    public boolean isModified(Xiankang_Dengju data) {
        if (tf_dengdikuan.getText() != null ? !tf_dengdikuan.getText().equals(data.getDengdikuan()) : data.getDengdikuan() != null)
            return true;
        if (tf_dengdichang.getText() != null ? !tf_dengdichang.getText().equals(data.getDengdichang()) : data.getDengdichang() != null)
            return true;
        if (tf_feiniuhao.getText() != null ? !tf_feiniuhao.getText().equals(data.getFeiniuhao()) : data.getFeiniuhao() != null)
            return true;
        if (tf_feiniukuan.getText() != null ? !tf_feiniukuan.getText().equals(data.getFeiniukuan()) : data.getFeiniukuan() != null)
            return true;
        if (tf_feiniuchang.getText() != null ? !tf_feiniuchang.getText().equals(data.getFeiniuchang()) : data.getFeiniuchang() != null)
            return true;
        if (tf_feiniugao.getText() != null ? !tf_feiniugao.getText().equals(data.getFeiniugao()) : data.getFeiniugao() != null)
            return true;
        if (tf_memo.getText() != null ? !tf_memo.getText().equals(data.getMemo()) : data.getMemo() != null) return true;
        if (tf_dengzhaobuliao.getText() != null ? !tf_dengzhaobuliao.getText().equals(data.getDengzhaobuliao()) : data.getDengzhaobuliao() != null)
            return true;
        if (tf_dengzhaochicun.getText() != null ? !tf_dengzhaochicun.getText().equals(data.getDengzhaochicun()) : data.getDengzhaochicun() != null)
            return true;
        if (tf_dengzhaobaozhuang.getText() != null ? !tf_dengzhaobaozhuang.getText().equals(data.getDengzhaobaozhuang()) : data.getDengzhaobaozhuang() != null)
            return true;
        if (tf_hafujia.getText() != null ? !tf_hafujia.getText().equals(data.getHafujia()) : data.getHafujia() != null)
            return true;
        if (tf_dengzhaochangshang.getText() != null ? !tf_dengzhaochangshang.getText().equals(data.getDengzhaochangshang()) : data.getDengzhaochangshang() != null)
            return true;
        if (tf_caizhi.getText() != null ? !tf_caizhi.getText().equals(data.getCaizhi()) : data.getCaizhi() != null)
            return true;
        return false;
    }
}
