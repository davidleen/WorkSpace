package com.giants3.hd.server.repository_erp;

import com.giants3.hd.entity.ErpOrder;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

/**
 * 从第三方数据库读取erpOrder数据相关 订单
 */
@Repository
public class ErpOrderRepository  extends ErpRepository{


    public static final String KEY_ORDER = "SO";
    public static final String OS_ID = "OS_ID";
    public static final String KEY_OS_NO = "OS_NO";
    public static final String KEY_ITM = "ITM";
    public static final String KEY_PRD_NO= "PRD_NO";
    private static final String KEY_SALE_NO = "SAL_NO";
    public static final String CHK_DD_START = "chk_dd_start";
    public static final String CHK_DD_END = "chk_dd_end";

    //业务员查询条件从句
    public static final  String   SALE_WHERE_CAUSE=" and p.sal_no in ( :SAL_NO ) ";
    /**
     * AS varchar(8000)   在sqlserver 2000 中  最大的varchar 长度为8000 varchar(max) 会报错。
     */
    //模糊查找  os_no like  ：os_no
    public static final String SQL_ORDER_LIST_SEARCH = "select  a.os_dd, a.chk_dd ,a.os_no,a.cus_no,a.cus_os_no , a.sal_no ,a.rem,a.est_dd,b.so_data from  (select p.os_dd    ,  p.chk_dd ,CAST(p.os_no AS varchar) as os_no ,CAST(p.cus_no AS varchar) as cus_no ,CAST(p.cus_os_no AS varchar) as  cus_os_no , CAST (p.sal_no as VARCHAR ) as sal_no , CAST(p.rem AS varchar(8000)) as rem ,p.est_dd  from  V_MF_POS p where p.OS_ID=:OS_ID and p.OS_NO like :OS_NO)  a  LEFT JOIN\n" +
            "  \n" +
            "(select pz.os_id,pz.os_no,pz.so_data from  MF_POS_Z pz where pz.OS_ID=:OS_ID and pz.OS_NO like :OS_NO )   b on a.os_no=b.os_no   order by a.os_dd DESC  ,a.os_no desc ";
    //模糊查找  os_no like  ：os_no
    public static final String SQL_ORDER_LIST_SEARCH_WITH_SALES = "select  a.os_dd,a.chk_dd  ,a.os_no,a.cus_no,a.cus_os_no , a.sal_no ,a.rem,a.est_dd,b.so_data from  (select p.os_dd , p.chk_dd    ,CAST(p.os_no AS varchar) as os_no ,CAST(p.cus_no AS varchar) as cus_no ,CAST(p.cus_os_no AS varchar) as  cus_os_no , CAST (p.sal_no as VARCHAR ) as sal_no , CAST(p.rem AS varchar(8000)) as rem ,p.est_dd  from  V_MF_POS p where p.OS_ID=:OS_ID and p.OS_NO like :OS_NO "+SALE_WHERE_CAUSE+ ")  a  LEFT JOIN\n" +
            "  \n" +
            "(select pz.os_id,pz.os_no,pz.so_data from  MF_POS_Z pz where pz.OS_ID=:OS_ID and pz.OS_NO like :OS_NO )   b on a.os_no=b.os_no   order by a.os_dd DESC  ,a.os_no desc ";

    //精确查找， os_no=  ：os_no
    public static final String SQL_ORDER_LIST_FIND = "select  a.os_dd,a.chk_dd  ,a.os_no,a.cus_no,a.cus_os_no , a.sal_no ,a.rem,a.est_dd,b.so_data from  (select p.os_dd,  p.chk_dd    ,CAST(p.os_no AS varchar) as os_no ,CAST(p.cus_no AS varchar) as cus_no ,CAST(p.cus_os_no AS varchar) as  cus_os_no , CAST (p.sal_no as VARCHAR ) as sal_no , CAST(p.rem AS varchar(8000)) as rem ,p.est_dd  from  V_MF_POS p where p.OS_ID=:OS_ID and p.OS_NO = :OS_NO)  a  LEFT JOIN\n" +
            "  \n" +
            "(select pz.os_id,pz.os_no,pz.so_data from  MF_POS_Z pz where pz.OS_ID=:OS_ID and pz.OS_NO = :OS_NO )   b on a.os_no=b.os_no   order by a.os_dd DESC ,a.os_no desc ";

