package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.model.BaseListTableModel;
import com.giants.hd.desktop.model.ProducerValueConfigTableModel;
import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.mvp.presenter.ProducerValueConfigPresenter;
import com.giants.hd.desktop.mvp.presenter.WorkFlowMessageReportPresenter;
import com.giants.hd.desktop.mvp.viewer.ProducerValueConfigViewer;
import com.giants.hd.desktop.mvp.viewer.WorkFlowMessageReportViewer;
import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.entity.ProducerValueConfig;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.noEntity.ProducerValueItem;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.StringUtils;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2018/7/7.
 */
public class Panel_ProducerValueConfig extends BasePanel<ProducerValueConfigPresenter> implements ProducerValueConfigViewer {

    private JPanel root;
    private JHdTable jt;
    private JButton save;
    private JButton edit;
    private JTextField tf_name;
    private JButton search;
    ProducerValueConfigTableModel model;
    private List<ProducerValueConfig> datas;

    public Panel_ProducerValueConfig(final ProducerValueConfigPresenter presenter) {
        super(presenter);
        model=new ProducerValueConfigTableModel();
        jt.setModel(model);

        save.setVisible(false);


        jt.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = jt.getSelectedRow();
                    ProducerValueConfig mesage = model.getItem(row);


                    int column = jt.convertColumnIndexToModel(jt.getSelectedColumn());
//                    //单击第一列 显示原图
//                    if (column == 0) {
//                        ImageViewDialog.showProductDialog(getWindow(getRoot()), mesage.productName, mesage.pVersion,mesage.url);
//                    }


                }

            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                getPresenter().save(datas);

            }
        });

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                model.setEdit(true);
                save.setVisible(true);
                edit.setVisible(false);

            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(datas==null) return;

                String key=tf_name.getText().trim();

                filterAndSetModel(key,datas);

            }
        });

    }

    @Override
    public JComponent getRoot() {
        return root;
    }

    @Override
    public void bindData(List<ProducerValueConfig> datas) {
        this.datas = datas;
        filterAndSetModel(tf_name.getText().trim(),datas);

    }

    private void filterAndSetModel(String key,List<ProducerValueConfig> datas)
    {
        List<ProducerValueConfig> configs=new ArrayList<>();
        if(StringUtils.isEmpty(key)
        )
        {
            configs.addAll(datas);
        }else {
            for (ProducerValueConfig config : datas) {
                if (config.dept.toLowerCase().contains(key.toLowerCase())||config.name.toLowerCase().contains(key.toLowerCase()))
                {
                    configs.add(config);
                }
            }

        }

        model.setDatas(configs);
    }


    private void createUIComponents() {


    }

    @Override
    public boolean hasModify(String originData) {

       return  !GsonUtils.toJson(model.getDatas()).equals(originData);

    }

    @Override
    public void completeEdit() {
        model.setEdit(false);
        save.setVisible(false);
        edit.setVisible(true);
    }
}
