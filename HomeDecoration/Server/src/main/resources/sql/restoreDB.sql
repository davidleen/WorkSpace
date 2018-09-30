 




BEGIN TRANSACTION;



delete from [DEST_DB].dbo.T_TABLE_TO_CHANGE
set identity_insert [DEST_DB].dbo.T_TABLE_TO_CHANGE on

 

---只能使用insert   并且要指定列

insert into [DEST_DB].dbo.T_TABLE_TO_CHANGE(

--列列表
    T_FIELD_TO_CHANGE

)
select
  --列列表
   T_FIELD_TO_CHANGE

  from [FROM_DB].dbo.T_TABLE_TO_CHANGE








--关闭标识列插入
set identity_insert [DEST_DB].dbo.T_TABLE_TO_CHANGE off



COMMIT;
select count(*) as totalCount,  isnull(MIN(id),0) as  minId, isnull( MAX(id ),0) as maxId from [DEST_DB].dbo.T_TABLE_TO_CHANGE
 