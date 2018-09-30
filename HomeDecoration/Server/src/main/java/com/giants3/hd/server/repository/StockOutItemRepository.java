package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.StockOutItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 出库附加数据细项
* Created by davidleen29 on 2014/9/17.
*/
public interface StockOutItemRepository extends JpaRepository<StockOutItem,Long> {


   List<StockOutItem> findByCkNoEqualsAndItmEquals(String ck_no, int item);

   List<StockOutItem> findByCkNoEquals(String ck_no);
}
