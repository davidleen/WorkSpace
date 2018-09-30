select  top 1
* from (


select A.SYS_DATE as ltime ,B.BL_ID,(select NAME FROM DEPT WHERE DEP=A.DEP) as factory,B.WH as wareHouse,B.PRD_NO  as prdNo,B.PRD_MARK as pVersion from MF_BLN A INNER JOIN TF_BLN B ON A.BL_NO=B.BL_NO  where B.prd_no = :prdNo and  B.PRD_MARK= :pVersion
union all
select C.SYS_DATE as ltime ,D.PS_ID as BL_ID,(select NAME FROM DEPT WHERE DEP=C.DEP)as factory,D.WH as wareHouse,D.PRD_NO as prdNo,D.PRD_MARK as pVersion  from MF_PSS C INNER JOIN TF_PSS D ON C.PS_NO=D.PS_NO  WHERE C.PS_ID='PC' AND D.WH LIKE '001-%'  and D.PRD_NO =:prdNo and D.PRD_MARK=:pVersion

) as t  order by  ltime desc