
SELECT   '' as moDd,	a.SQ_NO as moNo,	'' as staDd,	'' as endDd	,a.SO_NO AS osNo	,a.EST_ITM as ITM , 	a.prd_no as mrpNo,	a.prd_no AS prdNo, p.id_no as idNo,	a.QTY,a.cus_no as jgh,'' as scsx,isnull(b.so_zxs,0) as so_zxs

  FROM [DB_YF01].[dbo].[tf_SQ]  a inner join tf_SQ_z  b on a.sq_no=b.sq_no


inner join (
select   os_no,os_dd,itm,bat_no,prd_no,prd_name,id_no, up,qty,amt  from  tf_pos  where os_id='SO'
 and (os_no = :os_no and itm= :itm)
  ) as p on a.so_no=p.os_no and a.itm=p.itm

   where    a.so_no =  :os_no and  a.EST_ITM= :itm

   order by a.so_no desc