
select * from  t_workflowmessage   where  state in (0,4) and ToFlowStep not in (2000,3000) and receiverId =0


union

select  a.* from ( select * from  t_workflowmessage   where  state in (0,4) and ToFlowStep in (2000,3000) and receiverId =0 )as a

 left outer join (select  OsNo,  itm, WorkFlowStep,produceType from t_erpworkflowReport) as b on a.orderName=b.osNo and a.itm=b.itm and a.toFlowStep=b.workFlowStep


left outer join (select id, mrpNo,prdNo,subString(mrpNo,2,1) as mrpType from T_ErpOrderItemProcess ) c on c.id=a.orderItemProcessId

left outer join (select subString(idx1,1,2) as   prdType,name  from [DB_YF01].[dbo].prdt where knd='2' ) d on d.name=c.prdNo  collate Chinese_PRC_90_CI_AI

left outer join (select   UserId, mu,tie ,ProduceType,WorkFlowStep from T_WorkflowWorker where userId=40) e on  e.ProduceType=b.produceType and e.WorkFlowStep=a.toFlowStep


where

 c.mrpNo is null or c.prdNo is null
    or    e.produceType is null
     or  not (((c.mrpType='T' or d.prdType='TJ') and e.tie=0) or ((c.mrpType='M' or d.prdType='MJ') and e.mu=0)  )


     order by orderName desc ,itm