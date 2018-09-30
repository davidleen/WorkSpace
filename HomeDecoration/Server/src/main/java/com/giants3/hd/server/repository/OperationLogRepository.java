package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 用户
* Created by davidleen29 on 2014/9/17.
*/
public interface OperationLogRepository extends JpaRepository<OperationLog,Long> {


    /**
     * 查询某条记录的历史变更信息
     * @param tableName
     * @param recordId
     * @return
     */
    public List<OperationLog> findByTableNameEqualsAndRecordIdEqualsOrderByTimeDesc(String tableName,long recordId  );


    @Modifying
    @Query("update T_OperationLog p set    p.recordId=:newRecordId   WHERE p.recordId =   :oldRecordId  and  p.tableName= :tableName ")
    public void updateProductId(@Param("oldRecordId") long oldRecordId,@Param("tableName") String tableName  ,@Param("newRecordId") long newRecordId);

}
