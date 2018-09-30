package com.giants.hd.desktop.utils;

import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.entity.Authority;
import com.giants3.hd.entity.User;

import java.util.List;

/**
 * 权限判断
 * Created by davidleen29 on 2015/8/8.
 */
public class AuthorityUtil {


    private static AuthorityUtil authorityUtil = new AuthorityUtil();

    public static AuthorityUtil getInstance() {
        return authorityUtil;
    }


    /**
     * 判断是否业务员
     *
     *
     * @return
     */
    public  boolean cannotViewProductPrice( ) {

        return !canViewProductPrice();
    }


    /**
     * 判断是否可以查看产品分析表的价格信息
     *
     *
     * @return
     */
    public  boolean canViewProductPrice( ) {

        return canViewPrice(ModuleConstant.NAME_PRODUCT);

    }


    /**
     * 判断是否可以查看价格信息
     *
     *
     * @return
     */
    public  boolean canViewPrice( String module) {
        if (isAdmin())
            return true;
        for (Authority authority : getAuthority()) {
            if (module.equals(authority.module.name) && authority.viewPrice) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdmin() {
        return CacheManager.getInstance().bufferData.loginUser.name.equals(User.ADMIN);
    }

    /**
     * 查看模块是否有查看权限
     *
     * @param moduleName
     * @return
     */
    public boolean isViewable(String moduleName) {
        if (isAdmin())
            return true;
        for (Authority authority : getAuthority()) {
            if (moduleName.equals(authority.module.name) && authority.viewable) {
                return true;
            }

        }


        return false;
    }


    /**
     * 查看模块是否有审核权限
     *
     * @param moduleName
     * @return
     */
    private boolean isVerifiable(String moduleName) {
        if (isAdmin())
            return true;
        for (Authority authority : getAuthority()) {
            if (moduleName.equals(authority.module.name) && authority.checkable) {
                return true;
            }

        }


        return false;
    }

    /**
     * 查看模块是否有添加的权限
     *
     * @param moduleName
     * @return
     */
    private boolean isAddable(String moduleName) {
        if (isAdmin())
            return true;
        for (Authority authority : getAuthority()) {
            if (moduleName.equals(authority.module.name) && authority.addable) {
                return true;
            }

        }
        return false;
    }


    /**
     * 查看模块是否有查看权限
     *
     * @param moduleName
     * @return
     */
    private boolean editable(String moduleName) {
        if (isAdmin())
            return true;
        for (Authority authority : getAuthority()) {
            if (moduleName.equals(authority.module.name) && authority.editable) {
                return true;
            }

        }


        return false;
    }


    /**
     * 查看模块是否有查看权限
     *
     * @param moduleName
     * @return
     */
    private boolean deletable(String moduleName) {
        if (isAdmin())
            return true;
        for (Authority authority : getAuthority()) {
            if (moduleName.equals(authority.module.name) && authority.deletable) {
                return true;
            }

        }


        return false;
    }

    /**
     * 查看模块是否有查看权限
     *
     * @param moduleName
     * @return
     */
    private boolean exportable(String moduleName) {
        if (isAdmin())
            return true;
        for (Authority authority : getAuthority()) {
            if (moduleName.equals(authority.module.name) && authority.exportable) {
                return true;
            }

        }


        return false;
    }


    public List<Authority> getAuthority() {
        return CacheManager.getInstance().bufferData.authorities;
    }

    public boolean viewProductModule() {


        return viewProductList() || viewMaterialList() || viewMaterialPicture() || viewProductPicture() || viewProductDelete();
    }

    public boolean viewMaterialPicture() {

        return isViewable(ModuleConstant.NAME_MATERIAL_PICTURE);
    }

    public boolean viewProductPicture() {

        return isViewable(ModuleConstant.NAME_PRODUCT_PICTURE);
    }

    public boolean viewProductList() {


        return isViewable(ModuleConstant.NAME_PRODUCT);

    }


    public boolean viewMaterialList() {


        return isViewable(ModuleConstant.NAME_MATERIAL);
    }


    /**
     * 查看报价模块
     *
     * @return
     */
    public boolean viewQuotationModule() {


        return viewQuotationList() || viewQuotationDeleteList();
    }


    public boolean viewAppQuotationList() {


        return isViewable(ModuleConstant.NAME_APP_QUOTATION);

    }

    /**
     * 查看出入库管理
     *
     * @return
     */
    public boolean viewStockModule() {

        return isViewable(ModuleConstant.NAME_STOCK);


    }

    public boolean viewQuotationList() {


        return isViewable(ModuleConstant.NAME_QUOTATION);
    }

    public boolean viewQuotationDeleteList() {


        return isViewable(ModuleConstant.NAME_QUOTATION_DELETE);
    }


    /**
     * 查看基础数据模块
     *
     * @return
     */
    public boolean viewBaseDataModule() {
        return viewProcessList() || viewMaterialClassList() || viewCustomerList() || viewPackMaterialClassList();

    }


    /**
     * 查看工序列表
     *
     * @return
     */
    public boolean viewProcessList() {

        return isViewable(ModuleConstant.NAME_PROCESS);
    }


    /**
     * 查看工序列表
     *
     * @return
     */
    public boolean viewCustomerList() {


        return isViewable(ModuleConstant.NAME_CUSTOMER);

    }


    /**
     * 查看材料类型列表
     *
     * @return
     */
    public boolean viewMaterialClassList() {

        return isViewable(ModuleConstant.NAME_MATERIAL_CLASS);


    }


    /**
     * 查看材料类型列表
     *
     * @return
     */
    public boolean viewPackMaterialClassList() {

        return isViewable(ModuleConstant.NAME_PACK_MATERIAL_CLASS);


    }

    /**
     * 查看权限模块
     */
    public boolean viewAuthorityModule() {


        return viewUserList() || viewModuleList() || viewAuthorityList();

    }


    /**
     * 查看用户列表
     */

    public boolean viewUserList() {

        return isViewable(ModuleConstant.NAME_USER);
    }


    /**
     * 查看模块列表
     */

    public boolean viewModuleList() {

        return isViewable(ModuleConstant.NAME_MODULE);
    }

    /**
     * 查看模块列表
     */

    public boolean viewAuthorityList() {

        return isViewable(ModuleConstant.NAME_AUTHORITY);
    }


    /**
     * 系统模块是否可视
     *
     * @return
     */
    public boolean viewSystemModule() {

        return viewSyncData() || viewSysParam();
    }

    /**
     * 同步数据 菜单
     *
     * @return
     */
    public boolean viewSyncData() {

        return isViewable(ModuleConstant.NAME_SYNC_DATA);
    }


    /**
     * 图片上传菜单
     *
     * @return
     */
    public boolean viewPictureUpload() {


        return isViewable(ModuleConstant.NAME_PICTURE_UPLOAD);
    }


    /**
     * 是否有添加产品的权限
     *
     * @return
     */
    public boolean addProduct() {


        return isAddable(ModuleConstant.NAME_PRODUCT);


    }

    /**
     * 添加材料的权限
     *
     * @return
     */
    public boolean addMaterial() {
        return isAddable(ModuleConstant.NAME_MATERIAL);

    }


    /**
     * 添加报价单的权限
     *
     * @return
     */
    public boolean addQuotation() {


        return isAddable(ModuleConstant.NAME_QUOTATION);


    }

    public boolean editProduct() {

        return editable(ModuleConstant.NAME_PRODUCT);
    }


    public boolean deleteProduct() {

        return deletable(ModuleConstant.NAME_PRODUCT);
    }

    public boolean exportProduct() {
        return exportable(ModuleConstant.NAME_PRODUCT);
    }

    public boolean editQuotation() {

        return editable(ModuleConstant.NAME_QUOTATION);
    }

    public boolean exportQuotation() {

        return exportable(ModuleConstant.NAME_QUOTATION);
    }

    public boolean deleteQuotation() {

        return deletable(ModuleConstant.NAME_QUOTATION);
    }

    public boolean editMaterial() {

        return editable(ModuleConstant.NAME_MATERIAL);
    }

    public boolean deleteMaterial() {

        return deletable(ModuleConstant.NAME_MATERIAL);
    }

    public boolean viewProductDelete() {

        return isViewable(ModuleConstant.NAME_PRODUCT_DELETE);
    }


    /**
     * 查看设置系统参数
     *
     * @return
     */
    public boolean viewSysParam() {

        return isViewable(ModuleConstant.NAME_SYS_PARAM);

    }

    public boolean verifyQuotation() {
        return isVerifiable(ModuleConstant.NAME_QUOTATION);

    }

    public boolean viewProductReport() {

        return isViewable(ModuleConstant.NAME_PRODUCT_REPORT);
    }

    public boolean viewTaskManage() {
        return isViewable(ModuleConstant.NAME_TASK_MANAGE);
    }

    public boolean viewOrderMenu() {
        return isViewable(ModuleConstant.NAME_ORDER);
    }

    /**
     * 出库查看权限
     *
     * @return
     */
    public boolean viewStockOutList() {
        return isViewable(ModuleConstant.NAME_STOCK_OUT);
    }

    /**
     * 出库能否编辑权限
     *
     * @return
     */
    public boolean editStockOut() {
        return editable(ModuleConstant.NAME_STOCK_OUT);
    }


    /**
     * 出库单能否导出权限
     *
     * @return
     */
    public boolean exportStockOut() {
        return exportable(ModuleConstant.NAME_STOCK_OUT);
    }

    /**
     * 是否订单编辑权限
     *
     * @return
     */
    public boolean editOrder() {


        return editable(ModuleConstant.NAME_ORDER);
    }

    /**
     * 是否可以查看订单报表
     *
     * @return
     */
    public boolean viewOrderReport() {
        return isViewable(ModuleConstant.NAME_ORDER_REPORT);
    }

    /**
     * 是否可以查看生产流程
     *
     * @return
     */
    public boolean viewWorkFlow() {
        return isViewable(ModuleConstant.NAME_WORK_FLOW);
    }


    public boolean viewOrderWorkFlowReport() {
        return isViewable(ModuleConstant.NAME_ORDER_WORK_FLOW_REPORT);
    }

    public boolean viewStockInAndSubmit() {
        return isViewable(ModuleConstant.NAME_STOCK_IN_AND_SUBMIT);
    }

    public boolean viewTransportOut() {
        return isViewable(ModuleConstant.NAME_TRANSPORT_OUT);
    }


    public boolean viewXiaokuItemSearch() {
        return isViewable(ModuleConstant.NAME_TRANSPORT_OUT_SEARCH);
    }

    public boolean viewOutFactoryList() {

        return isViewable(ModuleConstant.NAME_OUT_FACTORY);
    }

    public boolean viewProductArrangeType() {

        return isViewable(ModuleConstant.NAME_PRODUCT_ARRANGE_TYPE);

    }

    public boolean viewEquation() {

        return isViewable(ModuleConstant.NAME_EQUATION);
    }


    public boolean viewZhilingdan() {

        return isViewable(ModuleConstant.NAME_ZHILINGDAN);
    }

    public boolean viewWorkFlowModule() {

        return isViewable(ModuleConstant.NAME_WORK_FLOW_MODULE);
    }

    public boolean viewWorkFlowWorker() {
        return isViewable(ModuleConstant.NAME_WORK_FLOW_WORKER);
    }
    public boolean viewWorkFlowArranger() {
        return isViewable(ModuleConstant.NAME_WORK_FLOW_ARRANGER);
    }

    /**
     * 订单排厂列表
     * @return
     */
    public boolean viewOrderItemForArrange() {

        return isViewable(ModuleConstant.NAME_ORDER_ITEM_FOR_ARRANGE);
    }

    public boolean viewWorkFLowEventConfig() {

        return isViewable(ModuleConstant.NAME_WORK_FLOW_ITEM_EVENT_CONFIG);
    }


    public boolean viewQuotationPictureExport() {


        return isViewable(ModuleConstant.NAME_QUTOTATION_PICTURE_EXPORT);

    }

    public boolean viewWorkFlowLimit() {
        return isViewable(ModuleConstant.NAME_WORK_FLOW_LIMIT);

    }

    public boolean viewSubWorkFlowList() {
        return isViewable(ModuleConstant.NAME_SUB_WORK_FLOW);
    }

    public boolean viewCompanyInfo() {

        return isViewable(ModuleConstant.NAME_COMPANY_INFO);
    }
}
