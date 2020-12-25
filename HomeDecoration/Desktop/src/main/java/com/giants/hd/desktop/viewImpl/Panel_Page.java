package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants3.hd.noEntity.Pageable;
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


                if(listener!=null&& pageable !=null)
                {

                    int newPageIndex=Integer.valueOf(jtf_pageIndex.getText().toString())-1;


                    listener.onPageChanged(newPageIndex,Integer.valueOf(pageRows.getSelectedItem().toString()));


                }

            }
        });



        turnTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(listener!=null&& pageable !=null)
                {

                    int newPageIndex=Integer.valueOf(jtf_pageIndex.getText().toString())-1;


                    listener.onPageChanged(newPageIndex,Integer.valueOf(pageRows.getSelectedItem().toString()));


                }
            }
        });

        first.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listener!=null&& pageable !=null)
                {

                    if(listener!=null&& pageable !=null)
                    {




                        listener.onPageChanged(0,Integer.valueOf(pageRows.getSelectedItem().toString()));


                    }

                }

            }
        });


        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listener!=null&& pageable !=null)
                {




                    listener.onPageChanged(pageable.getPageIndex()+1 ,Integer.valueOf(pageRows.getSelectedItem().toString()));


                }

            }
        });

        last.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listener!=null&& pageable !=null)
                {




                    listener.onPageChanged(Math.max(pageable.getPageNum()-1,0) ,Integer.valueOf(pageRows.getSelectedItem().toString()));

                }

            }
        });

        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(listener!=null&& pageable !=null)
                {




                    listener.onPageChanged(Math.max(pageable.getPageIndex()-1,0) ,Integer.valueOf(pageRows.getSelectedItem().toString()));

                }

            }
        });


    }



    public void setVisible(boolean visible)
    {
        contentPane.setVisible(visible);
    }



    private PageListener listener;

    private Pageable pageable;

    public void setListener(PageListener listener) {
        this.listener = listener;
    }


    public void bindRemoteData(Pageable pageable)
    {


        this.pageable =pageable;

        update();


    }


    private void update()
    {

        message.setText("共"+ pageable.getTotalCount()+"条");
        first.setEnabled(pageable.getPageIndex()>0);
        previous.setEnabled(pageable.getPageIndex()>0);

        previous.setEnabled(pageable.getPageIndex()>0);


        next.setEnabled(pageable.getPageIndex()< pageable.getPageNum()-1);
        last.setEnabled(pageable.getPageIndex()< pageable.getPageNum()-1);
        jtf_pageIndex.setText(String.valueOf(pageable.getPageIndex()+1));




        int itemCount = pageRows.getItemCount();
        int selectIndex=-1;
        for (int i = 0; i < itemCount; i++) {
            selectIndex=i;
            if(Float.valueOf(pageRows.getItemAt(i).toString())>= pageable.getPageSize())
            {
                break;
            }
        }
        pageRows.setSelectedIndex(selectIndex);

        turnTo.setEnabled(pageable !=null);

    }


    public int getPageSize() {
        return Integer.valueOf(pageRows.getSelectedItem().toString());
    }
}
