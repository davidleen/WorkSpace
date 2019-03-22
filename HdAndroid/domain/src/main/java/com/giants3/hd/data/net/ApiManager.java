package com.giants3.hd.data.net;

import android.util.Log;

import com.giants3.hd.appdata.AProduct;
import com.giants3.hd.appdata.AUser;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.ErpOrder;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.ErpOrderItemProcess;
import com.giants3.hd.entity.ErpWorkFlowReport;
import com.giants3.hd.entity.Material;
import com.giants3.hd.entity.OrderItemWorkFlowState;
import com.giants3.hd.entity.OrderItemWorkMemo;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.entity.ProductWorkMemo;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.WorkFlowArea;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.entity_erp.SampleState;
import com.giants3.hd.entity_erp.WorkFlowMaterial;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.BufferData;
import com.giants3.hd.noEntity.ErpOrderDetail;
import com.giants3.hd.noEntity.FileInfo;
import com.giants3.hd.noEntity.MessageInfo;
import com.giants3.hd.noEntity.NameCard;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.RemoteDateParameterizedType;
import com.giants3.hd.noEntity.WorkFlowMemoAuth;
import com.google.inject.Inject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.common.io.IoUtils;

/**
 * Created by david on 2016/2/13.
 */
public class ApiManager {
    private static final String TAG = "ApiManager";


    ApiConnection apiConnection;


    @Inject
    public ApiManager() {

        apiConnection = new ApiConnection();

    }

    /**
     * 登录
     *
     * @param loginData
     * @return RemoteData<AUser>
     * @throws HdException
     */
    public RemoteData<AUser> login(Map<String, String> loginData) throws HdException {

        String url = HttpUrl.login();
        String result = apiConnection.post(url, GsonUtils.toJson(loginData));
        RemoteData<AUser> remoteData = invokeByReflect(result, AUser.class);

        return remoteData;
    }


    /**
     * 获取产品列表
     *
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws HdException
     */
    public RemoteData<AProduct> getAProductList(String name, int pageIndex, int pageSize, boolean withCopy) throws HdException {

        String url = HttpUrl.getProductList(name, pageIndex, pageSize, withCopy);
        String result = apiConnection.getString(url);
        RemoteData<AProduct> remoteData = invokeByReflect(result, AProduct.class);

        return remoteData;
    }

    /**
     * 获取产品列表
     *
     * @param id
     * @return
     * @throws HdException
     */
    public RemoteData<Product> findProductById(long id) throws HdException {

        String url = HttpUrl.findProductById(id);
        String result = apiConnection.getString(url);
        RemoteData<Product> remoteData = invokeByReflect(result, Product.class);

        return remoteData;
    }


    public RemoteData<Product> findProductByNameAndVersion(String pName, String pVersion) throws HdException {

        String url = HttpUrl.findProductByNameAndVersion(pName,pVersion);
        String result = apiConnection.getString(url);
        RemoteData<Product> remoteData = invokeByReflect(result, Product.class);

        return remoteData;

    }


    /**
     * 通用方法 将字符串转换成指定类型的对象
     *
     * @param result
     * @param aClass
     * @param <T>
     * @return
     * @throws HdException
     */
    public <T> RemoteData<T> invokeByReflect(String result, Class<T> aClass) throws HdException {

        Type generateType = new RemoteDateParameterizedType(aClass);

        RemoteData<T> remoteData = GsonUtils.fromJson(result, generateType);

        if (remoteData.code == RemoteData.CODE_UNLOGIN) {
        }
        return remoteData;
    }

