package com.giants3.hd.server.repository_erp;

import com.giants3.hd.server.utils.SqlScriptHelper;
import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.entity.StockXiaoku;
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
 * 进货单里的数据是外厂到仓库的信息，缴库单的数据是车间到仓库的信息，销货单的数据是仓库到装柜的信息，
 */
@Repository
public class ErpStockSubmitRepository  extends  ErpRepository{

//进货单表身
//    select * FROM  TF_PSS WHERE PS_ID='PC' AND WH='CP'


    private static final String ID_NO = "ID_NO";


    //数据库数据值大小写匹配问题处理。--- 全部转换成大写，避免因数据库数据大小写敏感导致查询失败。
    private static final String CP_UPPER = " UPPER ('CP') ";
    private static final String PC_UPPER = " UPPER ('PC') ";
    private static final String YF_LIKE_UPPPER = " UPPER ('%YF%') ";
    private static final String SO_UPPER = " UPPER ('SO') ";
    private static final String SA_UPPER = " UPPER ('SA') ";
    //数据库数据值大小写匹配问题处理。--- 全部转换成大写，避免因数据库数据大小写敏感导致查询失败。



    public static final String stockInSql = "select os_no as so_no ,PRD_NO,PRD_NAME, PRD_MARK, ID_NO  ,BAT_NO,CUS_OS_NO,QTY  from TF_PSS WHERE  PS_ID=" + PC_UPPER + " AND WH=" + CP_UPPER + "  and  so_no like " + YF_LIKE_UPPPER + " and  ID_NO  is not null ";

    //缴库单
    //   select * FROM  TF_MM0 WHERE WH='CP'

    public static final String submitSql = "select SO_NO ,PRD_NO, PRD_NAME  ,ID_NO  ,BAT_NO,CUS_OS_NO,QTY from TF_MM0 where wh=" + CP_UPPER;

    //材料信息
    public static final String bomSql = "select BOM_NO ,KHXG,SO_ZXS,XGTJ from TF_MF_BOM_Z  where KHXG IS NOT NULL   and bom_no =:" + ID_NO + " ";


    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";



    /**
     * 查询厂家入库记录=
     */
    public static final String stockInFromFactory = "   select ps.ps_no as no, ps.so_no,ps.itm,ps.PRD_NO,ps.PRD_NAME,ps.PRD_MARK,ps.BAT_NO,ps.CUS_OS_NO,ps.qty,ps.dd,ps.type,pos.id_no ,mps.dept from (select PRD_MARK as so_no ,itm,PRD_NO,PRD_NAME, PRD_MARK, BAT_NO,CUS_OS_NO,QTY ,ps_dd as dd ,2 as type ,ps_no from TF_PSS   WHERE  PS_ID=" + PC_UPPER + " AND WH=" + CP_UPPER + "  and  PRD_MARK like " + YF_LIKE_UPPPER + "  and ps_dd  >= :" + START_DATE + "  and ps_dd  <= :" + END_DATE + " ) as ps inner join ( select os_no ,itm,prd_no,id_no,bat_no FROM  TF_POS where OS_ID=" + SO_UPPER + " ) as pos  on ps.PRD_MARK=pos.os_no  and ps.prd_no=pos.prd_no and  ps.bat_no=pos.bat_no     inner join (select  cus_no  as dept ,ps_no from mf_pss  WHERE  PS_ID=" + PC_UPPER + "      and ps_dd  >= :" + START_DATE + "  and ps_dd  <= :" + END_DATE + ")    as  mps  on mps.ps_no= ps.ps_no";

    //车间入库记录   缴库
    public static final String StockSubmitSql = " select  mm_no as no,SO_NO ,itm ,PRD_NO,PRD_NAME,PRD_MARK  ,BAT_NO,CUS_OS_NO,QTY ,mm_dd as dd   ,1 as type , ID_NO ,'' as dept from TF_MM0 where wh=" + CP_UPPER + "   and mm_dd  >= :" + START_DATE + "  and mm_dd  <= :" + END_DATE;

