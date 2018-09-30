package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.server.interf.TargetVersion;
import com.giants3.hd.server.repository.ProductRepository;
import com.giants3.hd.server.repository.QuotationItemRepository;
import com.giants3.hd.server.repository.QuotationXKItemRepository;
import com.giants3.hd.server.utils.SqlScriptHelper;
import com.giants3.hd.utils.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.util.*;

/**
 * 数据库 数据 复制服务
 * 清空当前数据库  从  指定数据库  复制全部数据 到当前数据。
 * Created by davidleen29 on 2017/4/12.
 */
@Service
@Deprecated
public class TableRestoreService extends AbstractService {


    private static final Logger logger = Logger.getLogger(TableRestoreService.class);
    private static final String FROM_DB = "FROM_DB";
    private static final String DEST_DB = "DEST_DB";
    private static final String T_TABLE_TO_CHANGE = "T_TABLE_TO_CHANGE";
    private static final String T_FIELD_TO_CHANGE = "T_FIELD_TO_CHANGE";
    @Autowired
    EntityManagerFactory entityManagerFactory;


    @Autowired
    ProductRepository productRepository;
    @Autowired
    QuotationItemRepository quotationItemRepository;
    @Autowired
    QuotationXKItemRepository quotationXKItemRepository;


    @Transactional
    public void restoreTable() {


        Set<EntityType<?>> entityTypes = entityManagerFactory.getMetamodel().getEntities();

        Set<EntityType> removedEntityTypes = new HashSet<>();

        for (EntityType entityType : entityTypes) {

            String tableName = entityType.getName();
            switch (tableName) {
                case "T_WorkFlowArranger":
                case "T_WorkFlowProduct":
                case "T_WorkFlow":
                case "T_WorkFlowWorker":
                case "T_WorkFlowEventWorker":
                case "T_ProductWorkFlow":
                case "T_ProductEquationUpdateTemp":
                case "T_OrderItemWorkFlow":
                case "T_OrderItemWorkFlowState":
                case "T_OrderItemWorkMessage": {
                    removedEntityTypes.add(entityType);

                }
            }
        }
        entityTypes.removeAll(removedEntityTypes);
        String fromDB = "yunfei_Data";
        String destDB = "yunfei";

        restoreEntityTypes(entityTypes, fromDB, destDB);

    }


    /**
     * 恢复广交会数据
     */
    public  void restoreGJHTableData() {

        Set<EntityType<?>> entityTypes = entityManagerFactory.getMetamodel().getEntities();

        Set<EntityType<?>> entityTypesForRestore = new HashSet<>();

        for (EntityType entityType : entityTypes) {

            String tableName = entityType.getName();
            switch (tableName) {
                case "T_Customer":
                case "T_AppQuotationItem":
                case "T_AppQuotation": {
                    entityTypesForRestore.add(entityType);

                }
            }
        }

        String fromDB = "gjh_init";
        String destDB = "yunfei";

        restoreEntityTypes(entityTypesForRestore, fromDB, destDB);


    }


    private void restoreEntityTypes(Set<EntityType<?>> entityTypes, String fromDB, String destDB) {


        //表与列的键对值
        Map<String, String> tableFieldMap = new HashMap<>();
        StringBuilder sb = new StringBuilder(3000);
        for (EntityType entityType : entityTypes) {
            String tableName = entityType.getName();
            sb.setLength(0);
            final Set<Attribute> attributes = entityType.getAttributes();

            for (Attribute attribute : attributes) {
                String fieldName = attribute.getName();
                if (attribute.getPersistentAttributeType() != Attribute.PersistentAttributeType.BASIC) {
                    fieldName += "_id";
                }
                sb.append(fieldName).append(StringUtils.STRING_SPLIT_COMMA);

            }

            if (sb.length() > 0)
                sb.setLength(sb.length() - 1);
            tableFieldMap.put(tableName, sb.toString());

        }


        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {


            Set<String> keySet = tableFieldMap.keySet();
            //关闭所有的表约束
            String unCheckConstraintScript = SqlScriptHelper.readScript("restoreDB_un_ckeck_constraints.sql");
            StringBuilder unCheckConstraints = new StringBuilder();
            for (String s : keySet) {
                unCheckConstraints.append(unCheckConstraintScript.replace(DEST_DB, destDB).replace(T_TABLE_TO_CHANGE, s)).append("   ");
            }


            try {

                entityManager.createNativeQuery(unCheckConstraints.toString()) ;
            } catch (Throwable t) {
                t.printStackTrace();


            }


            //执行数据迁移
            Iterator<String> iterable = keySet.iterator();

            while (iterable.hasNext()) {
                String tableName = iterable.next();
                String fields = tableFieldMap.get(tableName);
                String restoreSql = SqlScriptHelper.readScript("restoreDB.sql");
                restoreSql = restoreSql.replace(FROM_DB, fromDB).replace(DEST_DB, destDB).replace(T_TABLE_TO_CHANGE, tableName).replace(T_FIELD_TO_CHANGE, fields);
                try {
                    List<Result> result = entityManager.createNativeQuery(restoreSql).unwrap(SQLQuery.class).addScalar("totalCount", IntegerType.INSTANCE).addScalar("minId", IntegerType.INSTANCE).addScalar("maxId", IntegerType.INSTANCE).setResultTransformer(Transformers.aliasToBean(Result.class)).list();
                    logger.info(tableName + " ,result:" + result.get(0));
                } catch (Throwable t) {
                    t.printStackTrace();
                    logger.info(tableName + ", fail!!!!!!!!!!!!!");
                }
            }


            //打开表约束
            //关闭所有的表约束
            String checkConstraintScript = SqlScriptHelper.readScript("restoreDB_check_constraints.sql");
            StringBuilder checkConstraints = new StringBuilder();
            for (String s : keySet) {
                checkConstraints.append(checkConstraintScript.replace(DEST_DB, destDB).replace(T_TABLE_TO_CHANGE, s)).append("   ");
            }


            try {
                entityManager.createNativeQuery(checkConstraints.toString()) ;
            } catch (Throwable t) {
                t.printStackTrace();


            }

            entityManager.getTransaction().commit();
        } catch (Throwable t) {

            t.printStackTrace();
            entityManager.getTransaction().rollback();
        }


    }


