package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.ZhilingdanModel;
import com.giants.hd.desktop.mvp.presenter.ZhilingdanIPresenter;
import com.giants.hd.desktop.mvp.viewer.ZhilingdanViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.ZhilingdanCellRenderer;
import com.giants3.hd.entity_erp.Zhilingdan;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by davidleen29 on 2017/3/10.
 */
public class Panel_Zhilingdan  extends  BasePanel implements ZhilingdanViewer{
    private JPanel root;
    private JTextField jtf_key;
    private JButton btn_search;
    private JDatePickerImpl date_start;
    private JDatePickerImpl date_end;
    private JHdTable jt;
    private JCheckBox showall;
    private JCheckBox caigouDD;
    private JCheckBox jinhuodd;
    private ZhilingdanIPresenter presenter;

    ZhilingdanModel zhilingdanModel;
      ZhilingdanCellRenderer renderer=null;
    public Panel_Zhilingdan(final ZhilingdanIPresenter presenter) {

        this.presenter = presenter;

        zhilingdanModel=new ZhilingdanModel();
        jt.setModel(zhilingdanModel);
        renderer= new ZhilingdanCellRenderer();
        jt.setDefaultRenderer(Object.class, renderer);
        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String  key=jtf_key.getText().trim();
                String startDateString=date_start.getJFormattedTextField().getText().trim();
                String endDateString=date_end.getJFormattedTextField().getText().trim();
                presenter.search(key,startDateString,endDateString);
            }
        });

      showall.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              presenter.showAll(showall.isSelected(),caigouDD.isSelected(),jinhuodd.isSelected());
          }
      });


       caigouDD.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {


               presenter.showAll(showall.isSelected(),caigouDD.isSelected(),jinhuodd.isSelected());

           }
       });

        jinhuodd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.showAll(showall.isSelected(),caigouDD.isSelected(),jinhuodd.isSelected());

            }
        });
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

    @Override
    public void setData(List<Zhilingdan> datas) {


        zhilingdanModel.setDatas(datas);


    }
    private void createUIComponents() {
        JDatePanelImpl picker = new JDatePanelImpl(null);
        final HdDateComponentFormatter formatter = new HdDateComponentFormatter();
        date_start = new JDatePickerImpl(picker, formatter);

        try {
            long aMonthAgo= Calendar.getInstance().getTimeInMillis()-30l*24*60*60*1000;
            final Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(aMonthAgo );
            date_start.getJFormattedTextField().setText(formatter.valueToString(instance));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        picker = new JDatePanelImpl(null);
        date_end = new JDatePickerImpl(picker, formatter);
        try {
            date_end.getJFormattedTextField().setText(formatter.valueToString(Calendar.getInstance()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
