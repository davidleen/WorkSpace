--已完成訂單記錄數

select  count(*) from    [yunfei].[dbo].[T_OrderItemWorkState] where (-1=:workflowstate or  workflowstate = :workflowstate)  and ( -1=:workFlowStep or maxWorkFlowStep=:workFlowStep ) and (

-1=:alertType
or  (0=:alertType and   currentOverDueDay <-currentAlertDay  )
or  (1=:alertType and   currentOverDueDay <0 and currentAlertDay>0 and   currentOverDueDay>=-currentAlertDay  )
or  (2=:alertType and  currentOverDueDay between  0 and 5 )
or  (3=:alertType and  currentOverDueDay >5 )

)    and (osNo like :os_no or prdNo like :prd_no)