    public static class Result {

        public int totalCount;
        public long minId;
        public long maxId;

        @Override
        public String toString() {
            return "Result{" +
                    "totalCount=" + totalCount +
                    ", minId=" + minId +
                    ", maxId=" + maxId +
                    '}';
        }
    }


    /**
     * 从报价单恢复外厂产品的包装数据 并且重新计算统计数据
     */
    @Transactional
    @TargetVersion(TargetVersion.VERSION_RESTORE_PACK_FROM_QUOTATION)
    public void restoreOutFactoryProductPackInfoFromQuotation(GlobalData globalData) {


        if (globalData == null) {
            return;
        }

        int updateCount = 0;

        //外厂产品
        List<Product> products = productRepository.findByFactoryCodeEquals(Factory.CODE_OUT);


        Pageable pageable = constructPageSpecification(0, 10);
        for (Product product : products) {
            //无包装相关数据
            if (product.packLong <= 0 || product.packWidth <= 0 || product.packHeight <= 0 || product.packQuantity <= 0) {

                //从报价单恢复
                boolean hasRestore = false;
                //从普通报价单中查找
                Page<QuotationItem> page = quotationItemRepository.findByProductIdEqualsOrderByIdDesc(product.id, pageable);
                if (page.getNumberOfElements() > 0) {
                    for (QuotationItem quotationItem : page.getContent()) {
                        if (!hasRestore && !StringUtils.isEmpty(quotationItem.packageSize)) {


                            updateProductPackInfo(product, quotationItem.inBoxCount, quotationItem.packQuantity, quotationItem.packageSize);


                            hasRestore = true;

                        }

                    }


                }

                if (!hasRestore) {

                    //从XK报价单中查找
                    Page<QuotationXKItem> xkPage = quotationXKItemRepository.findByProductIdOrProductId2EqualsOrderByIdDesc(product.id, product.id, pageable);
                    if (xkPage.getNumberOfElements() > 0) {
                        for (QuotationXKItem quotationItem : xkPage.getContent()) {
                            if (!hasRestore && !StringUtils.isEmpty(quotationItem.packageSize) && quotationItem.productId == product.id) {

                                updateProductPackInfo(product, quotationItem.inBoxCount, quotationItem.packQuantity, quotationItem.packageSize);
                                hasRestore = true;

                            }

                            if (!hasRestore && !StringUtils.isEmpty(quotationItem.packageSize2) && quotationItem.productId2 == product.id) {
                                updateProductPackInfo(product, quotationItem.inBoxCount2, quotationItem.packQuantity2, quotationItem.packageSize2);
                                hasRestore = true;
                            }

                        }


                    }


                }


                if (hasRestore) {
                    logger.info("out factory product  before upate  :" + product.name + ",fob" + product.fob);
                    ProductAnalytics.updateForeignFactoryRelate(product, globalData);
                    productRepository.save(product);
                    updateCount++;

                    logger.info("out factory product  after update  :" + product.name + ",fob" + product.fob);
                }
                productRepository.flush();


            }
        }


        logger.info("out factory product count :" + products.size());
        logger.info("restore pack info from quotation count :" + updateCount);


    }


    private void updateProductPackInfo(Product product, int insideBoxCount, int packQuantity, String packageSize) {
        product.insideBoxQuantity = insideBoxCount;
        product.packQuantity = packQuantity;
        float[] packSize = StringUtils.decouplePackageString(packageSize);
        product.packLong = packSize[0];
        product.packWidth = packSize[1];
        product.packHeight = packSize[2];
    }
}
