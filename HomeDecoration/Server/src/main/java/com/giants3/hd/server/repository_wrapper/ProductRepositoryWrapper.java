package com.giants3.hd.server.repository_wrapper;

import com.giants3.hd.entity.Flow;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.ProductMaterial;
import com.giants3.hd.entity.ProductWage;
import com.giants3.hd.noEntity.ConstantData;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2018/6/13.
 */
@Repository
public class ProductRepositoryWrapper {
    @Autowired
    private ProductLogRepository productLogRepository;

    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private ProductWageRepository productWageRepository;
    @Autowired
    private ProductPaintRepository productPaintRepository;
    @Autowired
    ProductRepository productRepository;

    /**
     * 获取魔板产品数据
     * @return
     */
    public List<ProductDetail> getProductDemos() {
        List<ProductDetail> demos = new ArrayList<>();
        ProductDetail productDetail = null;
        List<Product> result = productRepository.findByNameLike(StringUtils.sqlRightLike(ConstantData.DEMO_PRODUCT_NAME));
        if (result.size() > 0) {

            int size = result.size();
            for (int i = 0; i < size; i++) {
                productDetail = findProductDetailById(result.get(i).id);
                if (productDetail != null) {
                    //擦除去记录信息
                    productDetail = (ProductDetail) ObjectUtils.deepCopy(productDetail);
                    productDetail.swipe();
                    demos.add(productDetail);
                }

            }

        }
        return demos;
    }


    /**
     * 根据产品id 查询产品详情
     *
     * @param productId
     * @return
     */
    public ProductDetail findProductDetailById(long productId) {


        //读取产品信息
        Product product = productRepository.findOne(productId);
        if (product == null) {
            return null;


        }

        ProductDetail detail = new ProductDetail();


        detail.product = product;

        detail.productLog = productLogRepository.findFirstByProductIdEquals(productId);


        //读取材料列表信息
        List<ProductMaterial> productMaterials = productMaterialRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);

        List<ProductMaterial> conceptusCost = new ArrayList<>();
        List<ProductMaterial> assemblesCost = new ArrayList<>();
        List<ProductMaterial> packCost = new ArrayList<>();


        for (ProductMaterial productMaterial : productMaterials) {

            switch ((int) productMaterial.flowId) {
                case Flow.FLOW_CONCEPTUS:
                    conceptusCost.add(productMaterial);
                    break;

                case Flow.FLOW_PAINT:

                    break;

                case Flow.FLOW_ASSEMBLE:
                    assemblesCost.add(productMaterial);
                    break;

                case Flow.FLOW_PACK:
                    packCost.add(productMaterial);
                    break;
            }

        }
        detail.conceptusMaterials = conceptusCost;
        detail.assembleMaterials = assemblesCost;
        detail.packMaterials = packCost;


        //读取工资数据
        List<ProductWage> productWages = productWageRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
        List<ProductWage> conceptusWage = new ArrayList<>();
        List<ProductWage> assemblesWage = new ArrayList<>();
        List<ProductWage> packWages = new ArrayList<>();
        for (ProductWage productWage : productWages) {

            switch ((int) productWage.flowId) {
                case Flow.FLOW_CONCEPTUS:
                    conceptusWage.add(productWage);
                    break;

                case Flow.FLOW_PAINT:

                    break;

                case Flow.FLOW_ASSEMBLE:
                    assemblesWage.add(productWage);
                    break;

                case Flow.FLOW_PACK:
                    packWages.add(productWage);
                    break;
            }

        }


        detail.conceptusWages = conceptusWage;
        detail.assembleWages = assemblesWage;
        detail.packWages = packWages;

        //读取油漆列表信息
        detail.paints = productPaintRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);


        return detail;

    }
}
