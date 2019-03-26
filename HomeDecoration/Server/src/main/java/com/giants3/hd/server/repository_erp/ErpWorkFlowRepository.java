package com.giants3.hd.server.repository_erp;

import com.giants3.hd.entity_erp.ErpWorkFlowItem;
import com.giants3.hd.server.utils.SqlScriptHelper;
import org.hibernate.SQLQuery;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by davidleen29 on 2019/3/23.
 */
@Repository
public class ErpWorkFlowRepository extends ErpRepository {


    public String sql_work_flow_item = "";

    public ErpWorkFlowRepository() {
        sql_work_flow_item = SqlScriptHelper.readScript("erp_work_flow_item.sql");
    }


    public List<ErpWorkFlowItem> findErpWorkFlowItems(String os_no, int itm) {


        Query query = getEntityManager().createNativeQuery(sql_work_flow_item)
                .setParameter("os_no", os_no)
                .setParameter("itm", itm) ;

        final SQLQuery sqlQuery = query.unwrap(SQLQuery.class)
                .addScalar("tz_no", StringType.INSTANCE)
                .addScalar("tz_dd", StringType.INSTANCE)
                .addScalar("osNo", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("mrpNo", StringType.INSTANCE)
                .addScalar("zc_no", StringType.INSTANCE)
                .addScalar("zc_name", StringType.INSTANCE)
                .addScalar("qty", FloatType.INSTANCE)
                .addScalar("jgh", StringType.INSTANCE);

        return
                listQuery(sqlQuery, ErpWorkFlowItem.class, 0, 0);


    }
}