    // +  StockSubmitSql+ "  union     "
    //查询入库与缴库的产品数据
    public static   String stockInAndSubmitSql ;
    //出销库单
    //  select * FROM  TF_PSS WHERE PS_ID='SA' AND WH='CP'
    public static final String stockOutTransportSql = " select * from (select os_no as so_no ,PRD_NO,PRD_NAME, PRD_MARK ,BAT_NO,CUS_OS_NO,QTY ,ps_dd as dd ,  3 as type, ID_NO ,'' as dep  from TF_PSS WHERE  PS_ID=" + SA_UPPER + " AND WH=" + CP_UPPER + "  and  PRD_MARK like " + YF_LIKE_UPPPER + " and  ID_NO  is not null and ps_dd  >= :" + START_DATE + "  and ps_dd  <= :" + END_DATE + " ) as a   inner join  (select BOM_NO ,KHXG,SO_ZXS,XGTJ from MF_BOM_Z  where KHXG IS NOT NULL  )  as b on a.ID_NO=b.BOM_NO order by a.dd desc ";
    public static final String stockXiaoKuSql = " select PS_ID   ,PS_NO    ,TCGS  ,isnull(XHDG ,0) as xhdg  ,XHFQ  ,XHGH     ,XHGX   ,XHPH from   MF_PSS_Z  WHERE PS_ID=" + SA_UPPER + " and ps_no like :ps_no  order by ps_no desc ";
    public static final String stockXiaoKuRecordCountSql = " select  count(*) from   MF_PSS_Z  WHERE PS_ID=" + SA_UPPER + "    and ps_no like :ps_no   ";

    public static final String stockXiaokuItemSql = " select * from (select ps_no as no, os_no as so_no ,PRD_NO,PRD_NAME, PRD_MARK ,BAT_NO,CUS_OS_NO,QTY ,ps_dd as dd ,  3 as type, ID_NO ,'' as dept,ITM  from TF_PSS WHERE  PS_ID=" + SA_UPPER + " AND WH=" + CP_UPPER + "   and ps_no  = :ps_no ) as a    inner join  (select BOM_NO ,KHXG,SO_ZXS,XGTJ from MF_BOM_Z  where KHXG IS NOT NULL  )  as b on a.ID_NO=b.BOM_NO  inner join (select os_no, cus_no from V_mf_pos    ) as c on a.so_no = c.os_no order by a.ITM asc ";


    //销库单明细日期区间查询  并关联销库记录数据
    public static final String searchstockXiaokuItemSql = " select * from (select ps_no as no, os_no as so_no ,PRD_NO,PRD_NAME, PRD_MARK ,BAT_NO,CUS_OS_NO,QTY ,ps_dd as dd ,  3 as type, ID_NO ,'' as dept,ITM  from TF_PSS WHERE  PS_ID=" + SA_UPPER + " AND WH=" + CP_UPPER + "   and ps_no  like :ps_no   and ps_dd  >= :" + START_DATE + "  and ps_dd  <= :" + END_DATE + " ) as a    inner join  (select BOM_NO ,KHXG,SO_ZXS,XGTJ from MF_BOM_Z  where KHXG IS NOT NULL  )  as b on a.ID_NO=b.BOM_NO  inner join (select os_no, cus_no from V_mf_pos where OS_ID=" + SO_UPPER + " ) as c on a.so_no = c.os_no    " +
            " inner  join (select PS_ID,PS_NO,TCGS,isnull(XHDG,0) as XHDG ,XHFQ,XHGBQ,XHGH,XHGX,XHPH  from MF_PSS_Z ) as d on  a.no=d.ps_no " +//关联柜子相关数据
            "" +
            " order by  LEFT (convert(nvarchar(50),a.dd, 120) ,7) desc,cast (ISNULL(xhdg,0) as float)  ASC ";//按月降序  按柜号升序


    public static final String KEY_CK_NO = "ck_no";
    public static final String KEY_SAL_NO = "SAL_NO";
    public static final String CUS_NO = "cus_no";
    public static final String CK_NO = "ck_no";



    /**
     * AS varchar(8000)   在sqlserver 2000 中  最大的varchar 长度为8000 varchar(max) 会报错。
     */

    public static final String STOCK_OUT_ITEM_LIST = " select B.ck_no,B.itm, B.PRD_NO ,b.id_no,b.os_no,b.bat_no,B.CUS_OS_NO,isnull(B.UP,0) as UP, isnull(B.QTY,0) as  QTY, isnull(B.AMT,0) as   AMT ,A.SO_ZXS,A.IDX_NAME,A.XS,A.KHXG,A.XGTJ,A.ZXGTJ,isnull(A.JZ1,0)as JZ1,isnull(A.MZ,0) as MZ  from TF_CK_Z A FULL JOIN TF_CK B ON A.CK_NO=B.CK_NO AND A.ITM=B.ITM  where A.ck_no=:ck_no  order by B.itm ASC  ";


