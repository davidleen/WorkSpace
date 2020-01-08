select a.os_no,a.os_dd,a.itm,a.bat_no,a.prd_no,a.prd_name,a.id_no, a.up,
isnull(d.ut,'') as ut,
 isnull(d.idx1,'') as idx1,
a.qty,a.amt , isnull(pdc.produceType,-1) as produceType, pdc.sys_date,
b.workFlowDescribe, isnull(b.workflowState,0) as workflowState,
 isnull(b.maxWorkFlowStep,0 )  as maxWorkFlowStep , isnull(b.maxWorkFlowName,'') as maxWorkFlowName,
 isnull(b.maxWorkFlowCode,'' ) as maxWorkFlowCode  ,
  isnull(b.currentOverDueDay,0) as currentOverDueDay  ,
 isnull(b.totalLimit,0) as totalLimit  ,
 isnull(b.currentLimitDay,0) as currentLimitDay  ,
 isnull(b.currentAlertDay,0) as currentAlertDay  ,
  isnull(c.modify_dd,0) as photoUpdateTime
,f.so_data
,g.cus_no
,k.cus_no as factory
,e.hpgg,e.khxg, isnull(e.so_zxs,0) as  so_zxs  from (

select os_no,os_dd,itm,bat_no,prd_no,prd_name,id_no, up,qty,amt from  tf_pos  where os_id='SO'
--订单起止日期  降低查询范围
and  os_dd >'2017-01-01' and (os_no like :os_no or prd_no like :prd_no)
 ) as  a
 --生产方式判断
  left outer join
   (
       --排厂单
      select  distinct  0 as produceType, SO_NO,EST_ITM ,'' as po_no , sys_date  from  MF_MO    where   bil_Id = upper('MP') and  so_no like upper('%YF%') and  ( so_no like :os_no or mrp_no like  :prd_no ) and mrp_no=MO_NO_ADD
       union
       --外购单
      select distinct 1 as produceType,OTH_NO as so_no,oth_itm1 as est_itm,os_no as po_no , os_dd as sys_date from  tf_POS   where  os_id=upper('PO') and OTH_NO like upper('%YF%')  and (OTH_NO like :os_no or prd_no like :prd_no ) and and  os_dd >'2017-01-01'



   ) as pdc on a.os_no=pdc.SO_NO    and a.itm=pdc.EST_ITM

  --厂商数据抓取
  left outer join   (select os_no as po_no, cus_no   from  V_mf_pos where OS_ID=upper('PO')   and  os_dd >'2017-01-01') as k on pdc.po_no=k.po_no


 --与 unCompleteOrderItem sql语句区别在于 inner join
inner   join
(

--9 表示 订单生产中
select osNo,itm,workflowstate,maxWorkFlowStep,maxWorkFlowName, maxWorkFlowCode,workFlowDescribe ,currentOverDueDay,totalLimit,currentLimitDay,currentAlertDay from  [yunfei].[dbo].[T_OrderItemWorkState] where workflowstate<>0  and (osNo like :os_no or prdNo like :prd_no)

) as b on a.os_no=b.osNo collate Chinese_PRC_90_CI_AI   and  a.itm=b.itm and b.workflowstate=VALUE_WORKING_STATE



 left outer JOIN
 --图片抓取关闭图片修改日期的抓取， ERP 图片改动时候， 客户端是无法感知的。
  (
   -- select  bom_no, CAST (modify_dd AS TIMESTAMP ) as modify_dd from mf_bom where  prd_knd='2'
    select  '' as bom_no,  0 as modify_dd
  ) as c on a.id_no=c.bom_no

  left outer join  (
                            --单位抓取
                            select prd_no, idx1,ut from  prdt where  knd='2'

                            ) d  on  a.prd_no=d.prd_no


            --货品规格 箱规 生产交期数据
             left outer join (
                            select bom_no,hpgg,khxg,so_zxs  from  mf_bom_z

                            ) e  on  a.id_no=e.bom_no

                                 -- 生产交期数据
             left outer join (
                           select os_no, so_data ,itm from  tf_pos_z where OS_ID='SO'

                            ) f  on  a.os_no=f.os_no and a.itm=f.itm

             left outer join (

                            select os_no, cus_no   from  V_mf_pos where OS_ID='SO'   and  os_dd >'2017-01-01'

                            ) g  on  a.os_no=g.os_no



order by a.os_no  DESC,a.itm   ASC



