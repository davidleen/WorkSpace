package com.giants3.hd.server.repository_erp;

import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * 从第三方数据库读取出库单数据相关
 */
@Repository
public class ErpStockOutRepository extends ErpRepository {


    public static final String KEY_CK_NO = "ck_no";
    public static final String KEY_SAL_NO = "SAL_NO";
    public static final String CUS_NO = "cus_no";
    public static final String CK_NO = "ck_no";


    /**
     * AS varchar(8000)   在sqlserver 2000 中  最大的varchar 长度为8000 varchar(max) 会报错。
     */

    public static final String STOCK_OUT_ITEM_LIST = " select B.ck_no,B.itm, B.PRD_NO ,b.id_no,b.os_no,b.bat_no,B.CUS_OS_NO,isnull(B.UP,0) as UP, isnull(B.QTY,0) as  QTY, isnull(B.AMT,0) as   AMT ,A.SO_ZXS,A.IDX_NAME,A.XS,A.KHXG,A.XGTJ,A.ZXGTJ,isnull(A.JZ1,0)as JZ1,isnull(A.MZ,0) as MZ ,A.HPGG ,isnull(c.rem,'') as hsCode , isnull(m.jmcc,'') as jmcc   from  (select * from TF_CK_Z  where  ck_no=:ck_no)  A FULL JOIN   (select * from TF_CK  where  ck_no=:ck_no) B ON A.CK_NO=B.CK_NO AND A.ITM=B.ITM  " +
            "   left outer join ( select distinct prd_no,rem from  (select rem,idx_no from INDX  ) a inner join    ( select idx1,prd_no from prdt )b  on a.idx_no=b.idx1 where a.rem is  not null) c on b.prd_no=c.prd_no " +

            " left outer join (select bom_no, jmcc from  MF_BOM_Z )  m on b.id_no=m.bom_no  "+
            "  order by B.itm ASC  ";



    //搜索基句
    public static final String  STOCK_OUT_LIST=" select A.CK_NO,A.CK_DD,A.CUS_NO,A.sal_no,B.MDG,B.TDH,B.GSGX, C.name,C.fp_name,C.ADR2,C.TEL1,C.FAX from   MF_CK  A FULL  JOIN  MF_CK_Z B  on A.CK_ID=b.CK_ID  and a.ck_no=b.ck_no  LEFT JOIN CUST C on A.CUS_NO=c.CUS_NO  ";

    //按日期排序
    public static final String STOCK_OUT_ORDER=" order by  A.ck_dd desc ";
    public static final String STOCK_OUT_SEARCH=STOCK_OUT_LIST +" where A.CUS_NO like :cus_no or A.CK_NO like :ck_no " +STOCK_OUT_ORDER;


    public static final String  SQL_WHERE_SALE_CAUSE=" and a.sal_no in ( :SAL_NO ) ";

    public static final String STOCK_OUT_SEARCH_WITH_SALE=STOCK_OUT_LIST +" where ( A.CUS_NO like :cus_no or A.CK_NO like :ck_no )  "+ SQL_WHERE_SALE_CAUSE +STOCK_OUT_ORDER;

    public static final String STOCK_OUT_COUNT_WITH_SALE="select  count(*) as count from   MF_CK  A FULL  JOIN  MF_CK_Z B  on A.CK_ID=b.CK_ID  and a.ck_no=b.ck_no  LEFT JOIN CUST C on A.CUS_NO=c.CUS_NO    where ( A.CUS_NO like :cus_no or A.CK_NO like :ck_no )  "+ SQL_WHERE_SALE_CAUSE  ;

    public static final String STOCK_OUT_FIND=STOCK_OUT_LIST+" where A.CK_NO = :ck_no " +STOCK_OUT_ORDER;


