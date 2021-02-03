package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.entity.Prdt;
import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.entity.ProductToUpdate;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.repository_erp.ErpPrdtRepository;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.pools.ObjectPool;
import com.giants3.pools.PoolCenter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;


/**
 * Created by david on 2016/4/16.
 */
@Service
public class MaterialService extends AbstractService {


    private static final Logger logger = Logger.getLogger(MaterialService.class);
    @Autowired
    ErpPrdtRepository erpPrdtRepository;

    @Autowired
    PrdtTempRepository prdtTempRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GlobalDataService globalDataService;

    @Autowired
    private MaterialClassRepository materialClassRepository;


    @Autowired
    private ProductToUpdateRepository productToUpdateRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Value("${materialfilepath}")
    private String rootFilePath;
    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    private Set<Long> materialPriceRelateProduct = new HashSet<>();

    private GlobalData globalData;

    @Autowired
    private ProductPaintRepository productPaintRepository;
    ObjectPool<Material> materialObjectPool;

    @Override
    public void afterPropertiesSet() throws Exception {



        materialObjectPool = PoolCenter.getObjectPool(Material.class);
    }

    @Override
    public void destroy() throws Exception {


    }


    /**
     * 判断数据是否一致
     *
     * @param material
     * @param prdt
     * @return
     */
    public boolean compare(Material material, Prdt prdt) {

        if (material.id <= 0) return false;
        if (!material.code.equals(prdt.prd_no))
            return false;


        if (!material.name.equals(prdt.name))
            return false;
        if (!material.unitName.equals(prdt.ut))
            return false;
        if (!material.spec.equals(prdt.spec))
            return false;
//       if(  Float.compare(material.price,prdt.price)!=0)
        if (Math.abs(material.price - prdt.price) > 0.01f)
            return false;
        if (material.memo == null && prdt.rem != null) {

            return false;
        } else if (material.memo != null && prdt.rem == null) {
            return false;
        } else if (material.memo != null && !material.memo.equals(prdt.rem))
            return false;

        if (Float.compare(material.typeId, prdt.type) != 0)
            return false;
        if (material.classId == null || !material.classId.equals(prdt.classId))
            return false;
        if (material.className == null || !material.className.equals(prdt.className))
            return false;


        if (material.available != prdt.available)
            return false;


        if (Float.compare(material.discount, prdt.discount) != 0)
            return false;


        if (Float.compare(material.wLong, prdt.wLong) != 0)
            return false;


        if (Float.compare(material.wWidth, prdt.wWidth) != 0)
            return false;

        if (Float.compare(material.wHeight, prdt.wHeight) != 0)
            return false;

        if (material.outOfService != (prdt.nouse_dd > 0)) {

            return false;
        }


        return true;


    }

    /**
     * erp 数据转换成材料数据
     *
     * @param material
     * @param prdt
     */
    public boolean convert(Material material, Prdt prdt) {


        if (compare(material, prdt)) {

            return true;
        }
        //todo 数据转化  更详细的数据


        material.code = prdt.prd_no;


        material.name = prdt.name;
        material.unitName = prdt.ut;

        material.spec = prdt.spec;
        material.price = prdt.price;
        material.memo = prdt.rem;
        material.typeId = prdt.type;


        material.classId = prdt.classId;
        material.className = prdt.className;

        material.available = prdt.available;
        material.discount = prdt.discount;
        material.wLong = prdt.wLong;
        material.wWidth = prdt.wWidth;
        material.wHeight = prdt.wHeight;


        //停用消息
        material.outOfService = prdt.nouse_dd > 0;
        material.outOfServiceDate = prdt.nouse_dd;
        material.outOfServiceDateString = prdt.nouse_dd <= 0 ? "" : DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(prdt.nouse_dd));


