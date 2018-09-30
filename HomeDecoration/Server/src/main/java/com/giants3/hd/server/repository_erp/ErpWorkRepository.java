package com.giants3.hd.server.repository_erp;

import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.ErpOrderItemProcess;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity_erp.Sub_workflow_state;
import com.giants3.hd.entity_erp.WorkFlowMaterial;
import com.giants3.hd.entity_erp.Zhilingdan;
import com.giants3.hd.noEntity.ProduceType;
import com.giants3.hd.server.repository_erp.ErpRepository;
import com.giants3.hd.server.utils.SqlScriptHelper;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * 从第三方数据库  数据相关
 * 物料采购相关的
 */


@Repository
public class ErpWorkRepository extends ErpRepository {

    //流程排序
    private Comparator<ErpOrderItemProcess> comparator = new Comparator<ErpOrderItemProcess>() {
        @Override
        public int compare(ErpOrderItemProcess o1, ErpOrderItemProcess o2) {
            int o1Index = ErpWorkFlow.findIndexByCode(o1.mrpNo.substring(0, 1));
            int o2Index = ErpWorkFlow.findIndexByCode(o2.mrpNo.substring(0, 1));
            return o1Index - o2Index;
        }
    };
    ;

    public String SQL_ZHILINGDAN = "";
    public String SQL_WORK_FLOW_MATERIAL = "";
    public String SQL_ORDER_ITEM_BY_OS_ITM = "";

    public String SQL_ORDER_ITEM_PROCESS_BY_ITM = "";
    public String SQL_ORDER_ITEM_PROCESS_PURCHASE_BY_ITM = "";
    public String SQL_ORDER_ITEM_UNCOMPLETE = "";
    public String SQL_COUNT_ORDER_ITEM_UNCOMPLETE = "";

    public String SQL_ORDER_ITEM_ON_WORK_FLOW = "";
    public String SQL_COUNT_ORDER_ITEM_ON_WORK_FLOW = "";
    public String SQL_ORDER_ITEM_COMPLETE = "";
    public String SQL_COUNT_ORDER_ITEM_COMPLETE = "";
    public String SQL_ORDER_ITEM_HAS_START_AND_UNCOMPLETE = "";
    public String SQL_SUB_WORK_FLOW_STATE = "";




