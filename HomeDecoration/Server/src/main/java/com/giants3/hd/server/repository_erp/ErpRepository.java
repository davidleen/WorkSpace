package com.giants3.hd.server.repository_erp;

import com.giants3.hd.entity.ErpOrderItem;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by davidleen29 on 2017/6/10.
 */
@NoRepositoryBean
public class ErpRepository {


    @Autowired
    @Qualifier("erpEntityManagerFactory")
    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    protected EntityManager getEntityManager() {

        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }
    protected org.hibernate.Query getOrderItemListQuery(Query query) {


        org.hibernate.Query hQuery = query.unwrap(SQLQuery.class)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("bat_no", StringType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("id_no", StringType.INSTANCE)
                .addScalar("up", FloatType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .addScalar("amt", FloatType.INSTANCE)
                .addScalar("htxs", IntegerType.INSTANCE)
                .addScalar("so_zxs", IntegerType.INSTANCE)
                .addScalar("khxg", StringType.INSTANCE)
                .addScalar("xgtj", FloatType.INSTANCE)
                .addScalar("zxgtj", FloatType.INSTANCE)
                .addScalar("hpgg", StringType.INSTANCE)
                .addScalar("ut", StringType.INSTANCE)
                .addScalar("os_dd", StringType.INSTANCE)
                .addScalar("so_data", StringType.INSTANCE)
                .addScalar("idx1", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpOrderItem.class));
        return hQuery;

    }


    protected <T> List<T> listQuery(SQLQuery query, Class<T> aclass) {


        return listQuery(query, aclass, 0, 0);


    }

    protected <T> List<T> listQuery(SQLQuery query, Class<T> aclass, int pageIndex, int pageSize) {


        org.hibernate.Query hibernateQuery = query
                .setResultTransformer(Transformers.aliasToBean(aclass));
        if (pageSize > 0) {
            hibernateQuery = hibernateQuery.setFirstResult(pageIndex * pageSize).setMaxResults(pageSize);
        }

        return hibernateQuery.list();


    }


    protected int getCount(Query query) {
        return (int) query.getSingleResult();
    }
}
