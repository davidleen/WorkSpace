 --流程物料材料  条件 订单号， itm, 流程标示号
        select b.mo_dd,a.mo_no,a.prd_no,a.prd_name,a.prd_mark,a.qty_rsv,a.qty  ,b.so_no as os_no ,b.mrp_no  ,b.mo_no_add as real_prd_name,b.est_itm as itm, a.rem, isnull(c.ut,'') as ut ,isnull(QTY_STD,0) as qty_std  from
                           (
                                 select  mo_no, prd_no, prd_name,prd_mark,   isnull(qty_rsv,0) as qty_rsv,isnull(qty,0) as qty ,rem ,QTY_STD from tf_mo
                             ) a  inner join
                           (
                              select  mo_dd, mo_no,  so_no, mrp_no ,mo_no_add,est_itm from mf_mo  where   bil_Id = upper('MP') and so_no =:os_no and mrp_no like :mrp_no  AND   est_itm=:itm

                            )  b
                            on a.mo_no=b.mo_no

                            left  join  (
                            --单位抓取
                            select prd_no, ut from  prdt where  knd='3' or knd='4'

                            ) c  on  a.prd_no=c.prd_no