        return false;


    }


    private static final int UPDATE_PRODUCT_PAGE_SIZE = 100;

    /**
     * 更新产品统计数据信息
     * <p/>
     * <p/>
     * 检查ProductToUpdateBiao是否有数据 ，有 则跟新指定的产品的统计信息
     */

    public void updateProductData() {


        //读取第一百条 更新
        Pageable pageable = new PageRequest(0, UPDATE_PRODUCT_PAGE_SIZE);
        Page<ProductToUpdate> page  ;


        do{
            page = productToUpdateRepository.findAll(pageable);
            List<ProductToUpdate> productIds = page.getContent();
            productService.updateProductDataBaseUpdateEvent(productIds);

        }while (page!=null&&page.hasNext());

    }









    public Material findMaterial(long materialId) {

        return materialRepository.findOne(materialId);
    }

    public void save(Material material) {

        materialRepository.save(material);
    }


    /**
     * 更新材料分类。
     *
     * @param materialClass
     * @return
     */
    @Transactional
    public RemoteData<MaterialClass> updateClass(MaterialClass materialClass) {

        //是否毛长 毛宽 毛高 利用率 折扣率改变  更新所有关联的产品分析表数据。
        if (materialClass.id > 0) {
            MaterialClass oldMaterialClass = materialClassRepository.findOne(materialClass.id);
            if (oldMaterialClass == null) {
                return wrapError("数据异常，未找到该材料记录");
            }


            if (!oldMaterialClass.code.equals(materialClass.code)) {
                return wrapError("分类编码不能修改");
            }


            boolean
                    needToUpdateProduct =
                    Float.compare(oldMaterialClass.wLong, materialClass.wLong) == 0
                            || Float.compare(oldMaterialClass.wHeight, materialClass.wHeight) == 0
                            || Float.compare(oldMaterialClass.wWidth, materialClass.wWidth) == 0
                            || Float.compare(oldMaterialClass.available, materialClass.available) == 0
                            || Float.compare(oldMaterialClass.discount, materialClass.discount) == 0;


            if (needToUpdateProduct) {

                List<Material> materials = materialRepository.findByClassIdEquals(materialClass.code);
                //所有有关联的 材料


            }


        }


        final MaterialClass result = materialClassRepository.save(materialClass);


        return wrapData(result);


    }
    /**
     * 从erp数据库 复制有改动的材料
     *
     * @return
     */
    public
    @Transactional
    RemoteData<Void> copyMaterialFromErp()
    {

        if(prdtTempRepository.findAll().size()>0) return wrapData();

        List<Prdt> datas = erpPrdtRepository.list();
//        List<Prdt> datas = erpPrdtRepository.findByPrd_noEquals("22030001",null);

        //int size = Math.min(datas.size(),1000);
        int size =  datas.size() ;
        logger.info("copyMaterialFromErp total  material size :" + size);

        int  copiedSize=0;
        List<MaterialClass> materialClasses = materialClassRepository.findAll();
        for (int i = 0; i < size; i++) {
            Prdt prdt = datas.get(i);
            String classId;
            int length = prdt.prd_no.length();
            if (length < 5) continue;
            if (prdt.prd_no.startsWith("C") || prdt.prd_no.startsWith("c"))
                classId = prdt.prd_no.substring(1, 5);
            else
                classId = prdt.prd_no.substring(0, 4);



            for (MaterialClass materialClass : materialClasses) {


                if (materialClass.code.equals(classId)) {
                    //数据附加
                    prdt.wLong = materialClass.wLong;
                    prdt.wWidth = materialClass.wWidth;
                    prdt.wHeight = materialClass.wHeight;
                    prdt.available = materialClass.available;
                    prdt.discount = materialClass.discount;
                    prdt.classId = materialClass.code;
                    prdt.className = materialClass.name;
                    prdt.type = materialClass.type;


                    break;
                }
            }
            Material oldData = materialRepository.findFirstByCodeEquals(prdt.prd_no);
            boolean isNew = oldData == null;
            prdt.isNew=isNew;
            if (!isNew)  {


                //单价比较调整
                prdt.isPriceChange= Math.abs(oldData.price - prdt.price) > 0.01f;
                prdt.isMemoChange = !StringUtils.equals(oldData.memo, prdt.rem);
                prdt.isDataChang =!compare(oldData, prdt);



            }
            if(prdt.isNew||prdt.isDataChang||prdt.isPriceChange||prdt.isMemoChange)
            {
                prdtTempRepository.save(prdt);
                copiedSize++;
                if(copiedSize%10==0) prdtTempRepository.flush();
            }

        }
        logger.info("copyMaterialFromErp total  material copied size  :" + copiedSize);
            return wrapData();


    }




    public @Transactional
    boolean   updateErpMaterial()
    {


        Page<Prdt> datas = prdtTempRepository.findAll(constructPageSpecification(0, 5));
        boolean hasMore = datas.hasNext();
        final Set<Long> relateProductIds = new HashSet<>();
        try {


            for (Prdt prdt : datas)
                updateErpPrdtToMaterial(prdt, relateProductIds);
        } catch (Throwable t) {
            logger.error("updateErpMaterial", t);
        }

        prdtTempRepository.delete(datas);
        logger.info("   relateProduct Count :" + relateProductIds.size());
        saveProductIdsToUpdate(relateProductIds);


        return hasMore;
    }


    /**
     * 同步erp 数据
     *
     * @return
     */
    public
    @Transactional
    RemoteData<Void> syncERP() {


        List<Prdt> datas = erpPrdtRepository.list();

        int size = datas.size();
        logger.info("syncErp total  material size :" + size);

        List<MaterialClass> materialClasses = materialClassRepository.findAll();


        final Set<Long> relateProductIds = new HashSet<>();


        for (int i = 0; i < size; i++) {
            Prdt prdt = datas.get(i);
            String classId;
            int length = prdt.prd_no.length();
            if (length < 5) continue;
            if (prdt.prd_no.startsWith("C") || prdt.prd_no.startsWith("c"))
                classId = prdt.prd_no.substring(1, 5);
            else
                classId = prdt.prd_no.substring(0, 4);


            boolean foundClass = false;
            for (MaterialClass materialClass : materialClasses) {

                foundClass = false;
                if (materialClass.code.equals(classId)) {
                    //数据附加
                    prdt.wLong = materialClass.wLong;
                    prdt.wWidth = materialClass.wWidth;
                    prdt.wHeight = materialClass.wHeight;
                    prdt.available = materialClass.available;
                    prdt.discount = materialClass.discount;
                    prdt.classId = materialClass.code;
                    prdt.className = materialClass.name;
                    prdt.type = materialClass.type;

                    foundClass = true;
                    break;
                }
            }
            // if(!foundClass)
            //logger.info("material :"+prdt.prd_no+" didnot found its class :"+classId);


            // logger.info("material :"+prdt.prd_no+",flowStep="+i);
            savePrdt(prdt, relateProductIds);

            materialRepository.flush();

            productMaterialRepository.flush();
            productPaintRepository.flush();


        }


        if (relateProductIds.size() > 0) {
            logger.info("   relateProduct Count :" + relateProductIds.size());
            saveProductIdsToUpdate(relateProductIds);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    updateProductData();
                }
            }).start();

        }


        return wrapData();
    }


    public void saveProductIdsToUpdate(Set<Long> relateProductIds)
    {




        List<ProductToUpdate> productToUpdates=new ArrayList<>();


        int i=0;
        for(long productId:relateProductIds)
        {


            if (productToUpdateRepository.findFirstByProductIdEquals(productId)!=null) continue;

            ProductToUpdate productToUpdate=new ProductToUpdate();
            productToUpdate.productId=productId;
            productToUpdate.updateWay="ERP材料同步";
            productToUpdates.add(productToUpdate);
        }

        if(productToUpdates.size()>0)
        {
            productToUpdateRepository.save(productToUpdates);
            productToUpdateRepository.flush();
        }


    }




    /**
     * 保存覆盖旧erp 的数据
     *
     * @param prdt
     * @return 产生关联的产品数据
     */
    public void savePrdt(Prdt prdt, Set<Long> productIds) {


        Material oldData = materialRepository.findFirstByCodeEquals(prdt.prd_no);
        boolean isNew = oldData == null;
        boolean isSameData = false;
        if (isNew) {
            Material material = materialObjectPool.newObject();
            material.id = -1;
            convert(material, prdt);
            materialRepository.save(material);
            //  materialObjectPool.release(material);

        } else {


            //单价比较调整
            boolean priceChange = Math.abs(oldData.price - prdt.price) > 0.01f;
//            boolean priceChange=Float.compare(oldData.price,prdt.price)!=0;

            boolean memoChange = !StringUtils.equals(oldData.memo, prdt.rem);
            isSameData = convert(oldData, prdt);
            if (priceChange) {
                updatePriceRelateData(oldData, productIds);

            }


            /**
             * 备注是否相同，不同则更新到分析表中
             */


            if (memoChange) {

                productMaterialRepository.updateMemoOnMaterialId(prdt.rem, oldData.id);
                productPaintRepository.updateMemoOnMaterialId(prdt.rem, oldData.id);

            }


            if (!isSameData) {
                materialRepository.save(oldData);
            }

        }

        if (isNew || !isSameData)
            logger.info("material :" + prdt.prd_no + ",isNew=" + isNew + ",isSame:" + isSameData);


    }


    public void updateErpPrdtToMaterial(Prdt prdt,Set<Long> productIds)
    {
        Material material =materialRepository.findFirstByCodeEquals(prdt.prd_no);
        if (prdt.isNew) {

            if (material == null) {

                material = materialObjectPool.newObject();
                material.id = -1;
                convert(material, prdt);
                materialRepository.save(material);
                return;
            }

        }
        if (material != null) {
                convert(material, prdt);
                if (prdt.isPriceChange) {
                    updatePriceRelateData(material, productIds);
                } else if (prdt.isMemoChange) {
                    productMaterialRepository.updateMemoOnMaterialId(prdt.rem, material.id);
                    productPaintRepository.updateMemoOnMaterialId(prdt.rem, material.id);
                }
                materialRepository.save(material);




        }





    }


    /**
     * 更新该材料相关的单价信息  必须在单价有变动的情况下调用。
     *
     * @param material
     * @return 与单价相关的 产品列表
     */
    public void updatePriceRelateData(Material material, Set<Long> productIds) {
        logger.info("price of material:" + material.code + " has changed!");
        materialPriceRelateProduct.clear();
        if (globalData == null)
            globalData = globalDataService.findCurrentGlobalData();


        //价格发生变动， 调整有用到该材料的费用
        long id = material.id;


        //调整材料相关的数据


        List<ProductMaterial> datas = productMaterialRepository.findByMaterialIdEquals(id);
        for (ProductMaterial productMaterial : datas) {
            productMaterial.materialCode = material.code;
            productMaterial.materialName = material.name;
            productMaterial.price = FloatHelper.scale(material.price, 3);


            productMaterial.amount = FloatHelper.scale(productMaterial.quota * material.price);
            //更新
            productMaterialRepository.save(productMaterial);
            //搜集关联的产品
            materialPriceRelateProduct.add(productMaterial.productId);


        }

        //调整油漆相关的材料数据

        List<ProductPaint> productPaints = productPaintRepository.findByMaterialIdEquals(id);
        for (ProductPaint productPaint : productPaints) {
            productPaint.materialCode = material.code;
            productPaint.materialName = material.name;
            productPaint.materialPrice = material.price;

            ProductAnalytics.updateProductPaintPriceAndCostAndQuantity(productPaint, globalData);


            //更新
            productPaintRepository.save(productPaint);
            //搜集关联的产品
            materialPriceRelateProduct.add(productPaint.productId);


        }


        logger.info(" material:" + material.code + ",new Price : " + material.price + " has related product count:" + materialPriceRelateProduct.size());

        productIds.addAll(materialPriceRelateProduct);
        materialPriceRelateProduct.clear();


    }


    /**
     * 更新产品统计数据信息  与材料单价 数量相关
     */
    public void updateProductData(final Collection<Long> productIds) {

        //保存到数据库
        //去除重复数据
        if (productIds != null) {
            ProductToUpdate productToUpdate;
            for (long productId : productIds) {

//                productToUpdate = productToUpdateRepository.findFirstByProductIdEquals(productId);
//                if (productToUpdate == null) {
                productToUpdate = new ProductToUpdate();
//                }

                productToUpdate.productId = productId;
                productToUpdate.updateWay="材料修改单价";
                productToUpdateRepository.save(productToUpdate);
            }

            productToUpdateRepository.flush();
        }

        long count = productToUpdateRepository.count();
        if (count <= 0) return;
        logger.info("product to update for statis data, count:" + count
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateProductData();
            }
        }).start();



    }

    public List<Material> getMaterialsByCodes(List<String> codes) {
        List<Material> materials = new ArrayList<>();
        for (String code : codes) {


            Material data = materialRepository.findFirstByCodeEquals(code);
            if (null != data)
                materials.add(data);


        }
        return materials;
    }

    public List<Material> getMaterialsForNames(List<String> names) {
        List<Material> materials = new ArrayList<>();
        for (String name : names) {
            Material data = materialRepository.findFirstByNameEquals(name);
            if (null != data)
                materials.add(data);
        }
        return materials;
    }


    public RemoteData<Material> searchMaterial(String codeOrName, String classId
            , int pageIndex, int pageSize) {

        Pageable pageable = constructPageSpecification(pageIndex, pageSize, sortByParam(Sort.Direction.ASC, "code"));
        String searchValue = "%" + codeOrName.trim() + "%";
        Page<Material> pageValue;
        if (StringUtils.isEmpty(classId)) {

            pageValue = materialRepository.findByCodeLikeOrNameLike(searchValue, searchValue, pageable);
        } else {
            pageValue = materialRepository.findByCodeLikeAndClassIdOrNameLikeAndClassIdEquals(searchValue, classId, searchValue, classId, pageable);
        }

        List<Material> materials = pageValue.getContent();
        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), materials);
    }


    public RemoteData<Material> searchInService(String codeOrName, String classId
            , int pageIndex, int pageSize) {

        Pageable pageable = constructPageSpecification(pageIndex, pageSize, sortByParam(Sort.Direction.ASC, "code"));
        String searchValue = "%" + codeOrName.trim() + "%";
        Page<Material> pageValue;
        if (StringUtils.isEmpty(classId)) {

            pageValue = materialRepository.findByCodeLikeAndOutOfServiceNotOrNameLikeAndOutOfServiceNot(searchValue, true, searchValue, true, pageable);
        } else {
            pageValue = materialRepository.findByCodeLikeAndClassIdOrNameLikeAndClassIdEquals(searchValue, classId, searchValue, classId, pageable);
        }

        List<Material> materials = pageValue.getContent();
        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), materials);
    }


    @Transactional
    public void saveList(List<Material> materials) {

        for (Material material : materials) {


            Material oldData = materialRepository.findFirstByCodeEquals(material.code);
            if (oldData == null) {
                material.id = -1;


            } else {
                material.id = oldData.id;

                //价格比较

            }
            save(material);


        }


    }


    /**
     * 修改或更新材质数据，关联更新相关的数据
     *
     * @param material
     * @return
     */
    @Transactional
    public RemoteData<Material> saveOrUpdate(Material material) {


        if (StringUtils.isEmpty(material.code) || StringUtils.isEmpty(material.name)) {
            return wrapError("编码与名称不能为空");
        }


        Material oldData = materialRepository.findOne(material.id);
        if (oldData == null) {
            // return wrapError("未找到材料信息  编码："+material.code+"，名称："+material.name);
            //确定编码唯一性

            if (!isDistinctCode(material.code)) {
                return wrapError("编码：[" + material.code + "],已经存在，唯一性字段不能重复");
            }


            material.id = -1;
            //更新缩略图
            updateMaterialPhoto(material);


        } else {

            //价格比较
            if (Float.compare(oldData.price, material.price) != 0) {


                //重置全局参数

                Set<Long> relateProductIds = new HashSet<>();
                updatePriceRelateData(material, relateProductIds);

                updateProductData(relateProductIds);
            }


            //编号
            if (!oldData.code.equals(material.code)) {

                if (!isDistinctCode(material.code)) {
                    return wrapError("编码：[" + material.code + "],已经存在，唯一性字段不能重复");
                }

                updateMaterialPhoto(material);
            }


            //同步相关信息  副单位 单价

            if (Float.compare(material.price2, oldData.price2) != 0 || !material.unit2.equals(oldData.unit2)) {
                //更新所有的分析表的相关数据

                productMaterialRepository.updateUnitPrice2ByMaterialId(material.unit2, material.price2, material.id);


            }


        }


        //更新停用相关信息
        boolean outOfService = material.outOfService;
        if (outOfService && (oldData == null || !oldData.outOfService)) {

            Date date = Calendar.getInstance().getTime();
            material.outOfServiceDate = date.getTime();
            material.outOfServiceDateString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(date);
        }


        material = materialRepository.save(material);

        return wrapData(material);
    }


    /**
     * 检查编码唯一性
     *
     * @param newCode
     * @return
     */
    private boolean isDistinctCode(String newCode) {

        return materialRepository.findFirstByCodeEquals(newCode) == null;
    }


    /**
     * 更新材料的缩略图数据
     *
     * @param material
     */
    private final void updateMaterialPhoto(Material material) {


        String filePath = FileUtils.getMaterialPicturePath(rootFilePath, material.code, material.classId);


        String url;
        //如果tup图片文件不存在  则 设置photo为空。
        if (!new File(filePath).exists()) {
            material.setLastPhotoUpdateTime(Calendar.getInstance().getTimeInMillis());

        } else {


        }

        url = FileUtils.getMaterialPictureURL(material.code, material.classId, material.lastPhotoUpdateTime);
        material.url = url;


    }

    public List<Material> findAll() {


        return materialRepository.findAll();
    }

    @Transactional
    public RemoteData<Void> logicDelete(long materialId) {
        //查询是否有产品使用该物料。
        if (productMaterialRepository.findFirstByMaterialIdEquals(materialId) != null) {


            return wrapError("该材料在产品材料中有使用 ，不能删除 ！");
        }
        //查询是否有产品使用该物料。
        if (productPaintRepository.findFirstByMaterialIdEquals(materialId) != null) {


            return wrapError("该材料在产品油漆中有使用 ，不能删除 ！");
        }


        Material material = materialRepository.findOne(materialId);
        if (material == null) {
            return wrapError("该材料已经删除， 请更新 ！");
        }

        materialRepository.delete(materialId);


        return wrapData();
    }

    /**
     * 同步材料图片数据
     *
     * @return
     */
    @Transactional
    public RemoteData<Void> synPhoto() {


        int count = 0;
        //遍历所有产品
        //一次处理20条记录

        int pageIndex = 0;
        int pageSize = 20;


        Page<Material> materialPage = null;

        do {
            Pageable pageable = constructPageSpecification(pageIndex++, pageSize);
            materialPage = materialRepository.findAll(pageable);

            for (Material material
                    : materialPage.getContent()) {


                String filePath = FileUtils.getMaterialPicturePath(rootFilePath, material.code, material.classId);
                long lastUpdateTime = FileUtils.getFileLastUpdateTime(new File(filePath));
                boolean isModified = false;
                if (lastUpdateTime > 0) {
                    if (lastUpdateTime != material.lastPhotoUpdateTime) {
                        updateMaterialPhoto(material);
                        material.lastPhotoUpdateTime = lastUpdateTime;
                        isModified = true;
                        count++;
                    }


                } else {

                    material.lastPhotoUpdateTime = lastUpdateTime;
                    isModified = true;
                    count++;


                }

                String newUrl = FileUtils.getMaterialPictureURL(material.code, material.classId, lastUpdateTime);
                if (!newUrl.equals(material.url)) {
                    material.url = newUrl;
                    isModified = true;

                }


                //图片改变  或者 url 未赋予初值  则 更新新数据
                if (isModified) {

                    materialRepository.save(material);
                }


            }


        } while (materialPage.hasNext());


        return wrapMessageData(count > 0 ? "同步材料数据图片成功，共成功同步" + count + "款材料！" : "所有材料图片已经都是最新的。");
    }






}
