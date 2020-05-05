package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.AbsTableModel;
import com.giants.hd.desktop.model.ItemPickTableModel;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2017/7/29.
 */
public class ItemPickDialog<S> extends BaseDialog<List<S>> {
    private JPanel root;

    private JHdTable jt;
    private JButton confirm;
    private JButton select;
    private JButton clear;
    private final List<S> items;


    public ItemPickDialog(Window window, String title, final List<S> items, AbsTableModel<S> tableModel){

        this(window,title,items,null,tableModel);

    }

    public ItemPickDialog(Window window, String title, final List<S> items,final List<S> preSelectItems, AbsTableModel<S> tableModel) {
        super(window, title);
        setContentPane(root);
        this.items = items;
        tableModel.setDatas(items);
        final ItemPickTableModel<S> dataModel = new ItemPickTableModel<>(tableModel);

        if (preSelectItems!=null) {


            for(S item:preSelectItems)
            {
                int index=items.indexOf(item);
                if(index>-1)
                {
                    dataModel.updateSelected(index);
                }
            }



        }

        jt.setModel(dataModel);

        jt.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {


              int[] rows=  JTableUtils.getSelectedRowSOnModel(jt);
                if(rows!=null&&rows.length>0)
                dataModel.updateSelected(rows[0]);


            }
        });

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                boolean[] selectState=dataModel.getSelectState();


                List<S> selectItems=new ArrayList<S>();

                int length=items.size();
                for (int i = 0; i < length; i++) {

                    if(selectState[i])
                         selectItems.add(items.get(i));
                }
                setResult(selectItems);

                close();



            }
        });

        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dataModel.selectAll();

            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dataModel.clearAll();

            }
        });

    }


}
