WITH query AS (
   SELECT     ROW_NUMBER() OVER (ORDER BY a.os_no DESC,a.itm ASC ) AS __rowindex__, a.os_no, a.os_dd, a.itm, a.bat_no, a.prd_no, a.prd_name, a.id_no, isnull(a.up,0) as up, isnull( a.qty,0) as qty, isnull(a.amt,0) amt,
b.workflowdescribe, isnull(b.workflowstate, 0) AS workflowstate, isnull(b.maxworkflowstep, 0) AS maxworkflowstep, isnull(b.maxworkflowname, '') AS maxworkflowname,
isnull(b.maxworkflowcode, '') AS maxworkflowcode,
  isnull(b.currentOverDueDay,0) as currentOverDueDay
 ,isnull(b.totalLimit,0) as totalLimit
 ,isnull(b.currentLimitDay,0) as currentLimitDay
 ,isnull(b.currentAlertDay,0) as currentAlertDay
FROM         (SELECT     TOP 99999999 os_no, os_dd, itm, bat_no, prd_no, prd_name, id_no, up, qty, amt
                       FROM          [DB_YF01].[dbo].tf_pos
                       WHERE      os_id = upper('so') /*订单起止日期  降低查询范围*/ AND os_dd > '2017-01-01'
                       ORDER BY os_no DESC, itm ASC) AS a
                       --关联上所有已经审核订单，有CLS_Date 值表示已经审核。
                        inner join (select os_no    from  [DB_YF01].[dbo].MF_POS  where CLS_DATE is not  null ) AS aa on a.os_no=aa.os_no

                        LEFT OUTER JOIN
                          (/*9 表示 订单生产中*/ SELECT osno, itm, workflowstate, maxworkflowstep, maxworkflowname, maxworkflowcode, workflowdescribe,currentOverDueDay,totalLimit,currentLimitDay,currentAlertDay
                            FROM          [yunfei].[dbo].[t_orderitemworkstate]
                            WHERE      workflowstate <> 0) AS b ON a.os_no = b.osno COLLATE chinese_prc_90_ci_ai AND a.itm = b.itm
WHERE     (b.workflowstate IS NULL OR b.workflowstate <> 99 )and ( a.os_no like :os_no or a.prd_no like :prd_no  )
GROUP BY a.os_no, a.os_dd, a.itm, a.bat_no, a.prd_no, a.prd_name, a.id_no, a.up, a.qty, a.amt, b.workflowdescribe, isnull(b.workflowstate, 0), isnull(b.maxworkflowstep, 0),
                      isnull(b.maxworkflowname, ''), isnull(b.maxworkflowcode, ''),isnull(b.currentOverDueDay,0)   , isnull(b.totalLimit,0),isnull(b.currentLimitDay,0)   , isnull(b.currentAlertDay,0)

)
 

 SELECT a.*,isnull(d.ut,'')  as ut,
 isnull(d.idx1,'') as idx1,
   isnull(pdc.producetype,-1) as producetype, pdc.sys_date,


 isnull(c.modify_dd,0) as photoupdatetime
,f.so_data
,g.cus_no
,k.cus_no as factory
,e.hpgg,e.khxg, isnull(e.so_zxs,0) as  so_zxs
  FROM (select   * from  query    WHERE __rowindex__ BETWEEN :firstRow AND  :lastRow      )  a
 
 --生产方式判断
  left outer join
   (
       --排厂单
      select   0 as producetype, so_no,est_itm ,'' as po_no, sys_date  from  mf_mo       where bil_Id = upper('MP') and so_no like upper('%yf%') and  ( so_no like :os_no or mrp_no like  :prd_no )     and mrp_no=MO_NO_ADD
       union
       --外购单
      select distinct 1 as producetype,oth_no as so_no,oth_itm1 as est_itm,os_no as po_no,  os_dd as sys_date from  tf_pos   where  os_id=upper('po') and oth_no like upper('%yf%') and (OTH_NO like :os_no or prd_no like :prd_no )  and  os_dd >'2017-01-01'



   ) as pdc on a.os_no=pdc.so_no    and a.itm=pdc.est_itm

  --厂商数据抓取
  left outer join   (select os_no as po_no, cus_no   from  V_mf_pos where os_id=upper('po')   and  os_dd >'2017-01-01') as k on pdc.po_no=k.po_no

 

 left outer join
 --图片抓取关闭图片修改日期的抓取， erp 图片改动时候， 客户端是无法感知的。
  (
   -- select  bom_no, cast (modify_dd as timestamp ) as modify_dd from mf_bom where  prd_knd='2'
    select  '' as bom_no,  0 as modify_dd
  ) as c on a.id_no=c.bom_no


  left outer join  (
                            --单位抓取
                            select prd_no,idx1, ut from  prdt where   knd='2'

                            ) d  on  a.prd_no=d.prd_no


            --货品规格 箱规 生产交期数据
             left outer join (
                            select bom_no,hpgg,khxg,so_zxs from  mf_bom_z

                            ) e  on  a.id_no=e.bom_no

                                 -- 生产交期数据
              left outer join (
                            select os_no, so_data ,itm from  tf_pos_z where os_id=upper ('so')

                            ) f  on  a.os_no=f.os_no and a.itm=f.itm

             left outer join (

                            select os_no, cus_no   from  V_mf_pos where os_id=upper ('so')   and  os_dd >'2017-01-01'

                            ) g  on  a.os_no=g.os_no



  order by a.__rowindex__
 
 
  