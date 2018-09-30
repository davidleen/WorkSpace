--已完成訂單記錄數

select  count(*) from


 (
--9 表示 订单生产中
select osNo,itm,workflowstate  from  [yunfei].[dbo].[T_OrderItemWorkState] where workflowstate<>0  and (osNo like :os_no or prdNo like :prd_no)

) as b  inner join
 (


select   os_no,os_dd,itm,bat_no,prd_no,prd_name,id_no, up,qty,amt  from  tf_pos  where os_id=upper('SO')
--订单起止日期  降低查询范围
and  os_dd >'2017-01-01' and (os_no like :os_no or prd_no like :prd_no)
 ) as  a
    on a.os_no=b.osNo collate Chinese_PRC_90_CI_AI   and  a.itm=b.itm and b.workflowstate=VALUE_COMPLETE_STATE




