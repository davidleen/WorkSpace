package com.giants3.hd.server.repository_erp;
//

import com.giants3.hd.entity.OutFactory;
import com.giants3.hd.entity.ProducerValueConfig;
import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.noEntity.ProducerValueItem;
import com.giants3.hd.noEntity.ProducerValueReport;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

/**
 * 加工户信息
 */

@Repository
public class OutFactoryRepository extends  ErpRepository {

        @Autowired
    @Qualifier("entityManagerFactory")
    private EntityManagerFactory myEntityMangerFactory;
    //查找所有的加工户信息
    private static final java.lang.String FIND_ALL = "select DEP,NAME from DEPT where   MAKE_ID=3  order by NAME asc";





    public OutFactoryRepository()
    {}




    public List<OutFactory> findAll() {


        Query query =getEntityManager().createNativeQuery(FIND_ALL);
        List<OutFactory> orders = query.unwrap(SQLQuery.class)
                .addScalar("dep", StringType.INSTANCE)
                .addScalar("name", StringType.INSTANCE)

                .setResultTransformer(Transformers.aliasToBean(OutFactory.class)).list();


        return orders;


    }






    public List<ProducerValueReport> findValueReport(String key)
    {
        Query nativeQuery = myEntityMangerFactory.createEntityManager().createNativeQuery("select * from View_ProducerWorkingValueReport where dept like :keywords or name like  :keywords ");
        nativeQuery.setParameter("keywords", StringUtils.sqlLike(key));
        List<ProducerValueReport> result=nativeQuery.unwrap(SQLQuery.class)
                .addScalar("dept",StringType.INSTANCE)
                .addScalar("name",StringType.INSTANCE)
                .addScalar("maxOutputValue", DoubleType.INSTANCE)
                .addScalar("minOutputValue", DoubleType.INSTANCE)
                .addScalar("workingValue", DoubleType.INSTANCE)
                .addScalar("workerNum", IntegerType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ProducerValueReport.class))
                .list();


        return result;
    }




    public List<ProducerValueConfig> findValueConfig()
    {
        Query nativeQuery = myEntityMangerFactory.createEntityManager().createNativeQuery("select a.dep as dept,a.name as name ,isnull(b.id,0) as id, isnull(b.maxOutputValue,0) as maxOutputValue ,isnull(b.minOutputValue,0) as minOutputValue ,isnull(b.workerNum,0) as workerNum  from (select DEP,NAME from DB_YF01.dbo.DEPT where   MAKE_ID=3 and  (STOP_DD IS NULL) ) as a left outer join T_ProducerValueConfig  b  on a.DEP=b.dept  COLLATE Chinese_PRC_90_CI_AI order by a.dep asc ");

        List<ProducerValueConfig> result=nativeQuery.unwrap(SQLQuery.class)
                .addScalar("dept",StringType.INSTANCE)
                .addScalar("name",StringType.INSTANCE)
                .addScalar("id", LongType.INSTANCE)
                .addScalar("maxOutputValue", DoubleType.INSTANCE)
                .addScalar("minOutputValue", DoubleType.INSTANCE)
                .addScalar("workerNum", IntegerType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ProducerValueConfig.class))
                .list();


        return result;
    }


    public List<ProducerValueItem> findProduceValueItems(String dept)
    {
        Query nativeQuery = myEntityMangerFactory.createEntityManager().createNativeQuery("select * from View_ProducerValueDetail where dept =:dept order by os_no desc ,prd_no asc ");
        nativeQuery.setParameter("dept", dept);
        List<ProducerValueItem> result=nativeQuery.unwrap(SQLQuery.class)
                .addScalar("dept",StringType.INSTANCE)
                .addScalar("name",StringType.INSTANCE)
                .addScalar("os_no",StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("value", DoubleType.INSTANCE)

                .addScalar("zc_no",StringType.INSTANCE)
                .addScalar("zc_name",StringType.INSTANCE)
                .addScalar("mrp_no",StringType.INSTANCE)
                .addScalar("prd_no",StringType.INSTANCE)
                .addScalar("prd_name",StringType.INSTANCE)
                .addScalar("os_dd",StringType.INSTANCE)
                .addScalar("id_no",StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ProducerValueItem.class))
                .list();

        for (ProducerValueItem item:result)
        {
            if (!StringUtils.isEmpty(item.os_dd)) {
                if (item.os_dd.length() > 10) {
                    item.os_dd = item.os_dd.substring(0, 10);
                }
            }
            item.thumbnail= FileUtils.getErpProductPictureUrl(item.id_no,"");
        }


        return result;
    }




}
