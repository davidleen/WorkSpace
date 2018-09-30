package com.giants3.hd.noEntity;

/**
 * Created by davidleen29 on 2015/8/8.
 */

import com.giants3.hd.entity.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 本地缓存数据
 */
public class BufferData implements Serializable{

    public   List<PClass> pClasses;
    public   List<Customer> customers;
    public   List<PackMaterialType> packMaterialTypes;
    public   List<PackMaterialPosition> packMaterialPositions;
    public   List<PackMaterialClass> packMaterialClasses;

    public   List<Pack> packs=new ArrayList<>();
    public   List<MaterialClass> materialClasses;
    public   List<MaterialType> materialTypes;

    public   List<User> salesmans;
    public List<Authority> authorities;
    public QuoteAuth quoteAuth;



    /**
     * 生产二级流程数据
     */
    public List<WorkFlowSubType> workFlowSubTypes;

    /**
     * 订单权限
     */
    public OrderAuth orderAuth;

    /**
     * 出库单权限
     */
    public StockOutAuth stockOutAuth;

    public  User loginUser;

    public GlobalData globalData;

    /**
     * 产品录入的模板数据
     */
    public List<ProductDetail> demos;

    public List<Factory> factories;





    /**
     * 流程相关权限（登录用户的）
     */
    public  List<WorkFlowWorker> workFlowWorkers;
}
