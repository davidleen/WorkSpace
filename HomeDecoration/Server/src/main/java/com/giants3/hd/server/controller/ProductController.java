package com.giants3.hd.server.controller;


import com.giants3.hd.app.AProduct;
import com.giants3.hd.entity.*;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.ProductListViewType;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.parser.DataParser;
import com.giants3.hd.server.parser.RemoteDataParser;
import com.giants3.hd.server.service.ProductRelateService;
import com.giants3.hd.server.service.ProductService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 产品信息
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {


    /**
     * 是否支持同步功能（从另外一个服务器复制数据）
     */
    @Value("${synchonize}")
    private boolean synchonize;

    @Value("${filepath}")
    private String productFilePath;


    @Value("${productBackFilePath}")
    private String deleteProductPath;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRelateService productRelateService;


    @Autowired
    @Qualifier("productParser")
    private DataParser<Product, AProduct> dataParser;

    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Product> search(@RequestParam(value = "proName", required = false, defaultValue = "") String prd_name
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize
            , @RequestParam(value = "viewType", required = false, defaultValue = "0") int viewType
    ) throws UnsupportedEncodingException {


        switch (viewType) {
            case ProductListViewType.VIEWTYPE_NONE:
                return productService.searchProductList(prd_name, pageIndex, pageSize);


            default:

                return productService.searchProductListByViewType(prd_name, viewType, pageIndex, pageSize);


        }


    }


    /**
     * 提供移动端接口  查询
     *
     * @param name
     * @param pageIndex
     * @return
     * @Param pageSize
     */
    @RequestMapping(value = "/appSearch", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<AProduct> appSearch(@RequestParam(value = "name", required = false, defaultValue = "") String name
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize, @RequestParam(value = "withCopy", required = false) boolean withCopy

    ) throws UnsupportedEncodingException {


        RemoteData<Product> productRemoteData = productService.searchAppProductList(name, pageIndex, pageSize, withCopy);
        RemoteData<AProduct> result = RemoteDataParser.parse(productRemoteData, dataParser);


        return result;


    }

    /**
     * 提供移动端接口  查询
     *
     * @param key
     * @param pageIndex
     * @param pageSize
     * @param withCopy
     * @return
     * @Param pageSize
     */
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<Product> query(@RequestParam(value = "key", required = false, defaultValue = "") String key
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize, @RequestParam(value = "withCopy", required = false) boolean withCopy

    ) throws UnsupportedEncodingException {


        RemoteData<Product> productRemoteData = productService.searchAppProductList(key, pageIndex, pageSize, withCopy);


        return productRemoteData;


    }


    @RequestMapping(value = "/loadByNameBetween", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Product> loadByNameBetween(@RequestParam(value = "startName") String startName
            , @RequestParam(value = "endName") String endName, @RequestParam(value = "withCopy") boolean withCopy

    ) throws UnsupportedEncodingException {


        List<Product> pageValue = productService.findByNameBetweenOrderByName(startName, endName);

        if (withCopy) {
            return wrapData(pageValue);
        }


        //！withcopy  表示版本号为空
        List<Product> result = new ArrayList<>();

        for (Product product : pageValue) {
            if (StringUtils.isEmpty(product.pVersion)) {
                result.add(product);
            }
        }

        return wrapData(result);


    }


    @RequestMapping(value = "/loadByNameRandom", method = {RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Product> loadByNameBetween(@RequestBody String productNames

    ) {

        return productService.loadProductByNameRandom(productNames, true);


    }

    /**
     * 随机查询货号
     *
     * @param productNames
     * @param withCopy     是否包含翻单
     * @return
     */
    @RequestMapping(value = "/loadByNameRandom2", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<Product> loadByNameBetween(@RequestParam(value = "productNames") String productNames, @RequestParam(value = "withCopy", required = false, defaultValue = "false") boolean withCopy

    ) {

        return productService.loadProductByNameRandom(productNames, withCopy);


    }


    @RequestMapping(value = "/searchByName", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Product> searchByName(@RequestParam(value = "proName") String prd_name
            , @RequestParam(value = "productId") int productId

    ) {


        List<Product> pageValue = productService.findByNameEquals(prd_name);

        int size = pageValue.size();
        for (int i = 0; i < size; i++) {
            Product product = pageValue.get(i);
            if (product.id == productId) {
                pageValue.remove(i);
                break;
            }
        }


        return wrapData(pageValue);


    }


    @RequestMapping(value = "/findXiankang", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<Xiankang> findXiankang(@RequestParam(value = "productId") long productId) {


        Xiankang xiankang = null;
        Product product = productService.findProductById(productId);
        if (product != null) {
            xiankang = product.xiankang;
        }


        if (xiankang == null)
            return wrapData();
        return wrapData(xiankang);


    }


    @RequestMapping(value = "/findByIds", method = {RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Product> searchByName(@RequestBody Set<Long> ids


    ) throws UnsupportedEncodingException {


        List<Product> products = new ArrayList<>();

        for (Long id : ids) {

            Product product = productService.findProductById(id);
            if (product != null)
                products.add(product);
        }


        return wrapData(products);


    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Product> findProdcutById(@RequestParam("id") long productId) {


        final RemoteData<Product> productRemoteData = wrapData(productService.findProductById(productId));
        return productRemoteData;

    }

    @RequestMapping(value = "/findByNameAndVersion", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Product> findByNameAndVersion(@RequestParam("pName") String pName, @RequestParam("pVersion") String pVersion) {


        final RemoteData<Product> productRemoteData = productService.findByNameAndVersion(pName, pVersion);
        return productRemoteData;

    }

    @RequestMapping(value = "/findByProductIds", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Product> findById(@RequestParam("id") String productId) {


        String[] productIds = StringUtils.split(productId, StringUtils.STRING_SPLIT_COMMA);
        List<Product> productList = productService.findProductById(productIds);

        return wrapData(productList);
    }


    /**
     * 查询产品的详细信息
     * 包括 包装信息
     * 物料清单（胚体，油漆，包装）
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/detail", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<ProductDetail> returnFindProductDetailById(@RequestParam("id") long productId) {


        ProductDetail detail = productService.findProductDetailById(productId);

        if (detail == null) {
            return wrapError("未能根据id找到产品");
        }


        return wrapData(detail);


    }


    /**
     * 根据产品编号 prdno
     * 查询产品的详细信息
     * 包括 包装信息
     * 物料清单（胚体，油漆，包装）
     *
     * @param prdNo
     * @return
     */
    @RequestMapping(value = "/detailByPrdNo", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<ProductDetail> productDetailByPrdNo(@RequestParam("prdNo") String prdNo) {


        ProductDetail detail = productService.findProductDetailByPrdNo(prdNo);

        if (detail == null) {
            return wrapError("未能根据" + prdNo + "找到产品");
        }


        return wrapData(detail);


    }


    /**
     * 产品完整信息的保存
     *
     * @param productDetail 产品全部信息
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<ProductDetail> saveProductDetail(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody ProductDetail productDetail) {

        try {
            return productService.saveProductDetail(user, productDetail);
        } catch (Throwable e) {


            return wrapError(e.getMessage());
        }
    }


    /**
     * 由操作纪录恢复产品分析表数据
     *
     * @param user
     * @param operationLogId
     * @return
     */
    @RequestMapping(value = "/restoreFromModify", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Void> restoreFromModify(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody long operationLogId) {

        try {
            return productService.restoreProductDetailFromModifyLog(user, operationLogId);
        } catch (HdException e) {


            return wrapError(e.getMessage());
        }
    }


    /**
     * Returns a Sort object which sorts persons in ascending order by using the last name.
     *
     * @return
     */
    protected Sort sortByLastNameAsc() {
        return new Sort(Sort.Direction.ASC, "name");
    }


    /**
     * 复制产品信息   即翻单
     * 检查是否存在同款记录  （product +version）
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/copy", method = {RequestMethod.GET, RequestMethod.POST})

    public

    @ResponseBody
    RemoteData<ProductDetail> copyProduct(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("id") long productId, @RequestParam("name") String newProductName, @RequestParam("version") String version, @RequestParam(value = "copyPicture", required = false, defaultValue = "true") boolean copyPicture) {


        return productService.copyProductDetail(user, productId, newProductName, version, copyPicture);

    }


    /**
     * 删除产品信息
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/logicDelete", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Void> logicDelete(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("id") long productId) {


        return productService.logicDelete(user, productId);

    }


    /**
     * 全局更新
     * <p/>
     * 这个操作非常耗时。
     * 同步产品图片数据
     *
     * @return
     */

    @RequestMapping(value = "/syncPhoto", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public RemoteData<Void> syncAllProductPhoto() {


        return productService.syncAllProductPhoto();

    }

    /**
     * 全局更新
     * <p/>
     * 这个操作非常耗时。
     * 同步产品图片数据
     *
     * @return
     */

    @RequestMapping(value = "/syncRelateProductPicture", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public RemoteData<Void> syncRelateProductPicture() {


        long count = productService.getProductCount();
        final int pageSize = 100;
        int pageCount = (int) ((count - 1) / pageSize + 1);
        for (int i = 0; i < pageCount; ++i) {


            productService.syncRelateProductPicture(i, pageSize, pageCount);
        }


        return wrapMessageData("所有报价，订单，等关联的产品图片已经都是最新。");

    }


    @RequestMapping(value = "/searchDelete", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ProductDelete> listDelete(@RequestParam(value = "proName", required = false, defaultValue = "") String prd_name, @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageCount) {


        return productService.listDelete(prd_name, pageIndex, pageCount);


    }


    @RequestMapping(value = "/detailDelete", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ProductDetail> detailDelete(@RequestParam(value = "id") long productDeleteId) {


        return productService.detailDelete(productDeleteId);

    }

    @RequestMapping(value = "/resumeDelete", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ProductDetail> resumeDelete(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "deleteProductId") long deleteProductId) {


        return productService.resumeDelete(user, deleteProductId);


    }


    /**
     * 读取包装模板数据
     *
     * @return
     */
    @RequestMapping(value = "/listProductPackTemplate", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ProductMaterial> listProductPackTemplate() {

        List<ProductMaterial> productMaterials = productRelateService.findProductMaterialsByProductIdFlow(0, Flow.FLOW_PACK);


        if (productMaterials == null) {
            return wrapError("数据异常");
        }


        return wrapData(productMaterials);

    }

    /**
     * 修复产品图片缩略图
     *
     * @return
     */
    @RequestMapping(value = "/correctThumbnail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> correctThumbnail(@RequestParam(value = "productId") long productId) {


        return productService.correctThumbnail(productId);


    }


    /**
     * 产品公式调整
     *
     * @return
     */
    @RequestMapping(value = "/adjustEquation", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> adjustEquation() {


        //  productService.remoteUpdateEquationTasks();


        productService.startUpdateEquationTask();


        return wrapData();


    }


    /**
     * 同步另外服务器的产品信息到当前服务器
     *
     * @return
     */
    @RequestMapping(value = "/syncProductFromRemote", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> syncProductFromRemote(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("remoteUrlHead") String remoteUrlHead, @RequestParam(value = "filterKey", required = false, defaultValue = "") String filterKey
            , @RequestParam(value = "shouldOverride", required = false, defaultValue = "false") boolean shouldOverride
    ) {


        if (!synchonize) {

            return wrapError("当前服务器不支持同步复制操作。");
        }


        return productService.syncProductFromRemote(user, remoteUrlHead, filterKey, shouldOverride);


    }


}
