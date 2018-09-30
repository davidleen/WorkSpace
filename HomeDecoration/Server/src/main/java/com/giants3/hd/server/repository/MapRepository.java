package com.giants3.hd.server.repository;

import com.giants3.hd.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Created by davidleen29 on 2018/9/16.
 */
@Repository
public class MapRepository {
    @Autowired
    @Qualifier("entityManagerFactory")
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {

        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }

    public List<Map> reportQuoteCount(String startDate, String endDate) {

        String sql=" select A.*,b.name,b.pVersion,b.pUnitName,b.thumbnail,b.url from (select     COUNT(*) as count,productId,round(avg(price),2) as avgPrice,round(MAX(price),2) as maxPrice, round(MIN(price),2)  as minPrice  FROM  T_AppQuotationItem where   quotationId in (select id from T_AppQuotation where qDate>=:startDate and qDate<=:endDate and  formal =1) group by productId   ) as A inner join T_Product  B on  a.productId=b.id order by b.name desc ";
    //    String sql = " 	select     COUNT(*) as count,productName,unit ,avg(price) as avgPrice,MAX(price) as maxPrice, MIN(price) as minPrice  FROM  T_AppQuotationItem where   quotationId in (select id from T_AppQuotation where qDate>=:startDate and qDate<=:endDate and formal =1) group by productName,unit  order by productName desc";

        sql = sql.replace(":startDate", StringUtils.sqlQuoteString(startDate));
        sql = sql.replace(":endDate", StringUtils.sqlQuoteString(endDate));

        return query(sql);

    }


    private List<Map> query(String sql) {
        final Query nativeQuery = getEntityManager().createNativeQuery(sql);


        nativeQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return nativeQuery.getResultList();
    }
}
