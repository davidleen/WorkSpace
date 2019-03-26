select a.tz_no,a.tz_dd , a.so_no as osNo,a.est_itm as itm ,a.mrp_no as mrpNo,a.zc_no   ,b.name as zc_name ,a.qty,c.jgh  from
    ( select tz_no,tz_dd,so_no,est_itm,mrp_no,zc_no,qty from mf_tz where so_no =:os_no and est_itm=:itm  )  a
      INNER JOIN
      (select zc_no,name from  zc_no ) b on a.zc_no=b.zc_no
     inner join (select  tz_no,jgh from mf_tz_z ) c on a.tz_no=c.tz_no

     order by a.so_no,a.est_itm ,a.mrp_no