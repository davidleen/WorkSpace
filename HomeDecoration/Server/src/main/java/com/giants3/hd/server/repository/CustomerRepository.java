package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
*  客户信息
 *
*/
public interface CustomerRepository extends JpaRepository<Customer,Long> {


    public Customer findFirstByCodeEquals(String code);
    @Query(" select   p from T_Customer  p    order by convert(int ,p.code) desc ")
    public Page<Customer> findFirstOrderByCodeDesc(Pageable pageable);
      List<Customer> findByCodeLikeOrNameLikeOrderByNameDesc(String codeLike,String nameLike);

    @Query(" select   p from T_Customer  p  where p.code like :key or  p.name like :key   order by convert(int ,p.code) asc ")
      List<Customer> findByCodeLikeOrNameLikeOrderByCodeAsc(@Param("key") String  key );


    @Query(" select   p from T_Customer  p  where p.code like :key or  p.name like :key   order by convert(int ,p.code) asc ")
      Page<Customer> findByCodeLikeOrNameLikeOrderByCodeAsc(@Param("key") String  key,Pageable pageable);


}
