package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.model.WorkFlowArrangeTableModel;
import com.giants.hd.desktop.mvp.presenter.WorkFlowProductIPresenter;
import com.giants.hd.desktop.utils.Config;
import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.mvp.viewer.WorkFlowConfigViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.noEntity.ProduceType;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.WorkFlow;
import com.giants3.hd.entity.WorkFlowProduct;
import com.giants3.hd.entity.WorkFlowSubType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程配置。
 * Created by davidleen29 on 2017/4/2.
 */
public class Panel_WorkFlow_Config extends BasePanel implements WorkFlowConfigViewer {



    List<TableField> tableFields;

    List<WorkFlowArrangeTableModel.WorkFlowConfig> data;


    WorkFlowArrangeTableModel tableModel;
    private List<WorkFlow> workFlows;


    public Panel_WorkFlow_Config(final WorkFlowProductIPresenter presenter) {
        this.tableFields = TableStructureUtils.getWorkFlowArrange();
        tableModel = new WorkFlowArrangeTableModel(WorkFlowArrangeTableModel.WorkFlowConfig.class, tableFields);

        table.setModel(tableModel);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.save();
            }
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(selfMade);
        buttonGroup.add(purchase);


        configPurchaseField();


    }

    private JPanel root;
    private JHdTable table;
    private JPanel check_group;
    private JButton save;
    private JRadioButton purchase;
    private JRadioButton selfMade;
    private JComboBox cb_group;

    List<JCheckBox> jCheckBoxes;
    List<WorkFlowSubType> workFlowSubTypes;


    @Override
    public void setWorkFlows(List<WorkFlow> workFlows, List<WorkFlowSubType> subTypes) {
        this.workFlows = workFlows;

        data = new ArrayList<>();

        for (WorkFlow workFlow : workFlows) {

            WorkFlowArrangeTableModel.WorkFlowConfig workFlowConfig = new WorkFlowArrangeTableModel.WorkFlowConfig();
            workFlowConfig.workFlowStep = workFlow.flowStep;
            workFlowConfig.workFlowName = workFlow.name;
            if (workFlow.isSelfMade) {

                data.add(workFlowConfig);
            } else {


            }

        }
        tableModel.setDatas(data);


        this.workFlowSubTypes = subTypes;
        jCheckBoxes = new ArrayList<>();
        check_group.removeAll();
        for (WorkFlowSubType subType : subTypes) {
            final JCheckBox checkBox = new JCheckBox(subType.typeName);
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {

                    updateTableColumn();
                }
            });
            jCheckBoxes.add(checkBox);
            check_group.add(checkBox);
        }


        cb_group.setModel(new DefaultComboBoxModel(ArrayUtils.changeListToVector(workFlows)));


        int index = Math.max(5, workFlows.size() - 3);
        cb_group.setSelectedIndex(index);


    }

    private void configPurchaseField() {

        List<TableField> configField = new ArrayList<>();
        configField.addAll(tableFields);

    }

    /**
     *
     */
    private void updateTableColumn() {

        List<WorkFlowSubType> configSubType = new ArrayList<>();
        List<TableField> configField = new ArrayList<>();
        configField.addAll(tableFields);
        for (int i = 0; i < jCheckBoxes.size(); i++) {
            JCheckBox checkBox = jCheckBoxes.get(i);
            if (!checkBox.isSelected()) {
                final WorkFlowSubType workFlowSubType = workFlowSubTypes.get(i);

                for (TableField tableField :
                        tableFields) {

                    if (tableField.columnName.equals(workFlowSubType.typeName)) {
                        configField.remove(tableField);
                        break;
                    }


                }

                configSubType.add(workFlowSubType);


            }

        }


        tableModel.updateStructure(configField);


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
    public void setData(WorkFlowProduct workFlowProduct) {


        int index = -1;
        final int size = workFlows.size();
        for (int i = 0; i < size; i++) {
            WorkFlow workFlow = workFlows.get(i);
            if (workFlow.flowStep == workFlowProduct.groupStep) {
                index = i;
            }
        }
        if (index != -1)
            cb_group.setSelectedIndex(index);


        final boolean isSelfMade = workFlowProduct.produceType == ProduceType.SELF_MADE;
        selfMade.setSelected(isSelfMade);
        purchase.setSelected(!isSelfMade);





        if (StringUtils.isEmpty(workFlowProduct.configs)) {

            for (int i = 0; i < data.size(); i++) {

                final WorkFlowArrangeTableModel.WorkFlowConfig workFlowConfig = this.data.get(i);




                    workFlowConfig.mujian = true;

                    if (WorkFlow.STEP_NAME_PENGSUO.equals(workFlowConfig.workFlowName)) {
                        workFlowConfig.mujian = false;

                    }
                    workFlowConfig.tiejian = true;



            }

            tableModel.fireTableDataChanged();
            return;
        }
        String[] configs = StringUtils.split(workFlowProduct.configs);
        String[] productTypeNames = StringUtils.split(workFlowProduct.productTypeNames);


        int workFlowSubTypeSize = workFlowSubTypes.size();
        for (int i = 0; i < workFlowSubTypeSize; i++) {
            WorkFlowSubType subType = workFlowSubTypes.get(i);
            jCheckBoxes.get(i).setSelected(StringUtils.index(productTypeNames, subType.typeName) > -1);
        }


        int length = productTypeNames.length;
        String[] steps = StringUtils.split(workFlowProduct.workFlowSteps);
        int workFlowLength = steps.length;

        for (int i = 0; i < workFlowLength; i++) {

            final WorkFlowArrangeTableModel.WorkFlowConfig workFlowConfig = this.data.get(i);


                String config = configs[i];


                String[] productTypeConfig = StringUtils.split(config, StringUtils.STRING_SPLIT_COMMA);

                for (int j = 0; j < length; j++) {


                    final boolean selected = !(String.valueOf(WorkFlow.STEP_SKIP).equals(productTypeConfig[j]));

                    switch (productTypeNames[j]) {
                        case WorkFlowSubType.TYPE_NAME_TIE:
                            workFlowConfig.tiejian = selected;
                            break;
                        case WorkFlowSubType.TYPE_NAME_MU:
                            workFlowConfig.mujian = selected;
                            break;
                        case WorkFlowSubType.TYPE_NAME_PU:
                            workFlowConfig.pu = selected;
                            break;
                    }




            }
        }

        tableModel.setDatas(this.data);


    }

    /**
     * 获取用户编辑结果
     *
     * @param data
     */
    @Override
    public void getData(WorkFlowProduct data) {

        int length = this.data.size();
        String[] workFlowSteps = new String[length];
        String[] workFLowNames = new String[length];


        List<WorkFlowSubType> checkSubTypes = new ArrayList<>();

        int typeLength = workFlowSubTypes.size();
        for (int i = 0; i < typeLength; i++) {
            JCheckBox jCheckBox = jCheckBoxes.get(i);
            if (jCheckBox.isSelected())
                checkSubTypes.add(workFlowSubTypes.get(i));
        }


        typeLength = checkSubTypes.size();
        String[] productTypes = new String[typeLength];
        String[] productTypeNames = new String[typeLength];
        for (int i = 0; i < typeLength; i++) {
            productTypes[i] = String.valueOf(checkSubTypes.get(i).typeId);
            productTypeNames[i] = checkSubTypes.get(i).typeName;
        }
        data.productTypes = StringUtils.combine(productTypes);
        data.productTypeNames = StringUtils.combine(productTypeNames);


        WorkFlow workFlow = (WorkFlow) cb_group.getSelectedItem();
        data.groupStep = workFlow.flowStep;

        String[] configs = new String[length];
        StringBuilder config = new StringBuilder();
        for (int i = 0; i < length; i++) {
            WorkFlowArrangeTableModel.WorkFlowConfig workFlowConfig = this.data.get(i);
            workFlowSteps[i] = String.valueOf(workFlowConfig.workFlowStep);
            workFLowNames[i] = workFlowConfig.workFlowName;
            if (Config.DEBUG) {
                Config.log(" workFlowSteps:" + workFLowNames[i]);
            }


                int workFlowConfigValue;

                workFlowConfigValue = WorkFlow.STEP_NORMAL;

                if (typeLength > 0) {
                    config.setLength(0);
                    for (int j = 0; j < typeLength; j++) {

                        WorkFlowSubType subType = checkSubTypes.get(j);


                        switch (subType.typeName) {
                            case WorkFlowSubType.TYPE_NAME_TIE:
                                config.append(workFlowConfig.tiejian ? workFlowConfigValue : WorkFlow.STEP_SKIP);
                                break;
                            case WorkFlowSubType.TYPE_NAME_MU:
                                config.append(workFlowConfig.mujian ? workFlowConfigValue : WorkFlow.STEP_SKIP);
                                break;
                            case WorkFlowSubType.TYPE_NAME_PU:
                                config.append(workFlowConfig.pu ? workFlowConfigValue : WorkFlow.STEP_SKIP);
                                break;
                        }
                        config.append(StringUtils.STRING_SPLIT_COMMA);


                    }
                    config.setLength(config.length() - 1);
                    configs[i] = config.toString();

                }

        }


        data.workFlowSteps = StringUtils.combine(workFlowSteps);
        data.workFlowNames = StringUtils.combine(workFLowNames);
        data.configs = StringUtils.combine(configs);
        data.produceType = selfMade.isSelected() ? ProduceType.SELF_MADE : ProduceType.PURCHASE;

    }
}
