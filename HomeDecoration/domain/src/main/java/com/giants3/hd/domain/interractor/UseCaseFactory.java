package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.module.*;
import com.giants3.hd.domain.repository.*;
import com.giants3.hd.entity.*;
import com.giants3.hd.entity.OutFactory;
import com.giants3.hd.entity.app.AppQuoteAuth;
import com.giants3.hd.noEntity.ErpOrderDetail;
import com.giants3.hd.noEntity.ErpStockOutDetail;

import com.giants3.hd.noEntity.app.QuotationDetail;
import com.google.inject.Guice;
import com.google.inject.Inject;
import rx.schedulers.Schedulers;

import java.io.File;
import java.util.List;


/**
 * 用例工厂类
 * Created by david on 2015/9/16.
 */
public class UseCaseFactory {


    @Inject
    HdTaskRepository taskRepository;

    @Inject
    HdTaskLogRepository taskLogRepository;

    @Inject
    OrderRepository orderRepository;
    @Inject
    WorkFlowRepository workFlowRepository;
    @Inject
    ProductRepository productRepository;
    @Inject
    MaterialRepository materialRepository;
    @Inject
    XiankangRepository xiankangRepository;
    @Inject
    QuotationRepository quotationRepository;
    @Inject
    StockRepository stockRepository;

    @Inject
    FileRepository fileRepository;

    @Inject
    UserRepository userRepository;


    @Inject
    AuthRepository authRepository;

    @Inject
    FactoryRepository factoryRepository;
    @Inject
    SettingRepository settingRepository;

    private UseCaseFactory() {


        Guice.createInjector(new HdTaskModule(), new QuotationModule(), new OrderModule(), new ProductModule(),
                new StockModule(), new FileModule(), new AuthModule()
                , new MaterialModule()
                , new SettingModule()
        ).injectMembers(this);

    }


    public static UseCaseFactory factory = null;


    public synchronized static UseCaseFactory getInstance() {


        if (factory == null) {

            factory = new UseCaseFactory();

        }
        return factory;
    }


    public UseCase createQuotationDetail(long qutationId) {


        return new GetQuotationDetail(Schedulers.newThread(), Schedulers.immediate(), qutationId, quotationRepository);
    }


    public UseCase createProductByNameBetween(String startName, String endName, boolean withCopy) {


        return new ProductUseCase(Schedulers.newThread(), Schedulers.immediate(), startName, endName, withCopy, productRepository);
    }


    public UseCase createUpdateXiankang() {

        return new UpdateXiankangUseCase(Schedulers.newThread(), Schedulers.immediate(), xiankangRepository);
    }


    public UseCase readTaskListUseCase() {

        return new HdTaskListUseCase(Schedulers.newThread(), Schedulers.immediate(), taskRepository);
    }

    public UseCase addHdTaskUseCase(HdTask hdTask) {

        return new HdTaskAddUseCase(Schedulers.newThread(), Schedulers.immediate(), hdTask, taskRepository);
    }


    public UseCase deleteHdTaskUseCase(long taskId) {


        return new HdTaskDeleteUseCase(Schedulers.newThread(), Schedulers.immediate(), taskId, taskRepository);
    }

    public UseCase findTaskLogUseCase(long taskId) {

        return new HdTaskLogListUseCase(Schedulers.newThread(), Schedulers.immediate(), taskId, taskLogRepository);

    }

    public UseCase updateHdTaskStateUseCase(long taskId, int taskState) {

        return new UpdateTaskStateUseCase(taskId, taskState, taskRepository);

    }


    public UseCase executeHdTaskUseCase(int taskType) {
        return new ExecuteHdTaskUseCase(taskType, taskRepository);
    }
    public UseCase createProductByNameRandom(String productList, boolean withCopy) {

        return new ProductRandomUseCase(productList, withCopy, productRepository);
    }


