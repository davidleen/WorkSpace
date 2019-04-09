select B.ck_no,B.itm, B.PRD_NO ,b.id_no,b.os_no,b.bat_no,B.CUS_OS_NO,isnull(B.UP,0) as UP, isnull(PS.QTY,0) as  QTY, isnull(PS.AMT,0) as   AMT , isnull(TPS.SA_BYXS,0) as SO_ZXS,A.IDX_NAME,A.XS,A.KHXG,A.XGTJ,TPS.SA_BYTJ as ZXGTJ,isnull(A.JZ1,0)as JZ1,isnull(A.MZ,0) as MZ ,A.HPGG ,isnull(c.rem,'') as hsCode , isnull(m.jmcc,'') as jmcc ,PSZ.xhfq as fengqianhao , PSZ.xhgh as guihao,PSZ.xhgx as guixing,PS.ps_no as ps_no
   ,isnull(B.QTY,0) as  QTY2, isnull(B.AMT,0) as   AMT2 ,A.SO_ZXS as SO_ZXS2,A.ZXGTJ as ZXGTJ2
   from
 (select * from TF_CK_Z    where  ck_no=:ck_no )  A
 FULL JOIN   (select * from TF_CK  where  ck_no=:ck_no) B ON A.CK_NO=B.CK_NO AND A.ITM=B.ITM
    left outer join ( select distinct prd_no,rem from  (select rem,idx_no from INDX  ) a
     inner join    ( select idx1,prd_no from prdt )b  on a.idx_no=b.idx1 where a.rem is  not null) c
     on b.prd_no=c.prd_no  left outer join (select bom_no, jmcc from  MF_BOM_Z )  m on b.id_no=m.bom_no


 left outer join

(select ps_no,os_no,id_no,itm,qty,amt,est_dd from TF_PSS where ps_id=UPPER ('SA')  and est_dd>'2016') PS
ON PS.os_no=b.os_no and ps.id_no=b.id_no
 left outer join
(select ps_no ,xhfq,xhgh,xhgx from MF_PSS_Z    where ps_id=UPPER ('SA')) PSZ
 ON PS.PS_NO=PSZ.PS_NO
 left outer join
 (select  ps_no, itm,SA_BYXS ,SA_BYTJ from TF_PSS_Z where ps_id=UPPER ('SA') ) TPS
   on PS.ps_no=TPS.ps_no and PS.itm=TPS.itm



order by B.itm ASC