    //搜索基句
    public static final String STOCK_OUT_LIST = " select A.CK_NO,A.CK_DD,A.CUS_NO,A.sal_no,B.MDG,B.TDH,B.GSGX ,C.ADR2,C.TEL1,C.FAX from   MF_CK  A FULL  JOIN  MF_CK_Z B  on A.CK_ID=b.CK_ID  and a.ck_no=b.ck_no  LEFT JOIN CUST C on A.CUS_NO=c.CUS_NO  ";

    //按日期排序
    public static final String STOCK_OUT_ORDER = " order by  A.ck_dd desc ";
    public static final String STOCK_OUT_SEARCH = STOCK_OUT_LIST + " where A.CUS_NO like :cus_no or A.CK_NO like :ck_no " + STOCK_OUT_ORDER;


    public static final String SQL_WHERE_SALE_CAUSE = " and a.sal_no in ( :SAL_NO ) ";

    public static final String STOCK_OUT_SEARCH_WITH_SALE = STOCK_OUT_LIST + " where ( A.CUS_NO like :cus_no or A.CK_NO like :ck_no )  " + SQL_WHERE_SALE_CAUSE + STOCK_OUT_ORDER;

    public static final String STOCK_OUT_COUNT_WITH_SALE = "select  count(*) as count from   MF_CK  A FULL  JOIN  MF_CK_Z B  on A.CK_ID=b.CK_ID  and a.ck_no=b.ck_no  LEFT JOIN CUST C on A.CUS_NO=c.CUS_NO    where ( A.CUS_NO like :cus_no or A.CK_NO like :ck_no )  " + SQL_WHERE_SALE_CAUSE;

    public static final String STOCK_OUT_FIND = STOCK_OUT_LIST + " where A.CK_NO = :ck_no " + STOCK_OUT_ORDER;


    public ErpStockSubmitRepository( ) {




        stockInAndSubmitSql= SqlScriptHelper.readScript("stockinandsubmit.sql");
    }

    /*
    *    查询出 进货与缴库 列表  日期参数格式 "2016-07-11"
     */
    public List<StockSubmit> getStockSubmitList(String key,String startDate, String endData) {




        Query query = getEntityManager().createNativeQuery(stockInAndSubmitSql)

                .setParameter("keywords", StringUtils.sqlLike(key))
                .setParameter(START_DATE, startDate)
                .setParameter(END_DATE, endData);
        List<StockSubmit> orders = getStockSubmits(query);


        return orders;

    }


    /*
    *    查询出库到柜记录  日期参数格式 "2016-07-11"
     */

    private List<StockSubmit> getStockSubmits(Query query) {

      return   getStockSubmits(query, false);
    }
    /*
    *    查询出库到柜记录  日期参数格式 "2016-07-11"
     */

