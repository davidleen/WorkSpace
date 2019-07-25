package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.mvp.productprocess.ProductProcessUpdatePresenter;
import com.giants.hd.desktop.mvp.productprocess.ProductProcessUpdateViewer;
import com.giants3.hd.entity.ProductProcess;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 材料分类修改添加 删除
 * Created by davidleen29 on 2017/4/2.
 */
public class Panel_ProductProcessUpdate extends BasePanel<ProductProcessUpdatePresenter> implements ProductProcessUpdateViewer {

    private JPanel root;
    private JTextField tf_code;
    private JTextField tf_name;
    private JButton btn_save;
    private JButton btn_delete;
    private JTextArea tf_memo;
    DocumentListener codeListener = new DocumentAdapter() {


        @Override
        public void onTextChange(DocumentEvent documentEvent) {
            getPresenter().updateCode(tf_code.getText().trim());
        }
    };


    DocumentListener nameListener = new DocumentAdapter() {


        @Override
        public void onTextChange(DocumentEvent documentEvent) {
            getPresenter().updateName(tf_name.getText().trim());

        }
    };
    DocumentListener memoListener = new DocumentAdapter() {


        @Override
        public void onTextChange(DocumentEvent documentEvent) {
            getPresenter().updateMemo(tf_memo.getText().trim());
        }
    };

    public Panel_ProductProcessUpdate(final ProductProcessUpdatePresenter presenter) {
        super(presenter);


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


        tf_code.getDocument().addDocumentListener(codeListener);
        tf_name.getDocument().addDocumentListener(nameListener);
        tf_memo.getDocument().addDocumentListener(memoListener);
//        tf_code.getDocument().addDocumentListener(codeListener);
//
//        tf_name.getDocument().addDocumentListener(nameListener);
//
//        tf_memo.getDocument().addDocumentListener(memoListener);

    }

    @Override
    public void bindData(ProductProcess productProcess) {

//        tf_code.getDocument().removeDocumentListener(codeListener);
//        tf_name.getDocument().removeDocumentListener(nameListener);
//        tf_memo.getDocument().removeDocumentListener(memoListener);


        tf_code.setText(productProcess == null ? "" : productProcess.code);
        tf_name.setText(productProcess == null ? "" : productProcess.name);
        tf_memo.setText(productProcess == null ? "" : productProcess.memo);

        btn_delete.setVisible(productProcess != null && productProcess.id > 0);

//        // if (materialClass == null) return;
//
//        ftf_available.setValue(new Float(materialClass.available));
//        ftf_discount.setValue(new Float(materialClass.discount));
//        ftf_maokuang.setValue(new Float(materialClass.wWidth));
//        ftf_maochang.setValue(new Float(materialClass.wLong));
//        ftf_maogao.setValue(new Float(materialClass.wHeight));
//        ftf_type.setValue(new Integer(materialClass.type));
//        tf_code.setText(materialClass.code);
//        tf_name.setText(materialClass.name);
//
//
//        tf_memo.setText(materialClass.memo);


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
