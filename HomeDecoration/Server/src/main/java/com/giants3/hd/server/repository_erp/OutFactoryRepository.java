package com.giants3.hd.server.repository_erp;
//

import com.giants3.hd.entity.OutFactory;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
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


    //查找所有的加工户信息
    private static final java.lang.String FIND_ALL = "select DEP,NAME from DEPT where MAKE_ID=3  order by NAME asc";





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
}
