package com.giants3.hd.domain.api;

import com.giants3.hd.entity.*;
import com.giants3.hd.entity.app.AppQuoteAuth;
import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.entity_erp.Sub_workflow_state;
import com.giants3.hd.entity_erp.Zhilingdan;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.*;
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.internal.MoreTypes;
import de.greenrobot.common.io.IoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 远程操作接口
 */
@Singleton
public class ApiManager {




    @Inject
    public ApiManager() {


    }

    @Inject
    Client client;

    public <T> RemoteData<T> invokeByReflect(String result, Class<T> aClass) {

         Type generateType;
       // generateType = tokenMaps.get(aClass);
//        generateType=new TypeToken<RemoteData<T>>() {
//        }.getType();
        //gson 解析类型处理。
//        generateType=  new MoreTypes.ParameterizedTypeImpl(null,RemoteData.class,new Type[]{aClass});

        generateType=  new RemoteDateParameterizedType( aClass);
       ;

        RemoteData<T> remoteData = GsonUtils.fromJson(result,generateType);
        return remoteData;
    }


    /**
     * 查询产品
     *
     * @param key
     * @param pageIndex
     * @param pageSize
     * @param withCopy 是否包含翻单
     * @return
     * @throws HdException
     */
    public RemoteData<Product> searchProduct(String key, int pageIndex, int pageSize,boolean withCopy) throws HdException {

        String url = HttpUrl.searchProduct(key,  pageIndex, pageSize,withCopy);

        String result = client.getWithStringReturned(url);

        RemoteData<Product> productRemoteData = invokeByReflect(result, Product.class);
        return productRemoteData;


    }

    /**
     * 读取产品列表
     *
     * @param productName
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws HdException
     */
    public RemoteData<Product> readProductList(String productName, int pageIndex, int pageSize) throws HdException {

        return readProductList(productName, 0, pageIndex, pageSize);


    }

    /**
     * 读取产品列表
     *
     * @param productName
     * @param pageIndex
     * @param viewType    查看类型 0 没有限制， 1 白胚未录入 2 组装未处理 3 油漆未处理 4 包装未处理。
     * @param pageSize
     * @return
     * @throws HdException
     */
    public RemoteData<Product> readProductList(String productName, int viewType, int pageIndex, int pageSize) throws HdException {


        String url = HttpUrl.loadProductList(productName, viewType, pageIndex, pageSize);

        String result = client.postWithStringReturned(url, null);

        RemoteData<Product> productRemoteData = invokeByReflect(result, Product.class);
        return productRemoteData;


    }


    /**
     * 读取指定范围产品列表
     *
     * @param startName
     * @param endName
     * @param withCopy
     * @return
     * @throws HdException
     */
    public RemoteData<Product> loadProductListByNameBetween(String startName, String endName, boolean withCopy) throws HdException {


        String url = HttpUrl.loadProductListByNameBetween(startName, endName, withCopy);
        String result = client.getWithStringReturned(url);

        RemoteData<Product> productRemoteData = invokeByReflect(result, Product.class);

        return productRemoteData;


    }


    /**
     * 保存产品数据
     *
     * @param productDetail
     * @return
     */
    public RemoteData<ProductDetail> saveProduct(ProductDetail productDetail) throws HdException {


        String url = HttpUrl.saveProduct();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(productDetail));


        RemoteData<ProductDetail> productRemoteData = invokeByReflect(result, ProductDetail.class);