    public ErpStockOutRepository( ) {

    }
    /*
    *   查询出库单明细列表
     */
    public List<ErpStockOutItem> stockOutItemsList(String ck_no ) {


        Query query = getEntityManager().createNativeQuery(STOCK_OUT_ITEM_LIST)
               .setParameter(KEY_CK_NO, ck_no) ;
        List<ErpStockOutItem> orders = query.unwrap(SQLQuery.class)
                .addScalar("ck_no", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("id_no", StringType.INSTANCE)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("bat_no", StringType.INSTANCE)
                .addScalar("cus_os_no", StringType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                //.addScalar("amt", FloatType.INSTANCE)
                .addScalar("so_zxs", IntegerType.INSTANCE)
                .addScalar("up", FloatType.INSTANCE)
               // .addScalar("xs", FloatType.INSTANCE)
                .addScalar("khxg", StringType.INSTANCE)
                .addScalar("idx_name", StringType.INSTANCE)
                .addScalar("xgtj", FloatType.INSTANCE)
                .addScalar("hpgg", StringType.INSTANCE)
                .addScalar("jz1", FloatType.INSTANCE)
                .addScalar("mz", FloatType.INSTANCE)
                .addScalar("hsCode", StringType.INSTANCE)
                .addScalar("jmcc", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpStockOutItem.class))  .list();




        return orders;

    }

    /*
      *   查询出库单明细列表
       */
    public List<ErpStockOut> stockOutList(String key,List<String> salNos ,int pageIndex, int pageSize) {



        org.hibernate.Query query= getStockOutListQuery(STOCK_OUT_SEARCH_WITH_SALE);

        query.setParameter(CK_NO, "%" + key + '%').setParameter(CUS_NO, "%" + key + '%').setParameterList(KEY_SAL_NO,salNos);
                ;
        List<ErpStockOut> orders = query
                .setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).list();

        modifyDateString(orders);

        return orders;

    }


    /**
     * 获取关键字查询记录总数
     *
     * @param key
     * @return
     */
    public int getRecordCountByKey(String key,List<String> saleNos) {


        org.hibernate.Query      query =   getEntityManager().createNativeQuery(STOCK_OUT_COUNT_WITH_SALE).unwrap(SQLQuery.class) ;

        return (Integer) ( query.setParameter(CK_NO, "%" + key + '%').setParameter(CUS_NO, "%" + key + '%').setParameterList(KEY_SAL_NO,saleNos).list().get(0));
    }


    /**
     * 调整日期数据
     * @param data
     */
    protected void modifyDateString(List<ErpStockOut> data)
    {
        if(data==null) return ;
        for(ErpStockOut item:data)
        {

            if(!StringUtils.isEmpty(item.ck_dd))
            {
                if(item.ck_dd.length()>10)
                {
                    item.ck_dd=item.ck_dd.substring(0,10);
                }
            }
        }
    }


    private org.hibernate.Query getStockOutListQuery(String sql)
    {
        Query query = getEntityManager().createNativeQuery(sql);
     return    query.unwrap(SQLQuery.class).addScalar("ck_no", StringType.INSTANCE)
                .addScalar("ck_dd", StringType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("mdg", StringType.INSTANCE)
                .addScalar("tdh", StringType.INSTANCE)
                .addScalar("gsgx", StringType.INSTANCE)
                .addScalar("name", StringType.INSTANCE)
                .addScalar("fp_name", StringType.INSTANCE)
                .addScalar("adr2", StringType.INSTANCE)
                .addScalar("tel1", StringType.INSTANCE)
                .addScalar("sal_no", StringType.INSTANCE)
                .addScalar("fax", StringType.INSTANCE)
             .setResultTransformer(Transformers.aliasToBean(ErpStockOut.class));

    }

    /*
        *   查询出库单明细列表
         */
    public ErpStockOut findStockOut(String ck_no ) {


        org.hibernate.Query query = getStockOutListQuery(STOCK_OUT_FIND)
                .setParameter("ck_no", ck_no);
        List<ErpStockOut> stockOuts = query.list() ;
        modifyDateString(stockOuts);

        return stockOuts==null||stockOuts.size()==0?null:stockOuts.get(0);

    }



}
