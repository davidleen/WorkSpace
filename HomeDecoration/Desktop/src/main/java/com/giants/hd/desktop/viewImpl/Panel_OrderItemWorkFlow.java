package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants.hd.desktop.model.WorkFlowArrangeTableModel;
import com.giants.hd.desktop.mvp.presenter.OrderItemWorkFlowIPresenter;
import com.giants.hd.desktop.utils.Config;
import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.mvp.viewer.OrderItemWorkFlowViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.noEntity.ProduceType;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 订单排厂界面    自制 与 外购
 * <p/>
 * Created by davidleen29 on 2017/1/11.
 */
public class Panel_OrderItemWorkFlow extends BasePanel implements OrderItemWorkFlowViewer {
    private JPanel root;
    private JPanel panel_types;
    private JButton save;
    private JLabel photo;
    private JLabel order;
    private JLabel product;


    private JComboBox jbFactory;
    private JPanel panel_purchase;

    private JLabel produceType;
    private JHdTable jt;
    private JButton btn_reimport;
    private JButton cancelArrange;
    private JLabel state;


    private List<JComboBox<OutFactory>> factories;
    private OrderItemWorkFlowIPresenter presenter;


    private WorkFlow[] workFlows;


    WorkFlowArrangeTableModel workFlowArrangeTableModel;
    List<WorkFlowArrangeTableModel.WorkFlowConfig> data;

    /**
     * 自制生产流程列表
     */
    List<WorkFlow> selfMadeWorkFlowList;

    /**
     * 外购产品的流程列表
     */
    List<WorkFlow> purchaseWorkFlowList;


    private List<OutFactory> outFactories;


