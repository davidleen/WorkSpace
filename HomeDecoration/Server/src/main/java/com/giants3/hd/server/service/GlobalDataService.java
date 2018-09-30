package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.GlobalDataRepository;
import com.giants3.hd.server.repository.OperationLogRepository;
import com.giants3.hd.server.repository.ProductPaintRepository;
import com.giants3.hd.server.repository.ProductRepository;
import com.giants3.hd.utils.DateFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

/**
 *
 * Created by davidleen29 on 2016/12/21.
 */
@Service
public class GlobalDataService extends AbstractService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private GlobalDataRepository globalDataRepository;
    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private ProductPaintRepository productPaintRepository;


    /**
     * 获取最新的全局配置数据表
     *
     * @return
     */
    public GlobalData findCurrentGlobalData() {
        if (globalDataRepository.count() == 0)
            return null;
      Pageable pageable= constructPageSpecification(0,1,new Sort(Sort.Direction.DESC,"updateTime"));
        return globalDataRepository.findAll(pageable).getContent().get(0);
    }

    public GlobalData find(long id) {
        return globalDataRepository.findOne(id);
    }

    @Transactional
    public GlobalData save(GlobalData globalData) {

        globalData.id=0;
        globalData.updateTime= DateFormats.FORMAT_YYYY_MM_DD_HH_MM_SS.format(Calendar.getInstance().getTime());


        globalDataRepository.save(globalData);
        return globalData;
    }





    @Transactional(rollbackFor = Throwable.class)
    public RemoteData<GlobalData> updateGlobalData(User user,GlobalData globalData) throws HdException {
        GlobalData oldData = find(globalData.id);
        if (oldData == null) {

            return wrapError("系统异常");
        }


        if (oldData.isGlobalSettingEquals(globalData)) {


            return wrapError("全局参数无改变");


        }
        //判断是否产品相关的材料信息改变
        boolean isProductRelateChange=!oldData.isProductRelateDataEquals(globalData);




        if(isProductRelateChange) {


            //判断是否材料相关的固定参数改变
            boolean materialRelateChanged =!oldData.isMaterialRelateEquals(globalData);


            //遍历所有产品数据
            int pageIndex = 0;
            int pageSize = 10;

            Page<Product> productPage = null;

            final StringBuilder stringBuilder = new StringBuilder();
            do {
                Pageable pageable = constructPageSpecification(pageIndex++, pageSize);
                productPage = productRepository.findAll(pageable);

                //一次处理10条
                for (Product product : productPage.getContent()) {

                    stringBuilder.setLength(0);
                    logger.info(stringBuilder.append("update global on product:").append(product.name).append(",-").append(product.pVersion).toString());
                    boolean isForeignProduct = !Factory.CODE_LOCAl.equals(product.factoryCode);

                    //外厂数据 只需更新 成本值
                    if (isForeignProduct) {
                        ProductAnalytics.updateForeignFactoryRelate(product, globalData);
                    } else

                        //如果有跟油漆材料相关 先统计油漆相关数据
                        if (materialRelateChanged) {






                            List<ProductPaint> productPaints = productPaintRepository.findByProductIdEqualsOrderByItemIndexAsc(product.id);
                            ProductAnalytics.updateProductWithProductPaints(product,productPaints,globalData);



                            //更新油漆数据
                            productPaintRepository.save(productPaints);
                            productPaintRepository.flush();


                            //更新产品油漆统计数据 自动联动更新全局数据。






                        }

                    //各道材料++修理工资+搬运工资
                    ProductAnalytics.updateProductInfoOnly(globalData,product);






                    //保存全部数据
                    //重新保存数据
                    productRepository.save(product);



                    //增加历史操作记录
                    OperationLog operationLog = OperationLog.createForGloBalDataChange(product, user);
                    operationLogRepository.save(operationLog);

                }
                if(productPage.getSize()>0) {
                    productRepository.flush();
                    operationLogRepository.flush();
                }


            } while (productPage.hasNext());


        }
        //更新全局数据
        GlobalData  saveResult=  save(globalData);


        return wrapData(saveResult);
    }
}