    public UseCase createOrderListUseCase(String key, long salesId, int pageIndex, int pageSize) {

        return new GetOrderListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, salesId, pageIndex, pageSize, orderRepository);
    }

    public UseCase createOrderItemListUseCase(String os_no) {


        return new GetOrderItemListUseCase(Schedulers.newThread(), Schedulers.immediate(), os_no, orderRepository);
    }

    public UseCase searchOrderItemListUseCase(String key, int pageIndex, int pageSize) {


        return new SearchOrderItemListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, pageIndex, pageSize, orderRepository);
    }

    public UseCase createGetProductByPrdNo(String prdNo) {
        return new GetProductByPrdNoUseCase(prdNo, productRepository);
    }

    public UseCase createGetProductByIdUseCase(long... productId) {
        return new GetProductByIdUseCase(productId, productRepository);
    }
 public UseCase createGetProductDetailByIdUseCase(long  productId) {
        return new GetProductDetailByIdUseCase(productId, productRepository);
    }


    /**
     * 读取出库列表case
     *
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public UseCase createStockOutListUseCase(String key, long salesId, int pageIndex, int pageSize) {

        return new GetStockOutListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, salesId, pageIndex, pageSize, stockRepository);
    }

    /**
     * 读取销库列表case
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public UseCase createStockXiaokuListUseCase(String key, int pageIndex, int pageSize) {

        return new GetStockXiaokuListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, pageIndex, pageSize, stockRepository);
    }

    /**
     * 读取进库与缴库数据case
     *
     * @param key
     * @param startDate
     * @param endDate
     * @return
     */
    public UseCase createStockInAndSubmitListUseCase(String key, String startDate, String endDate) {

        return new StockInAndSubmitListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, startDate, endDate, stockRepository);

    }

    /**
     * 读取出库列表case
     *
     * @param ck_no 出库单号
     * @return
     */
    public UseCase createStockOutDetailUseCase(String ck_no) {

        return new GetStockOutDetailUseCase(Schedulers.newThread(), Schedulers.immediate(), ck_no, stockRepository);
    }

    /**
     * 上传临时文件
     *
     * @param file
     * @return
     */
    public UseCase uploadTempFileUseCase(File[] file) {

        return new UploadTempFileUseCase(Schedulers.newThread(), Schedulers.immediate(), file, fileRepository);

    }

    /**
     * @param erpStockOutDetail
     * @return
     */
    public UseCase saveStockOutDetail(ErpStockOutDetail erpStockOutDetail) {

        return new SaveStockOutDetailUseCase(Schedulers.newThread(), Schedulers.immediate(), erpStockOutDetail, stockRepository);
    }

    public UseCase createOrderDetailUseCase(String os_no) {

        return new GetOrderDetailUseCase(Schedulers.newThread(), Schedulers.immediate(), os_no, orderRepository);
    }

    /**
     * 保存订单详情用例
     *
     * @param orderDetail
     * @return
     */
    public UseCase saveOrderDetail(ErpOrderDetail orderDetail) {
        return new SaveOrderDetailUseCase(Schedulers.newThread(), Schedulers.immediate(), orderDetail, orderRepository);
    }

    /**
     * 读取报价明细权限
     *
     * @return
     */
    public UseCase createQuoteAuthListCase() {

        return new GetQuoteAuthListUseCase(Schedulers.newThread(), Schedulers.immediate(), authRepository);
    }


    /**
     * 读取订单明细权限
     *
     * @return
     */
    public UseCase createOrderAuthListCase() {

        return new GetOrderAuthListUseCase(Schedulers.newThread(), Schedulers.immediate(), authRepository);
    }


    /**
     * 读取出库明细权限
     *
     * @return
     */
    public UseCase createStockOutAuthListCase() {

        return new GetStockOutAuthListUseCase(Schedulers.newThread(), Schedulers.immediate(), authRepository);
    }

    /**
     * 获取保存出库权限用例
     *
     * @param stockOutAuths
     * @return
     */
    public UseCase createStockOutAuthSaveCase(List<StockOutAuth> stockOutAuths) {

        return new SaveStockOutAuthUseCase(Schedulers.newThread(), Schedulers.immediate(), stockOutAuths, authRepository);


    }


    /**
     * 获取保存订单权限用例
     *
     * @param orderAuths
     * @return
     */
    public UseCase createOrderAuthSaveCase(List<OrderAuth> orderAuths) {

        return new SaveOrderAuthUseCase(Schedulers.newThread(), Schedulers.immediate(), orderAuths, authRepository);


    }

    /**
     * 获取保存报价权限用例
     *
     * @param quoteAuths
     * @return
     */
    public UseCase createQuoteAuthSaveCase(List<QuoteAuth> quoteAuths) {

        return new SaveQuoteAuthUseCase(Schedulers.newThread(), Schedulers.immediate(), quoteAuths, authRepository);


    }

    /**
     * 订单报表查询
     *
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public UseCase createOrderReportUseCase(long userId, String dateStart, String dateEnd, int pageIndex, int pageSize) {
        return new GetOrderReportUseCase(Schedulers.newThread(), Schedulers.immediate(), userId, dateStart, dateEnd, pageIndex, pageSize, orderRepository);

    }

    /**
     * 获取生产流程列表
     *
     * @return
     */
    public UseCase createGetWorkFlowUseCase() {

        return new GetWorkFlowUseCase(
                Schedulers.newThread(), Schedulers.immediate(), workFlowRepository);
    }

    /**
     * 获取生产流程二级流程列表
     *
     * @return
     */
    public UseCase createGetWorkFlowSubTypeUseCase() {

        return new GetWorkFlowSubTypeUseCase(
                Schedulers.newThread(), Schedulers.immediate(), workFlowRepository);
    }

    /**
     * 保存生产流程列表
     *
     * @return
     */
    public UseCase createSaveWorkFlowUseCase(List<WorkFlow> workFlows) {

        return new SaveWorkFlowUseCase(
                Schedulers.newThread(), Schedulers.immediate(), workFlows, workFlowRepository);
    }


    /**
     * 保存产品相关生产流程列表
     *
     * @return
     */
    public UseCase createSaveWorkProductUseCase(WorkFlowProduct workFlowProduct) {

        return new SaveWorkFlowProductUseCase(
                Schedulers.newThread(), Schedulers.immediate(), workFlowProduct, workFlowRepository);

    }

    /**
     * 获取用户列表
     *
     * @return
     */
    public UseCase createGetUserListUseCase() {

        return new GetUserListUseCase(
                Schedulers.newThread(), Schedulers.immediate(), userRepository);
    }


    /**
     * 获取列表
     *
     * @return
     */
    public <T> RemoteDataUseCase<T> createGetUseCase(String url,Class<T> tClass) {

        return new RemoteDataGetUseCase(url
                , tClass);
    }
  /**
     * 获取列表
     *
     * @return
     */
    public <T> RemoteDataPostUseCase<T> createPostUseCase(String url,T data,Class<T> tClass) {

        return new RemoteDataPostUseCase(url,data
                , tClass);
    }/**
     * 获取列表
     *
     * @return
     */
    public <T> RemoteDataUseCase<T> createDeleteUseCase(String url, Class<T> tClass) {

        return new RemoteDataDeleteUseCase(url
                , tClass);
    }

    /**
     * 启动订单跟踪
     *
     * @return
     */
    public UseCase startOrderTrackUseCase(String os_no) {

        return new StartOrderTrackUseCase(
                Schedulers.newThread(), Schedulers.immediate(), os_no, workFlowRepository);
    }

    public UseCase createUnCompleteOrderWorkFlowReportUseCase() {

        return new UnCompleteOrderWorkFlowReportUseCase(orderRepository);

    }

    public UseCase createOrderWorkFlowReportUseCase(String key, int pageIndex, int pageSize) {

        return new OrderWorkFlowReportUseCase(Schedulers.newThread(), Schedulers.immediate(), key, pageIndex, pageSize, orderRepository);
    }

    public UseCase createStockXiaokuItemListUseCase(String ps_no) {
        return new StockXiaokuItemUseCase(Schedulers.newThread(), Schedulers.immediate(), ps_no, stockRepository);
    }


    public UseCase createStockXiaokuItemListUseCase(String key, final String startDate, final String endDate) {
        return new StockXiaokuItemSearchUseCase(Schedulers.newThread(), Schedulers.immediate(), key, startDate, endDate, stockRepository);
    }

    /**
     * 获取产品的生产流程配置信息
     *
     * @param productId
     * @return
     */
    public UseCase createGetWorkFlowOfProduct(long productId) {

        return new GetWorkFlowOfProduct(Schedulers.newThread(), Schedulers.immediate(), productId, workFlowRepository);
    }


    /**
     * 获取产品的生产流程配置信息
     *
     * @param
     * @return
     */
    public UseCase createGetOutFactoryUseCase() {

        return new GetOutFactoryUseCase(Schedulers.newThread(), Schedulers.immediate(), factoryRepository);
    }

    public UseCase createSaveOutFactoryListUseCase(List<OutFactory> datas) {
        return new SaveOutFactoryListUseCase(Schedulers.newThread(), Schedulers.immediate(), datas, factoryRepository);
    }

    /**
     * 保存排厂类型列表
     *
     * @param datas
     * @return
     */
    public UseCase createSaveWorkFlowSubTypeListUseCase(List<WorkFlowSubType> datas) {
        return new SaveWorkFlowSubTypeListUseCase(Schedulers.newThread(), Schedulers.immediate(), datas, workFlowRepository);
    }


    /**
     * 启动订单生产流程
     *
     * @param orderItemWorkFlow
     * @return
     */
    public UseCase createStartOrderItemWorkFlowUseCase(OrderItemWorkFlow orderItemWorkFlow) {
        return new StartOrderItemWorkFlowUseCase(Schedulers.newThread(), Schedulers.immediate(), orderItemWorkFlow, factoryRepository);

    }

    public UseCase createGetOrderItemWorkFlowState(long orderItemId) {
        return new GetOrderItemWorkFlowState(Schedulers.newThread(), Schedulers.immediate(), orderItemId, workFlowRepository);

    }


    public UseCase createCorrectThumbnaiUseCase(long productId) {
        return new CorrectThumbnaiUseCase(Schedulers.newThread(), Schedulers.immediate(), productId, productRepository);


    }

    public UseCase createGetOrderItemWorkFlow(long orderItemId) {
        return new GetOrderItemWorkFlowUseCase(Schedulers.newThread(), Schedulers.immediate(), orderItemId, orderRepository);
    }

    public UseCase createSearchZhilingdanUseCase(String osName, String startDate, String endDate) {
        return new SearchZhilingdanUseCase(Schedulers.newThread(), Schedulers.immediate(), osName, startDate, endDate, workFlowRepository);
    }

    /**
     * 公式改变时候， 进行产品表的数据同步。
     *
     * @return
     */
    public UseCase createSynchronizeProductOnEquationUpdate() {


        return new SynchronizeProductOnEquationUpdateUseCase(Schedulers.newThread(), Schedulers.immediate(), productRepository);
    }

    public UseCase createUpdateMaterialClassUseCase(MaterialClass materialClass) {
        return new UpdateMaterialClassUseCase(Schedulers.newThread(), Schedulers.immediate(), materialClass, materialRepository);
    }

    public UseCase createDeleteMaterialClassUseCase(long materialClassId) {
        return new DeleteMaterialClassUseCase(Schedulers.newThread(), Schedulers.immediate(), materialClassId, materialRepository);
    }

    public UseCase createGetWorkFlowWorkerUseCase() {

        return new GetWorkFlowWorkerUseCase(Schedulers.newThread(), Schedulers.immediate(), workFlowRepository);

    }

    public UseCase createSaveWorkFlowWorkerUseCase(WorkFlowWorker workFlowWorker) {
        return new UpdateWorkFlowWorkerUseCase(Schedulers.newThread(), Schedulers.immediate(), workFlowWorker, workFlowRepository);
    }

    public UseCase createSaveWorkFlowArrangerUseCase(WorkFlowArranger workFlowArranger) {
        return new UpdateWorkFlowArrangerUseCase(Schedulers.newThread(), Schedulers.immediate(), workFlowArranger, workFlowRepository);
    }

    public UseCase createGetWorkFlowArrangerUseCase() {

        return new GetWorkFlowArrangerUseCase(Schedulers.newThread(), Schedulers.immediate(), workFlowRepository);
    }

    public UseCase createGetWorkFlowLimitUseCase() {

        return new GetWorkFlowLimitUseCase(workFlowRepository);
    }

    public UseCase createSaveWorkFlowLimitUseCase(List<WorkFlowTimeLimit> workFlowLimit, boolean updateCompletedOrderItem) {

        return new SaveWorkFlowLimitUseCase(workFlowLimit, updateCompletedOrderItem, workFlowRepository);
    }

    public UseCase createDeleteWorkFlowWorkerUseCase(long workFlowWorkerId) {


        return new DeleteWorkFlowWorkerUseCase(Schedulers.newThread(), Schedulers.immediate(), workFlowWorkerId, workFlowRepository);

    }

    public UseCase createDeleteWorkFlowArrangerUseCase(long workFlowArrangerId) {
        return new DeleteWorkFlowArrangerUseCase(Schedulers.newThread(), Schedulers.immediate(), workFlowArrangerId, workFlowRepository);
    }


    /**
     * 同步所有产品关联的图片
     *
     * @return
     */
    public UseCase createSyncRelateProductPictureUseCase() {

        return new SyncRelateProductPictureUseCase(Schedulers.newThread(), Schedulers.immediate(), productRepository);
    }

    public UseCase CreateCancelOrderWorkFlowUseCase(long orderItemWorkFlowId) {


        return new CancelOrderWorkFlowUseCase(orderItemWorkFlowId, orderRepository);
    }

    public UseCase createGetWorkFlowEventListUseCase() {

        return new GetWorkFlowEventListUseCase(workFlowRepository);
    }

    public UseCase createGetWorkFlowEvenWorkerListtUseCase() {

        return new GetWorkFlowEventWorkerListUseCase(workFlowRepository);
    }

    public UseCase createGetErpOrderItemProcessUseCase(String osNo, String prdNo) {
        return new GetErpOrderItemProcessUseCase(osNo, prdNo, workFlowRepository);
    }

    public UseCase createGetErpOrderItemReportUseCase(String osNo, String prdNo) {
        return new GetErpOrderItemReportUseCase(osNo, prdNo, workFlowRepository);
    }

    public UseCase createGetWorkFlowAreaListUseCase() {

        return new GetWorkFlowEventAreaListUseCase(workFlowRepository);
    }

    public UseCase createSaveWorkFlowAreaUseCase(WorkFlowArea data) {
        return new SaveWorkFlowAreaUseCase(data, workFlowRepository);

    }

    public UseCase createDeleteWorkFlowAreaUseCase(long id) {
        return new DeleteWorkFlowAreaUseCase(id, workFlowRepository);
    }

    public UseCase updateMaitouFileUseCase(String os_no, File file) {


        return new UpdateMaitouFileUseCase(os_no, file, orderRepository);
    }

    public UseCase createGetSubWorkFlowListUseCase(String key, String dateStart, String dateEnd) {
        return new GetSubWorkFlowListUseCase(key, dateStart, dateEnd, workFlowRepository);
    }

    public UseCase createUpdateCompanyUseCase(Company company) {

        return new UpdateCompanyUseCase(company, settingRepository);

    }

    public UseCase createGetAppQuotationListUseCase(String key,String dateStart, String dateEnd, long userId, int pageIndex, int pageSize) {
        return new GetAppQuotationListUseCase(key,  dateStart,   dateEnd,   userId, pageIndex, pageSize, quotationRepository);
    }

    public UseCase createGetAppQuotationDetailUseCase(long quotationId, String qNumber) {

        return new GetAppQuotationDetailUseCase(quotationId, qNumber, quotationRepository);

    }


    public UseCase createGetWorkFlowMessageReportUseCase(String dateStart, String dateEnd, boolean unhandle, boolean overdue) {


        return new GetWorkFlowMessageReportUseCase(    dateStart,   dateEnd,   unhandle,   overdue, workFlowRepository);

    }

    public UseCase createSaveAppQuoteAuthCase(List<AppQuoteAuth> appQuoteAuths) {

        return new SaveAppQuoteAuthUseCase(  appQuoteAuths , authRepository);
    }

    /**
     * 读取广交会报价权限
     *
     * @return
     */
    public UseCase createGetAppQuoteAuthListCase() {

        return new GetAppQuoteAuthListCase(  authRepository);
    }

    public UseCase createNewAppQuotationDetailUseCase() {

      return   new NewAppQuotationUseCase(  quotationRepository);
    }


    public UseCase createAddProductToAppQuotationUseCase(long quotationId, long productId) {

      return   new AddProductToAppQuotationUseCase(    quotationId,   productId,quotationRepository);
    }

    public UseCase createSaveAppQuotationUseCase(QuotationDetail quotationDetail) {
        return   new SaveAppQuotationUseCase(    quotationDetail,   quotationRepository);
    } public UseCase createDeleteAppQuotationUseCase(long quotationId) {
        return   new DeleteAppQuotationUseCase(    quotationId,   quotationRepository);
    }

    /**
     *
     * @param quotationId 报价单id
     * @param filePath  打印文件存放路径。
     * @return
     */
    public UseCase createPrintAppQuotationUseCase(long quotationId, String filePath) {
        return   new PrintAppQuotationUseCase(    quotationId,  filePath, quotationRepository);
    }

    public UseCase createSyncAppQuotationUseCase(String urlHead,String startDate,String endDate) {

        return   new SyncAppQuotationUseCase(    urlHead,startDate,endDate,   quotationRepository);

    }

    public UseCase createSyncProductPictureUseCase(String remoteResource,String filterKey,boolean shouldOverride) {


        return   new SyncProductPictureUseCase(  remoteResource,   filterKey,   shouldOverride , fileRepository);
    }



    public UseCase createSyncProductInfoUseCase(String remoteResource, String filterKey, boolean shouldOverride) {

        return   new SyncProductInfoUseCase(  remoteResource,   filterKey,   shouldOverride , productRepository);

    }

    public UseCase createInitGjhDataUseCase() {

        return   new InitGjhDataUseCase(    quotationRepository);
    }

    public UseCase createAppQuoteCountReportUseCase(String startDate, String endDate) {

        return   new AppQuoteCountReportUseCase( startDate,endDate,   quotationRepository);

    }


    public UseCase createRunnableUseCase(Runnable runnable) {

        return   new RunnableWorkReportUseCase( runnable);

    }


}
