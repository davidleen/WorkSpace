package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.interf.CommonSearch;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.viewImpl.Panel_Page;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.noEntity.RemoteData;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchDialog<T> extends BaseDialog<T> {
    private JPanel contentPane;
    private JTextField jtf;
    private JButton btn_search;
    private JHdTable table;
    private Panel_Page panel_page;
    private JPanel panel_param;


    private BaseTableModel<T> tableModel;
    private CommonSearch<T> commonSearch;

    private java.util.List<JComponent> paramCompnents;

    private ResultChecker<T> checker;



    public SearchDialog(Window window, final BaseTableModel<T> tableModel, CommonSearch<T> commonSearch, String value, RemoteData<T> remoteData, boolean searchTextFixed, final ResultChecker<T> checker, java.util.List<ParamConfig> paramConfigs) {
        super(window);
        setContentPane(contentPane);
        setModal(true);
        this.checker = checker;


        this.tableModel = tableModel;
        this.commonSearch = commonSearch;

        jtf.setText(value);


        jtf.setEnabled(!searchTextFixed);


        panel_page.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                search(jtf.getText().trim(), pageIndex, pageSize);


            }
        });


        table.setModel(tableModel);

        if(paramConfigs!=null)
        {

            for(ParamConfig config:paramConfigs)
            {

                switch (config.fieldType)
                {
                    case B:

                        JCheckBox jCheckBox=new JCheckBox(config.titleName);
                        jCheckBox.setName(config.fieldName);
                        panel_param.add(jCheckBox);
                        addToParamComponents(jCheckBox);


                        break;

                    case L:
                        break;
                    case F:
                        break;
                    case S:
                        break;
                    case O:
                        break;
                    case iMG:
                        break;
                }
            }




        }



        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {

                    int modelRowId = table.convertRowIndexToModel(table.getSelectedRow());
                    T material = tableModel.getItem(modelRowId);

                    if (checker != null) {

                        if (checker.isValid(material)) {
                            setResult(material);
                            setVisible(false);
                            dispose();
                        } else {

                            checker.warn(material);
                        }
                    } else {


                        setResult(material);
                        setVisible(false);
                        dispose();
                    }


                }
            }
        });


        if (checker != null) {
            TableCellRenderer renderer = table.getDefaultRenderer(Object.class);
            table.setDefaultRenderer(Object.class, new ValidTableCellRender(checker, tableModel, renderer));
        }


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String value = jtf.getText();
                search(value);
            }
        });

        if (remoteData != null) {
            bindRemoteData(remoteData);
        } else {
            search(value);
        }


    }

    public void show(JComponent jComponent)
    {
        if(jComponent!=null)
        setLocationRelativeTo(jComponent);
        setVisible(true);
    }
    private void addToParamComponents(JComponent jComponent) {


        if(paramCompnents==null)
        {
            paramCompnents=new ArrayList<>();

        }
        paramCompnents.add(jComponent);
    }


    /**
     * 绑定远程查询到的数据
     *
     * @param materialRemoteData
     */
    private void bindRemoteData(RemoteData<T> materialRemoteData) {
        panel_page.bindRemoteData(materialRemoteData);
        tableModel.setDatas(materialRemoteData.datas);
    }

    /**
     * 开启搜索功能
     *
     * @param value
     */

    public void search(final String value) {
        search(value, 0, panel_page.getPageSize());
    }

    public void search(final String value, final int pageIndex, final int pageSize) {


        new HdSwingWorker<T, Object>((Window) getParent()) {
            @Override
            protected RemoteData<T> doInBackground() throws Exception {


                if(paramCompnents!=null&&paramCompnents.size()>0)
                {


                    java.util.Map<String,Object> map=new HashMap<String, Object>();
                    for (JComponent component:paramCompnents)
                    {

                        if(component instanceof JCheckBox)
                        {
                            map.put(component.getName(),((JCheckBox)component).isSelected());
                        }
                    }


                    commonSearch.setAdditionalParam(map);
                }

                return commonSearch.search(value, pageIndex, pageSize);

            }

            @Override
            public void onResult(RemoteData<T> data) {


                bindRemoteData(data);

            }
        }.go();


    }


    public static class Builder<T> {
        private Window window;
        private BaseTableModel<T> tableModel;
        private CommonSearch<T> commonSearch;
        private String value;
        private RemoteData<T> remoteData;
        private boolean searchTextFixed = false;
        private java.util.List<ParamConfig> paramConfigs;

        private ResultChecker<T> checker;

        public Builder setWindow(Window window) {
            this.window = window;
            return this;
        }

        public Builder setTableModel(BaseTableModel<T> tableModel) {
            this.tableModel = tableModel;
            return this;
        }

        public Builder setCommonSearch(CommonSearch<T> commonSearch) {
            this.commonSearch = commonSearch;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setSearchTextFixed(boolean value) {
            this.searchTextFixed = value;
            return this;
        }

        public Builder setRemoteData(RemoteData<T> remoteData) {
            this.remoteData = remoteData;
            return this;
        }

        public Builder addParam(String field, String title, TableField.FieldType fieldType)
        {

            if(paramConfigs==null)
            {
                paramConfigs=new ArrayList<>();
            }

            ParamConfig paramConfig=new ParamConfig();
            paramConfig.fieldName=field;
            paramConfig.titleName=title;
            paramConfig.fieldType=fieldType;
            paramConfigs.add(paramConfig);
            return this;
        }

        public SearchDialog createSearchDialog() {
            SearchDialog dialog = new SearchDialog(window, tableModel, commonSearch, value, remoteData, searchTextFixed, checker,  paramConfigs);
            dialog.setMinimumSize(new Dimension(800, 600));
            dialog.pack();
            return dialog;
        }

        public Builder setResultChecker(ResultChecker<T> checker) {
            this.checker = checker;
            return this;
        }

    }


    public interface ResultChecker<T> {


        public boolean isValid(T result);

        void warn(T material);
    }


    /**
     * 自定义表格生成
     *
     * @param <T>
     */
    static class ValidTableCellRender<T> extends DefaultTableCellRenderer {

        public ResultChecker<T> checker;
        public BaseTableModel<T> model;
        private TableCellRenderer renderer;


        public ValidTableCellRender(ResultChecker<T> checker, BaseTableModel<T> model, TableCellRenderer renderer) {
            this.checker = checker;
            this.model = model;


            this.renderer = renderer;
        }

        public Component getTableCellRendererComponent(
                JTable table, Object data,
                boolean isSelected, boolean hasFocus,
                int row, int column) {


            Component component = renderer.getTableCellRendererComponent(table, data, isSelected, hasFocus, row, column);


            if (checker != null) {
                int modelRow = table.convertRowIndexToModel(row
                );
                T modelItem = model.getItem(modelRow);


                if (checker.isValid(modelItem)) {

                    component.setForeground(Color.DARK_GRAY);
                } else {
                    component.setForeground(Color.LIGHT_GRAY);
                }

            }


            return component;
        }
    }


    public static class   ParamConfig
    {
        public  String fieldName;
        public String titleName;
        public TableField.FieldType fieldType;

        public Object defaultValue;
    }
}
