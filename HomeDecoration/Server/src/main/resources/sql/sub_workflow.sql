
SELECT A.TZ_NO,A.TZ_DD ,A.MO_NO,C.SO_NO AS OS_NO,C.EST_ITM AS ITM,A.MRP_NO,D.workFlowName,D.prd_no, D.completeDate,A.ZC_NO,B.NAME AS ZC_NAME,A.QTY FROM  (select * from MF_TZ   where    tz_dd >= :para_start and tz_dd<= :para_end     and TZ_NO NOT IN (SELECT TZ_NO FROM TF_WR) ) AS A

INNER JOIN (SELECT ZC_NO ,NAME FROM ZC_NO) AS B  ON A.ZC_NO=B.ZC_NO


INNER JOIN (SELECT MO_NO, SO_NO,EST_ITM FROM MF_MO where bil_Id = upper('MP'))  AS C ON A.MO_NO =C.MO_NO

LEFT OUTER JOIN (

    select aa.* ,bb.mrpNo  from (
        SELECT OSNO,ITM,prdNo as prd_no,workflowStep,workFlowName ,endDateString as completeDate,PERCENTAGE FROM [yunfei].[dbo].[T_eRPWORKFLOWREPORT]
            WHERE WORKFLOWSTEP<>1000 and  WORKFLOWSTEP<>6000
            AND ( (endDateString >= :para_start and endDateString<= :para_end)   ) --白胚不限制要完成（完成了 enddate才有值）
            ) as aa inner  join (
            --关联找出mrp_no
         select distinct osNo,  itm ,mrpNo,currentWorkFlowStep from   [yunfei].[dbo].[T_ErpOrderItemProcess]
        )as bb  on aa.OSNO=bb.osNo    and aa.itm=bb.itm and aa.workflowStep=bb.currentWorkFlowStep


) AS D

ON C.SO_NO=D.OSNO  collate Chinese_PRC_90_CI_AI  AND C.EST_ITM=D.ITM and A.MRP_NO=D.mrpNo collate Chinese_PRC_90_CI_AI



where (A.MRP_NO like :para_name or  b.NAME like  :para_name or A.ZC_NO LIKE  :para_name  or  c.SO_NO  like :para_name or  d.prd_no like :para_name     )


---------------------------这里额外增加判断 如果A07A09 ，只要胚体加工（1000） 有交接（>0），就显示。
and (D.PERCENTAGE>=1 or ((A.ZC_NO=upper('A07') or  A.ZC_NO=upper('A09')) and     exists(  SELECT id FROM [yunfei].[dbo].[T_eRPWORKFLOWREPORT] b where  b.WORKFLOWSTEP=1000 and b.percentage>0 and c.SO_NO=b.osNo collate Chinese_PRC_90_CI_AI  and C.EST_ITM=b.itm)   ) )

order by C.SO_NO DESC , C.EST_ITM ASC