    /**
     *
     * @param query
     * @param fromSearch   搜索查询 额外返回冗余字段。
     * @return
     */
    private List<StockSubmit> getStockSubmits(Query query, boolean fromSearch) {
        final SQLQuery sqlQuery = query.unwrap(SQLQuery.class)

                .addScalar("no", StringType.INSTANCE)
                .addScalar("so_no", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("dd", StringType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("prd_mark", StringType.INSTANCE)
                .addScalar("id_no", StringType.INSTANCE)
                .addScalar("bat_no", StringType.INSTANCE)
                .addScalar("cus_os_no", StringType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .addScalar("so_zxs", IntegerType.INSTANCE)
                .addScalar("type", IntegerType.INSTANCE)
                .addScalar("xgtj", FloatType.INSTANCE)
                .addScalar("khxg", StringType.INSTANCE)
                .addScalar("dept", StringType.INSTANCE);


        if (fromSearch ) {
            //销库信息
            sqlQuery.addScalar("tcgs", StringType.INSTANCE)
                    .addScalar("xhdg", FloatType.INSTANCE)
                    .addScalar("xhgh", StringType.INSTANCE)
                    .addScalar("xhfq", StringType.INSTANCE)
                    .addScalar("xhgx", StringType.INSTANCE)
                    .addScalar("xhph", StringType.INSTANCE);
        }


        final List<StockSubmit> list = sqlQuery


                .setResultTransformer(Transformers.aliasToBean(StockSubmit.class)).list();


        return list;
    }

    /**
     * 根据时间段区间搜索 销库记录列表
     *
     * @param key
     * @param startDate
     * @param endData
     * @return
     */
    public List<StockSubmit> getStockXiaokuItemList(String key, String startDate, String endData) {


        Query query = getEntityManager().createNativeQuery(searchstockXiaokuItemSql)
                .setParameter("ps_no", "%" + key.trim() + "%")
                .setParameter(START_DATE, startDate)
                .setParameter(END_DATE, endData);
        List<StockSubmit> orders = getStockSubmits(query,true);


        return orders;

    }


    /**
     * 根据销库单，查询销库记录列表
     *
     * @param ps_no
     * @return
     */
    public List<StockSubmit> getStockXiaokuItemList(String ps_no) {


        Query query = getEntityManager().createNativeQuery(stockXiaokuItemSql)
                .setParameter("ps_no", ps_no);

        List<StockSubmit> orders = getStockSubmits(query);


        return orders;

    }


    /**
     * 获取销库单列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StockXiaoku> getStockXiaokuList(String key, int pageIndex, int pageSize) {


        org.hibernate.Query query = getStockXiaokuListQuery(stockXiaoKuSql);

        List<StockXiaoku> orders = query.setParameter("ps_no", "%" + key.trim() + "%")
                .setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).list();

        return orders;
    }

    /*
      *   查询出库单明细列表
       */
    public List<ErpStockOut> stockOutList(String key, List<String> salNos, int pageIndex, int pageSize) {


        org.hibernate.Query query = getStockOutListQuery(STOCK_OUT_SEARCH_WITH_SALE);

        query.setParameter(CK_NO, "%" + key + '%').setParameter(CUS_NO, "%" + key + '%').setParameterList(KEY_SAL_NO, salNos);
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
    public int getRecordCountByKey(String key, List<String> saleNos) {


        org.hibernate.Query query = getEntityManager().createNativeQuery(STOCK_OUT_COUNT_WITH_SALE).unwrap(SQLQuery.class);

        return (Integer) (query.setParameter(CK_NO, "%" + key + '%').setParameter(CUS_NO, "%" + key + '%').setParameterList(KEY_SAL_NO, saleNos).list().get(0));
    }


    /**
     * 调整日期数据
     *
     * @param data
     */
    protected void modifyDateString(List<ErpStockOut> data) {
        if (data == null) return;
        for (ErpStockOut item : data) {

            if (!StringUtils.isEmpty(item.ck_dd)) {
                if (item.ck_dd.length() > 10) {
                    item.ck_dd = item.ck_dd.substring(0, 10);
                }
            }
        }
    }


    private org.hibernate.Query getStockOutListQuery(String sql) {
        Query query = getEntityManager().createNativeQuery(sql);
        return query.unwrap(SQLQuery.class).addScalar("ck_no", StringType.INSTANCE)
                .addScalar("ck_dd", StringType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("mdg", StringType.INSTANCE)
                .addScalar("tdh", StringType.INSTANCE)
                .addScalar("gsgx", StringType.INSTANCE)
                .addScalar("adr2", StringType.INSTANCE)
                .addScalar("tel1", StringType.INSTANCE)
                .addScalar("sal_no", StringType.INSTANCE)
                .addScalar("fax", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpStockOut.class));

    }

    private org.hibernate.Query getStockXiaokuListQuery(String sql) {
        Query query = getEntityManager().createNativeQuery(sql);
        return query.unwrap(SQLQuery.class).addScalar("ps_id", StringType.INSTANCE)
                .addScalar("ps_no", StringType.INSTANCE)
                .addScalar("tcgs", StringType.INSTANCE)
                .addScalar("xhdg", FloatType.INSTANCE)
                .addScalar("xhgh", StringType.INSTANCE)
                .addScalar("xhfq", StringType.INSTANCE)
                .addScalar("xhgx", StringType.INSTANCE)
                .addScalar("xhph", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(StockXiaoku.class));

    }


    /**
     * 获取销库记录数量
     *
     * @return
     */
    public int getXiaokuRecordCount(String key) {
        org.hibernate.Query query = getEntityManager().createNativeQuery(stockXiaoKuRecordCountSql).unwrap(SQLQuery.class);

        return (Integer) (query.setParameter("ps_no", "%" + key.trim() + "%").list().get(0));
    }
}