    public static final String SQL_ORDER_LIST_COUNT = " select  count(p.os_no)  from  V_MF_POS p where p.OS_ID=:OS_ID and p.OS_NO like :OS_NO  ";

    public static final String CheckDateWhereCause = " and p.chk_dd> :chk_dd_start and p.chk_dd < :chk_dd_end ";
    /**
     * 根据验货日期关键字查询记录数
     */
    public static final String SQL_ORDER_LIST_COUNT_CHECK_DATE =SQL_ORDER_LIST_COUNT+ CheckDateWhereCause;
    public static final String SQL_ORDER_LIST_COUNT_WITH_SALES = " select  count(p.os_no) as count  from  V_MF_POS p where p.OS_ID=:OS_ID and p.OS_NO like :OS_NO    "  +SALE_WHERE_CAUSE;

    public  static final String OR_WHERE_PRD_LIKE_STUB ="orxxxxxxxxx";
    public  static final String AND_WHERE_PRD_LIKE_STUB ="andxxxxxxxxx";
    public  static final String OR_WHERE_PRD_LIKE_SQL =" or  prd_no like :PRD_NO ";
    public  static final String AND_WHERE_PRD_LIKE_SQL =" and  prd_no like :PRD_NO ";
    private static final String OS_NO_CONDITION_EQUALS = " os_no = ";
    private static final String OS_NO_CONDITION_LIKE = " os_no like ";
    public static String sql_item_1 = "   select os_no,os_dd,itm,bat_no,prd_no,prd_name,id_no, up,qty,amt from tf_pos  where os_id=:OS_ID and "+ OS_NO_CONDITION_EQUALS +":OS_NO " + OR_WHERE_PRD_LIKE_STUB;
    public static String sql_item_2 = " select isnull(htxs,0) as htxs,isnull(so_zxs,0) as so_zxs ,khxg,isnull(xgtj,0) as xgtj , isnull(zxgtj,0) as zxgtj ,hpgg ,os_no,itm from tf_pos_z    where os_id=:OS_ID and "+ OS_NO_CONDITION_EQUALS +":OS_NO " ;

    /**
     * 订单指定查询细项
     */
    public static final String SQL_ORDER_ITEM  = "  select  a.* ,isnull(b.htxs,0) as htxs, isnull(b.so_zxs,0) as so_zxs   ,      b.khxg, isnull(b.xgtj,0) as xgtj , isnull(b.zxgtj,0) as zxgtj  ,b.hpgg, c.so_data,isnull(d.ut,'') as ut ,isnull(d.idx1,'') as idx1 from (  " + sql_item_1 + ") a left outer join (" + sql_item_2 + ") b on a.os_no=b.os_no and a.itm=b.itm " +
            " left outer join (select os_no,so_data from mf_pos_z ) as c on a.os_no=c.os_no " +


            " left outer join (select ut,prd_no,idx1 from prdt where knd='2' "+ AND_WHERE_PRD_LIKE_STUB +") as d on d.prd_no=a.prd_no " +
            " order by a.os_no DESC ,a.itm asc";
 /**
     * 订单指定查询细项
     */
    public static final String SQL_ORDER_ITEM_LIST = SQL_ORDER_ITEM.replace(OR_WHERE_PRD_LIKE_STUB,"").replace(AND_WHERE_PRD_LIKE_STUB,"");


    /**
     * 订单item模糊查询
     */
    public static final String SEARCH_ORDER_ITEMS=SQL_ORDER_ITEM.replace(OS_NO_CONDITION_EQUALS,OS_NO_CONDITION_LIKE).replace(OR_WHERE_PRD_LIKE_STUB, OR_WHERE_PRD_LIKE_SQL).replace(AND_WHERE_PRD_LIKE_STUB, AND_WHERE_PRD_LIKE_SQL);


    /**
     * 订单item模糊查询 总记录数
     */
    public static final String SEARCH_COUNT_ORDER_ITEMS=(" select count(*) from ("+sql_item_1 +") a left outer join (" + sql_item_2 + ") b on a.os_no=b.os_no and a.itm=b.itm ").replace(OS_NO_CONDITION_EQUALS,OS_NO_CONDITION_LIKE).replace(OR_WHERE_PRD_LIKE_STUB, OR_WHERE_PRD_LIKE_SQL).replace(AND_WHERE_PRD_LIKE_STUB, AND_WHERE_PRD_LIKE_SQL);

