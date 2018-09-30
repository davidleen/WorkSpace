package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
*  生产流程信息
 *
*/
public interface WorkFlowRepository extends JpaRepository<WorkFlow,Long> {


    List<WorkFlow> findByCheckerIdEqualsOrUserIdEquals(long id, long id1);

    WorkFlow findFirstByFlowStepEquals(int flowStep);

    List<WorkFlow> findByUserIdEquals(long id);

    /**
     *
     * @param id
     * @param flowStep
     * @return
     */
    List<WorkFlow> findByUserIdEqualsAndFlowStepNot(long id, int flowStep);
}
