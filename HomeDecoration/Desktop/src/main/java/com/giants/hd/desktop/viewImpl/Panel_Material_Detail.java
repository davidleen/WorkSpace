package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.entity.Material;
import com.giants3.hd.entity.MaterialClass;
import com.giants3.hd.entity.MaterialType;
import com.google.inject.Inject;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.*;

/**
 * 材料详细
 */
public class Panel_Material_Detail extends BasePanel {
    private JTextField tf_code;
    private JTextField tf_name;
    private JComboBox<MaterialClass> cb_materialClass;
    private JTextField tf_unit;
    private JFormattedTextField ftf_price;
    private JTextField tf_spec;
    private JComboBox<MaterialType> cb_materialType;
    private JFormattedTextField ftf_wLong;
    private JFormattedTextField ftf_wWdith;
    private JFormattedTextField ftf_wHeight;
    private JButton btn_save;
    private JTextField tf_memo;
    private JFormattedTextField ftf_unitRatio;
    //private JComboBox<MaterialEquation> cb_equation;
    private JPanel rootPanel;
    private JFormattedTextField ftf_available;
    private JButton btn_delete;
    private JLabel lb_photo;
    private JTextField jtf_ingredientRatio;
    private JTextField jtf_discount;
    private JComboBox cb_outofservice;
    private JFormattedTextField tf_price2;
    private JTextField tf_unit2;


    private ItemListener materialClassListener;
    private Material data;


    @Inject
    public Panel_Material_Detail() {

        init();


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (listener != null) {
                    listener.save();
                }
            }
        });

        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (listener != null) {
                    listener.delete();
                }
            }
        });

        materialClassListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    MaterialClass materialClass = (MaterialClass) e.getItem();
                    ftf_wLong.setValue(materialClass.wLong);
                    ftf_wHeight.setValue(materialClass.wHeight);
                    ftf_wWdith.setValue(materialClass.wWidth);
                    ftf_available.setValue(materialClass.available);


                }
            }
        };


    }


    @Override

    public JComponent getRoot() {
        return rootPanel;
    }


    public void init() {


        // cb_materialClass.addItem();


        for (MaterialClass materialClass : CacheManager.getInstance().bufferData.materialClasses) {
            cb_materialClass.addItem(materialClass);
        }

        for (MaterialType materialType : CacheManager.getInstance().bufferData.materialTypes) {
            cb_materialType.addItem(materialType);
        }

//        for(MaterialEquation equation: BufferData.materialEquations)
//        {
//            cb_equation.addItem(equation);
//        }

        cb_outofservice.addItem("使用");
        cb_outofservice.addItem("停用");

        lb_photo.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() >= 2) {


                    if (data != null) {
                        ImageViewDialog.showMaterialDialog(getWindow(getRoot()), data.code, data.url);
                    }

                }
            }
        });


        //配置权限  是否修改  是否可以删除

        boolean modifiable = AuthorityUtil.getInstance().editMaterial() || AuthorityUtil.getInstance().addMaterial();

        btn_save.setVisible(modifiable);


        btn_delete.setVisible(AuthorityUtil.getInstance().deleteMaterial());


    }


    public void setData(Material data) {

        this.data = data;

        cb_materialClass.removeItemListener(materialClassListener);


        tf_code.setText(data.getCode());
        tf_name.setText(data.getName());
        tf_unit.setText(data.getUnitName());
        tf_spec.setText(data.getSpec());
        tf_memo.setText(data.getMemo());

        ftf_price.setValue(new Float(data.getPrice()));
        ftf_wLong.setValue(new Float(data.getwLong()));
        ftf_wWdith.setValue(new Float(data.getwWidth()));
        ftf_wHeight.setValue(new Float(data.getwHeight()));
        ftf_available.setValue(new Float(data.getAvailable()));
        ftf_unitRatio.setValue(new Float(data.getUnitRatio()));
        jtf_discount.setText(String.valueOf(data.discount));
        jtf_ingredientRatio.setText(data.ingredientRatio <= 0 ? "" : String.valueOf(data.ingredientRatio));



        tf_price2.setValue(new Float(data.price2));
        tf_unit2.setText(data.unit2);

        int selectedItem = 0;
        for (int i = 0, count = cb_materialClass.getItemCount(); i < count; i++) {

            if (cb_materialClass.getItemAt(i).code.equals(data.classId)) {
                selectedItem = i;
                break;
            }
        }
        cb_materialClass.setSelectedIndex(selectedItem);


        selectedItem = 0;
        for (int i = 0, count = cb_materialType.getItemCount(); i < count; i++) {

            if (data.typeId == cb_materialType.getItemAt(i).typeId) {
                selectedItem = i;
                break;
            }
        }
        cb_materialType.setSelectedIndex(selectedItem);


        cb_outofservice.setSelectedIndex(data.outOfService ? 1 : 0);

//        selectedItem=-1;
//        for(int i=0,count=cb_equation.getItemCount();i<count;i++)
//        {
//
//            if(data.equationId==cb_equation.getItemAt(i).equationId)
//            {
//                selectedItem=i;
//                break;
//            }
//        }
//        cb_equation.setSelectedIndex(selectedItem);


        cb_materialClass.addItemListener(materialClassListener);

        ImageLoader.getInstance().displayImage(new Iconable() {
            @Override
            public void setIcon(ImageIcon icon, String url) {
                lb_photo.setIcon(icon);
            }

            @Override
            public void onError(String message) {
                lb_photo.setText("");
            }
        }, HttpUrl.loadPicture(data.url));

    }

    public void getData(Material data) {
        data.setCode(tf_code.getText());
        data.setName(tf_name.getText());
        data.setUnitName(tf_unit.getText());
        data.setSpec(tf_spec.getText());
        data.setMemo(tf_memo.getText());

        MaterialClass materialClass = (MaterialClass) cb_materialClass.getSelectedItem();
        data.setClassId(materialClass.code);
        data.setClassName(materialClass.name);


        MaterialType materialType = (MaterialType) cb_materialType.getSelectedItem();
        data.setTypeId(materialType.typeId);
        data.setTypeName(materialType.typeName);


//        MaterialEquation equation= (MaterialEquation) cb_equation.getSelectedItem();
//        data.equationId=equation.equationId;


        data.setPrice(Float.valueOf(ftf_price.getValue().toString()));

        data.setwLong(Float.valueOf(ftf_wLong.getValue().toString()));

        data.setwWidth(Float.valueOf(ftf_wWdith.getValue().toString()));

        data.setwHeight(Float.valueOf(ftf_wHeight.getValue().toString()));

        data.setAvailable(Float.valueOf(ftf_available.getValue().toString()));

        data.setUnitRatio(Float.valueOf(ftf_unitRatio.getValue().toString()));


        data.unit2 = tf_unit2.getText().trim();
        try {
            data.price2 = Float.valueOf(tf_price2.getValue().toString());
        } catch (Throwable t) {

        }


        try {
            data.ingredientRatio = Float.valueOf(jtf_ingredientRatio.getText());
        } catch (Throwable t) {
            data.ingredientRatio = 0;
        }

        try {
            data.discount = Float.valueOf(jtf_discount.getText());
        } catch (Throwable t) {
            data.discount = 0;
        }

        data.outOfService = cb_outofservice.getSelectedIndex() == 0 ? false : true;

    }




}
