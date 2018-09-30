package com.giants3.hd.domain.repository;

import com.giants3.hd.entity.HdTaskLog;
import rx.Observable;

import java.util.List;

/**任务记录列表
 * Created by david on 2015/10/13.
 */
public interface HdTaskLogRepository {


    /**
     * 获取任务执行记录
     * @return
     */
    public  Observable<List<HdTaskLog>> getTaskLogList(long taskId);


}
