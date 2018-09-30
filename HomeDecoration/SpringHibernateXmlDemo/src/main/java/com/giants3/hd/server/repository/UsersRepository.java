package com.giants3.hd.server.repository;

import com.giants3.hd.server.entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by david on 2015/11/5.
 */
@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {


//    public List<Xiankang> findByProductIdEqualsOrderByItemIndexAsc(long productId);
}
