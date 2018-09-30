 ----  恢复 id 字段标识化

 --使用方法    UPDATE_TABLE_NAME  全文替换指定表
 --中间部分 插入表 增加指定列（所有列） --必须指定



 ---删除临时表
IF OBJECT_ID('tempdb.dbo.#UPDATE_TABLE_NAME', 'U') IS NOT NULL
  DROP TABLE #UPDATE_TABLE_NAME;

---查看这个ID是否,正常增长
 select count(*),max(id),min(id) from UPDATE_TABLE_NAME

--插入临时表
select * into #UPDATE_TABLE_NAME from [UPDATE_TABLE_NAME]
go
--删除表数据
delete [UPDATE_TABLE_NAME]
go

--删除字段ID
alter table [UPDATE_TABLE_NAME] drop column [id]
---增加ID自动增长字段
alter table [UPDATE_TABLE_NAME] add [id] bigint identity(1,1)

set identity_insert [UPDATE_TABLE_NAME] on

--将数据从临时表转移过来

---只能使用insert   并且要指定列

insert into [UPDATE_TABLE_NAME](

--列列表


)
select
  --列列表




  from #UPDATE_TABLE_NAME








--关闭标识列插入
set identity_insert [UPDATE_TABLE_NAME] off

---删除临时表
drop table #UPDATE_TABLE_NAME



---查看这个ID是否,正常增长
 select count(*),max(id),min(id) from UPDATE_TABLE_NAME