package com.giants3.hd.server.repository;

import com.giants3.hd.entity.WorkFlowArranger;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by davidleen29 on 2017/4/8.
 */
public interface WorkFlowArrangerRepository extends JpaRepository<WorkFlowArranger, Long> {


    public WorkFlowArranger findFirstByUserIdEquals(long userId);
    public WorkFlowArranger findFirstByUserIdEqualsAndSelfMadeEquals(long userId,boolean selfMade);
    public WorkFlowArranger findFirstByUserIdEqualsAndPurchaseEquals(long userId,boolean purchase);

}