    public ErpWorkRepository( ) {


        if (StringUtils.isEmpty(SQL_ZHILINGDAN)) {


            SQL_ZHILINGDAN = SqlScriptHelper.readScript("zhilingdan");
            SQL_WORK_FLOW_MATERIAL = SqlScriptHelper.readScript("work_flow_material.sql");

            SQL_ORDER_ITEM_PROCESS_BY_ITM = SqlScriptHelper.readScript("orderItemProcess_itm.sql");
            SQL_ORDER_ITEM_PROCESS_PURCHASE_BY_ITM = SqlScriptHelper.readScript("orderItemProcess_purchase_itm.sql");
            SQL_ORDER_ITEM_BY_OS_ITM = SqlScriptHelper.readScript("FindErpOrderItem.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_COMPLETE);
            SQL_ORDER_ITEM_UNCOMPLETE = SqlScriptHelper.readScript("unCompleteOrderItem.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_COMPLETE);
            SQL_COUNT_ORDER_ITEM_UNCOMPLETE = SqlScriptHelper.readScript("unCompleteOrderItemcount.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_COMPLETE);
            SQL_ORDER_ITEM_COMPLETE = SqlScriptHelper.readScript("completeOrderItem.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_COMPLETE);
            SQL_COUNT_ORDER_ITEM_COMPLETE = SqlScriptHelper.readScript("completeOrderItemcount.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_COMPLETE);
            SQL_ORDER_ITEM_HAS_START_AND_UNCOMPLETE = SqlScriptHelper.readScript("unCompleteAndStartWorkOrderItem.sql").replace("VALUE_WORKING_STATE", "" + ErpWorkFlow.STATE_WORKING);
            SQL_ORDER_ITEM_ON_WORK_FLOW = SqlScriptHelper.readScript("order_item_on_work_flow.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_COMPLETE);
            SQL_COUNT_ORDER_ITEM_ON_WORK_FLOW = SqlScriptHelper.readScript("order_item_on_work_flow_count.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_COMPLETE);

            SQL_SUB_WORK_FLOW_STATE = SqlScriptHelper.readScript("sub_workflow.sql");


        }
    }


    /**
     * 查询指令单完成状态表
     *
     * @param osName
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Zhilingdan> searchZhilingdan(String osName, String startDate, String endDate) {


        Query query = getEntityManager().createNativeQuery(SQL_ZHILINGDAN)
                .setParameter("osname", "%" + osName.trim() + "%")
                .setParameter("startdate", startDate)
                .setParameter("enddate", endDate);
        List<Zhilingdan> orders = query.unwrap(SQLQuery.class)
                .addScalar("mo_dd", StringType.INSTANCE)
                .addScalar("mo_no", StringType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("prd_mark", StringType.INSTANCE)
                .addScalar("qty_rsv", IntegerType.INSTANCE)
                .addScalar("mrp_no", StringType.INSTANCE)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("real_prd_name", StringType.INSTANCE)

                .addScalar("caigou_no", StringType.INSTANCE)
                .addScalar("caigouQty", IntegerType.INSTANCE)
                .addScalar("caigou_dd", StringType.INSTANCE)

                .addScalar("jinhuo_no", StringType.INSTANCE)
                .addScalar("jinhuoQty", IntegerType.INSTANCE)
                .addScalar("jinhuo_dd", StringType.INSTANCE)


                .addScalar("jinhuo_dd", StringType.INSTANCE)
                .addScalar("need_days", FloatType.INSTANCE)

                .addScalar("need_dd", IntegerType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(Zhilingdan.class)).list();


        return orders;
    }


    /**
     * 查询采购单完成状态表
     *
     * @param os_no
     * @param itm
     * @return
     */
    public List<ErpOrderItemProcess> findPurchaseOrderItemProcesses(String os_no, int itm) {


        Query query = getEntityManager().createNativeQuery(SQL_ORDER_ITEM_PROCESS_PURCHASE_BY_ITM)

                .setParameter("os_no", os_no)
                .setParameter("itm", itm);
        final List<ErpOrderItemProcess> orderItemProcesses = extractOrderItemProcessFromQuery(query);


        /**
         * 构建外购的生产流程数据
         */
        if (orderItemProcesses.size() > 0) {
            ErpOrderItemProcess process = orderItemProcesses.get(0);
            orderItemProcesses.clear();
            ErpOrderItemProcess first = ObjectUtils.deepCopyWidthJson(process, ErpOrderItemProcess.class);

            first.mrpNo = ErpWorkFlow.FIRST_STEP_CODE + "-" + first.mrpNo;
            orderItemProcesses.add(first);

            ErpOrderItemProcess last = ObjectUtils.deepCopyWidthJson(process, ErpOrderItemProcess.class);
            last.mrpNo = ErpWorkFlow.CODE_CHENGPIN + "-" + last.mrpNo;
            orderItemProcesses.add(last);
        }


        for (ErpOrderItemProcess process : orderItemProcesses) {

            String url = com.giants3.hd.server.utils.FileUtils.getErpProductPictureUrl(process.idNo, "");

            String pVersion = StringUtils.spliteId_no(process.idNo)[1];
            process.pVersion = pVersion;
            //派给外厂， 流程总数量就是单次process数量
            process.orderQty = process.qty;
            process.photoUrl = url;
            process.photoThumb = url;
        }

        return orderItemProcesses;


    }

    /**
     * 查询指令单完成状态表
     *
     * @param os_no
     * @param itm
     * @return
     */
    public List<ErpOrderItemProcess> findOrderItemProcesses(String os_no, int itm) {

        return findOrderItemProcesses(os_no, itm, false);
    }

    /**
     * 查询指令单完成状态表
     *
     * @param os_no
     * @param itm
     * @param includeZuzhuang 是否包含组装流程数据
     * @return
     */
    public List<ErpOrderItemProcess> findOrderItemProcesses(String os_no, int itm, boolean includeZuzhuang) {

        Query query = getEntityManager().createNativeQuery(SQL_ORDER_ITEM_PROCESS_BY_ITM)

                .setParameter("os_no", os_no)
                .setParameter("itm", itm);
        return getErpOrderItemProcesses(query, includeZuzhuang);
    }

    private List<ErpOrderItemProcess> getErpOrderItemProcesses(Query query, boolean includeZuzhuang) {
        List<ErpOrderItemProcess> orders = extractOrderItemProcessFromQuery(query);

        if (orders.size() > 0) {


            //模拟第一个流程的数据。     其实就是白胚体数据
            {


                List<ErpOrderItemProcess> peitis = new ArrayList<>();

                ErpOrderItemProcess yanse = null;

                List<ErpOrderItemProcess> firstSteps = new ArrayList<>();
                for (ErpOrderItemProcess process : orders) {
                    if (process.mrpNo.startsWith(ErpWorkFlow.SECOND_STEP_CODE)) {
                        final ErpOrderItemProcess object = process;
                        ErpOrderItemProcess first = ObjectUtils.deepCopyWidthJson(object, ErpOrderItemProcess.class);
                        first.mrpNo = ErpWorkFlow.FIRST_STEP_CODE + first.mrpNo.substring(1);
                        firstSteps.add(first);
                        peitis.add(process);
                    }

                    if (process.mrpNo.startsWith(ErpWorkFlow.CODE_YANSE)) {
                        yanse = process;
                    }

                }

                orders.addAll(firstSteps);


//                //颜色流程根据胚体流程同步（有铁木就拆铁木）
//
//
//                int size = peitis.size();
//                List<ErpOrderItemProcess> yanses = new ArrayList<>();
//                if (size > 1) {//胚体类型数超过1
//                    for (int i = 0; i < size - 1; i++) {
//                        yanses.add(ObjectUtils.deepCopyWidthJson(yanse, ErpOrderItemProcess.class));
//                    }
//                }
//
//
//                if (yanses.size() > 0) {
//                    orders.remove(yanse);
//                    yanses.add(yanse);
//                    for (int i = 0; i < size; i++) {
//                        ErpOrderItemProcess temp = yanses.get(i);
//
//                        final String mrpNo = peitis.get(i).mrpNo;
//                        String type = mrpNo.substring(1, 2);
//
//                        //名称不同， 表名 这个胚体 由不同的产品（合并货号）
//                        boolean sameName = temp.mrpNo.substring(2).trim().equals(mrpNo.substring(2).trim());
//
//                        temp.mrpNo = temp.mrpNo.substring(0, 1) + type + temp.mrpNo.substring(2) + (sameName ? "" : mrpNo.substring(2));
//
//                    }
//                    orders.addAll(yanses);
//                }


            }


            //移除组装流程


            List<ErpOrderItemProcess> zuzhuangTemp = new ArrayList<>();
            for (ErpOrderItemProcess process : orders) {

                if (process.mrpNo.startsWith(ErpWorkFlow.CODE_ZUZHUANG)) {
                    zuzhuangTemp.add(process);
                }


            }
            orders.removeAll(zuzhuangTemp);


            ArrayUtils.sortList(orders, comparator);
            ErpOrderItemProcess chengping = orders.get(orders.size() - 1);

            //成品仓数据
            chengping.mrpNo = ErpWorkFlow.CODE_CHENGPIN + chengping.mrpNo;


            String url = com.giants3.hd.server.utils.FileUtils.getErpProductPictureUrl(chengping.idNo, "");


            //需要组装数据 重新添加上。只有在配置流程名称时候需要
            //有组装 包装流程名称为  组装包装， 否则就叫做包装
            if (includeZuzhuang) {
                orders.addAll(zuzhuangTemp);

            }


            Set<String> typeSet = new HashSet<>();
            int workflowQty;
            for (ErpWorkFlow erpWorkFlow : ErpWorkFlow.WorkFlows) {

                typeSet.clear();
                workflowQty = 0;
                for (ErpOrderItemProcess process : orders) {


                    if (process.mrpNo.startsWith(erpWorkFlow.code)) {

                        typeSet.add(process.mrpNo);
                        workflowQty += process.qty;


                    }
                }


                int typesetCount = typeSet.size();
                if (typesetCount == 0) typesetCount = 1;
                workflowQty = workflowQty / typesetCount;
                for (ErpOrderItemProcess process : orders) {


                    if (process.mrpNo.startsWith(erpWorkFlow.code)) {


                        process.orderQty = workflowQty;

                    }
                }


            }


            String pVersion = StringUtils.spliteId_no(chengping.idNo)[1];
            for (ErpOrderItemProcess process : orders) {
                process.pVersion = pVersion;

                process.photoUrl = url;
                process.photoThumb = url;
            }


        }
        return orders;
    }

    private List<ErpOrderItemProcess> extractOrderItemProcessFromQuery(Query query) {
        return query.unwrap(SQLQuery.class)
                .addScalar("moDd", StringType.INSTANCE)
                .addScalar("moNo", StringType.INSTANCE)
                .addScalar("prdNo", StringType.INSTANCE)
                .addScalar("mrpNo", StringType.INSTANCE)
                .addScalar("osNo", StringType.INSTANCE)
                .addScalar("idNo", StringType.INSTANCE)
                .addScalar("staDd", StringType.INSTANCE)

                .addScalar("endDd", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("mrpNo", StringType.INSTANCE)
                .addScalar("so_zxs", IntegerType.INSTANCE)

                .addScalar("jgh", StringType.INSTANCE)
                .addScalar("scsx", StringType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpOrderItemProcess.class)).list();
    }


    /**
     * 查找已排产未出货的订单列表
     *
     * @param key
     * @return
     */
    public List<ErpOrderItem> searchUnCompleteOrderItems(String key, int pageIndex, int pageSize) {


        final String value = StringUtils.sqlLike(key);
        Query query = getEntityManager().createNativeQuery(SQL_ORDER_ITEM_UNCOMPLETE)
                .setParameter("os_no", value)
                .setParameter("prd_no", value)
                .setParameter("firstRow", pageIndex * pageSize)
                .setParameter("lastRow", (pageIndex + 1) * pageSize - 1);
        return getORderItemsFromSQL(query, 0, 0);


    }

    /**
     * 查找完工的订单列表
     *
     * @param key
     * @return
     */
    public List<ErpOrderItem> searchCompleteOrderItems(String key, int pageIndex, int pageSize) {


        final String value = StringUtils.sqlLike(key);
        Query query = getEntityManager().createNativeQuery(SQL_ORDER_ITEM_COMPLETE)
                .setParameter("os_no", value)
                .setParameter("prd_no", value)
        .setParameter("firstRow", pageIndex * pageSize)
                .setParameter("lastRow", (pageIndex + 1) * pageSize - 1);
        return getORderItemsFromSQL(query, 0, 0);


    }

    /**
     * 已經完工縂記錄查詢
     *
     * @param key
     * @return
     */
    public int getCompleteOrderItemCount(String key) {
        final String value = StringUtils.sqlLike(key);
        Query query = getEntityManager().createNativeQuery(SQL_COUNT_ORDER_ITEM_COMPLETE)
                .setParameter("os_no", value)
                .setParameter("prd_no", value);
        return getCount(query);

    }

    private List<ErpOrderItem> getWorkFlowOrderItemListQuery(Query query, int pageIndex, int pageSize) {


        //增加字段 up

        final SQLQuery sqlQuery = query.unwrap(SQLQuery.class)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)

                .addScalar("bat_no", StringType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("id_no", StringType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .addScalar("photoUpdateTime", LongType.INSTANCE)
                .addScalar("ut", StringType.INSTANCE)
                .addScalar("up", FloatType.INSTANCE)
                .addScalar("amt", FloatType.INSTANCE)
                .addScalar("hpgg", StringType.INSTANCE)
                .addScalar("khxg", StringType.INSTANCE)
                .addScalar("so_zxs", IntegerType.INSTANCE)
                .addScalar("so_data", StringType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("factory", StringType.INSTANCE)
                .addScalar("os_dd", StringType.INSTANCE)
                .addScalar("amt", FloatType.INSTANCE)
                .addScalar("produceType", IntegerType.INSTANCE)
                .addScalar("workFlowState", IntegerType.INSTANCE)
                .addScalar("maxWorkFlowStep", IntegerType.INSTANCE)
                .addScalar("maxWorkFlowName", StringType.INSTANCE)
                .addScalar("maxWorkFlowCode", StringType.INSTANCE)
                .addScalar("workFlowDescribe", StringType.INSTANCE)
                .addScalar("idx1", StringType.INSTANCE)
                .addScalar("currentOverDueDay", IntegerType.INSTANCE)
                .addScalar("totalLimit", IntegerType.INSTANCE)
                .addScalar("currentLimitDay", IntegerType.INSTANCE)
                .addScalar("currentAlertDay", IntegerType.INSTANCE);
        return
                listQuery(sqlQuery, ErpOrderItem.class, pageIndex, pageSize);


    }

    public List<WorkFlowMaterial> searchWorkFlowMaterials(String osNo, int itm, String code) {


        Query query = getEntityManager().createNativeQuery(SQL_WORK_FLOW_MATERIAL)
                .setParameter("os_no", osNo)
                .setParameter("itm", itm)
                .setParameter("mrp_no", StringUtils.sqlRightLike(code));


        final List<WorkFlowMaterial> list = getList(query);

        for (WorkFlowMaterial material : list) {

            material.mo_dd = StringUtils.clipSqlDateData(material.mo_dd);
        }
        return list;


    }


    private List<WorkFlowMaterial> getList(Query query) {


        return query.unwrap(SQLQuery.class)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("prd_mark", StringType.INSTANCE)
                .addScalar("mrp_no", StringType.INSTANCE)
                .addScalar("mo_no", StringType.INSTANCE)
                .addScalar("mo_dd", StringType.INSTANCE)
                .addScalar("qty_rsv", IntegerType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .addScalar("qty_std", FloatType.INSTANCE)
                .addScalar("real_prd_name", StringType.INSTANCE)
                .addScalar("rem", StringType.INSTANCE)
                .addScalar("ut", StringType.INSTANCE)

                .setResultTransformer(Transformers.aliasToBean(WorkFlowMaterial.class)).list();

    }


    public List<ErpOrderItem> searchHasStartWorkFlowUnCompleteOrderItems(String key) {


        return getOrderItemsFromSQL(SQL_ORDER_ITEM_HAS_START_AND_UNCOMPLETE, key, 0, 0);

    }


    private List<ErpOrderItem> getOrderItemsFromSQL(String sql, String key, int pageIndex, int pageSieze) {

        final String value = StringUtils.sqlLike(key);
        Query query = getEntityManager().createNativeQuery(sql)
                .setParameter("os_no", value)
                .setParameter("prd_no", value);
        final List<ErpOrderItem> oRderItemsFromSQL = getORderItemsFromSQL(query, pageIndex, pageSieze);


        return oRderItemsFromSQL;

    }


    private List<ErpOrderItem> getORderItemsFromSQL(Query query, int pageIndex, int pageSize) {
        final List<ErpOrderItem> workFlowOrderItemListQuery = getWorkFlowOrderItemListQuery(query, pageIndex, pageSize);


        for (ErpOrderItem item : workFlowOrderItemListQuery) {

            item.pVersion = StringUtils.spliteId_no(item.id_no)[1];
            item.so_data = StringUtils.clipSqlDateData(item.so_data);
            item.os_dd = StringUtils.clipSqlDateData(item.os_dd);

            switch (item.produceType) {
                case ProduceType.NOT_SET:
                    item.produceTypeName = ProduceType.NOT_SET_NAME;
                    break;
                case ProduceType.SELF_MADE:
                    item.produceTypeName = ProduceType.SELF_MADE_NAME;
                    break;
                case ProduceType.PURCHASE:
                    item.produceTypeName = ProduceType.PURCHASE_NAME;
                    break;
            }

        }
        return workFlowOrderItemListQuery;
    }


    /**
     * 查找订单数据
     *
     * @param os_no
     * @param itm
     * @return
     */
    public ErpOrderItem findOrderItem(String os_no, int itm) {


        Query query = getEntityManager().createNativeQuery(SQL_ORDER_ITEM_BY_OS_ITM)
                .setParameter("os_no", os_no)
                .setParameter("itm", itm);
        final List<ErpOrderItem> oRderItemsFromSQL = getORderItemsFromSQL(query, 0, 0);
        if (oRderItemsFromSQL != null && oRderItemsFromSQL.size() > 0) return oRderItemsFromSQL.get(0);
        return null;
    }

    /**
     * 指定條件下 所有未完成的订单数量
     *
     * @param key
     * @return
     */
    public int getUnCompleteOrderItemCount(String key) {

        final String value = StringUtils.sqlLike(key);
        Query query = getEntityManager().createNativeQuery(SQL_COUNT_ORDER_ITEM_UNCOMPLETE)
                .setParameter("os_no", value)
                .setParameter("prd_no", value);
        return getCount(query);

    }

    /**
     * 所有未完成的，指定流程的订单数量
     *
     * @param key
     * @param workFlowStep
     * @return
     */
    public int getOrderItemCountOnWorkFlow(String key, int workFlowStep) {


        final String value = StringUtils.sqlLike(key);
        Query query = getEntityManager().createNativeQuery(SQL_COUNT_ORDER_ITEM_ON_WORK_FLOW)
                .setParameter("os_no", value)
                .setParameter("prd_no", value)
                .setParameter("workFlowStep", workFlowStep);
        return getCount(query);


    }

    public List<ErpOrderItem> searchUnCompleteOrderItemsOnWorkFlow(String key, int workFlowStep, int pageIndex, int pageSize) {


        final String value = StringUtils.sqlLike(key);
        Query query = getEntityManager().createNativeQuery(SQL_ORDER_ITEM_ON_WORK_FLOW)
                .setParameter("os_no", value)
                .setParameter("prd_no", value)
                .setParameter("workFlowStep", workFlowStep);
        final List<ErpOrderItem> oRderItemsFromSQL = getORderItemsFromSQL(query, pageIndex, pageSize);


        return oRderItemsFromSQL;


    }


    /**
     * 查询指定期间内 所有已完成工序，小工序未交接的数据列表
     *
     * @param key
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public List<Sub_workflow_state> searchErpSubWorkFlow(String key, String dateStart, String dateEnd) {


        final String value =  StringUtils.sqlLike(key) ;
        Query query = getEntityManager().createNativeQuery(SQL_SUB_WORK_FLOW_STATE)
                .setParameter("para_name", value)
                .setParameter("para_start", dateStart)
                .setParameter("para_end", dateEnd);
        final List<Sub_workflow_state> oRderItemsFromSQL = getSubWorkFlowListQuery(query);


        return oRderItemsFromSQL;


    }


    private List<Sub_workflow_state> getSubWorkFlowListQuery(Query query) {


        //增加字段 up

        final SQLQuery sqlQuery = query.unwrap(SQLQuery.class)
                .addScalar("tz_no", StringType.INSTANCE)
                .addScalar("mo_no", StringType.INSTANCE)
                .addScalar("tz_dd", StringType.INSTANCE)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("mrp_no", StringType.INSTANCE)
                .addScalar("workFlowName", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)

                .addScalar("zc_no", StringType.INSTANCE)
                .addScalar("zc_name", StringType.INSTANCE)
                .addScalar("completeDate", StringType.INSTANCE)

                .addScalar("qty", FloatType.INSTANCE);
        return
                listQuery(sqlQuery, Sub_workflow_state.class, 0, 0);
    }
}
