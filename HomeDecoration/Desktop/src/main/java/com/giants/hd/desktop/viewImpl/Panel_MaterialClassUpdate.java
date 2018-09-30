package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.mvp.presenter.MaterialClassUpdateIPresenter;
import com.giants.hd.desktop.mvp.viewer.MaterialClassUpdateViewer;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.MaterialClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 材料分类修改添加 删除
 * Created by davidleen29 on 2017/4/2.
 */
public class Panel_MaterialClassUpdate extends BasePanel implements MaterialClassUpdateViewer {

    private final MaterialClassUpdateIPresenter presenter;
    private JPanel root;
    private JTextField tf_code;
    private JTextField tf_name;
    private JFormattedTextField ftf_type;
    private JButton btn_save;
    private JTextField tf_memo;
    private JFormattedTextField ftf_maokuang;
    private JFormattedTextField ftf_available;
    private JButton btn_delete;
    private JFormattedTextField ftf_maogao;
    private JFormattedTextField ftf_maochang;
    private JFormattedTextField ftf_discount;


    public Panel_MaterialClassUpdate(final MaterialClassUpdateIPresenter presenter) {
        super();

        this.presenter = presenter;


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.save();

            }
        });

        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.delete();

            }
        });

    }

    @Override
    public void bindData(MaterialClass materialClass) {


        btn_delete.setVisible(materialClass != null&&materialClass.id>0);
       // if (materialClass == null) return;

        ftf_available.setValue(new Float(materialClass.available));
        ftf_discount.setValue(new Float(materialClass.discount));
        ftf_maokuang.setValue(new Float(materialClass.wWidth));
        ftf_maochang.setValue(new Float(materialClass.wLong));
        ftf_maogao.setValue(new Float(materialClass.wHeight));
        ftf_type.setValue(new Integer(materialClass.type));
        tf_code.setText(materialClass.code);
        tf_name.setText(materialClass.name);



        tf_memo.setText(materialClass.memo);


    }

    @Override
    public void getData(MaterialClass materialClass) {


        materialClass.code=tf_code.getText().trim();
        materialClass.name=tf_name.getText().trim();
        final String memoValue = tf_memo.getText().trim();
        materialClass.memo= StringUtils.isEmpty(memoValue)?null:memoValue;



        materialClass.wHeight=Float.valueOf(ftf_maogao.getValue().toString());
        materialClass.wLong=Float.valueOf(ftf_maochang.getValue().toString());
        materialClass.wWidth=Float.valueOf(ftf_maokuang.getValue().toString());
        materialClass.available=Float.valueOf(ftf_available.getValue().toString());
        materialClass.discount=Float.valueOf(ftf_discount.getValue().toString());
        materialClass.type=Integer.valueOf(ftf_type.getValue().toString());

    }

    /**
     * 获取实际控件
     *
     * @return
     */
    @Override
    public JComponent getRoot() {
        return root;
    }
}
