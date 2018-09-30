package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants3.hd.noEntity.RemoteData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 自定义分页控件面版
 */
public class Panel_Page<T>   {
    private JPanel panel1;
    private JLabel message;
    private JButton first;
    private JButton previous;
    private JButton next;
    private JButton last;
    private JTextField jtf_pageIndex;
    private JButton turnTo;
    private JComboBox pageRows;
    private JPanel contentPane;


    public Panel_Page()
    {

        super();
        message.setText("");
        jtf_pageIndex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if(listener!=null&&remoteData!=null)
                {

                    int newPageIndex=Integer.valueOf(jtf_pageIndex.getText().toString())-1;


                    listener.onPageChanged(newPageIndex,Integer.valueOf(pageRows.getSelectedItem().toString()));


                }

            }
        });



        turnTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(listener!=null&&remoteData!=null)
                {

                    int newPageIndex=Integer.valueOf(jtf_pageIndex.getText().toString())-1;


                    listener.onPageChanged(newPageIndex,Integer.valueOf(pageRows.getSelectedItem().toString()));


                }
            }
        });

        first.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listener!=null&&remoteData!=null)
                {

                    if(listener!=null&&remoteData!=null)
                    {




                        listener.onPageChanged(0,Integer.valueOf(pageRows.getSelectedItem().toString()));


                    }

                }

            }
        });


        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listener!=null&&remoteData!=null)
                {




                    listener.onPageChanged(remoteData.pageIndex+1 ,Integer.valueOf(pageRows.getSelectedItem().toString()));


                }

            }
        });

        last.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listener!=null&&remoteData!=null)
                {




                    listener.onPageChanged(Math.max(remoteData.pageCount-1,0) ,Integer.valueOf(pageRows.getSelectedItem().toString()));

                }

            }
        });

        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(listener!=null&&remoteData!=null)
                {




                    listener.onPageChanged(Math.max(remoteData.pageIndex-1,0) ,Integer.valueOf(pageRows.getSelectedItem().toString()));

                }

            }
        });


    }







    private PageListener listener;

    private RemoteData<T> remoteData;

    public void setListener(PageListener listener) {
        this.listener = listener;
    }


    public void bindRemoteData(RemoteData<T> remoteData)
    {


        this.remoteData=remoteData;

        update();


    }


    private void update()
    {

        message.setText("共"+remoteData.totalCount+"条");
        first.setEnabled(remoteData.pageIndex>0);
        previous.setEnabled(remoteData.pageIndex>0);

        previous.setEnabled(remoteData.pageIndex>0);


        next.setEnabled(remoteData.pageIndex<remoteData.pageCount-1);
        last.setEnabled(remoteData.pageIndex<remoteData.pageCount-1);
        jtf_pageIndex.setText(String.valueOf(remoteData.pageIndex+1));




        int itemCount = pageRows.getItemCount();
        int selectIndex=-1;
        for (int i = 0; i < itemCount; i++) {
            selectIndex=i;
            if(Float.valueOf(pageRows.getItemAt(i).toString())>=remoteData.pageSize)
            {
                break;
            }
        }
        pageRows.setSelectedIndex(selectIndex);

        turnTo.setEnabled(remoteData!=null);

    }


    public int getPageSize() {
        return Integer.valueOf(pageRows.getSelectedItem().toString());
    }
}
