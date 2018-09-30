package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.QuotationXKItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
*  咸康报价
 *
*/
public interface QuotationXKItemRepository extends JpaRepository<QuotationXKItem,Long> {

   public QuotationXKItem findFirstByProductIdEquals(long productId);
   public QuotationXKItem findFirstByProductId2Equals(long productId);

   public List<QuotationXKItem> findByQuotationIdEqualsOrderByIIndex(long quotationId);
   int  deleteByQuotationIdEquals(long quotationId);

   @Modifying
   @Query("update T_QuotationXKItem p set    p.photoUrl=:photoUrl , p.thumbnail=:thumbnail   WHERE p.productId =   :productId ")
   public int updatePhotoByProductId(@Param("thumbnail") String thumbnail, @Param("photoUrl") String photoUrl,@Param("productId") long productId);

   @Modifying
   @Query("update T_QuotationXKItem p set     p.photo2Url=:photo2Url, p.thumbnail2=:thumbnail2    WHERE p.productId2 =   :productId ")
   public int updatePhoto2ByProductId( @Param("thumbnail2") String thumbnail,@Param("photo2Url") String photo2Url,@Param("productId") long productId);

   Page<QuotationXKItem> findByProductIdOrProductId2EqualsOrderByIdDesc(long id, long id2,Pageable pageable);

}
