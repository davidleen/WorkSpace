 select * from (

    select ps_no as no, os_no as so_no ,PRD_NO,PRD_NAME, PRD_MARK ,BAT_NO,CUS_OS_NO,QTY ,ps_dd as dd ,  3 as type, ID_NO ,'' as dept,ITM ,est_itm from TF_PSS
    WHERE  PS_ID= UPPER ('SA')  AND WH= UPPER ('CP')   and os_no=:os_no and est_itm=:itm
    ) as a    inner join  (
    select BOM_NO ,KHXG,SO_ZXS,XGTJ from MF_BOM_Z  where KHXG IS NOT NULL
    )  as b
     on a.ID_NO=b.BOM_NO
     inner join (select os_no, cus_no from V_mf_pos where OS_ID= UPPER ('SO')  ) as c on a.so_no = c.os_no
          inner  join (select PS_ID,PS_NO,TCGS,isnull(XHDG,0) as XHDG ,XHFQ,XHGBQ,XHGH,XHGX,XHPH  from MF_PSS_Z ) as d on  a.no=d.ps_no
