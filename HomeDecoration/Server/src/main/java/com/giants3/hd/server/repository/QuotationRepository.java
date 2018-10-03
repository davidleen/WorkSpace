package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Modifying
    @Query("update T_Quotation p set    p.customerCode=:customerCode, p.customerName=:customerName   WHERE p.customerId =   :customerId   ")
    void updateCustomer(@Param("customerId") long customerId, @Param("customerCode") String customerCode, @Param("customerName") String customerName);
}
