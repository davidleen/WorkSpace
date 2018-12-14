package com.giants3.hd.server.service;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.entity.*;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.*;
import com.giants3.hd.server.entity.ProductEquationUpdateTemp;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.repository_wrapper.ProductRepositoryWrapper;
import com.giants3.hd.server.utils.AttachFileUtils;
import com.giants3.hd.server.utils.BackDataHelper;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.server.utils.HttpUrl;
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.file.ImageUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.Remote;
import java.util.*;
import java.util.concurrent.*;

/**
 * quotation 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class ProductService extends AbstractService implements InitializingBean, DisposableBean {

    private static final Logger logger = Logger.getLogger(ProductService.class);
    @Value("${filepath}")
    private String productFilePath;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private ProductWageRepository productWageRepository;
    @Autowired
    private ProductPaintRepository productPaintRepository;

    @Autowired
    private WorkFlowMessageRepository workFlowMessageRepository;
    @Autowired
    private ProductEquationUpdateTempRepository productEquationUpdateTempRepository;


    @Autowired
    private ProductLogRepository productLogRepository;

    @Autowired
    private XiankangRepository xiankangRepository;
    @Autowired
    private OperationLogRepository operationLogRepository;


    @Autowired
    private QuotationItemRepository quotationItemRepository;

    @Autowired
    private QuotationXKItemRepository quotationXKItemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;


    @Autowired
    private ProductDeleteRepository productDeleteRepository;

    @Autowired
    private WorkFlowRepository workFlowRepository;


    @Value("${productBackFilePath}")
    private String productBackFilePath;
    //附件文件夹
    @Value("${attachfilepath}")
    private String attachfilepath;
    //临时文件夹
    @Value("${tempfilepath}")
    private String tempFilePath; //临时文件夹


    //产品图片全部同步现场池
    ExecutorService executorService;

    @Autowired
    ProductRepositoryWrapper productWrapperRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    TransactionTemplate transactionTemplate;

    private ApiManager apiManager;


    @Override
    public void destroy() throws Exception {

        transactionTemplate = null;
        if (executorService != null) {

            executorService.shutdown();
        }
        if (apiManager != null)
            apiManager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        transactionTemplate = new TransactionTemplate(platformTransactionManager);
        apiManager = new ApiManager();
    }

    public RemoteData<Product> searchProductList(String name, int pageIndex, int pageSize) {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        String likeValue = "%" + name.trim() + "%";
        Page<Product> pageValue;

        pageValue = productRepository.findByNameLikeOrPVersionLikeOrderByNameAsc(likeValue, likeValue, pageable);

        List<Product> products = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);

    }

    public RemoteData<Product> searchAppProductList(String name, int pageIndex, int pageSize, boolean withCopy) {

        Pageable pageable = constructPageSpecification(pageIndex, pageSize,sortByParam(Sort.Direction.DESC,"name"));
        String likeValue = "%" + name.trim() + "%";
        Page<Product> pageValue;
        if (!withCopy) {
            //查询原单，过滤掉有版本号的产品信息
            pageValue = productRepository.findByKeyWithoutCopy(likeValue, pageable);


        } else {
            pageValue = productRepository.findByNameLikeOrPVersionLikeOrderByNameDesc(likeValue, likeValue, pageable);
        }

        List<Product> products = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }

    public RemoteData<Product> searchProductListByViewType(String name, int viewType, int pageIndex, int pageSize) {


        Page<Product> pageValue = null;
        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        String likeValue = "%" + name.trim() + "%";
        switch (viewType) {


            case ProductListViewType.VIEWTYPE_PEITI_UNSET:
                pageValue = productRepository.findByNameLikeAndConceptusCostEqualsOrderByNameAsc(likeValue, 0, pageable);
                break;
            case ProductListViewType.VIEWTYPE_ZUZHUANG_UNSET:
                pageValue = productRepository.findByNameLikeAndAssembleCostEqualsOrderByNameAsc(likeValue, 0, pageable);
                break;
            case ProductListViewType.VIEWTYPE_YOUQI_UNSETE:

                pageValue = productRepository.findByNameLikeAndPaintCostEqualsOrderByNameAsc(likeValue, 0, pageable);
                break;
            case ProductListViewType.VIEWTYPE_BAOZHUANG_UNSETE:
                pageValue = productRepository.findByNameLikeAndPackCostEqualsOrderByNameAsc(likeValue, 0, pageable);
                break;


        }


        if (pageValue == null)
            return wrapData();


        List<Product> products = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }


    /**
     * 根据产品id 查询产品详情
     *
     * @param productId
     * @return
     */
    public ProductDetail findProductDetailById(long productId) {


        return productWrapperRepository.findProductDetailById(productId);


    }

    /**
     * 随机读取
     *
     * @param productNames 货号必须绾完全匹配。
     * @return
     */
    public RemoteData<Product> loadProductByNameRandom(String productNames, boolean withCopy) {

        String[] productNameArray = productNames.split(",|，| ");
        //保证不重复
        List<Product> products = new ArrayList<>();
        for (String s : productNameArray) {
            String trimS = s.trim();
            if (StringUtils.isEmpty(trimS))
                continue;

            if (trimS.contains("_") || trimS.contains("-")) {
                String[] dividerParts = trimS.split("_|-");
                Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(dividerParts[0], dividerParts[1]);
                if (product != null) {
                    products.add(product);
                }

            } else {

                if (withCopy) {
                    List<Product> subProductSet = productRepository.findByNameEquals(trimS);
                    products.addAll(subProductSet);
                } else {

                    Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(trimS, "");
                    if (product != null) {
                        products.add(product);
                    }
                }

            }


        }
        List<Product> result = new ArrayList<>(products.size());
        result.addAll(products);
        return wrapData(result);
    }


    /**
     * 根据产品no找到产品详细信息
     *
     * @param prdNo
     * @return
     */
    public ProductDetail findProductDetailByPrdNo(String prdNo) {


        Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(prdNo, "");
        if (product != null) {
            return findProductDetailById(product.getId());

        }
        return null;

    }

    /**
     * 查找同名产品 并更新缩略图
     *
     * @param fullProductName 产品全名称  09A3334,  09A9889-12211;
     */
    @Transactional
    public void updateProductPhoto(String fullProductName) {


        //拆分产品全名
        String productName;
        String pversion;
        int indexOfDivider = fullProductName.indexOf(StringUtils.PRODUCT_NAME_SPILT);
        if (indexOfDivider == -1) {
            productName = fullProductName;
            pversion = "";
        } else {
            productName = fullProductName.substring(0, indexOfDivider);
            pversion = fullProductName.substring(indexOfDivider + 1);
        }


        //所有同名的产品 都要遍历产品图片的url  同名 不同pversion的产品 可能共用同一张图片
        List<Product> products = productRepository.findByNameEquals(productName);


        updateProductAndRelateImageInfo(products);


    }

    /**
     * 更新产品图片相关的数据
     *
     * @param products
     */
    private int updateProductAndRelateImageInfo(List<Product> products) {

        return updateProductAndRelateImageInfo(products, true);
    }

    /**
     * @param products
     * @param updateRelateTable 是否级联更新关联的表
     * @return
     */
    private int updateProductAndRelateImageInfo(List<Product> products, boolean updateRelateTable) {


        int updateCount = 0;

        for (Product product : products) {
            boolean changedPhoto = updateProductPhotoData(product);
            if (changedPhoto) {
                updateCount++;
                productRepository.save(product);
                if (updateRelateTable) {
                    //   更新报价表中的图片url
                    //更新报价表中的图片缩略图
                    quotationItemRepository.updatePhotoAndPhotoUrlByProductId(product.thumbnail, product.url, product.id);
                    quotationXKItemRepository.updatePhotoByProductId(product.thumbnail, product.url, product.id);
                    quotationXKItemRepository.updatePhoto2ByProductId(product.thumbnail, product.url, product.id);
                    orderItemRepository.updateUrlByProductInfo(product.thumbnail, product.url, product.name, product.pVersion);
                    //workFlowMessageRepository.updateUrlByProductId(product.thumbnail, product.url, product.id);
                }
            }


        }
        if (updateRelateTable) {

            quotationItemRepository.flush();
            quotationXKItemRepository.flush();
            orderItemRepository.flush();
            workFlowMessageRepository.flush();


        }
        productRepository.flush();
        return updateCount;

    }

    /**
     * 更新产品的原圖，缩略图数据
     *
     * @param product
     * @return boolean  是否修改数据
     */
    public final boolean updateProductPhotoData(Product product) {


        String filePath = FileUtils.getProductPicturePath(productFilePath, product.name, product.pVersion);

        //如果tup图片文件不存在  则 设置photo,url
        //  为空。
        final File file = new File(filePath);
        if (!file.exists()) {
            if ((!StringUtils.isEmpty(product.url))) {

                product.setLastPhotoUpdateTime(Calendar.getInstance().getTimeInMillis());
                product.setUrl("");
                clearThumbnailFile(product.thumbnail);
                product.thumbnail = "";
                return true;
            }

        } else {
            long lastPhotoUpdateTime = FileUtils.getFileLastUpdateTime(file);
            String newUrl = FileUtils.getProductPictureURL(product.name, product.pVersion, lastPhotoUpdateTime);

            //構建縮略路径 保证文件夹存在
            //    String thumbnailPath = FileUtils.getProductThumbnailFilePath(productFilePath, product);
            //   boolean thumbnailFileExist = new File(thumbnailPath).exists();

            String oldThumbnailFilePath = FileUtils.convertThumbnailUrlToPath(productFilePath, product.thumbnail);
            boolean oldThumbnailFileExist = new File(oldThumbnailFilePath).exists();
            //四种情况下 更新图片路径  1 图片已经被修改。  2  新图片路径与旧路径不一致  3 缩略图未生成 4 缩略图对应图片不存在
            if (lastPhotoUpdateTime != product.lastPhotoUpdateTime || !newUrl.equals(product.url) || StringUtils.isEmpty(product.thumbnail) || !oldThumbnailFileExist) {

                product.setLastPhotoUpdateTime(lastPhotoUpdateTime);
                product.setUrl(newUrl);
                clearThumbnailFile(product.thumbnail);


                String thumbnailUrl = FileUtils.getProductThumbnailUrl(product);
                String thumbnailPath = FileUtils.convertThumbnailUrlToPath(productFilePath, thumbnailUrl);

                if (ImageUtils.scalePicture(filePath, thumbnailPath)) {
                    product.thumbnail = thumbnailUrl;
                } else {
                    logger.error("product:" + ProductAgent.getFullName(product) + ",图片缩略图需要同步， 但是同步失败,原因：原图地址:" + filePath + ",缩略图地址：" + thumbnailPath);

                }

                try {

                    byte[] bytes = ImageUtils.scaleProduct(filePath);
                    if (bytes != null) {
                        InputStream in = new ByteArrayInputStream(bytes);
                        BufferedImage bufferedImage = ImageIO.read(in);
                        in.close();
                        final FileOutputStream output = new FileOutputStream(thumbnailPath);
                        ImageIO.write(bufferedImage, "jpg", output);
                        output.flush();
                        output.close();
                        product.thumbnail = thumbnailUrl;
                    } else {

                        logger.error("filePath cant not be scale --" + filePath);

                    }
                } catch (Throwable e) {
                    e.printStackTrace();

                }


                return true;
            }


        }


        return false;


    }

    private void createThumbNailFileIfNotExist() {


    }

    /**
     * 清除縮微圖片
     *
     * @param thumbnailUrl
     */
    private void clearThumbnailFile(String thumbnailUrl) {
        if (StringUtils.isEmpty(thumbnailUrl)) return;
        String fileName = FileUtils.convertThumbnailUrlToPath(productFilePath, thumbnailUrl);

        File file = new File(fileName);
        if (file.exists())
            file.delete();


    }


    /**
     * 同步所有产品图片， 将文件夹与产品的图片字段匹配
     * 不更新关联的表
     *
     * @return
     */
    public RemoteData<Void> syncAllProductPhoto() {

        //遍历所有产品
        //一次处理100条记录
        long count = productRepository.count();
        final int pageSize = 100;
        int pageCount = (int) ((count - 1) / pageSize + 1);
        int updateCount = 0;

        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(5);
        }
        Collection<Future<Integer>> futures = new LinkedList<Future<Integer>>();
        for (int i = 0; i < pageCount; ++i) {
            final int pageIndex = i;
            futures.add(executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {


                    int size = transactionTemplate.execute(new TransactionCallback<Integer>() {
                        @Override
                        public Integer doInTransaction(TransactionStatus status) {

                            return asyncProductPhotoPartlyAndCommit(pageIndex, pageSize, false);

                        }
                    });
                    return size;

                }
            }));
        }

        //等待所有线程结束
        for (Future<Integer> future : futures) {
            try {

                updateCount += future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        return wrapMessageData(count > 0 ? "同步产品数据图片成功，共成功同步" + updateCount + "款产品！" : "所有产品图片已经都是最新的。");
    }


    /**
     * 同步产品关联表的图片url
     *
     * @return
     */
    @Transactional
    public int syncRelateProductPicture(int pageIndex, int pageSize, int pageCount) {


        long time;


        time = Calendar.getInstance().getTimeInMillis();


        Page<Product> productPage = null;
        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        productPage = productRepository.findAll(pageable);


        List<Product> products = productPage.getContent();

        for (Product product : products) {

            //   更新报价表中的图片url
            //更新报价表中的图片缩略图
            quotationItemRepository.updatePhotoAndPhotoUrlByProductId(product.thumbnail, product.url, product.id);
            quotationXKItemRepository.updatePhotoByProductId(product.thumbnail, product.url, product.id);
            quotationXKItemRepository.updatePhoto2ByProductId(product.thumbnail, product.url, product.id);
            orderItemRepository.updateUrlByProductInfo(product.thumbnail, product.url, product.name, product.pVersion);
            // workFlowMessageRepository.updateUrlByProductId(product.thumbnail, product.url, product.id);
        }


        quotationItemRepository.flush();
        quotationXKItemRepository.flush();
        orderItemRepository.flush();
        workFlowMessageRepository.flush();

        logger.info("update product relate table picture " + pageIndex + ",pageCount:" + pageCount + ",use time" + ((Calendar.getInstance().getTimeInMillis() - time) / 1000));


        return 0;


    }

//    /**
//     * 同步所有与产品图片相关的数据， 更新图片url 至最新。
//     */
//    @Transactional
//    public RemoteData<Void> syncProductPhotoRelateData() {
//
//        //遍历所有产品
//        //一次处理100条记录
//        long count = 30000;
//        int pageSize =1;
//        int pageCount = (int) ((count - 1) / pageSize + 1);
//        int updateCount=0;
//        for (int i = 0; i < pageCount; i++) {
//            updateCount+= asyncProductPhotoPartlyAndCommit(i, pageSize);
//        }
//
//
//        return wrapMessageData(count > 0 ? "同步产品数据图片成功，共成功同步" + updateCount + "款产品！" : "所有产品图片已经都是最新的。");
//    }


    /**
     * 图片更新  部分更新就提交
     *
     * @param pageIndex
     * @param pageSize
     * @param updateRelateTable 更新关联的表
     * @return
     */

    public int asyncProductPhotoPartlyAndCommit(int pageIndex, int pageSize, boolean updateRelateTable) {

        Page<Product> productPage = null;
        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        productPage = productRepository.findAll(pageable);
        int updateCount = updateProductAndRelateImageInfo(productPage.getContent(), updateRelateTable);
        return updateCount;


    }


    /**
     * @param user           登录用户信息
     * @param productId
     * @param newProductName
     * @param version
     * @param copyPicture    是否复制图片
     * @return
     */
    @Transactional
    public RemoteData<ProductDetail> copyProductDetail(User user, long productId, String newProductName, String
            version, boolean copyPicture) {

        if (productRepository.findFirstByNameEqualsAndPVersionEquals(newProductName, version) != null) {


            return wrapError("货号：" + newProductName + ",版本号：" + version
                    + "已经存在,请更换");
        }

        Product product = productRepository.findOne(productId);

        if (product == null) {
            return wrapError("未找到当前产品信息");
        }

        //深度复制对象， 重新保存数据， 能直接使用源数据保存，会报错。
        Product newProduct = (Product) ObjectUtils.deepCopy(product);
        Xiankang xiankang = (Xiankang) ObjectUtils.deepCopy(product.xiankang);

        if (xiankang != null) {
            xiankang.setId(-1);
            if (xiankang.xiankang_dengju != null) {
                xiankang.xiankang_dengju.setId(-1);
            }
            if (xiankang.xiankang_jiaju != null) {
                xiankang.xiankang_jiaju.setId(-1);
            }
            if (xiankang.xiankang_jingza != null) {
                xiankang.xiankang_jingza.setId(-1);
            }
            newProduct.xiankang = xiankang;
        }
        newProduct.id = -1;
        newProduct.name = newProductName;
        newProduct.pVersion = version;
        newProduct.thumbnail = "";


        if (copyPicture) {
            //复制产品图片

            String newproductPicturePath = FileUtils.getProductPicturePath(productFilePath, newProductName, version);
            File newProductFile = new File(newproductPicturePath);
            //如果文件不存在 则将原样图片复制为新图片
            if (!newProductFile.exists()) {

                String oldProductFilePath = FileUtils.getProductPicturePath(productFilePath, product.name, product.pVersion);

                File oldProductFile = new File(oldProductFilePath);
                if (oldProductFile.exists()) {
                    try {
                        org.apache.commons.io.FileUtils.copyFile(oldProductFile, newProductFile);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }


            }


            //更新缩略图信息
            boolean changPhoto = updateProductPhotoData(newProduct);
        }
//        else
//        {
//            newProduct.photo=null;
//            newProduct.url="";
//            newProduct.lastPhotoUpdateTime=Calendar.getInstance().getTimeInMillis();
//        }


        //保存新产品信息
        newProduct = productRepository.save(newProduct);

        long newProductId = newProduct.id;

        updateProductLog(newProduct, user);


        //更新产品材料信息
        List<ProductMaterial> materials = productMaterialRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
        //深度复制对象， 重新保存数据， 不能能直接使用源数据保存，会报错。
        List<ProductMaterial> newMaterials = (List<ProductMaterial>) ObjectUtils.deepCopy(materials);
        for (ProductMaterial material : newMaterials) {


            material.id = -1;
            material.productId = newProductId;
            productMaterialRepository.save(material);

        }


        //更新油漆表信息
        //更新产品材料信息
        List<ProductPaint> productPaints = productPaintRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
        //深度复制对象， 重新保存数据， 不能直接使用源数据保存，会报错。
        List<ProductPaint> newProductPaints = (List<ProductPaint>) ObjectUtils.deepCopy(productPaints);


        for (ProductPaint productPaint : newProductPaints) {
            productPaint.id = -1;
            productPaint.productId = newProductId;
            productPaintRepository.save(productPaint);

        }

        //复制工资

        List<ProductWage> productWages = productWageRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
        //深度复制对象， 重新保存数据， 能直接使用源数据保存，会报错。
        List<ProductWage> newProductWages = (List<ProductWage>) ObjectUtils.deepCopy(productWages);
        for (ProductWage productWage
                : newProductWages) {
            productWage.id = -1;
            productWage.productId = newProductId;
            productWageRepository.save(productWage);

        }


        return returnFindProductDetailById(newProductId);
    }

    /**
     * 查询产品详情
     *
     * @param productId
     * @return
     */
    RemoteData<ProductDetail> returnFindProductDetailById(long productId) {


        ProductDetail detail = findProductDetailById(productId);

        if (detail == null) {
            return wrapError("未能根据id找到产品");
        }


        return wrapData(detail);


    }

    /**
     * 记录产品修改信息
     */

    public OperationLog updateProductLog(Product product, User user) {

        //记录数据当前修改着相关信息
        ProductLog productLog = productLogRepository.findFirstByProductIdEquals(product.id);
        if (productLog == null) {
            productLog = new ProductLog();
            productLog.productId = product.id;

        }
        productLog.productName = product.name;
        productLog.pVersion = product.pVersion;
        productLog.setUpdator(user);
        productLogRepository.save(productLog);


        //增加历史操作记录
        OperationLog operationLog = OperationLog.createForProductModify(product, user);
        operationLog = operationLogRepository.save(operationLog);


        return operationLog;


    }


    /**
     * 保存产品详情
     *
     * @param user
     * @param productDetail
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public RemoteData<ProductDetail> saveProductDetail(User user, ProductDetail productDetail) throws HdException

    {
        return saveProductDetail(user, productDetail, true);

    }


    @Transactional(rollbackFor = Throwable.class)
    public RemoteData<ProductDetail> saveProductDetail(User user, ProductDetail productDetail, boolean checkUpdateTime) throws HdException {


        ProductDetail oldDetail = ObjectUtils.deepCopyWidthJson(findProductDetailById(productDetail.product.id), ProductDetail.class);
//将修改前的数据缓存起来
        String productDetailBackPath = "";
        if (oldDetail != null) {
            productDetailBackPath = BackDataHelper.backProductModifyData(oldDetail, productBackFilePath);
            if (StringUtils.isEmpty(productDetailBackPath) || !(new File(productDetailBackPath).exists())) {
                throw HdException.create("产品数据缓存出错");
            }
        }


        long productId = productDetail.product.id;


        //新增加的产品数据
        Product newProduct = productDetail.product;


        Product sameNameAndVersionProduct = productRepository.findFirstByNameEqualsAndPVersionEquals(newProduct.name, newProduct.pVersion);
        //新增加数据
        //检查唯一性 货号版本号形成唯一的索引
        if (sameNameAndVersionProduct != null && sameNameAndVersionProduct.id != newProduct.id) {
            return wrapError("货号：" + newProduct.name + ",版本号：" + newProduct.pVersion
                    + "已经存在,请更换");
        }

        /**
         * 未生成id 添加记录
         */
        if (productRepository.exists(productId)) {
            //找出旧的记录
            Product oldData = productRepository.findOne(productId);


            //检查一致性  最近更新时间是否一致。
            if (checkUpdateTime && (oldData.lastUpdateTime != newProduct.lastUpdateTime)) {
                return wrapError("货号：" + newProduct.name + ",版本号：" + newProduct.pVersion
                        + " 已经被改变，请刷新数据重新保存。");
            }

            //版本号被改变了
            if (checkUpdateTime && (!oldData.pVersion.equals(newProduct.pVersion))) {
                //版本号被改变了。
                //检查报价单
                QuotationItem quotationItem = quotationItemRepository.findFirstByProductIdEquals(oldData.id);
                if (quotationItem != null) {
                    return wrapError("货号：" + ProductAgent.getFullName(oldData) + ", 不能修改版本号， 有报价单已经使用这款货 。");
                }
                QuotationXKItem quotationXkItem = quotationXKItemRepository.findFirstByProductIdEquals(oldData.id);
                if (quotationXkItem != null) {
                    return wrapError("货号：" + ProductAgent.getFullName(oldData) + ", 不能修改版本号， 有报价单已经使用这款货 。");
                }
                quotationXkItem = quotationXKItemRepository.findFirstByProductId2Equals(oldData.id);
                if (quotationXkItem != null) {
                    return wrapError("货号：" + ProductAgent.getFullName(oldData) + ", 不能修改版本号， 有报价单已经使用这款货 。");
                }

            }


            //如果产品名称修改  则修正缩略图
            if (!(oldData.name.equals(newProduct.name) && oldData.pVersion.equals(newProduct.pVersion))) {
                updateProductPhotoData(newProduct);
            }

        } else {

            //更新缩略图
            updateProductPhotoData(newProduct);

        }


        //赋于最新的 更新时间
        newProduct.lastUpdateTime = Calendar.getInstance().getTimeInMillis();


        //最新product 数据
        Product product = productRepository.save(newProduct);


        productId = product.id;


        if (productDetail.paints != null) {
            //保存油漆数据
            List<ProductPaint> oldPaints = productPaintRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
            //查找旧记录是否存在新纪录中  如果不存在就删除。删除旧记录操作。
            for (ProductPaint oldPaint : oldPaints) {
                boolean exist = false;
                for (ProductPaint newPaint : productDetail.paints) {

                    if (oldPaint.getId() == newPaint.id) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    //不存在新纪录中 删除
                    productPaintRepository.delete(oldPaint.id);
                }

            }

            //添加或者修改记录
            int newIndex = 0;
            for (ProductPaint newPaint : productDetail.paints) {
                //过滤空白记录 工序编码 跟名称都为空的情况下 也为设定材料情况下  为空记录。
                if (newPaint.isEmpty()) {
                    continue;
                }

                newPaint.setProductId(product.id);
                newPaint.setProductName(product.name);
                newPaint.setFlowId(Flow.FLOW_PAINT);
                newPaint.itemIndex = newIndex++;
                productPaintRepository.save(newPaint);
            }


        }

        //保存白胚数据
        if (productDetail.conceptusMaterials != null) {

            saveProductMaterial(productDetail.conceptusMaterials, productId, Flow.FLOW_CONCEPTUS);
        }

        //保存组装数据
        if (productDetail.assembleMaterials != null) {

            saveProductMaterial(productDetail.assembleMaterials, productId, Flow.FLOW_ASSEMBLE);
        }

        //保存包装数据
        if (productDetail.packMaterials != null) {

            saveProductMaterial(productDetail.packMaterials, productId, Flow.FLOW_PACK);
        }


        if (productDetail.assembleWages != null)
            saveProductWage(productDetail.assembleWages, productId, Flow.FLOW_ASSEMBLE);

        if (productDetail.conceptusWages != null)
            saveProductWage(productDetail.conceptusWages, productId, Flow.FLOW_CONCEPTUS);

        if (productDetail.packWages != null)
            saveProductWage(productDetail.packWages, productId, Flow.FLOW_PACK);

        //更新修改记录
        //将此次修改记录插入历史修改记录表中。
        OperationLog operationLog = updateProductLog(product, user);

        //将修改前的数据缓存起来

        if (oldDetail != null) {
            BackDataHelper.productBackFileRenameTo(productDetailBackPath, oldDetail, operationLog, productBackFilePath);
        }


        return returnFindProductDetailById(productId);
    }


    /**
     * 更新产品的工资信息
     *
     * @param wages
     * @param flowId
     */
    private void saveProductWage(List<ProductWage> wages, long productId, long flowId) {


        //保存油漆数据
        List<ProductWage> oldDatas = productWageRepository.findByProductIdEqualsAndFlowIdEquals(productId, flowId);
        //查找就记录是否存在新纪录中  如果不存在就删除。删除旧记录操作。
        for (ProductWage oldData : oldDatas) {
            boolean exist = false;
            for (ProductWage newData : wages) {

                if (oldData.getId() == newData.id) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                //不存在新纪录中 删除
                productWageRepository.delete(oldData.id);
            }

        }


        int newItemIndex = 0;

        //添加或者修改记录
        for (ProductWage newData
                : wages) {

            if (newData.isEmpty()) {
                continue;
            }

            newData.setProductId(productId);
            newData.setFlowId(flowId);
            newData.itemIndex = newItemIndex++;
            productWageRepository.save(newData);
        }


    }

    /**
     * 更新产品的材料数据， 不同流程 不同处理。
     *
     * @param materials
     * @param productId
     * @param flowId
     */
    private void saveProductMaterial(List<ProductMaterial> materials, long productId, long flowId) {

        //保存油漆数据
        List<ProductMaterial> oldDatas = productMaterialRepository.findByProductIdEqualsAndFlowIdEquals(productId, flowId);
        //查找就记录是否存在新纪录中  如果不存在就删除。删除旧记录操作。
        for (ProductMaterial oldData : oldDatas) {
            boolean exist = false;
            for (ProductMaterial newData : materials) {

                if (oldData.getId() == newData.id) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                //不存在新纪录中 删除
                productMaterialRepository.delete(oldData.id);
            }

        }

        //添加或者修改记录
        int newItemIndex = 0;
        for (ProductMaterial newData
                : materials) {

            //无设定材料  数据为空
            if (newData.isEmpty()) {
                continue;
            }
            newData.setProductId(productId);
            newData.setFlowId(flowId);
            newData.itemIndex = newItemIndex++;
            productMaterialRepository.save(newData);
        }


    }

    @Transactional
    public RemoteData<ProductDetail> resumeDelete(User user, long deleteProductId) {


        ProductDelete productDelete = productDeleteRepository.findOne(deleteProductId);

        if (productDelete == null) {
            return wrapError("该产品已经不在删除列表中");
        }


        ProductDetail detail = BackDataHelper.getProductDetail(productBackFilePath, productDelete);

        if (detail == null)
            return wrapError("备份文件读取失败");


        //新增记录
        //移除id
        detail.product.id = 0;
        detail.product.xiankang.setId(0);

        RemoteData<ProductDetail> result = null;
        try {
            result = saveProductDetail(user, detail);
        } catch (HdException e) {
            e.printStackTrace();
        }

        if (result != null && result.isSuccess()) {


            ProductDetail newProductDetail = result.datas.get(0);
            long newProductId = newProductDetail.product.id;
            //更新修改记录中所有旧productId 至新id；

            if (detail.productLog != null) {
                detail.productLog.productId = newProductId;
                productLogRepository.save(detail.productLog);
                newProductDetail.productLog = detail.productLog;
            }

            //更新修改记录中所有旧productId 至新id；
            operationLogRepository.updateProductId(productDelete.productId, Product.class.getSimpleName(), newProductId);

            //添加恢复消息记录。
            OperationLog operationLog = OperationLog.createForProductResume(newProductDetail.product, user);
            operationLogRepository.save(operationLog);


            //移除删除记录
            productDeleteRepository.delete(deleteProductId);

            //移除备份的文件
            BackDataHelper.deleteProduct(productBackFilePath, productDelete);

        }


        return wrapData(detail);

    }


    /**
     * 删除产品信息
     *
     * @param productId
     * @return
     */
    @Transactional
    public
    @ResponseBody
    RemoteData<Void> logicDelete(User user, long productId) {


        ProductDetail detail = findProductDetailById(productId);


        //查询是否有关联的报价单

        QuotationItem quotationItem = quotationItemRepository.findFirstByProductIdEquals(productId);
        if (quotationItem != null) {
            Quotation quotation = quotationRepository.findOne(quotationItem.quotationId);

            return wrapError("该货号在报价单：[" + (quotation == null ? "" : quotation.qNumber)
                    + "]有使用记录，不能删除");
        }

        Product product = productRepository.findOne(productId);
        if (product == null) {
            return wrapError("该产品已经删除， 请更新 ！");
        }

        productRepository.delete(productId);
        //增加历史操作记录
        operationLogRepository.save(OperationLog.createForProductDelete(product, user));


        int affectedRow = 0;
        affectedRow = productWageRepository.deleteByProductIdEquals(productId);
        // logger.info("productWageRepository delete affectedRow:" + affectedRow);
        affectedRow = productMaterialRepository.deleteByProductIdEquals(productId);
        //  logger.info("productMaterialRepository delete affectedRow:" + affectedRow);
        affectedRow = productPaintRepository.deleteByProductIdEquals(productId);
        //  logger.info("productPaintRepository delete affectedRow:" + affectedRow);

        //    save the delete item to the wareHouse .
        //备份产品数据
        ProductDelete productDelete = new ProductDelete();
        productDelete.setProductAndUser(product, user);
        ProductDelete newDelete = productDeleteRepository.save(productDelete);
        BackDataHelper.backProduct(detail, productBackFilePath, newDelete);


        return wrapData();
    }


    public
    @ResponseBody
    RemoteData<ProductDetail> detailDelete(long productDeleteId) {


        ProductDelete productDelete = productDeleteRepository.findOne(productDeleteId);

        if (productDelete == null) {
            return wrapError("该产品已经不在删除列表中");
        }


        ProductDetail detail = null;

        detail = BackDataHelper.getProductDetail(productBackFilePath, productDelete);

        if (detail == null)
            return wrapError("备份文件读取失败");


        return wrapData(detail);

    }


    public
    @ResponseBody
    RemoteData<ProductDelete> listDelete(String prd_name, int pageIndex, int pageCount) {


        Pageable pageable = constructPageSpecification(pageIndex, pageCount);
        Page<ProductDelete> pageValue = productDeleteRepository.findByProductNameLikeOrderByTimeDesc("%" + prd_name.trim() + "%", pageable);

        List<ProductDelete> products = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }


    /**
     * 更新产品附件     包装附件 与 产品附件
     */
    // @Async
    public void updateAttachFiles() {
        String threadName = Thread.currentThread().getName();
        System.out.println("   " + threadName + " beginning work   ");

        Page<Product> page;
        Pageable pageable = constructPageSpecification(0, 100);
        page = productRepository.findAll(pageable);
        handleProductList(page.getContent());

        while (page.hasNext()) {
            pageable = page.nextPageable();
            page = productRepository.findAll(pageable);
            List<Product> products = page.getContent();
            handleProductList(products);

        }


    }

    private void handleProductList(List<Product> products) {


        for (Product product : products) {
            handleProductAttach(product);
        }


        productRepository.flush();

    }

    /**
     * @param product
     */
    private void handleProductAttach(Product product) {

        boolean update = false;
        String newAttaches = AttachFileUtils.getNewAttaches(product.attaches, attachfilepath, tempFilePath, AttachFileUtils.PRODUCT_ATTACH_PREFIX + ProductAgent.getFullName(product));
        if (!newAttaches.equals(product.attaches)) {
            product.attaches = newAttaches;
            update = true;

        }
        newAttaches = AttachFileUtils.getNewAttaches(product.packAttaches, attachfilepath, tempFilePath, AttachFileUtils.PRODUCT_PACK_ATTACH_PREFIX + ProductAgent.getFullName(product));
        if (!newAttaches.equals(product.packAttaches)) {
            product.packAttaches = newAttaches;
            update = true;

        }

        if (update) {

            productRepository.save(product);
        }


    }


    /**
     * 设置默认的流程数据
     */
    @Transactional
    public void setDefaultWorkFlowIds() {

        List<WorkFlow> workFlows = workFlowRepository.findAll();


        StringBuilder stringBuilder = new StringBuilder();
        for (WorkFlow workFlow : workFlows) {
            stringBuilder.append(workFlow.flowStep).append(StringUtils.PRODUCT_NAME_COMMA);

        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        String workFlowIndexStrings = stringBuilder.toString();

        stringBuilder.setLength(0);
        for (WorkFlow workFlow : workFlows) {
            stringBuilder.append(workFlow.name).append(StringUtils.PRODUCT_NAME_COMMA);

        }
        stringBuilder.setLength(stringBuilder.length() - 1);

        String workFlowNameStrings = stringBuilder.toString();
        productRepository.setDefaultWorkFlowIds(workFlowIndexStrings, workFlowNameStrings);

    }

    public Product findProductById(long productId) {

        return productRepository.findOne(productId);
    }




    public RemoteData<Product> findByNameAndVersion(String pName, String pVersion) {
        final Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(pName, pVersion);
        if(product==null) return wrapError("未找到"+pName+","+pVersion+"的产品信息");
        return wrapData(product);
    }

    public List<Product> findProductById(String[] productIds) {

        long[] productIdArray = new long[productIds.length];
        for (int i = 0; i < productIds.length; i++) {

            productIdArray[i] = Long.parseLong(productIds[i]);
        }

        return productRepository.findByIdIn(productIdArray);
    }

    /**
     * 修复产品图片
     *
     * @param productId
     * @return
     */
    @Transactional
    public RemoteData<Void> correctThumbnail(long productId) {


        Product product = productRepository.findOne(productId);
        if (product == null)
            return wrapError("产品不存在");


        List<Product> products = new ArrayList<>();
        products.add(product);
        updateProductAndRelateImageInfo(products);


        return wrapData();
    }

    /**
     * 启动产品更新任务流程
     */
    @Transactional
    public void startUpdateEquationTask() {

        //删除当前存在的任务  如果有的话


        productEquationUpdateTempRepository.deleteAll();


        ProductEquationUpdateTemp productEquationUpdateTemp = new ProductEquationUpdateTemp();
        Pageable pageable = new PageRequest(0, 100);

        Page<Product> page;

        int i = 0;
        do {


            page = productRepository.findAll(pageable);
            for (Product product : page.getContent()) {
                productEquationUpdateTemp.productId = product.id;
                productEquationUpdateTemp.id = -1;
                productEquationUpdateTempRepository.save(productEquationUpdateTemp);
                i++;
            }

            if (i > 100) {
                productEquationUpdateTempRepository.flush();
                i -= 100;
            }

            if (page.hasNext()) {
                pageable = page.nextPageable();
            } else {
                break;
            }

        } while (true);

        productEquationUpdateTempRepository.flush();


    }

    public long getProductCount() {

        return productRepository.count();
    }


    @Transactional(rollbackFor = Throwable.class)
    public RemoteData<Void> restoreProductDetailFromModifyLog(User LoginUser, long operationLogId) throws HdException {


        OperationLog operationLog = operationLogRepository.findOne(operationLogId);
        if (operationLog == null)

            return wrapError("未找到该操作记录");

        if (Product.class.getSimpleName().equals(operationLog.tableName)) {

            Product product = productRepository.findOne(operationLog.recordId);
            if (product == null) {
                return wrapError("未找到该关联的产品数据");
            }

            ProductDetail productDetail = BackDataHelper.restoreProductModifyData(ProductAgent.getFullName(product), operationLogId, productBackFilePath);


            if (productDetail == null)
                return wrapError("恢复分析表全部数据记录失败");


            RemoteData<ProductDetail> result = saveProductDetail(LoginUser, productDetail, false);
            if (result.isSuccess()) {
                return wrapData();
            } else {
                return wrapError(result.message);
            }


        } else {

            return wrapError("该操作记录不支持恢复数据");
        }


    }

    public List<Product> findByNameBetweenOrderByName(String startName, String endName) {

        List<Product> pageValue = productRepository.findByNameBetweenOrderByName(startName, endName);
        return pageValue;
    }

    public List<Product> findByNameEquals(String prd_name) {
        return productRepository.findByNameEquals(prd_name);
    }

    /**
     * 从远处端复制产品信息。
     *
     * @param user
     * @param remoteUrlHead
     * @param filterKey
     * @param shouldOverride
     * @return
     */
    public RemoteData<Void> syncProductFromRemote(User user, String remoteUrlHead, String filterKey, boolean shouldOverride) {

        String remoteServerUrlHead = remoteUrlHead + "Server/";

        String loginUrl = HttpUrl.login(remoteServerUrlHead, user.id,  user.passwordMD5);
        String result = apiManager.getString(loginUrl);
        RemoteData remoteData = GsonUtils.fromJson(result, RemoteData.class);
        int copyCount = 0;
        if (remoteData.isSuccess()) {

            final int PageSize = 20;
            RemoteData<Product> productRemoteData = readProductFromRemote(remoteServerUrlHead, remoteData.token, filterKey, 0, PageSize);


            while (productRemoteData.isSuccess()) {

                for (Product product : productRemoteData.datas) {

                    final Product oldProduct = productRepository.findFirstByNameEqualsAndPVersionEquals(product.name, product.pVersion);
                    if (oldProduct != null) {
                        //有找到旧数据，判断是否覆盖
                        if (shouldOverride) {
                            product.id = oldProduct.id;
                            product.xiankang = oldProduct.xiankang;
                            productRepository.save(product);
                            copyCount++;

                        }

                    } else {
                        product.setId(-1);
                        Xiankang xiankang =    product.xiankang;;
                        if (xiankang != null) {
                            xiankang.setId(-1);
                            if (xiankang.xiankang_dengju != null) {
                                xiankang.xiankang_dengju.setId(-1);
                            }
                            if (xiankang.xiankang_jiaju != null) {
                                xiankang.xiankang_jiaju.setId(-1);
                            }
                            if (xiankang.xiankang_jingza != null) {
                                xiankang.xiankang_jingza.setId(-1);
                            }
                            product.xiankang = xiankang;
                        }
                        productRepository.save(product);
                        copyCount++;

                    }


                }
                productRepository.flush();

                if (productRemoteData.hasNext()) {


                    productRemoteData = readProductFromRemote(remoteServerUrlHead, remoteData.token, filterKey, productRemoteData.pageIndex + 1, PageSize);

                } else {
                    break;
                }


            }


        }


        return wrapMessageData("===共复制" + (shouldOverride ? "并覆盖" : "") + copyCount + ",款产品");

    }


    private RemoteData<Product> readProductFromRemote(String remoteServerUrlHead, String token, String filterKey, int pageIndex, int pageSize) {
        String productUrl = HttpUrl.findProductList(remoteServerUrlHead, token, filterKey, pageIndex, pageSize);

        String result = apiManager.getString(productUrl);

        RemoteData<Product> productRemoteData = GsonUtils.fromJson(result, new RemoteDateParameterizedType(Product.class));
        return productRemoteData;
    }

}
