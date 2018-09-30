package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.frames.MaterialDetailFrame;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.MaterialTableModel;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Material;
import com.giants3.hd.entity.MaterialClass;
import com.google.inject.Inject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 材料列表面板
 */
public class Panel_Material extends BasePanel {
    private JPanel panel1;
    private JTable tb_material;
    private JTextField jtf_value;
    private JButton btn_search;
    private JButton btn_import;
    private Panel_Page pagePanel;
    private JButton btn_add;
    private JComboBox<MaterialClass> cb_class;


    @Override
    public JComponent getRoot() {
        return panel1;
    }


    @Inject
    ApiManager apiManager;

    @Inject
    MaterialTableModel materialTableModel;


    public Panel_Material(final String value) {


        jtf_value.setText(value);


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String value = jtf_value.getText();
                search(value);
            }
        });
        jtf_value.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String value = jtf_value.getText();
                search(value);
            }
        });

        tb_material.setModel(materialTableModel);


        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                search(jtf_value.getText().toString().trim(), pageIndex, pageSize);


            }
        });

        btn_import.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                showImportDialog();

            }
        });
       tb_material.setDefaultRenderer(Object.class,new DefaultCellRenderer());
       //  tb_material.setDefaultRenderer(ImageIcon.class,new DefaultTableCellRenderer());


        tb_material.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);


                if (e.getClickCount() == 2) {


                    Material material = materialTableModel.getItem(tb_material.convertRowIndexToModel(tb_material.getSelectedRow()));
                    if (material == null) return;

                    int column = tb_material.convertColumnIndexToModel(tb_material.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 0) {


                        ImageViewDialog.showMaterialDialog(SwingUtilities.getWindowAncestor(tb_material), material.code, material.url);

                    } else {


                        showDetailDialog(material);
                    }
                }

            }
        });

        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                showDetailDialog(new Material());
            }
        });

//        tb_material.setDefaultRenderer(ImageView.class,new DefaultTableCellRenderer(){
//
//
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//               // return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                ImageView image=new ImageView();
//
//                if(value!=null&&!StringUtils.isEmpty(value.toString()))
//               image.setImageUrl( HttpUrl.loadMaterialPicture(value.toString()));
//
//                return image;
//
//
//
//
//
//            }
//        });

        //初始化下拉框
        MaterialClass aMaterialClass = new MaterialClass();
        aMaterialClass.name = "所有分类";
        aMaterialClass.code = "";
        cb_class.addItem(aMaterialClass);
        for (MaterialClass materialClass : CacheManager.getInstance().bufferData.materialClasses) {
            cb_class.addItem(materialClass);
        }


        btn_add.setVisible(AuthorityUtil.getInstance().addMaterial());


        //执行查询
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                search(value);
            }
        });
    }


    /**
     * 显示详细界面
     *
     * @param material
     */
    private void showDetailDialog(Material material) {

        MaterialDetailFrame dialog = new MaterialDetailFrame(SwingUtilities.getWindowAncestor(getRoot()), material);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(400, 600));
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);


    }

    private void showImportDialog() {

        JDialog dialog = new JDialog(getWindow(getRoot()));
        dialog.setModal(true);
        Panel_ImportMaterial panel = new Panel_ImportMaterial();
        dialog.setContentPane(panel.getRoot());
        dialog.setMinimumSize(new Dimension(400, 300));
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        search(jtf_value.getText().trim());
    }


    /**
     * 开启搜索功能
     *
     * @param value
     */

    public void search(final String value) {
        search(value, 0, pagePanel.getPageSize());
    }

    public void search(final String value, final int pageIndex, final int pageSize) {


        final String mClassId = ((MaterialClass) cb_class.getSelectedItem()).code;


        new HdSwingWorker<Material, Object>(getWindow(getRoot())) {
            @Override
            protected RemoteData<Material> doInBackground() throws Exception {


                return apiManager.loadMaterialByCodeOrName(value, mClassId, pageIndex, pageSize);

            }

            @Override
            public void onResult(RemoteData<Material> data) {


                pagePanel.bindRemoteData(data);
                materialTableModel.setDatas(data.datas);

            }
        }.go();


    }


    /**
     * 表格绘制 停用的红底显示
     */
    public class DefaultCellRenderer extends DefaultTableCellRenderer {


        public DefaultCellRenderer() {
           // setOpaque(true);

        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            Component component = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            MaterialTableModel model = (MaterialTableModel) table.getModel();
            if(component instanceof  JLabel) {
                JLabel jLabel = (JLabel) component;
                if (value instanceof ImageIcon) {

                    jLabel.setIcon((ImageIcon) value);
                    jLabel.setText("");
                } else {

                    jLabel.setIcon(null);
                    Material material = model.getItem(row);
                    if (material != null) {

                        if (material.outOfService)

                            component.setForeground(Color.RED);

                        else if (isSelected) {
                            component.setForeground(Color.white);

                        } else {
                            component.setForeground(Color.black);
                        }


                    }

                }
            }

            return component;
        }
    }
}
