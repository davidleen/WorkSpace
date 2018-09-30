package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 报价
 *
*/
public interface QuotationRepository extends JpaRepository<Quotation,Long> {

   // Page<Quotation> findByCustomerNameLikeOrQDateLikeOrSalesmanLikeOrderByQNumberDesc(String name, String qNumber,String salesman,Pageable pageable);
   Page<Quotation> findByCustomerNameLikeAndSalesmanIdInOrQNumberLikeAndSalesmanIdInOrderByQDateDesc(String customerName, long[] salesManIds, String qNumber,long[] salesManIds2, Pageable pageable);

    Page<Quotation> findByCustomerNameLikeAndSalesmanIdEqualsOrQNumberLikeAndSalesmanIdEqualsOrderByQDateDesc(String customerName, long salesManId, String qNumber, long salesManId2, Pageable pageable);
    Page<Quotation> findByCustomerNameLikeOrQNumberLikeOrderByQDateDesc(String customerName, String qNumber, Pageable pageable);
     Quotation findFirstByqNumberEquals(String qNumber);
    Quotation findFirstByCustomerIdEquals(long customer);
}
