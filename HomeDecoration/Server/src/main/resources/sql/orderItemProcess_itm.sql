SELECT   a.MO_DD as moDd,	a.MO_NO as moNo,	a.STA_DD as staDd,	a.END_DD as endDd	,a.SO_NO AS osNo	,

a.EST_ITM as ITM , 	a.MRP_NO as mrpNo,	a.MO_NO_ADD AS prdNo,a.id_no as idNo,	a.QTY,b.jgh,b.scsx,isnull(b.so_zxs,0) as so_zxs

  FROM (
			--指令单数据
			select MO_DD,mo_no,sta_dd,end_dd,so_no,est_itm,mrp_no,mo_no_add,id_no,qty from [DB_YF01].[dbo].[MF_MO] where   bil_Id = upper('MP') and so_no = :os_no and   EST_ITM= :itm
		  	and (mrp_no like 'A%' or mrp_no like 'B%' or mrp_no like 'C%' or mrp_no like 'D%' )  and mrp_no like '%-%'

		  	union

		  	 --成品数据
		  	select MO_DD,mo_no,sta_dd,end_dd,so_no,est_itm,mrp_no,mo_no_add,id_no,qty from [DB_YF01].[dbo].[MF_MO] where   bil_Id = upper('MP') and  so_no = :os_no and   EST_ITM= :itm and mrp_no=MO_NO_ADD




  ) a inner join mf_mo_z  b on a.mo_no=b.mo_no

     order by a.so_no desc,a.mrp_no ASC
