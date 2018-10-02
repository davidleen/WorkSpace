package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.app.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * 广交会报价单
 * Created by davidleen29 on 2017/9/17.
 */
public interface AppQuotationRepository extends JpaRepository<Quotation, Long> {


    @Query(value = "select p from  T_AppQuotation  p where  (p.customerCode like :key or p.customerName=:key  or p.qNumber like :key  or p.salesman like :key )  and p.formal=true order by p.qDate desc "
            , countQuery = "select count(p.id) from  T_AppQuotation  p where  (p.customerCode like :key or p.customerName=:key  or p.qNumber like :key  or p.salesman like :key )  and p.formal=true ")
    Page<Quotation> findByKey(@Param("key") String key, Pageable pageable);


    Quotation findFirstByQNumberLikeAndFormalIsTrueOrderByQNumberDesc(String key);

    Quotation findFirstByQNumberEquals(String key);

    Quotation findFirstByQNumberEqualsAndFormalIsFalse(String key);
    Quotation findFirstByQNumberEqualsAndFormalIsTrue(String key);
    Page<Quotation> findByQDateGreaterThanEqualAndQDateLessThanEqualAndFormalIsTrueOrderByQDateAsc(String startDate,String endDate,Pageable pageable);
    Page<Quotation> findByQDateBetweenAndFormalIsTrueOrderByQDateAsc(String startDate,String endDate,Pageable pageable);
    List<Quotation> findByQDateBetweenAndFormalIsTrueOrderByQDateAsc(String startDate, String endDate );
    @Modifying
    @Query("update T_AppQuotation p set   p.customerId=:newCustomerId    WHERE p.customerId =   :oldCustomerId ")
     void replaceCustomerId( @Param("newCustomerId") long newCustomerId,  @Param("oldCustomerId") long oldCustomerId);



    @Query("select p from  T_AppQuotation p   where" +
            "  p.formal =true  "+
            " and  (p.customerCode like :key or p.customerName like :key or p.qNumber like  :key   ) " +
            "and    p.qDate >=:dateStart and  p.qDate <=:dateEnd  and (:userId = -1l or p.saleId=:userId ) " +
            " order by  p.qNumber desc ")
    Page<Quotation> search( @Param("key") String  key,@Param("dateStart") String  dateStart,@Param("dateEnd") String  dateEnd,@Param("userId") long userId, Pageable pageable );


}
