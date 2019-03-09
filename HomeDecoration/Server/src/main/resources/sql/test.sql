 WITH query AS (select ROW_NUMBER() OVER (order by a.os_no  desc,a.itm   asc) as __hibernate_row_nr__, a.os_no,a.os_dd,a.itm,a.bat_no,a.prd_no,a.prd_name,a.id_no,
 a.up,isnull(d.ut,'') as ut,

isnull(d.idx1,'') as idx1,
 a.qty,a.amt , isnull(pdc.producetype,-1) as producetype,

b.workflowdescribe, isnull(b.workflowstate,0) as workflowstate,
 isnull(b.maxworkflowstep,0 )  as maxworkflowstep , isnull(b.maxworkflowname,'') as maxworkflowname,
 isnull(b.maxworkflowcode,'' ) as maxworkflowcode  ,
  isnull(b.currentoverdueday,0) as currentoverdueday  ,
 isnull(b.totallimit,0) as totallimit

 ,isnull(b.currentlimitday,0) as currentlimitday
 ,isnull(b.currentalertday,0) as currentalertday
 , isnull(c.modify_dd,0 ) as photoupdatetime
,f.so_data
,g.cus_no
,k.cus_no as factory
,e.hpgg,e.khxg, isnull(e.so_zxs,0) as  so_zxs    from



 (
--9 表示 订单生产中
select osno,itm,workflowstate,maxworkflowstep,maxworkflowname, maxworkflowcode,workflowdescribe ,currentoverdueday,totallimit ,currentlimitday,currentalertday from  [yunfei].[dbo].[t_orderitemworkstate] where workflowstate<>99 and   maxworkflowstep=? and (osno like ? or prdno like ?)

) as b  inner join
 (


select   os_no,os_dd,itm,bat_no,prd_no,prd_name,id_no, up,qty,amt  from  tf_pos  where os_id= upper('so')
--订单起止日期  降低查询范围
and  os_dd >'2017-01-01' and (os_no like ? or prd_no like ?)
 ) as  a
    on a.os_no=b.osno collate chinese_prc_90_ci_ai   and  a.itm=b.itm
 --生产方式判断
   left outer join
   (
       --排厂单
      select   0 as producetype, so_no,est_itm ,'' as po_no from  mf_mo    where   bil_Id = upper('MP') and  so_no like upper('%yf%') and so_no like ?  and     mrp_no=MO_NO_ADD
       union
       --外购单
      select distinct 1 as producetype,oth_no as so_no,oth_itm1 as est_itm,os_no as po_no from  tf_pos   where  os_id=upper('po') and oth_no like upper('%yf%')  and oth_no like ? and  os_dd >'2017-01-01'



   ) as pdc on a.os_no=pdc.so_no    and a.itm=pdc.est_itm

  --厂商数据抓取
  left outer join   (select os_no as po_no, cus_no   from  V_mf_pos where os_id=upper('po')   and  os_dd >'2017-01-01') as k on pdc.po_no=k.po_no

  left outer join
  --图片抓取关闭图片修改日期的抓取， erp 图片改动时候， 客户端是无法感知的。
    (
      select  bom_no,  0 as modify_dd from mf_bom where prd_knd='2'

    ) as c on a.id_no=c.bom_no


      left outer join  (
                            --单位抓取
                            select prd_no,idx1,  ut from  prdt where   knd='2'

                            ) d  on  a.prd_no=d.prd_no


            --货品规格 箱规 生产交期数据
             left outer join (
                            select bom_no,hpgg,khxg,so_zxs from  mf_bom_z

                            ) e  on  a.id_no=e.bom_no

                                 -- 生产交期数据
              left outer join (
                            select os_no, so_data ,itm from  tf_pos_z where os_id=upper('so')

                            ) f  on  a.os_no=f.os_no and a.itm=f.itm

             left outer join (

                            select os_no, cus_no   from  V_mf_pos where os_id=upper('so')   and  os_dd >'2017-01-01'

                            ) g  on  a.os_no=g.os_no






 group by a.os_no,a.os_dd,a.itm,a.bat_no,a.prd_no,a.prd_name,a.id_no,
 a.up,isnull(d.ut,''),

isnull(d.idx1,''),
 a.qty,a.amt , isnull(pdc.producetype,-1),

b.workflowdescribe, isnull(b.workflowstate,0),
 isnull(b.maxworkflowstep,0 ) , isnull(b.maxworkflowname,''),
 isnull(b.maxworkflowcode,'' ),
  isnull(b.currentoverdueday,0),
 isnull(b.totallimit,0),isnull(b.currentlimitday,0),isnull(b.currentalertday,0), isnull(c.modify_dd,0 ),f.so_data
,g.cus_no
,k.cus_no,e.hpgg,e.khxg, isnull(e.so_zxs,0)) SELECT * FROM query WHERE __hibernate_row_nr__ BETWEEN ? AND ?