    //模糊查找  os_no like  ：os_no  and  chk_dd in[]
    public static final String SQL_ORDER_LIST_SEARCH_WITH_CHECK_DATE = "select  a.os_dd,a.os_no,a.cus_no,a.cus_os_no , a.sal_no ,a.rem,a.est_dd,a.chk_dd  ,b.so_data from  (select p.os_dd  ,p.chk_dd  ,CAST(p.os_no AS varchar) as os_no ,CAST(p.cus_no AS varchar) as cus_no ,CAST(p.cus_os_no AS varchar) as  cus_os_no , CAST (p.sal_no as VARCHAR ) as sal_no , CAST(p.rem AS varchar(8000)) as rem ,p.est_dd  from  V_MF_POS p where p.OS_ID=:OS_ID and p.OS_NO like :OS_NO "+  CheckDateWhereCause+" )  a  LEFT JOIN\n" +
            "  \n" +
            "(select pz.os_id,pz.os_no,pz.so_data from  MF_POS_Z pz where pz.OS_ID=:OS_ID and pz.OS_NO like :OS_NO )   b on a.os_no=b.os_no   order by a.chk_dd DESC  ";


    public ErpOrderRepository( ) {

    }


    /*
    *   查询订单
    *   返回2个元素数组    【0】 表示查询记录数
    *       【1】 表示查询的结果列表  List<ErpOrder>
     */
    public List<ErpOrder> findOrders(String os_no, int pageIndex, int pageSize) {




        List<ErpOrder> orders = getOrderListQuery(SQL_ORDER_LIST_SEARCH).setParameter(OS_ID, KEY_ORDER). setParameter(KEY_OS_NO, "%" + os_no + '%').setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).list();


        modifyDateString(orders);