    public RemoteData<ErpOrder> getOrderList(String name, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.getOrderList(name, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<ErpOrder> remoteData = invokeByReflect(result, ErpOrder.class);
        return remoteData;
    }

    public RemoteData<ErpOrderDetail> getOrderDetail(String orderNo) throws HdException {

        String url = HttpUrl.getOrderDetail(orderNo);
        String result = apiConnection.getString(url);
        RemoteData<ErpOrderDetail> remoteData = invokeByReflect(result, ErpOrderDetail.class);
        return remoteData;
    }

    public RemoteData<ProductDetail> getProductDetail(long productId) throws HdException {

        String url = HttpUrl.getProductDetail(productId);
        String result = apiConnection.getString(url);
        RemoteData<ProductDetail> remoteData = invokeByReflect(result, ProductDetail.class);
        return remoteData;
    }

    public RemoteData<Quotation> getQuotationList(String name, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.getQuotationList(name, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<Quotation> remoteData = invokeByReflect(result, Quotation.class);
        return remoteData;
    }

    /**
     * 、
     * 获取报价详情
     *
     * @param quotationId
     * @return
     */
    public RemoteData<QuotationDetail> getQuotationDetail(long quotationId) throws HdException {


        String url = HttpUrl.getQuotationDetail(quotationId);
        String result = apiConnection.getString(url);
        RemoteData<QuotationDetail> remoteData = invokeByReflect(result, QuotationDetail.class);
        return remoteData;


    }

    /**
     * 读取材料列表
     *
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws HdException
     */
    public RemoteData<Material> getMaterialList(String name, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.getMaterialList(name, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<Material> remoteData = invokeByReflect(result, Material.class);
        //移动端不需要photo
        if (remoteData.isSuccess()) {

        }
        return remoteData;
    }

    /**
     * 读取未停用的材料列表
     *
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws HdException
     */
    public RemoteData<Material> getMaterialListInService(String name, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.getMaterialListInService(name, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<Material> remoteData = invokeByReflect(result, Material.class);
        //移动端不需要photo
        if (remoteData.isSuccess()) {

        }
        return remoteData;
    }

    /**
     * 上传材料图片
     *
     * @param bytes
     * @return
     * @throws HdException
     */
    public RemoteData<Void> uploadMaterialPicture(long materialId, byte[] bytes) throws HdException {

        String url = HttpUrl.uploadMaterialPicture(materialId);
        String result = "";

        result = apiConnection.postBytes(url, bytes);

        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }


    public RemoteData<BufferData> getInitData(long userId) throws HdException {

        String url = HttpUrl.loadInitData(userId);
        String result = apiConnection.getString(url);

        RemoteData<BufferData> remoteData = invokeByReflect(result, BufferData.class);

        return remoteData;
    }

    public RemoteData<ProductDetail> saveProductDetail(ProductDetail productDetail) throws HdException {
        String url = HttpUrl.saveProductDetail();
        String result = apiConnection.post(url, GsonUtils.toJson(productDetail));
        RemoteData<ProductDetail> remoteData = invokeByReflect(result, ProductDetail.class);
        return remoteData;
    }

    public RemoteData<ProductProcess> getProductProcessList(String name, int pageIndex, int pageSize) throws HdException {
        String url = HttpUrl.getProductProcessList(name, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<ProductProcess> remoteData = invokeByReflect(result, ProductProcess.class);
        //移动端不需要photo
        return remoteData;
    }

    /**
     * 获取未处理的流程信息
     *
     * @return
     * @throws HdException
     */
    public RemoteData<WorkFlowMessage> getUnHandleWorkFlowList(String key) throws HdException {

        String url = HttpUrl.getUnHandleWorkFlowList(key);
        String result = apiConnection.getString(url);
        RemoteData<WorkFlowMessage> remoteData = invokeByReflect(result, WorkFlowMessage.class);
        //移动端不需要photo
        return remoteData;
    }

    /**
     * 审核流程传递
     */
    public RemoteData<Void> checkWorkFlowMessage(long workFlowMessageId) throws HdException {

        String url = HttpUrl.checkWorkFlowMessage(workFlowMessageId);
        String result = apiConnection.getString(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }

    /**
     * 接受流程传递
     */
    public RemoteData<Void> receiveWorkFlowMessage(long workFlowMessageId, File[] files, String area) throws HdException {
        String url = HttpUrl.receiveWorkFlowMessage(workFlowMessageId);
        String result = apiConnection.updatePictures(url, files);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }

    /**
     * 获取可以传递流程的订单item
     *
     * @return
     */
    public RemoteData<OrderItemWorkFlowState> getAvailableOrderItemForTransform() throws HdException {

        String url = HttpUrl.getAvailableOrderItemForTransform();
        String result = apiConnection.getString(url);
        RemoteData<OrderItemWorkFlowState> remoteData = invokeByReflect(result, OrderItemWorkFlowState.class);
        //移动端不需要photo
        return remoteData;

    }

    public RemoteData<Void> sendWorkFlowMessage(ErpOrderItemProcess erpOrderItemProcess, int tranQty, long area, String memo) throws HdException {
        String url = HttpUrl.sendWorkFlowMessage(tranQty, area, memo);
        String result = apiConnection.post(url, GsonUtils.toJson(erpOrderItemProcess));
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }

    public RemoteData<WorkFlowMessage> mySendWorkFlowMessage() throws HdException {

        String url = HttpUrl.mySendWorkFlowMessage();
        String result = apiConnection.getString(url);
        RemoteData<WorkFlowMessage> remoteData = invokeByReflect(result, WorkFlowMessage.class);

        return remoteData;

    }

    public RemoteData<Void> rejectWorkFlowMessage(long workFlowMessageId, final File[] file, final String memo) throws HdException {
        String url = HttpUrl.rejectWorkFlowMessage(workFlowMessageId, memo);
        String result = apiConnection.updatePictures(url, file);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }


    public RemoteData<Void> rollbackWorkFlowMessage(long workFlowMessageId, String memo) throws HdException {

        String url = HttpUrl.rollbackWorkFlowMessage(workFlowMessageId, memo);
        String result = apiConnection.getString(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }
    /**
     * 读取未出库订单货款列表
     *
     * @return
     */
    public RemoteData<OrderItemWorkFlowState> loadUnCompleteOrderItemWorkFlowReport() throws HdException {

        String url = HttpUrl.loadUnCompleteOrderItemWorkFlowReport();
        String result = apiConnection.getString(url);
        RemoteData<OrderItemWorkFlowState> remoteData = invokeByReflect(result, OrderItemWorkFlowState.class);

        return remoteData;
    }

    public RemoteData<OrderItemWorkFlowState> loadOrderWorkFlowReport(String key, int pageIndex, int pageSize) throws HdException {
        String url = HttpUrl.loadOrderWorkFlowReport(key, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<OrderItemWorkFlowState> remoteData = invokeByReflect(result, OrderItemWorkFlowState.class);

        return remoteData;
    }

    public RemoteData<FileInfo> loadAppUpgradeInfo() throws HdException {

        String url = HttpUrl.loadAppUpgradeInfo();
        String result = apiConnection.getString(url);
        RemoteData<FileInfo> remoteData = invokeByReflect(result, FileInfo.class);

        return remoteData;
    }

    /**
     * 查询订单的报表
     *
     * @return
     */

    public RemoteData<ErpWorkFlowReport> getOrderItemWorkFlowReport(final String os_no, final int itm) throws HdException {

        String url = HttpUrl.getOrderItemWorkFlowReport(os_no, itm);
        String result = apiConnection.getString(url);
        RemoteData<ErpWorkFlowReport> remoteData = invokeByReflect(result, ErpWorkFlowReport.class);

        return remoteData;
    }

    /**
     * 查询订单
     *
     * @param key
     * @return
     */
    public RemoteData<ErpOrderItem> searchErpOrderItems(String key, final int pageIndex, final int pageSize) throws HdException {
        String url = HttpUrl.searchErpOrderItem(key, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<ErpOrderItem> remoteData = invokeByReflect(result, ErpOrderItem.class);

        return remoteData;
    }

    public RemoteData<ErpOrderItemProcess> getAvailableOrderItemProcess(final String osNo, final int itm, int workFlowStep) throws HdException {


        String url = HttpUrl.getAvailableOrderItemProcess(osNo, itm, workFlowStep);
        String result = apiConnection.getString(url);
        RemoteData<ErpOrderItemProcess> remoteData = invokeByReflect(result, ErpOrderItemProcess.class);

        return remoteData;


    }

    /**
     * 读取指定订单，流程的消息列表
     *
     * @param os_no
     * @param workFlowStep
     * @return
     */
    public RemoteData<WorkFlowMessage> getOrderItemWorkFlowMessage(String os_no, int itm, int workFlowStep) throws HdException {

        String url = HttpUrl.getOrderItemWorkFlowMessage(os_no, itm, workFlowStep);
        String result = apiConnection.getString(url);
        RemoteData<WorkFlowMessage> remoteData = invokeByReflect(result, WorkFlowMessage.class);

        return remoteData;


    }


    public RemoteData<User> loadUsers() throws HdException {


        String url = HttpUrl.loadUsers();
        String result = apiConnection.getString(url);
        RemoteData<User> remoteData = invokeByReflect(result, User.class);

        return remoteData;


    }

    public RemoteData<ErpOrderItem> getUnCompleteWorkFlowOrderItems(String key, int workFlowStep, int pageIndex, int pageSize) throws HdException {
        String url = HttpUrl.getUnCompleteWorkFlowOrderItems(key, workFlowStep, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<ErpOrderItem> remoteData = invokeByReflect(result, ErpOrderItem.class);

        return remoteData;
    }


    public RemoteData<ErpOrderItem> getCompleteWorkFlowOrderItems(String key, int pageIndex, int pageSize) throws HdException {


        String url = HttpUrl.getCompleteWorkFlowOrderItems(key, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<ErpOrderItem> remoteData = invokeByReflect(result, ErpOrderItem.class);

        return remoteData;

    }

    public RemoteData<OrderItemWorkMemo> getOrderItemWorkMemoList(String os_no, int itm) throws HdException {
        String url = HttpUrl.getOrderItemWorkMemoList(os_no, itm);
        String result = apiConnection.getString(url);
        RemoteData<OrderItemWorkMemo> remoteData = invokeByReflect(result, OrderItemWorkMemo.class);
        return remoteData;

    }

    public RemoteData<ProductWorkMemo> getProductWorkMemoList(String productName, String pversion) throws HdException {
        String url = HttpUrl.getProductWorkMemoList(productName, pversion);
        String result = apiConnection.getString(url);
        RemoteData<ProductWorkMemo> remoteData = invokeByReflect(result, ProductWorkMemo.class);
        return remoteData;

    }

    public RemoteData<Void> saveWorkMemo(int workFlowStep, String os_no, int itm, String orderItemWorkMemo, String prd_name, String pVersion, String productWorkMemo) throws HdException {

        String url = HttpUrl.saveWorkMemo();


        Map<String, Object> map = new HashMap<>();
        map.put("workFlowStep", workFlowStep);
        map.put("os_no", os_no);
        map.put("itm", itm);
        map.put("orderItemWorkMemo", orderItemWorkMemo);
        map.put("prd_name", prd_name);
        map.put("pVersion", pVersion);
        map.put("productWorkMemo", productWorkMemo);
        String result = apiConnection.post(url, GsonUtils.toJson(map));
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }


    public RemoteData<WorkFlowArea> getWorkFlowAreaList() throws HdException {


        String url = HttpUrl.getWorkFlowAreaList();
        String result = apiConnection.getString(url);
        RemoteData<WorkFlowArea> remoteData = invokeByReflect(result, WorkFlowArea.class);
        return remoteData;

    }

    public RemoteData<MessageInfo> getNewMessageInfo() throws HdException {

        String url = HttpUrl.getNewMessageInfo();
        String result = apiConnection.getString(url);
        RemoteData<MessageInfo> remoteData = invokeByReflect(result, MessageInfo.class);
        return remoteData;
    }

    public RemoteData<WorkFlowMaterial> getWorkFlowMaterials(String osNo, int itm, String workFlowCode) throws HdException {
        String url = HttpUrl.getWorkFlowMaterials(osNo, itm, workFlowCode);
        String result = apiConnection.getString(url);
        RemoteData<WorkFlowMaterial> remoteData = invokeByReflect(result, WorkFlowMaterial.class);
        return remoteData;
    }

    public RemoteData<WorkFlowMessage> getWorkFlowMessageByOrderItem(String osNo, int itm) throws HdException {
        String url = HttpUrl.getWorkFlowMessageByOrderItem(osNo, itm);
        String result = apiConnection.getString(url);
        RemoteData<WorkFlowMessage> remoteData = invokeByReflect(result, WorkFlowMessage.class);
        return remoteData;
    }

    public RemoteData<WorkFlowMessage> getMyWorkFlowMessage(String key, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.getMyWorkFlowMessage(key, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<WorkFlowMessage> remoteData = invokeByReflect(result, WorkFlowMessage.class);
        return remoteData;
    }

    public RemoteData<WorkFlowMemoAuth> getWorkFlowMemoAuth() throws HdException {

        String url = HttpUrl.getWorkFlowMemoAuth();
        String result = apiConnection.getString(url);
        RemoteData<WorkFlowMemoAuth> remoteData = invokeByReflect(result, WorkFlowMemoAuth.class);
        return remoteData;
    }

    public RemoteData<Void> checkWorkFlowMemo(long orderItemWorkMemoId, boolean check) throws HdException {

        String url = HttpUrl.checkWorkFlowMemo(orderItemWorkMemoId, check);
        String result = apiConnection.getString(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<Void> updatePassword(String oldPasswordMd5, String newPasswordMd5) throws HdException {
        String url = HttpUrl.updatePassword(oldPasswordMd5, newPasswordMd5);
        String result = apiConnection.getString(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<User> loginByUserId(long userId, String passwordMd5, String deviceToken) throws HdException {

        String url = HttpUrl.loginByUserId(userId, passwordMd5, deviceToken);
        String result = apiConnection.getString(url);
        RemoteData<User> remoteData = invokeByReflect(result, User.class);
        return remoteData;
    }

    public RemoteData<SampleState> searchSampleData(String prdNo, String pVersion) throws HdException {

        String url = HttpUrl.searchSampleData(prdNo, pVersion);
        String result = apiConnection.getString(url);
        RemoteData<SampleState> remoteData = invokeByReflect(result, SampleState.class);
        return remoteData;

    }


    public RemoteData<Void> clearWorkFlow(String os_no, int itm) throws HdException {
        String url = HttpUrl.clearWorkFlow(os_no, itm);
        String result = apiConnection.getString(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }


    public RemoteData<Void> adjustWorkFlowItem(String os_no, String prd_no,String pVersion,int itm) throws HdException {
        String url = HttpUrl.adjustWorkFlowItem(os_no, prd_no,pVersion,itm);
        String result = apiConnection.getString(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }


    public RemoteData<com.giants3.hd.entity.app.Quotation> getAppQotations(String key, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.getAppQotations(key, pageIndex, pageSize);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.entity.app.Quotation> remoteData = invokeByReflect(result, com.giants3.hd.entity.app.Quotation.class);
        return remoteData;

    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> getAppQuotationDetail(long quotationId) throws HdException {

        String url = HttpUrl.getAppQuotationDetail(quotationId);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;

    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> createAppQuotation() throws HdException {
        String url = HttpUrl.createTempAppQuotation();
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;
    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> addProductToQuotation(long quotationId, long productId) throws HdException {
        String url = HttpUrl.addProductToQuotation(quotationId, productId);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;
    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> removeItemFromQuotation(long quotationId, int item) throws HdException {
        String url = HttpUrl.removeItemFromQuotation(quotationId, item);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;
    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> updateQuotationItemPrice(long quotationId, int itm, float price) throws HdException {


        String url = HttpUrl.updateQuotationItemPrice(quotationId, itm, price);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;
    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> updateQuotationItemQty(long quotationId, int itm, int newQty) throws HdException {

        String url = HttpUrl.updateQuotationItemQty(quotationId, itm, newQty);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;


    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> updateQuotationItemDiscount(long quotationId, int itm, float newDisCount) throws HdException {
        String url = HttpUrl.updateQuotationItemDiscount(quotationId, itm, newDisCount);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;
    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> updateQuotationDiscount(long quotationId, float newDisCount) throws HdException {
        String url = HttpUrl.updateQuotationDiscount(quotationId, newDisCount);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;

    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> saveAppQuotation(com.giants3.hd.noEntity.app.QuotationDetail quotationDetail) throws HdException {
        String url = HttpUrl.saveAppQuotation();
        String result = apiConnection.post(url, GsonUtils.toJson(quotationDetail));
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;

    }

    public RemoteData<Void> printQuotation(long quotationId, String filePath) throws HdException {
        String url = HttpUrl.printQuotation(quotationId);
        InputStream inputStream = apiConnection.getInputStream(url);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            IoUtils.copyAllBytes(inputStream, out);
            out.flush();
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
            throw HdException.create("文件路径未找到:" + filePath);
        } finally {
            IoUtils.safeClose(inputStream);
            IoUtils.safeClose(out);
        }
        RemoteData<Void> remoteData = new RemoteData<>();


        return remoteData;


    }

    public RemoteData<Customer> getCustomerList(String key,int pageIndex,int pageSize) throws HdException {
        String url = HttpUrl.getCustomerList(key,  pageIndex,  pageSize);
        String result = apiConnection.getString(url);
        RemoteData<Customer> remoteData = invokeByReflect(result, Customer.class);
        return remoteData;

    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> updateQuotationCustomer(long quotationId, long customerId) throws HdException {

        String url = HttpUrl.updateQuotationCustomer(quotationId, customerId);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;


    }

    public RemoteData<Customer> saveCustomer(Customer customer) throws HdException {

        String url = HttpUrl.saveCustomer();
        String result = apiConnection.post(url, GsonUtils.toJson(customer));
        RemoteData<Customer> remoteData = invokeByReflect(result, Customer.class);
         return remoteData;

    }

    public RemoteData<NameCard> scanNameCard(File file) throws HdException {


        String s = apiConnection.updatePictures(HttpUrl.scanNameCard(), new File[]{file});
        RemoteData<NameCard> remoteData = invokeByReflect(s, NameCard.class);


        return remoteData;

    }

    public RemoteData<String> generateCustomerCode() throws HdException {


        String url = HttpUrl.generateCustomerCode();
        String result = apiConnection.getString(url);
        RemoteData<String> remoteData = invokeByReflect(result, String.class);
        return remoteData;

    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> updateQuotationItemMemo(long quotationId, int itm, String memo) throws HdException {
        String url = HttpUrl.updateQuotationItemMemo(quotationId, itm, memo);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;
    }

    public RemoteData<Void> deleteQuotation(long quotationId) throws HdException {
        String url = HttpUrl.deleteQuotation(quotationId);
        String result = apiConnection.getString(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> updateQuotationFieldValue(long quotationId, String field, String data) throws HdException {
        String url = HttpUrl.updateQuotationFieldValue(quotationId, field, data);
        String result = apiConnection.getString(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;

    }

    public RemoteData<WorkFlowMessage> getWorkFlowMessageById(long workFlowMessageId) throws HdException {


        String url = HttpUrl.getWorkFlowMessageById(workFlowMessageId);
        String result = apiConnection.getString(url);
        RemoteData<WorkFlowMessage> remoteData = invokeByReflect(result, WorkFlowMessage.class);
        return remoteData;

    }

    public RemoteData<WorkFlowMessage> getUnHandleWorkFlowMessageReport(int hourLimit) throws HdException {
        String url = HttpUrl.getUnHandleWorkFlowMessageReport(hourLimit);
        String result = apiConnection.getString(url);
        RemoteData<WorkFlowMessage> remoteData = invokeByReflect(result, WorkFlowMessage.class);
        return remoteData;
    }


    public RemoteData<NameCard> scanResourceUrl(String resourceUrl) throws HdException {


        String url = HttpUrl.scanResourceUrl(resourceUrl);
        String result = apiConnection.getString(url );
        RemoteData<NameCard> remoteData = invokeByReflect(result, NameCard.class);
        return remoteData;

    }

    public RemoteData<Void> deleteCustomer(long customerId) throws HdException {
        String url = HttpUrl.deleteCustomer(customerId);
        String result = apiConnection.getString(url );
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;

    }

}
