package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.QuotationItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
* 报价
 *
*/
public interface QuotationItemRepository extends JpaRepository<QuotationItem,Long> {

   public QuotationItem findFirstByProductIdEquals(long productId);

   public List<QuotationItem> findByProductIdEqualsOrderByIdDesc(long productId);

   Page<QuotationItem> findByProductIdEqualsOrderByIdDesc(long productId, Pageable pageable);



   public List<QuotationItem> findByQuotationIdEqualsOrderByIIndex(long quotationId);

   public int  deleteByQuotationIdEquals(long quotationId);

   @Modifying
   @Query("update T_QuotationItem p set   p.photoUrl=:photoUrl,p.thumbnail=:thumbnail   WHERE p.productId =   :productId ")
   public void updatePhotoAndPhotoUrlByProductId( @Param("thumbnail") String thumbnail, @Param("photoUrl") String url,  @Param("productId") long productId);

}