    public Panel_OrderItemWorkFlow(final OrderItemWorkFlowIPresenter presenter) {

        this.presenter = presenter;


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.save();


            }
        });

        cancelArrange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.cancelArrange();
            }
        });

        btn_reimport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.reimportProduct();
            }
        });

        workFlowArrangeTableModel = new WorkFlowArrangeTableModel(WorkFlowArrangeTableModel.WorkFlowConfig.class, TableStructureUtils.getWorkFlowArrange());
        jt.setModel(workFlowArrangeTableModel);

        int size = 0;

        workFlows = new WorkFlow[size];
        for (int i = 0; i < size; i++) {

            workFlows[i] = null;

        }


        selfMadeWorkFlowList = new ArrayList<>();
        purchaseWorkFlowList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            final WorkFlow workFlow =null;
            if (workFlow.isSelfMade)
                selfMadeWorkFlowList.add(workFlow);
            if (workFlow.isPurchased)
                purchaseWorkFlowList.add(workFlow);
        }


        data = new ArrayList<>();
        init();


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
    public List<OutFactory> getArrangedFactories() throws Exception {


        List<OutFactory> result = new ArrayList<>();
        for (JComboBox<OutFactory> jComboBox : factories) {


            OutFactory factory = (OutFactory) jComboBox.getSelectedItem();
            if (factory == null) {

                throw new Exception("还有厂家未选择");
            }


            result.add((OutFactory) jComboBox.getSelectedItem());
        }


        return result;

    }


    @Override
    public void setOutFactories(List<OutFactory> outFactories) {
        this.outFactories = outFactories;
        Vector<OutFactory> outFactoryVector = ArrayUtils.changeListToVector(outFactories);
        ComboBoxModel comboBoxModel = new DefaultComboBoxModel(outFactoryVector);
        jbFactory.setModel(comboBoxModel);
    }


    @Override
    public OutFactory getProduceFactory() {


        return (OutFactory) jbFactory.getSelectedItem();
    }

    @Override
    public List<WorkFlow> getSelectedWorkFlows() {


        return purchaseWorkFlowList;


    }


    @Override
    public void bindOrderItemWorkFlow(OrderItemWorkFlow orderItemWorkFlow) {



        final boolean isSelfMade = orderItemWorkFlow.produceType == ProduceType.SELF_MADE;
        produceType.setText(isSelfMade ? "自制" : "外购");


        panel_types.setVisible(isSelfMade);
        panel_purchase.setVisible(!isSelfMade);


        Config.log("orderItemWorkFlow.workFlowSteps:" + orderItemWorkFlow.workFlowSteps);
        String[] workFlowSteps = StringUtils.split(orderItemWorkFlow.workFlowSteps);

        List<WorkFlow> orderWorkFlows = new ArrayList<>();
        for (String s : workFlowSteps) {
            for (WorkFlow workFlow : workFlows) {

                final boolean equals = String.valueOf(workFlow.flowStep).equals(s);

                if (equals) {
                    orderWorkFlows.add(workFlow);
                }
            }
        }

        data.clear();
        for (WorkFlow workFlow : orderWorkFlows) {

            WorkFlowArrangeTableModel.WorkFlowConfig workFlowConfig = new WorkFlowArrangeTableModel.WorkFlowConfig();
            workFlowConfig.workFlowStep = workFlow.flowStep;
            workFlowConfig.workFlowName = workFlow.name;
            data.add(workFlowConfig);


        }
        workFlowArrangeTableModel.setDatas(data);


        if (orderItemWorkFlow.produceType == ProduceType.PURCHASE) {
            int index = findFactoryIndex(orderItemWorkFlow.produceFactoryId);
            jbFactory.setSelectedIndex(index);
        }


        if (isSelfMade) {
            String[] factoryIds = StringUtils.split(orderItemWorkFlow.conceptusFactoryIds);

            if (factoryIds != null && factoryIds.length > 0) {
                for (int i = 0; i < factories.size(); i++) {
                    JComboBox checkbox = factories.get(i);

                    int index = findFactoryIndex(factoryIds[i]);
                    checkbox.setSelectedIndex(index);

                }
            }


            String[] configs = StringUtils.split(orderItemWorkFlow.configs);

            String[] productTypeNames = StringUtils.split(orderItemWorkFlow.productTypeNames);


            int
                    productTypeLength = productTypeNames.length;

            int length = data.size();
            for (int i = 0; i < length; i++) {
                String config = configs[i];
                final WorkFlowArrangeTableModel.WorkFlowConfig workFlowConfig = this.data.get(i);
                String[] productTypeConfig = StringUtils.split(config, StringUtils.STRING_SPLIT_COMMA);

                for (int j = 0; j < productTypeLength; j++) {


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
        }


    }

    private int findFactoryIndex(String dept) {

        int index = -1;
        for (int i = 0; i < outFactories.size(); i++) {

            OutFactory outFactory = outFactories.get(i);
            if (outFactory.dep.equals(dept)) {
                index = i;
                break;
            }

        }
        return index;

    }


    @Override
    public void bindOrderData(ErpOrderItem erpOrderItem) {

        product.setText(erpOrderItem.prd_no);
        order.setText(erpOrderItem.getOs_no());

        ImageLoader.getInstance().displayImage(new Iconable() {
            @Override
            public void setIcon(ImageIcon icon, String url) {
                photo.setIcon(icon);
            }

            @Override
            public void onError(String message) {
                photo.setText("");
            }
        }, HttpUrl.loadPicture(erpOrderItem.thumbnail), 100, 100);

    }

    @Override
    public void setProductTypes(String[] productTypes, String[] productTypeNames, java.util.List<OutFactory> outFactoryList) {


        int size = productTypes.length;
        panel_types.setLayout(new GridLayout(size, 1, 10, 10));
        List<WorkFlowSubType> subTypes = CacheManager.getInstance().bufferData.workFlowSubTypes;
        factories = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String workFlowType = String.valueOf(subTypes.get(i).typeId);
            String workFlowTypeName = subTypes.get(i).typeName;
            final JComponent panelBaseOnWorkFlow = createPanelBaseOnWorkFlow(workFlowType, workFlowTypeName, outFactoryList);
            final Dimension preferredSize = new Dimension(600, 30);
            panelBaseOnWorkFlow.setPreferredSize(preferredSize);
            panelBaseOnWorkFlow.setMinimumSize(preferredSize);
            panel_types.add(panelBaseOnWorkFlow);
        }
//        presenter.pack();
    }

    private JComponent createPanelBaseOnWorkFlow(String workFlowType, String workFlowTypeName, List<OutFactory> outFactoryList) {


        JPanel jPanel = new JPanel();

        jPanel.setLayout(new FlowLayout());
        JLabel jLabel = new JLabel(workFlowTypeName);
        jPanel.add(jLabel);


        jPanel.add(new JLabel("   选择厂家:"));
        JComboBox jComboBox = new JComboBox();

        Vector<OutFactory> outFactoryVector = ArrayUtils.changeListToVector(outFactoryList);

        ComboBoxModel comboBoxModel = new DefaultComboBoxModel(outFactoryVector) {

        };

        jComboBox.setModel(comboBoxModel);

        factories.add(jComboBox);

        jComboBox.setMinimumSize(new Dimension(200, 60));
        //   jComboBox.setPreferredSize(new Dimension(150,60));
        jPanel.add(jComboBox);
        return jPanel;


    }


    private void init() {


    }


}