        return productRemoteData;


    }

    /**
     * 保存产品数据
     *
     * @param operationLogId
     * @return
     */
    public RemoteData<Void> restoreProductDetailFromOperationLog(long operationLogId) throws HdException {


        String url = HttpUrl.restoreProductDetailFromOperationLog();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(operationLogId));
        RemoteData<Void> productRemoteData = invokeByReflect(result, Void.class);
        return productRemoteData;


    }

    /**
     * 读取产品信息详情
     *
     * @param id
     * @return
     */
    public RemoteData<ProductDetail> loadProductDetail(long id) throws HdException {


        String url = HttpUrl.loadProductDetail(id);

        String result = client.postWithStringReturned(url, null);

        RemoteData<ProductDetail> productRemoteData = invokeByReflect(result, ProductDetail.class);

        return productRemoteData;

    }


    /**
     * \读取产品类别列表
     *
     * @return
     */

    public RemoteData<PClass> readProductClass() throws HdException {


        String url = HttpUrl.loadProductClass();

        String result = client.getWithStringReturned(url);


        RemoteData<PClass> remoteData = invokeByReflect(result, PClass.class);


        return remoteData;


    }


    /**
     * \读取客户列表
     *
     * @return
     */

    public RemoteData<Customer> readCustomers() throws HdException {


        String url = HttpUrl.loadCustomers();

        String result = client.getWithStringReturned(url);

        RemoteData<Customer> remoteData = invokeByReflect(result, Customer.class);

        return remoteData;


    }


    /**
     * 保存客户列表
     *
     * @return
     */

    public RemoteData<Customer> saveCustomers(List<Customer> customers) throws HdException {


        String url = HttpUrl.saveCustomers();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(customers));


        RemoteData<Customer> remoteData = invokeByReflect(result, Customer.class);

        return remoteData;

    }


    /**
     * \读取业务员列表
     *
     * @return
     */

    public RemoteData<User> readSalesmans() throws HdException {


        String url = HttpUrl.loadSalesmans();

        String result = client.getWithStringReturned(url);


        RemoteData<User> remoteData = invokeByReflect(result, User.class);

        return remoteData;


    }


    /**
     * \读取用户员列表
     *
     * @return
     */

    public RemoteData<User> readUsers() throws HdException {


        String url = HttpUrl.loadUsers();

        String result = client.getWithStringReturned(url);


        RemoteData<User> remoteData = invokeByReflect(result, User.class);

        return remoteData;


    }


    /**
     * 保存用户列表
     *
     * @return
     */

    public RemoteData<User> saveUsers(List<User> users
    ) throws HdException {


        String url = HttpUrl.saveUsers();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(users));


        RemoteData<User> remoteData = invokeByReflect(result, User.class);

        return remoteData;

    }


    /**
     * \读取用户员列表
     *
     * @return
     */

    public RemoteData<Module> readModules() throws HdException {


        String url = HttpUrl.loadModules();

        String result = client.getWithStringReturned(url);


        RemoteData<Module> remoteData = invokeByReflect(result, Module.class);

        return remoteData;


    }


    /**
     * 保存模块
     *
     * @return
     */

    public RemoteData<Module> saveModules(List<Module> users
    ) throws HdException {


        String url = HttpUrl.saveModules();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(users));


        RemoteData<Module> remoteData = invokeByReflect(result, Module.class);

        return remoteData;

    }

    /**
     * 根据物料编码与名称 模糊搜索材料
     *
     * @param value
     * @return
     */
    public RemoteData<Material> loadMaterialByCodeOrName(String value, int pageIndex, int pageSize) throws HdException {


        return loadMaterialByCodeOrName(value, "", pageIndex, pageSize);
    }

    /**
     * 根据工序编码名称 模糊搜索工序
     *
     * @param value
     * @return
     */
    public RemoteData<ProductProcess> loadProcessByCodeOrName
    (String value, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.loadProcessByCodeOrName(value, pageIndex, pageSize);
        String result = client.getWithStringReturned(url);


        RemoteData<ProductProcess> remoteData = invokeByReflect(result, ProductProcess.class);

        return remoteData;
    }

    /**
     * 根据物料编码与名称 模糊搜索材料
     *
     * @param value
     * @return
     */
    public RemoteData<Material> loadMaterialByCodeOrName(String value, String classId, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.loadMaterialByCodeOrName(value, classId, pageIndex, pageSize);
        String result = client.getWithStringReturned(url);


        RemoteData<Material> remoteData = invokeByReflect(result, Material.class);

        return remoteData;

    }


    /**
     * 读取包装材料类型列表
     *
     * @return
     */
    public RemoteData<PackMaterialType> readPackMaterialType() throws HdException {

        String url = HttpUrl.loadPackMaterialType();

        String result = client.getWithStringReturned(url);


        RemoteData<PackMaterialType> remoteData = invokeByReflect(result, PackMaterialType.class);

        return remoteData;
    }


    /**
     * 读取包装材料大类别列表
     *
     * @return
     */
    public RemoteData<PackMaterialClass> readPackMaterialClass() throws HdException {

        String url = HttpUrl.loadPackMaterialClass();

        String result = client.getWithStringReturned(url);


        RemoteData<PackMaterialClass> remoteData = invokeByReflect(result, PackMaterialClass.class);


        return remoteData;
    }


    /**
     * 读取包装材料类型列表
     *
     * @return
     */
    public RemoteData<PackMaterialPosition> readPackMaterialPosition() throws HdException {

        String url = HttpUrl.loadPackMaterialPosition();

        String result = client.getWithStringReturned(url);


        RemoteData<PackMaterialPosition> remoteData = invokeByReflect(result, PackMaterialPosition.class);

        return remoteData;
    }

    /**
     * 保存材料列表
     *
     * @param materials
     */
    public RemoteData<Void> saveMaterials(List<Material> materials) throws HdException {

        String url = HttpUrl.saveMaterials();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(materials));


        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;

    }


    /**
     * 根据材料编码列表 查询材料列表
     *
     * @param codes
     * @return
     */
    public RemoteData<Material> readMaterialListByCodeEquals(List<String> codes) throws HdException {
        String url = HttpUrl.loadMaterialListByCodeEquals();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(codes));


        RemoteData<Material> remoteData = invokeByReflect(result, Material.class);

        return remoteData;
    }

    /**
     * 根据材料名称 查询材料列表
     *
     * @param names
     * @return
     */
    public RemoteData<Material> readMaterialListByNameEquals(List<String> names) throws HdException {
        String url = HttpUrl.loadMaterialListByNameEquals();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(names));

        RemoteData<Material> remoteData = invokeByReflect(result, Material.class);

        return remoteData;
    }


    /**
     * 读取包装列表
     *
     * @return
     */
    public RemoteData<Pack> readPacks() throws HdException {

        String url = HttpUrl.loadPacks();

        String result = client.getWithStringReturned(url);


        RemoteData<Pack> remoteData = invokeByReflect(result, Pack.class);

        return remoteData;
    }


    /**
     * 读取材料分类
     *
     * @return
     */
    public RemoteData<MaterialClass> readMaterialClasses() throws HdException {

        String url = HttpUrl.loadMaterialClasses();

        String result = client.getWithStringReturned(url);

        RemoteData<MaterialClass> remoteData = invokeByReflect(result, MaterialClass.class);

        return remoteData;
    }


    /**
     * 材料分类保存
     *
     * @return
     */
    public RemoteData<MaterialClass> saveMaterialClasses(List<MaterialClass> materialClasses) throws HdException {

        String url = HttpUrl.saveMaterialClasses();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(materialClasses));

        RemoteData<MaterialClass> remoteData = invokeByReflect(result, MaterialClass.class);

        return remoteData;
    }


    /**
     * 读取材料类型
     *
     * @return
     */
    public RemoteData<MaterialType> readMaterialTypes() throws HdException {

        String url = HttpUrl.loadMaterialTypes();

        String result = client.getWithStringReturned(url);


        RemoteData<MaterialType> remoteData = invokeByReflect(result, MaterialType.class);

        return remoteData;
    }


    /**
     * 保存材料信息
     *
     * @return
     */
    public RemoteData<Material> saveMaterial(Material material) throws HdException {

        String url = HttpUrl.saveMaterial();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(material));

        RemoteData<Material> remoteData = invokeByReflect(result, Material.class);

        return remoteData;
    }

    /**
     * 翻新产品
     *
     * @param id
     * @return
     */
    public RemoteData<ProductDetail> copyProductDetail(long id, String productName, String version, boolean copyPicture) throws HdException {

        String url = HttpUrl.copyProductDetail(id, productName, version, copyPicture);

        String result = client.postWithStringReturned(url, null);

        RemoteData<ProductDetail> remoteData = invokeByReflect(result, ProductDetail.class);

        return remoteData;

    }

    /**
     * 逻辑删除产品数据
     *
     * @return
     */
    public RemoteData<Void> deleteProductLogic(long productId) throws HdException {
        String url = HttpUrl.deleteProductLogic(productId);

        String result = client.postWithStringReturned(url, null);

        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;

    }

    public RemoteData<Void> deleteMaterialLogic(long materialId) throws HdException {
        String url = HttpUrl.deleteMaterialLogic(materialId);

        String result = client.postWithStringReturned(url, null);


        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }


    /**
     * 同步更新材料图片
     *
     * @return
     */
    public RemoteData<Void> syncMaterialPhoto() throws HdException {
        String url = HttpUrl.syncMaterialPhoto();

        String result = client.getWithStringReturned(url);


        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }

    /**
     * 同步更新产品图片
     *
     * @return
     */
    public RemoteData<Void> syncProductPhoto() throws HdException {

        String url = HttpUrl.syncProductPhoto();
        String result = client.getWithStringReturned(url);

        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }


    /**
     * 上传产品图片
     *
     * @param file
     * @return
     * @throws HdException
     */
    public RemoteData<Void> uploadProductPicture(File file, boolean doesOverride) throws HdException {


        String productName = file.getName();
        String url = HttpUrl.uploadProductPicture(productName, doesOverride);


        String result = client.uploadWidthStringReturned(url, file);

        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }

    /**
     * 上传材料图片
     *
     * @param file
     * @return
     * @throws HdException
     */
    public RemoteData<Void> uploadMaterialPicture(File file, boolean doesOverride) throws HdException {
        String materialName = file.getName();
        String url = HttpUrl.uploadMaterialPicture(materialName, doesOverride);

        String result = client.uploadWidthStringReturned(url, file);

        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }


    /**
     * 读取工序列表
     *
     * @return
     */
    public RemoteData<ProductProcess> loadProductProcess() throws HdException {

        String url = HttpUrl.loadProductProcess();

        String result = client.getWithStringReturned(url);

        RemoteData<ProductProcess> remoteData = invokeByReflect(result, ProductProcess.class);

        return remoteData;
    }


    /**
     * 保存工序列表数据
     *
     * @param datas
     * @return
     */
    public RemoteData<ProductProcess> saveProductProcess(List<ProductProcess> datas) throws HdException {
        String url = HttpUrl.saveProductProcesses();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(datas));

        RemoteData<ProductProcess> remoteData = invokeByReflect(result, ProductProcess.class);


        return remoteData;
    }


    /**
     * 同步erp材料
     *
     * @return
     */
    public RemoteData<Void> syncErpMaterial() throws HdException {
        String url = HttpUrl.syncErpMaterial();

        String result = client.getWithStringReturned(url);


        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }


    /**
     * 读取报价历史记录
     *
     * @param searchValue
     * @param salesmanId  业务员id  查询指定业务员的报价单  -1 标示查询所有
     * @return
     */
    public RemoteData<Quotation> loadQuotation(String searchValue, long salesmanId, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.loadQuotation(searchValue, salesmanId, pageIndex, pageSize);

        String result = client.getWithStringReturned(url);


        RemoteData<Quotation> remoteData = invokeByReflect(result, Quotation.class);


        return remoteData;

    }


    /**
     * 读取报价详细
     *
     * @param id
     * @return
     */
    public RemoteData<QuotationDetail> loadQuotationDetail(long id) throws HdException {

        String url = HttpUrl.loadQuotationDetail(id);

        String result = client.getWithStringReturned(url);
        RemoteData<QuotationDetail> remoteData = invokeByReflect(result, QuotationDetail.class);


        return remoteData;

    }

    /**
     * 保存报价详情数据
     *
     * @param detail
     * @return
     */
    public RemoteData<QuotationDetail> saveQuotationDetail(QuotationDetail detail) throws HdException {

        String url = HttpUrl.saveQuotationDetail();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(detail));

        RemoteData<QuotationDetail> remoteData = invokeByReflect(result, QuotationDetail.class);

        return remoteData;

    }


    /**
     * 保存并审核报价详情数据
     *
     * @param detail
     * @return
     */
    public RemoteData<QuotationDetail> saveAndVerifyQuotationDetail(QuotationDetail detail) throws HdException {

        String url = HttpUrl.saveAndVerifyQuotationDetail();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(detail));

        RemoteData<QuotationDetail> remoteData = invokeByReflect(result, QuotationDetail.class);

        return remoteData;

    }


    /**
     * 撤销报价单审核
     *
     * @param quotationId
     * @return
     */
    public RemoteData<Void> unVerifyQuotation(long quotationId) throws HdException {


        String url = HttpUrl.unVerifyQuotation(quotationId);

        String result = client.postWithStringReturned(url, null);


        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;

    }


    /**
     * 删除报价单
     *
     * @param quotationId
     * @return
     */
    public RemoteData<Void> deleteQuotationLogic(long quotationId) throws HdException {


        String url = HttpUrl.deleteQuotationLogic(quotationId);

        String result = client.postWithStringReturned(url, null);

        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;

    }


    /**
     * 读取用户权限
     *
     * @param id
     * @return
     */
    public RemoteData<Authority> readAuthorityByUser(long id) throws HdException {

        String url = HttpUrl.loadAuthorityByUser(id);

        String result = client.getWithStringReturned(url);

        RemoteData<Authority> remoteData = invokeByReflect(result, Authority.class);

        return remoteData;

    }


    /**
     * 保存用户权限
     */
    public RemoteData<Authority> saveAuthorities(long userId, List<Authority> authorities) throws HdException {

        String url = HttpUrl.saveAuthorities(userId);

        String result = client.postWithStringReturned(url, GsonUtils.toJson(authorities));


        RemoteData<Authority> remoteData = invokeByReflect(result, Authority.class);

        return remoteData;

    }


    /**
     * @param product2Name 货号名称
     * @param productId    排除的id
     * @return
     */
    public RemoteData<Product> readSameNameProductList(String product2Name, long productId) throws HdException {

        String url = HttpUrl.readSameNameProductList(product2Name, productId);

        String result = client.getWithStringReturned(url);

        RemoteData<Product> remoteData = invokeByReflect(result, Product.class);

        return remoteData;
    }


    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     * @throws HdException
     */
    public RemoteData<User> login(String userName, String password) throws HdException {

        String url = HttpUrl.login();
        User user = new User();
        user.name = userName;
        //  user.password =  password ;
        user.password = DigestUtils.md5(password);

        String result = client.postWithStringReturned(url, GsonUtils.toJson(user));

        RemoteData<User> remoteData = invokeByReflect(result, User.class);

        return remoteData;
    }


    /**
     * 获取初始数据
     *
     * @param user
     */
    public RemoteData<BufferData> loadInitData(User user) throws HdException {

        String url = HttpUrl.loadInitData();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(user));


        RemoteData<BufferData> remoteData = invokeByReflect(result, BufferData.class);

        return remoteData;

    }


    /**
     * 修改密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws HdException
     */
    public RemoteData<Void> updatePassword(long userId, String oldPassword, String newPassword) throws HdException {


        String url = HttpUrl.updatePassword();
        String[] body = new String[3];
        body[0] = String.valueOf(userId);
        body[1] = oldPassword;
        body[2] = newPassword;

        String result = client.postWithStringReturned(url, GsonUtils.toJson(body));


        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);


        return remoteData;
    }


    /**
     * 读取客户端版本信息
     *
     * @return
     */
    public RemoteData<AppVersion> readAppVersion() throws HdException {

        String url = HttpUrl.readAppVersion();

        String result = client.getWithStringReturned(url);


        RemoteData<AppVersion> remoteData = invokeByReflect(result, AppVersion.class);

        return remoteData;
    }

    /**
     * 读取报价权限列表
     *
     * @return
     */
    public RemoteData<QuoteAuth> readQuoteAuth() throws HdException {

        String url = HttpUrl.readQuoteAuth();

        String result = client.getWithStringReturned(url);


        RemoteData<QuoteAuth> remoteData = invokeByReflect(result, QuoteAuth.class);

        return remoteData;
    }


    /**
     * 保存报价权限信息
     *
     * @param datas
     * @return
     */
    public RemoteData<QuoteAuth> saveQuoteAuthList(List<QuoteAuth> datas) throws HdException {

        String url = HttpUrl.saveQuoteAuthList();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(datas));


        RemoteData<QuoteAuth> remoteData = invokeByReflect(result, QuoteAuth.class);

        return remoteData;
    }


    /**
     * 读取历史操作记录
     *
     * @param className
     * @param id
     */
    public RemoteData<OperationLog> readOperationLog(String className, long id) throws HdException {
        String url = HttpUrl.readOperationLog(className, id);

        String result = client.getWithStringReturned(url);

        RemoteData<OperationLog> remoteData = invokeByReflect(result, OperationLog.class);


        return remoteData;
    }


    /**
     * 读取被删除的产品列表
     *
     * @param value
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public RemoteData<ProductDelete> loadDeleteProducts(String value, int pageIndex, int pageSize) throws HdException {
        String url = HttpUrl.loadDeleteProducts(value, pageIndex, pageSize);

        String result = client.getWithStringReturned(url);

        RemoteData<ProductDelete> remoteData = invokeByReflect(result, ProductDelete.class);

        return remoteData;
    }


    /**
     * 查询被删除的产品信息详情
     *
     * @param deleteProductId
     * @return
     */
    public RemoteData<ProductDetail> productDetailOfDeleted(long deleteProductId) throws HdException {
        String url = HttpUrl.productDetailDelete(deleteProductId);

        String result = client.getWithStringReturned(url);


        RemoteData<ProductDetail> remoteData = invokeByReflect(result, ProductDetail.class);


        return remoteData;

    }

    /**
     * 查询被删除的报价信息详情
     *
     * @param deleteQuotationId
     * @return
     */
    public RemoteData<QuotationDetail> quotationDetailOfDeleted(long deleteQuotationId) throws HdException {
        String url = HttpUrl.quotationDetailDelete(deleteQuotationId);

        String result = client.getWithStringReturned(url);


        RemoteData<QuotationDetail> remoteData = invokeByReflect(result, QuotationDetail.class);

        return remoteData;

    }

    /**
     * 恢复被删除的产品 信息
     *
     * @param deleteProductId
     * @return
     */
    public RemoteData<ProductDetail> resumeDeleteProduct(long deleteProductId) throws HdException {

        String url = HttpUrl.resumeDeleteProduct(deleteProductId);

        String result = client.getWithStringReturned(url);


        RemoteData<ProductDetail> remoteData = invokeByReflect(result, ProductDetail.class);

        return remoteData;
    }


    /**
     * 读取被删除的报价列表
     *
     * @param value
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws HdException
     */
    public RemoteData<QuotationDelete> loadDeleteQuotations(String value, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.loadDeleteQuotations(value, pageIndex, pageSize);

        String result = client.getWithStringReturned(url);


        RemoteData<QuotationDelete> remoteData = invokeByReflect(result, QuotationDelete.class);

        return remoteData;
    }


    /**
     * 恢复被删除的报价单
     *
     * @param deleteQuotationId
     * @return
     */
    public RemoteData<QuotationDetail> resumeDeleteQuotation(long deleteQuotationId) throws HdException {
        String url = HttpUrl.resumeDeleteQuotation(deleteQuotationId);

        String result = client.getWithStringReturned(url);

        RemoteData<QuotationDetail> remoteData = invokeByReflect(result, QuotationDetail.class);

        return remoteData;
    }


    /**
     * 包装材料类别列表
     *
     * @param datas
     */
    public RemoteData<PackMaterialClass> savePackMaterialClass(List<PackMaterialClass> datas) throws HdException {


        String url = HttpUrl.savePackMaterialClass();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(datas));


        RemoteData<PackMaterialClass> remoteData = invokeByReflect(result, PackMaterialClass.class);

        return remoteData;

    }


    /**
     * 设置全局参数
     *
     * @param globalData
     * @return
     */
    public RemoteData<GlobalData> setGlobalData(GlobalData globalData) throws HdException {
        String url = HttpUrl.setGlobalData();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(globalData));

        RemoteData<GlobalData> remoteData = invokeByReflect(result, GlobalData.class);


        return remoteData;
    }


    /**
     * 读取指定ids 的产品数据
     *
     * @param productIds
     * @return
     */
    public RemoteData<Product> readProductsByIds(Set<Long> productIds) throws HdException {

        String url = HttpUrl.readProductsByIds();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(productIds));

        RemoteData<Product> remoteData = invokeByReflect(result, Product.class);

        return remoteData;

    }


    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    public RemoteData<String> uploadTempPicture(File file) throws HdException {


        String url = HttpUrl.uploadTempPicture();

        String result = client.uploadWidthStringReturned(url, file);

        RemoteData<String> remoteData = invokeByReflect(result, String.class);

        return remoteData;


    }


    /**
     * 根据产品读取其咸康数据
     *
     * @param productId
     * @return
     */
    public RemoteData<Xiankang> loadXiankangDataByProductId(long productId) throws HdException {


        String url = HttpUrl.loadXiankangDataByProductId(productId);

        String result = client.getWithStringReturned(url);

        RemoteData<Xiankang> remoteData = invokeByReflect(result, Xiankang.class);

        return remoteData;


    }


    /**
     * 更新咸康数据  数据库结构变动，产生的调整接口 临时使用
     *
     * @return
     */
    public RemoteData<Void> updateXiankang() throws HdException {


        String url = HttpUrl.updateXiankang();

        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;


    }

    /**
     * 读取定时任务列表
     *
     * @return
     * @throws HdException
     */
    public RemoteData<HdTask> loadTaskList() throws HdException {

        String url = HttpUrl.loadTaskList();
        String result = client.getWithStringReturned(url);
        RemoteData<HdTask> remoteData = invokeByReflect(result, HdTask.class);

        return remoteData;
    }


    /**
     * 添加定时任务 并返回现有任务列表
     *
     * @return
     * @throws HdException
     */
    public RemoteData<HdTask> addHdTask(HdTask task) throws HdException {

        String url = HttpUrl.addHdTask();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(task));

        RemoteData<HdTask> remoteData = invokeByReflect(result, HdTask.class);

        return remoteData;
    }


    /**
     * 删除定时任务 并返回现有任务列表
     *
     * @return
     * @throws HdException
     */
    public RemoteData<HdTask> deleteHdTask(long taskId) throws HdException {

        String url = HttpUrl.deleteHdTask(taskId);
        String result = client.postWithStringReturned(url, null);
        RemoteData<HdTask> remoteData = invokeByReflect(result, HdTask.class);

        return remoteData;
    }

    /**
     * 返回任务执行记录列表
     *
     * @return
     */
    public RemoteData<HdTaskLog> loadTaskLogList(long taskId) throws HdException {

        String url = HttpUrl.loadHdTaskLogList(taskId);
        String result = client.getWithStringReturned(url);
        RemoteData<HdTaskLog> remoteData = invokeByReflect(result, HdTaskLog.class);

        return remoteData;
    }

    /**
     * 读取包装材料录入模板
     *
     * @return
     */
    public RemoteData<ProductMaterial> readProductPackTemplate() throws HdException {

        String url = HttpUrl.readProductPackTemplate();
        String result = client.getWithStringReturned(url);
        RemoteData<ProductMaterial> remoteData = invokeByReflect(result, ProductMaterial.class);
        return remoteData;
    }

    public RemoteData<ProductMaterial> saveProductPackMaterialTemplate(List<ProductMaterial> datas) throws HdException {


        String url = HttpUrl.saveProductPackMaterialTemplate();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(datas));
        RemoteData<ProductMaterial> remoteData = invokeByReflect(result, ProductMaterial.class);
        return remoteData;

    }

    /**
     * 随机读取产品列表。
     *
     * @param productNames 产品名称，以逗号隔开。
     * @param withCopy
     * @return
     * @throws HdException
     */
    public RemoteData<Product> loadProductListByNameRandom(String productNames, boolean withCopy) throws HdException {

        String url = HttpUrl.loadProductListByNameRandom(productNames, withCopy);
        String result = client.getWithStringReturned(url);
        RemoteData<Product> productRemoteData = invokeByReflect(result, Product.class);

        return productRemoteData;

    }

    /**
     * 读取订单列表
     *
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public RemoteData<ErpOrder> getOrderList(String key, long salesId, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.loadOrderList(key, salesId, pageIndex, pageSize);
        String result = client.getWithStringReturned(url);
        RemoteData<ErpOrder> remoteData = invokeByReflect(result, ErpOrder.class);
        return remoteData;
    }


    /**
     * 读取订单列表
     *
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public RemoteData<ErpStockOut> getStockOutList(String key, long salesId, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.loadStockOutList(key, salesId, pageIndex, pageSize);
        String result = client.getWithStringReturned(url);
        RemoteData<ErpStockOut> remoteData = invokeByReflect(result, ErpStockOut.class);
        return remoteData;
    }

    /**
     * 读取订单明细列表
     *
     * @param or_no
     * @return
     * @throws HdException
     */
    public RemoteData<ErpOrderItem> getOrderItemList(String or_no) throws HdException {
        String url = HttpUrl.loadOrderItemList(or_no);
        String result = client.getWithStringReturned(url);
        RemoteData<ErpOrderItem> remoteData = invokeByReflect(result, ErpOrderItem.class);
        return remoteData;
    }

    /**
     * 模糊查询订单明细列表
     *
     * @param key
     * @return
     * @throws HdException
     */
    public RemoteData<ErpOrderItem> searchOrderItemList(String key, final int pageIndex, final int pageSize) throws HdException {
        String url = HttpUrl.searchOrderItemList(key, pageIndex, pageSize);
        String result = client.getWithStringReturned(url);
        RemoteData<ErpOrderItem> remoteData = invokeByReflect(result, ErpOrderItem.class);
        return remoteData;
    }


    /**
     * 根据产品no 读取产品详情
     *
     * @param prdNo
     * @return
     */
    public RemoteData<ProductDetail> loadProductDetailByPrdNo(String prdNo) throws HdException {
        String url = HttpUrl.loadProductDetailByPrdNo(prdNo);
        String result = client.postWithStringReturned(url, null);
        RemoteData<ProductDetail> productRemoteData = invokeByReflect(result, ProductDetail.class);
        return productRemoteData;
    }

    /**
     * 根据出库单号读取出库详情
     *
     * @param ck_no
     * @return
     */
    public RemoteData<ErpStockOutDetail> getStockOutDetail(String ck_no) throws HdException {
        String url = HttpUrl.getStockOutDetail(ck_no);
        String result = client.getWithStringReturned(url);
        RemoteData<ErpStockOutDetail> productRemoteData = invokeByReflect(result, ErpStockOutDetail.class);
        return productRemoteData;
    }

    /**
     * 保存出库数据
     *
     * @param stockOutDetail
     * @return
     * @throws HdException
     */
    public RemoteData<ErpStockOutDetail> saveStockOutDetail(ErpStockOutDetail stockOutDetail) throws HdException {


        String url = HttpUrl.saveStockOutDetail();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(stockOutDetail));
        RemoteData<ErpStockOutDetail> productRemoteData = invokeByReflect(result, ErpStockOutDetail.class);
        return productRemoteData;

    }

    /**
     * 获取订单详情
     *
     * @param os_no
     * @return
     * @throws HdException
     */
    public RemoteData<ErpOrderDetail> getOrderDetail(String os_no) throws HdException {
        String url = HttpUrl.getOrderDetail(os_no);
        String result = client.getWithStringReturned(url);
        RemoteData<ErpOrderDetail> remoteData = invokeByReflect(result, ErpOrderDetail.class);
        return remoteData;
    }

    /**
     * 保存订单详情
     *
     * @param orderDetail
     * @return
     */
    public RemoteData<ErpOrderDetail> saveOrderDetail(ErpOrderDetail orderDetail) throws HdException {
        String url = HttpUrl.saveOrderDetail();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(orderDetail));
        RemoteData<ErpOrderDetail> productRemoteData = invokeByReflect(result, ErpOrderDetail.class);
        return productRemoteData;

    }

    /**
     * 读取出库单权限
     *
     * @return
     */
    public RemoteData<StockOutAuth> readStockOutAuth() throws HdException {

        String url = HttpUrl.readStockOutAuth();

        String result = client.getWithStringReturned(url);


        RemoteData<StockOutAuth> remoteData = invokeByReflect(result, StockOutAuth.class);

        return remoteData;

    }

    /**
     * 读取订单单权限
     *
     * @return
     */
    public RemoteData<OrderAuth> readOrderAuth() throws HdException {
        String url = HttpUrl.readOrderAuth();

        String result = client.getWithStringReturned(url);


        RemoteData<OrderAuth> remoteData = invokeByReflect(result, OrderAuth.class);

        return remoteData;
    }

    /**
     * 保存出库权限
     *
     * @param stockOutAuths
     * @return
     */
    public RemoteData<StockOutAuth> saveStockOutAuth(List<StockOutAuth> stockOutAuths) throws HdException {

        String url = HttpUrl.saveStockOutAuthList();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(stockOutAuths));


        RemoteData<StockOutAuth> remoteData = invokeByReflect(result, StockOutAuth.class);

        return remoteData;
    }

    /**
     * 保存订单权限
     *
     * @param orderAuths
     * @return
     */
    public RemoteData<OrderAuth> saveOrderAuth(List<OrderAuth> orderAuths) throws HdException {

        String url = HttpUrl.saveOrderAuthList();

        String result = client.postWithStringReturned(url, GsonUtils.toJson(orderAuths));


        RemoteData<OrderAuth> remoteData = invokeByReflect(result, OrderAuth.class);

        return remoteData;

    }

    /**
     * 读取订单报表  验货日期
     *
     * @param userId
     * @param dateStart
     * @param dateEnd
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public RemoteData<OrderReportItem> getOrderReport(long userId, String dateStart, String dateEnd, int pageIndex, int pageSize) throws HdException {


        String url = HttpUrl.getOrderReportByCheckDate(userId, dateStart, dateEnd, pageIndex, pageSize);
        String result = client.getWithStringReturned(url);
        RemoteData<OrderReportItem> remoteData = invokeByReflect(result, OrderReportItem.class);
        return remoteData;


    }

    /**
     * 获取生产流程表
     *
     * @return
     * @throws HdException
     */
    public RemoteData<WorkFlow> getWorkFlowList() throws HdException {
        String url = HttpUrl.getWorkFlowList();
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlow> remoteData = invokeByReflect(result, WorkFlow.class);
        return remoteData;
    }

    /**
     * 保存生产流程表
     *
     * @param workFlows
     * @return
     */
    public RemoteData<WorkFlow> saveWorkFlowList(List<WorkFlow> workFlows) throws HdException {

        String url = HttpUrl.saveWorkFlowList();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(workFlows));
        RemoteData<WorkFlow> remoteData = invokeByReflect(result, WorkFlow.class);
        return remoteData;
    }

    /**
     * 启动订单生产流程跟踪
     *
     * @param os_no
     * @return
     */
    public RemoteData<Void> startOrderTrack(String os_no) throws HdException {
        String url = HttpUrl.startOrderTrack(os_no);
        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;

    }

    public RemoteData<ErpOrderItemProcess> getUnCompleteOrderWorkFlowReport() throws HdException {

        String url = HttpUrl.getUnCompleteOrderWorkFlowReport();
        String result = client.getWithStringReturned(url);
        RemoteData<ErpOrderItemProcess> remoteData = invokeByReflect(result, ErpOrderItemProcess.class);
        return remoteData;

    }

    public RemoteData<ErpOrderItem> getOrderWorkFlowReport(String key, int pageIndex, int pageSize) throws HdException {
        String url = HttpUrl.getOrderWorkFlowReport(key, pageIndex, pageSize);
        String result = client.getWithStringReturned(url);
        RemoteData<ErpOrderItem> remoteData = invokeByReflect(result, ErpOrderItem.class);
        return remoteData;
    }

    public RemoteData<StockSubmit> getStockInAndSubmitList(String key, String startDate, String endDate) throws HdException {
        String url = HttpUrl.getStockInAndSubmitList(key, startDate, endDate);
        String result = client.getWithStringReturned(url);
        RemoteData<StockSubmit> remoteData = invokeByReflect(result, StockSubmit.class);
        return remoteData;
    }

    public RemoteData<StockSubmit> getStockXiaokuItemList(String ps_no) throws HdException {
        String url = HttpUrl.getStockXiaokuItemList(ps_no);
        String result = client.getWithStringReturned(url);
        RemoteData<StockSubmit> remoteData = invokeByReflect(result, StockSubmit.class);
        return remoteData;
    }

    public RemoteData<StockSubmit> getStockXiaokuItemList(final String key, final String dateStart, final String dateEnd) throws HdException {
        String url = HttpUrl.getStockXiaokuItemList(key, dateStart, dateEnd);
        String result = client.getWithStringReturned(url);
        RemoteData<StockSubmit> remoteData = invokeByReflect(result, StockSubmit.class);
        return remoteData;
    }

    public RemoteData<StockXiaoku> getStockXiaokuList(String key, int pageIndex, int pageSize) throws HdException {
        String url = HttpUrl.getStockXiaokuList(key, pageIndex, pageSize);
        String result = client.getWithStringReturned(url);
        RemoteData<StockXiaoku> remoteData = invokeByReflect(result, StockXiaoku.class);
        return remoteData;
    }


    public RemoteData<WorkFlowSubType> getWorkFlowSubTypes() throws HdException {
        String url = HttpUrl.getWorkFlowSubTypes();
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlowSubType> remoteData = invokeByReflect(result, WorkFlowSubType.class);
        return remoteData;
    }

    public RemoteData<WorkFlowProduct> getWorkFlowOfProduct(long productId) throws HdException {

        String url = HttpUrl.getWorkFlowOfProduct(productId);
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlowProduct> remoteData = invokeByReflect(result, WorkFlowProduct.class);
        return remoteData;
    }

    public RemoteData<WorkFlowProduct> saveWorkFlowProduct(WorkFlowProduct workFlowProduct) throws HdException {

        String url = HttpUrl.saveWorkFlowProduct();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(workFlowProduct));
        RemoteData<WorkFlowProduct> remoteData = invokeByReflect(result, WorkFlowProduct.class);
        return remoteData;
    }

    /**
     * 获取外厂数据
     *
     * @return
     * @throws HdException
     */
    public RemoteData<OutFactory> getOutFactories() throws HdException {
        String url = HttpUrl.getOutFactories();
        String result = client.getWithStringReturned(url);
        RemoteData<OutFactory> remoteData = invokeByReflect(result, OutFactory.class);
        return remoteData;
    }

    /**
     * 保存外厂数据
     *
     * @return
     * @throws HdException
     */
    public RemoteData<OutFactory> saveOutFactories(List<OutFactory> datas) throws HdException {
        String url = HttpUrl.saveOutFactories();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(datas));
        RemoteData<OutFactory> remoteData = invokeByReflect(result, OutFactory.class);
        return remoteData;
    }

    /**
     * 启动订单生产流程
     *
     * @param orderItemWorkFlow
     * @return
     */
    public RemoteData<OrderItemWorkFlow> startOrderItemWorkFlow(OrderItemWorkFlow orderItemWorkFlow) throws HdException {
        String url = HttpUrl.startOrderItemWorkFlow();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(orderItemWorkFlow));
        RemoteData<OrderItemWorkFlow> remoteData = invokeByReflect(result, OrderItemWorkFlow.class);
        return remoteData;
    }

    public RemoteData<ErpOrderItemProcess> getOrderItemWorkFlowState(long orderItemId) throws HdException {
        String url = HttpUrl.getOrderItemWorkFlowState(orderItemId);
        String result = client.getWithStringReturned(url);
        RemoteData<ErpOrderItemProcess> remoteData = invokeByReflect(result, ErpOrderItemProcess.class);
        return remoteData;
    }

    /**
     * 保存排厂类型列表
     *
     * @param datas
     * @return
     */
    public RemoteData<WorkFlowSubType> saveWorkFlowSubTypeList(List<WorkFlowSubType> datas) throws HdException {


        String url = HttpUrl.saveWorkFlowSubTypeList();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(datas));
        RemoteData<WorkFlowSubType> remoteData = invokeByReflect(result, WorkFlowSubType.class);
        return remoteData;


    }

    /**
     * 修复产品缩略图图片
     *
     * @param productId
     * @return
     * @throws HdException
     */
    public RemoteData<Void> correctProductThumbnail(long productId) throws HdException {

        String url = HttpUrl.correctProductThumbnail(productId);
        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<OrderItemWorkFlow> getOrderItemWorkFlow(long orderItemId) throws HdException {

        String url = HttpUrl.getOrderItemWorkFlow(orderItemId);
        String result = client.getWithStringReturned(url);
        RemoteData<OrderItemWorkFlow> remoteData = invokeByReflect(result, OrderItemWorkFlow.class);
        return remoteData;
    }

    /**
     * 查询指令单状态
     *
     * @param osName
     * @param startDate
     * @param endDate
     * @return
     * @throws HdException
     */
    public RemoteData<Zhilingdan> searchZhiling(String osName, String startDate, String endDate) throws HdException {

        String url = HttpUrl.searchZhiling(osName, startDate, endDate);
        String result = client.getWithStringReturned(url);
        RemoteData<Zhilingdan> remoteData = invokeByReflect(result, Zhilingdan.class);
        return remoteData;
    }

    public RemoteData<Void> synchronizeProductOnEquationUpdate() throws HdException {

        String url = HttpUrl.synchronizeProductOnEquationUpdate();
        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    /**
     * 更新材料分类
     *
     * @param materialClass
     * @return
     */
    public RemoteData<MaterialClass> updateMaterialClass(MaterialClass materialClass) throws HdException {
        String url = HttpUrl.updateMaterialClass();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(materialClass));
        RemoteData<MaterialClass> remoteData = invokeByReflect(result, MaterialClass.class);
        return remoteData;
    }

    public RemoteData<Void> deleteMaterialClass(long materialClassId) throws HdException {
        String url = HttpUrl.deleteMaterialClass(materialClassId);
        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<WorkFlowWorker> loadWorkFlowWorks() throws HdException {
        String url = HttpUrl.loadWorkFlowWorks();
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlowWorker> remoteData = invokeByReflect(result, WorkFlowWorker.class);
        return remoteData;
    }

    public RemoteData<WorkFlowWorker> saveWorkFlowWorker(WorkFlowWorker workFlowWorker) throws HdException {
        String url = HttpUrl.saveWorkFlowWorker();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(workFlowWorker));
        RemoteData<WorkFlowWorker> remoteData = invokeByReflect(result, WorkFlowWorker.class);
        return remoteData;
    }

    public RemoteData<WorkFlowArranger> saveWorkFlowArranger(WorkFlowArranger workFlowArranger) throws HdException {
        String url = HttpUrl.saveWorkFlowArranger();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(workFlowArranger));
        RemoteData<WorkFlowArranger> remoteData = invokeByReflect(result, WorkFlowArranger.class);
        return remoteData;
    }

    public RemoteData<WorkFlowArranger> getWorkFlowArrangers() throws HdException {

        String url = HttpUrl.getWorkFlowArrangers();
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlowArranger> remoteData = invokeByReflect(result, WorkFlowArranger.class);
        return remoteData;
    }

    public RemoteData<Void> deleteWorkFlowWorker(long workFlowWorkerId) throws HdException {
        String url = HttpUrl.deleteWorkFlowWorker(workFlowWorkerId);
        String result = client.postWithStringReturned(url, null);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<Void> deleteWorkFlowArranger(long workFlowArrangerId) throws HdException {
        String url = HttpUrl.deleteWorkFlowArranger(workFlowArrangerId);
        String result = client.postWithStringReturned(url, null);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }


    /**
     * 同步所有产品关联的图片
     *
     * @return
     */

    public RemoteData<Void> syncRelateProductPicture() throws HdException {

        String url = HttpUrl.syncRelateProductPicture();
        String result = client.postWithStringReturned(url, null);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<OrderItemWorkFlow> cancelOrderWorkFlow(long orderItemWorkFlowId) throws HdException {
        String url = HttpUrl.cancelOrderWorkFlow(orderItemWorkFlowId);
        String result = client.getWithStringReturned(url);
        RemoteData<OrderItemWorkFlow> remoteData = invokeByReflect(result, OrderItemWorkFlow.class);
        return remoteData;
    }

    public RemoteData<WorkFlowEvent> getWorkFLowEventList() throws HdException {
        String url = HttpUrl.loadWorkFlowEvents();
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlowEvent> remoteData = invokeByReflect(result, WorkFlowEvent.class);
        return remoteData;

    }

    public RemoteData<WorkFlowEventWorker> getWorkFLowEventWorkerList() throws HdException {
        String url = HttpUrl.loadWorkFlowEventWorkers();
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlowEventWorker> remoteData = invokeByReflect(result, WorkFlowEventWorker.class);
        return remoteData;

    }

    public RemoteData<ErpOrderItemProcess> getErpOrderItemProcess(String osNo, String prdNo) throws HdException {
        String url = HttpUrl.getErpOrderItemProcess(osNo, prdNo);
        String result = client.getWithStringReturned(url);
        RemoteData<ErpOrderItemProcess> remoteData = invokeByReflect(result, ErpOrderItemProcess.class);
        return remoteData;

    }

    public RemoteData<ErpWorkFlowReport> getErpOrderItemReport(String osNo, String prdNo) throws HdException {
        String url = HttpUrl.getErpOrderItemReport(osNo, prdNo);
        String result = client.getWithStringReturned(url);
        RemoteData<ErpWorkFlowReport> remoteData = invokeByReflect(result, ErpWorkFlowReport.class);
        return remoteData;

    }

    public RemoteData<WorkFlowArea> getWorkFlowAreas() throws HdException {
        String url = HttpUrl.getWorkFlowArea();
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlowArea> remoteData = invokeByReflect(result, WorkFlowArea.class);
        return remoteData;

    }

    public RemoteData<WorkFlowArea> saveWorkFlowArea(WorkFlowArea data) throws HdException {
        String url = HttpUrl.saveWorkFlowArea();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(data));
        RemoteData<WorkFlowArea> remoteData = invokeByReflect(result, WorkFlowArea.class);
        return remoteData;
    }

    public RemoteData<WorkFlowArea> deleteWorkFlowArea(long id) throws HdException {
        String url = HttpUrl.deleteWorkFlowArea(id);
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlowArea> remoteData = invokeByReflect(result, WorkFlowArea.class);
        return remoteData;
    }

    /**
     * 上传订单唛头文件
     *
     * @param os_no
     * @param file
     * @return
     * @throws HdException
     */
    public RemoteData<String> updateMaitouFile(String os_no, File file) throws HdException {
        String url = HttpUrl.uploadMaitouFile(os_no);
        String result = client.uploadWidthStringReturned(url, file);
        RemoteData<String> remoteData = invokeByReflect(result, String.class);
        return remoteData;

    }

    public RemoteData<WorkFlowTimeLimit> getWorkFlowLimit() throws HdException {
        String url = HttpUrl.getWorkFlowLimit();
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlowTimeLimit> remoteData = invokeByReflect(result, WorkFlowTimeLimit.class);
        return remoteData;
    }

    public RemoteData<Sub_workflow_state> searchErpSubWorkFlow(String key, String dateStart, String dateEnd) throws HdException {
        String url = HttpUrl.searchErpSubWorkFlow(key, dateStart, dateEnd);
        String result = client.getWithStringReturned(url);
        RemoteData<Sub_workflow_state> remoteData = invokeByReflect(result, Sub_workflow_state.class);
        return remoteData;
    }

    public RemoteData<Void> saveWorkFlowLimit(List<WorkFlowTimeLimit> workFlowLimit, boolean updateCompletedOrderItem) throws HdException {
        String url = HttpUrl.saveWorkFlowLimit(updateCompletedOrderItem);
        String result = client.postWithStringReturned(url, GsonUtils.toJson(workFlowLimit));
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<Company> updateCompany(Company company) throws HdException {

        String url = HttpUrl.updateCompany();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(company));
        RemoteData<Company> remoteData = invokeByReflect(result, Company.class);
        return remoteData;

    }

    public RemoteData<com.giants3.hd.entity.app.Quotation> getAppQuotationList(String key,final String dateStart,final String dateEnd, final long userId, int pageIndex, int pageSize) throws HdException {

        String url = HttpUrl.getAppQuotationList(key,    dateStart,    dateEnd,     userId, pageIndex, pageSize);
        String result = client.getWithStringReturned(url);
        RemoteData<com.giants3.hd.entity.app.Quotation> remoteData = invokeByReflect(result, com.giants3.hd.entity.app.Quotation.class);
        return remoteData;
    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> getAppQuotationDetail(long quotationId, String qNumber) throws HdException {
        String url = HttpUrl.getAppQuotationDetail(quotationId, qNumber);
        String result = client.getWithStringReturned(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;
    }

    public RemoteData<Product> loadProductById(long[] productIds) throws HdException {

        String url = HttpUrl.loadProductByIds(productIds);
        String result = client.getWithStringReturned(url);
        RemoteData<Product> productRemoteData = invokeByReflect(result, Product.class);
        return productRemoteData;
    }

    public RemoteData<HdTask> updateTaskState(long taskId, int taskState) throws HdException {
        String url = HttpUrl.updateTaskState(taskId, taskState);
        String result = client.getWithStringReturned(url);
        RemoteData<HdTask> remoteData = invokeByReflect(result, HdTask.class);

        return remoteData;
    }

    public RemoteData<Void> executeHdTask(int taskType) throws HdException {
        String url = HttpUrl.executeHdTask(taskType);
        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);

        return remoteData;
    }

    public RemoteData<WorkFlowMessage> getWorkFlowMessageReport(String dateStart, String dateEnd, boolean unhandle, boolean overdue) throws HdException {


        String url = HttpUrl.getWorkFlowMessageReport(  dateStart,   dateEnd,   unhandle,   overdue);
        String result = client.getWithStringReturned(url);
        RemoteData<WorkFlowMessage> remoteData = invokeByReflect(result, WorkFlowMessage.class);

        return remoteData;

    }

    public RemoteData<AppQuoteAuth> saveAppQuoteAuthList(List<AppQuoteAuth> auths) throws HdException {

        String url = HttpUrl.saveAppQuoteAuthList();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(auths));
        RemoteData<AppQuoteAuth> remoteData = invokeByReflect(result, AppQuoteAuth.class);
        return remoteData;


    }

    public RemoteData<AppQuoteAuth> getAppQuoteAuthList() throws HdException {

        String url = HttpUrl.getAppQuoteAuthList();
        String result = client.getWithStringReturned(url);
        RemoteData<AppQuoteAuth> remoteData = invokeByReflect(result, AppQuoteAuth.class);

        return remoteData;


    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> newAppQuotation() throws HdException {

        String url = HttpUrl.createTempAppQuotation();
        String result = client.getWithStringReturned(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);

        return remoteData;

    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> addProductToAppQuotation(long quotationId, long productId) throws HdException {
        String url = HttpUrl.addProductToQuotation(quotationId, productId);
        String result = client.getWithStringReturned(url);
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;
    }

    public RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> saveAppQuotation(com.giants3.hd.noEntity.app.QuotationDetail quotationDetail) throws HdException {
        String url = HttpUrl.saveAppQuotation();
        String result = client.postWithStringReturned(url, GsonUtils.toJson(quotationDetail));
        RemoteData<com.giants3.hd.noEntity.app.QuotationDetail> remoteData = invokeByReflect(result, com.giants3.hd.noEntity.app.QuotationDetail.class);
        return remoteData;
    }

    public RemoteData<Void> deleteAppQuotation(long quotationId) throws HdException {
        String url = HttpUrl.deleteAppQuotation(quotationId);
        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<Void> printQuotationToFile(long quotationId, String filePath) throws HdException {

        String url = HttpUrl.printQuotation(quotationId);
        InputStream inputStream = client.openInputStream(url);
        try {
            IoUtils.copyAllBytes(inputStream, new FileOutputStream(filePath));
        } catch (Throwable e) {
            e.printStackTrace();
            throw HdException.create("文件路径未找到:" + filePath);
        } finally {
           IoUtils.safeClose(inputStream);
        }
        RemoteData<Void> remoteData = new RemoteData<>();


        return remoteData;

    }

    public RemoteData<Void> syncAppQuotation(String urlHead, String startDate, String endDate) throws HdException {
        String url = HttpUrl.syncAppQuotation(urlHead,startDate,endDate);
        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<Void> syncProductPicture(String remoteResource,String filterKey,boolean shouldOverride) throws HdException {
        String url = HttpUrl.syncProductPicture(remoteResource,filterKey,  shouldOverride);
        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;

    }


    public RemoteData<Void> syncProductInfo(String remoteResource, String filterKey, boolean shouldOverride) throws HdException {
        String url = HttpUrl.syncProductInfo(remoteResource,filterKey,  shouldOverride);
        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;
    }

    public RemoteData<Void> initGjhData() throws HdException {
        String url = HttpUrl.initGjhData( );
        String result = client.getWithStringReturned(url);
        RemoteData<Void> remoteData = invokeByReflect(result, Void.class);
        return remoteData;

    }

    public RemoteData<Map> getAppQuoteCountReport(String startDate, String endDate) throws HdException {

        String url = HttpUrl.getAppQuoteCountReport(startDate, endDate);
        String result = client.getWithStringReturned(url);
        RemoteData<Map> remoteData = invokeByReflect(result, Map.class);
        return remoteData;


    }



    public <T> RemoteData<T> getData(String url,Class<T> tClass)throws HdException
    {
        String result = client.getWithStringReturned(url);
        RemoteData<T> remoteData = invokeByReflect(result,tClass);
        return remoteData;
    }

    public <T> RemoteData<T> postData(String url, T data, Class<T> tClass) throws HdException {
        return postData(url,GsonUtils.toJson(data),tClass);
    }


    public <T> RemoteData<T> postData(String url, String json, Class<T> tClass) throws HdException {
        String result = client.postWithStringReturned(url, json);
        RemoteData<T> remoteData = invokeByReflect(result,tClass);
        return remoteData;
    }


    public <T> RemoteData<T> deleteData(String url,  Class<T> tClass) throws HdException {
        String result = client.deleteWithStringReturned(url );
        RemoteData<T> remoteData = invokeByReflect(result,tClass);
        return remoteData;
    }
}
