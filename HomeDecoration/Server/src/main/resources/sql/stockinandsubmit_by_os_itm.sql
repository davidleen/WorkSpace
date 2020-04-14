--外厂进库 ||--车间缴库 单
 select * from (

		 select  mm_no as no,SO_NO ,itm ,est_itm as os_itm,PRD_NO,PRD_NAME,PRD_MARK  ,BAT_NO,CUS_OS_NO,QTY ,mm_dd as dd   ,1 as type , ID_NO ,'' as dept from TF_MM0 where wh= UPPER ('CP')   and ( so_no = :so_no   )  and est_itm= :itm

   union



        select ps.ps_no as no, ps.so_no,ps.itm,ps.OTH_ITM1 as os_itm,ps.PRD_NO,ps.PRD_NAME,ps.PRD_MARK,ps.BAT_NO,ps.CUS_OS_NO,ps.qty,ps.dd,ps.type,pos.id_no ,mps.dept from (

            select a.*,b.OTH_ITM1  from(    select PRD_MARK as so_no ,itm,  est_itm,PRD_NO,PRD_NAME, PRD_MARK, BAT_NO,CUS_OS_NO,QTY ,ps_dd as dd ,2 as type ,ps_no,ps_id from TF_PSS where  PS_ID= UPPER ('PC')  AND WH= UPPER ('CP')   and  PRD_MARK like  UPPER ('%YF%')
                 and (PRD_MARK = :so_no   )) as    a  inner join (select OTH_ITM1 , itm,oth_no from tf_pos where  oth_no =:so_no and OTH_ITM1=:itm) as  b on a.prd_mark=b.oth_no and a.est_itm=b.itm

        ) as ps

         inner join ( select os_no ,itm,prd_no,id_no,bat_no FROM  TF_POS where OS_ID= UPPER ('SO')  ) as pos  on ps.PRD_MARK=pos.os_no  and ps.prd_no=pos.prd_no  and  ps.bat_no=pos.bat_no

         inner join (select  cus_no  as dept ,ps_no from mf_pss  WHERE  PS_ID= UPPER ('PC')      )    as  mps  on mps.ps_no= ps.ps_no






   ) as a

       inner join  (select BOM_NO ,KHXG,SO_ZXS,XGTJ from MF_BOM_Z  where KHXG IS NOT NULL  )  as b on a.ID_NO=b.BOM_NO

          inner join (select os_no, cus_no from V_mf_pos   where OS_ID= UPPER ('SO')  ) as c on a.so_no = c.os_no

          order by a.dd desc