        return orders;


    }

    /*
   *   查询订单
   *   返回2个元素数组
      *   @Param saleCodes  指定的业务员
      *   【0】 表示查询记录数
   *       【1】 表示查询的结果列表  List<ErpOrder>
    */
    public List<ErpOrder> findOrders(String os_no, List<String> saleCodes,int pageIndex, int pageSize) {



        List<ErpOrder> orders = getOrderListQuery(SQL_ORDER_LIST_SEARCH_WITH_SALES).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, "%" + os_no + '%').setParameterList(KEY_SALE_NO,saleCodes).setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).list();


        modifyDateString(orders);

        return orders;


    }



    /**
     * 获取关键字查询记录总数
     *
     * @param key
     * @return
     */
    public int getOrderCountByKey(String key) {
        return (Integer) getEntityManager().createNativeQuery(SQL_ORDER_LIST_COUNT).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, "%" + key + '%').getSingleResult();
    }

    /**
     * 获取关键字查询记录总数
     *
     * @param key
     * @return
     */
    public int getOrderCountByKey(String key,List<String> saleNos) {


        org.hibernate.Query      query =   getEntityManager().createNativeQuery(SQL_ORDER_LIST_COUNT_WITH_SALES).unwrap(SQLQuery.class) ;

        return (Integer) (query.setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, "%" + key + '%').setParameterList(KEY_SALE_NO,saleNos).list().get(0));
    }



    private org.hibernate.Query getOrderListQuery(String sql)
    {


        Query query = getEntityManager().createNativeQuery(sql) ;
       return query.unwrap(SQLQuery.class).addScalar("os_dd", StringType.INSTANCE)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("cus_os_no", StringType.INSTANCE)
                .addScalar("rem", StringType.INSTANCE)
                .addScalar("est_dd", StringType.INSTANCE)
                .addScalar("sal_no", StringType.INSTANCE)
               .addScalar("chk_dd", StringType.INSTANCE)
                .addScalar("so_data", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpOrder.class));


    }

    /**
     * 调整日期数据
     * @param data
     */
    protected void modifyDateString(List<ErpOrder> data)
    {
        if(data==null) return ;
        for(ErpOrder item:data)
        {

            //时间相关 取前10
            if(!StringUtils.isEmpty(item.os_dd)) {
                item.os_dd=item.os_dd.trim();
                if (item.os_dd.length() > 10) {
                    item.os_dd=item.os_dd.substring(0, 10);
                }
            }

            if(!StringUtils.isEmpty(item.est_dd)) {
                item.est_dd=item.est_dd.trim();
                if (item.est_dd.length() > 10) {
                    item.est_dd=item.est_dd.substring(0, 10);
                }
            }

            if(!StringUtils.isEmpty(item.so_data)) {
                item.so_data=item.so_data.trim();
                if (item.so_data.length() > 10) {
                    item.so_data=item.so_data.substring(0, 10);
                }
            }

            if(!StringUtils.isEmpty(item.chk_dd)) {
                item.chk_dd=item.chk_dd.trim();
                if (item.chk_dd.length() > 10) {
                    item.chk_dd=item.chk_dd.substring(0, 10);
                }
            }


        }
    }
    /**
     * 查看订单item
     *
     * @param orderNo
     * @return
     */
    public List<ErpOrderItem> findItemsByOrderNo(String orderNo) {


        final Query query = getEntityManager().createNativeQuery(SQL_ORDER_ITEM_LIST).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, orderNo);
        List<ErpOrderItem> result = getOrderItemListQuery(query).list();



        for(ErpOrderItem item:result )
        {

            item.so_data=StringUtils.clipSqlDateData(item.so_data);
            item.os_dd=StringUtils.clipSqlDateData(item.os_dd);
        }




        return result;


    }

    /**
     * 查看订单item
     *
     * @param key
     * @return
     */
    public List<ErpOrderItem> findItemsByOrderNoLike(String key,int pageIndex,int pageSize) {
        final String value = StringUtils.sqlLike(key);
        Query  query=   getEntityManager().createNativeQuery(
                SEARCH_ORDER_ITEMS).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, value).setParameter(KEY_PRD_NO, value);


        List<ErpOrderItem> result = getOrderItemListQuery(query).setFirstResult(pageIndex*pageSize).setMaxResults(pageSize).list();

        for(ErpOrderItem item:result )
        {

            item.so_data=StringUtils.clipSqlDateData(item.so_data);
            item.os_dd=StringUtils.clipSqlDateData(item.os_dd);
        }



        return result;


    }


    /**
     * 查找指定条件的 订单项目数量， 这个可以优化
     * @param key
     * @return
     */

    public  int getCountOfSearchOrderItem(String key)
    {

        final String value = StringUtils.sqlLike(key);
        return (Integer) getEntityManager().createNativeQuery(SEARCH_COUNT_ORDER_ITEMS ).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, value).setParameter(KEY_PRD_NO, value)

                .getSingleResult();
    }


    /**
     * 精确查询订单
     * @param os_no
     * @return
     */
    public ErpOrder findOrderByNO(String os_no) {


        List<ErpOrder> orders = getOrderListQuery(SQL_ORDER_LIST_FIND).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO,   os_no  ).list();

                if(orders!=null&&orders.size()>0)
                {

                    modifyDateString(orders);
                    ErpOrder order=orders.get(0);
                    return order;

                }


        return null;
    }

    /**
     * 根据关键字验货日期的报表
     * @param key
     * @param dateStart
     * @param dateEnd
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<ErpOrder> findOrderByKeyCheckDate(String key, String dateStart, String dateEnd, int pageIndex, int pageSize) {

        List<ErpOrder> orders = getOrderListQuery(SQL_ORDER_LIST_SEARCH_WITH_CHECK_DATE).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, "%" + key + '%').setParameter(KEY_OS_NO, "%" + key + '%')
                .setParameter(CHK_DD_START, dateStart).setParameter(CHK_DD_END,dateEnd).setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).list();


        modifyDateString(orders);
        return orders;
    }


    /**
     * 获取关键字与验货日期查询记录总数
     *
     * @param key
     * @return
     */
    public int getOrderCountByKeyAndCheckDate(String key,String dateStart,String dateEnd) {
        return (Integer) getEntityManager().createNativeQuery(SQL_ORDER_LIST_COUNT_CHECK_DATE).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, "%" + key + '%')
                .setParameter("chk_dd_start", dateStart).setParameter("chk_dd_end",dateEnd)
                .getSingleResult();
    }

    /**
     * 查询id_no
     * @param osNo
     * @param itm
     * @return
     */
    public String  findId_noByOrderItem(String osNo, int itm) {
        final Query query = getEntityManager().createNativeQuery("select id_no from tf_pos where os_No =:"+KEY_OS_NO+" and itm=:"+KEY_ITM+" and os_id='SO'") .setParameter(KEY_OS_NO, osNo).setParameter(KEY_ITM,itm
        );
      return (String) query.getSingleResult() ;




    }
}
