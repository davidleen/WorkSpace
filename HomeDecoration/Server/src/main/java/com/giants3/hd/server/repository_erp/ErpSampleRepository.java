package com.giants3.hd.server.repository_erp;

import com.giants3.hd.entity_erp.SampleState;
import com.giants3.hd.server.utils.SqlScriptHelper;
import org.hibernate.SQLQuery;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Erp 样品管理
 * Created by davidleen29 on 2017/11/9.
 */
@Repository
public class ErpSampleRepository extends ErpRepository {


    private static String SQL_SAMPLE_STATES = "";
    private static String SQL_BEIBANG_STATES = "";


    public ErpSampleRepository( ) {


        SQL_SAMPLE_STATES = SqlScriptHelper.readScript("search_sample_state.sql");
        SQL_BEIBANG_STATES = SqlScriptHelper.readScript("search_beibang_state.sql");

    }


    public List<SampleState> findByPrdNo(String prdNo, String pVersion) {

        Query query = getEntityManager().createNativeQuery(SQL_SAMPLE_STATES)
                .setParameter("prdNo", prdNo)
                .setParameter("pVersion", pVersion)
//                .setParameter("enddate", endDate)
                ;

        return getQuery(query);

    }


    public List<SampleState> findBeibangByPrdNo(String prdNo, String pVersion)
    {


        String bomNo=prdNo+"->"+pVersion;

        Query query = getEntityManager().createNativeQuery(SQL_BEIBANG_STATES)
                .setParameter("bomNo", bomNo)


                ;

        return getBeibangQuery(query);

    }


    protected List<SampleState> getQuery(Query query) {


        final SQLQuery sqlQuery = query.unwrap(SQLQuery.class)
                .addScalar("ltime", StringType.INSTANCE)
                .addScalar("BL_ID", StringType.INSTANCE)
                .addScalar("factory", StringType.INSTANCE)
                .addScalar("prdNo", StringType.INSTANCE)
                .addScalar("wareHouse", StringType.INSTANCE)
                .addScalar("prdNo", StringType.INSTANCE)
                .addScalar("pVersion", StringType.INSTANCE);
        return listQuery(sqlQuery, SampleState.class);


    }

    protected List<SampleState> getBeibangQuery(Query query) {


        final SQLQuery sqlQuery = query.unwrap(SQLQuery.class)
                .addScalar("bb", StringType.INSTANCE)
                .addScalar("ypbb", StringType.INSTANCE);

        return listQuery(sqlQuery, SampleState.class);


    }



}
