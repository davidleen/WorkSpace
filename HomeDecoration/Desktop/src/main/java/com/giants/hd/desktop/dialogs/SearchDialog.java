package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.interf.ComonSearch;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.BaseTableModel;
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

public class SearchDialog<T> extends BaseDialog<T> {
    private JPanel contentPane;
    private JTextField jtf;
    private JButton btn_search;
    private JHdTable table;
    private Panel_Page panel_page;


    private BaseTableModel<T> tableModel;
    private ComonSearch<T> comonSearch;

    private ResultChecker<T> checker;

    public SearchDialog(Window window, final BaseTableModel<T> tableModel,ComonSearch<T> comonSearch,String value,RemoteData<T> remoteData,boolean searchTextFixed) {
        this(window,tableModel,comonSearch,value,remoteData,searchTextFixed,null);
    }
    public SearchDialog(Window window, final BaseTableModel<T> tableModel, ComonSearch<T> comonSearch, String value, RemoteData<T> remoteData, boolean searchTextFixed, final ResultChecker<T> checker) {
        super(window);
        setContentPane(contentPane);
        setModal(true);
        this.checker=checker;


        this.tableModel=tableModel;
        this.comonSearch=comonSearch;

        jtf.setText(value);


        jtf.setEnabled(!searchTextFixed);


        panel_page.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                search(jtf.getText().trim(), pageIndex, pageSize);


            }
        });



        table.setModel(tableModel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {

                    int modelRowId = table.convertRowIndexToModel(table.getSelectedRow());
                    T material = tableModel.getItem(modelRowId);

                    if(checker!=null)
                    {

                        if(checker.isValid(material))
                        {
                            setResult(material);
                            setVisible(false);
                            dispose();
                        }else
                        {

                            checker.warn(material);
                        }
                    }else{


                        setResult(material);
                        setVisible(false);
                        dispose();
                    }



                }
            }
        });


   if(checker!=null) {
          TableCellRenderer renderer = table.getDefaultRenderer(Object.class);
          table.setDefaultRenderer(Object.class, new ValidTableCellRender(checker, tableModel,renderer));
     }


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String value = jtf.getText();
                search(value);
            }
        });

        if(remoteData!=null)
        {
            bindRemoteData(remoteData);
        }else
        {
            search(value);
        }



    }


    /**
     * 绑定远程查询到的数据
     * @param materialRemoteData
     */
    private  void bindRemoteData(RemoteData<T> materialRemoteData)
    {
        panel_page.bindRemoteData(materialRemoteData);
        tableModel.setDatas(materialRemoteData.datas);
    }
    /**
     * 开启搜索功能
     *
     * @param value
     */

    public void search(final String value )
    {
        search(value, 0, panel_page.getPageSize());
    }
    public void search(final String value,final int pageIndex, final int pageSize)
    {



        new HdSwingWorker<T,Object>((Window)getParent())
        {
            @Override
            protected RemoteData<T> doInBackground() throws Exception {


                return   comonSearch.search(value, pageIndex, pageSize);

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
        private ComonSearch<T> comonSearch;
        private String value;
        private RemoteData<T> remoteData;
        private boolean searchTextFixed=false;

        private ResultChecker<T> checker;

        public Builder setWindow(Window window) {
            this.window = window;
            return this;
        }

        public Builder setTableModel(BaseTableModel<T> tableModel) {
            this.tableModel = tableModel;
            return this;
        }

        public Builder setComonSearch(ComonSearch<T> comonSearch) {
            this.comonSearch = comonSearch;
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

        public SearchDialog createSearchDialog() {
            SearchDialog dialog = new SearchDialog(window, tableModel, comonSearch, value, remoteData,searchTextFixed,checker);
            return dialog;
        }

        public Builder setResultChecker(ResultChecker<T> checker) {
            this.checker = checker;
            return this;
        }

    }




    public interface  ResultChecker<T>
    {


        public  boolean isValid(T result);

        void warn(T material);
    }


    /**
     * 自定义表格生成
     * @param <T>
     */
    static class ValidTableCellRender<T> extends DefaultTableCellRenderer {

        public ResultChecker<T> checker;
        public BaseTableModel<T> model;
        private TableCellRenderer renderer;


        public ValidTableCellRender(ResultChecker<T> checker, BaseTableModel<T> model, TableCellRenderer renderer)
        {
            this.checker=checker;
            this.model=model;


            this.renderer = renderer;
        }

        public Component getTableCellRendererComponent(
                JTable table, Object data,
                boolean isSelected, boolean hasFocus,
                int row, int column) {



         Component component=   renderer.getTableCellRendererComponent(table,data,isSelected,hasFocus,row ,column);




                if (checker != null) {
                    int modelRow = table.convertRowIndexToModel(row
                    );
                    T modelItem = model.getItem(modelRow);




                    if (checker.isValid(modelItem)  )
                    {

                        component.setForeground(Color.DARK_GRAY);
                    }else
                    {
                        component.setForeground(Color.LIGHT_GRAY);
                    }

                }



            return component;
        }
    }
}
