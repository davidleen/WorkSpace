package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 出库附加数据细项
* Created by davidleen29 on 2014/9/17.
*/
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {


   OrderItem findFirstByOsNoEqualsAndItmEquals(String os_no, int item);
   //public Page<OrderItem> findByVerifyDateGreaterThan(String startDate , Pageable pageable);
  // public Page<OrderItem> findByVerifyDateGreaterThanEqual(String startDate , Pageable pageable);
    public List<OrderItem> findByVerifyDateGreaterThanEqualAndVerifyDateLessThanEqual(String startDate , String endDate  );


    List<OrderItem> findByOsNoEqualsOrderByItm(String osNo);

    Page<OrderItem> findByOsNoLikeOrPrdNoLikeOrderByOsNoDesc(String key,String key1,Pageable pageable);

    @Modifying
    @Query("update T_OrderItem p set   p.url=:photoUrl , p.thumbnail=:thumbnail  WHERE p.prdNo =   :prd_no  and  p.pVersion= :pVersion")
    int updateUrlByProductInfo(@Param("thumbnail") String thumbnail,@Param("photoUrl") String url, @Param("prd_no")  String  prd_no,@Param("pVersion")  String  pVersion);
}
