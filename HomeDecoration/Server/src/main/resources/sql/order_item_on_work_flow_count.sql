--已完成訂單記錄數

select  count(*) from    [yunfei].[dbo].[T_OrderItemWorkState] where workflowstate<>VALUE_COMPLETE_STATE  and  maxWorkFlowStep=:workFlowStep and  (osNo like :os_no or prdNo like :prd_no)




