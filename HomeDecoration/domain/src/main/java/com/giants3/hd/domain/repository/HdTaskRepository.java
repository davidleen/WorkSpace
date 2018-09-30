package com.giants3.hd.domain.repository;

import com.giants3.hd.entity.HdTask;
import com.giants3.hd.noEntity.RemoteData;
import rx.Observable;

import java.util.List;

/**任务资源
 * Created by david on 2015/10/13.
 */
public interface HdTaskRepository {


    /**
     * 获取任务
     * @return
     */
    public  Observable<List<HdTask>> getTaskList();

    /**
     * 添加新任务， 并返回新的任务列表
     * @param task
     * @return
     */
    public  Observable<List<HdTask>> addTask(HdTask task);
    /**
     * 添加旧 并返回旧任务列表
     * @param taskId
     * @return
     */
    public  Observable<List<HdTask>> delete(long taskId);

    Observable<RemoteData<HdTask>> updateTaskState(long taskId, int taskState);

    Observable<RemoteData<Void>> executeHdTask(int taskType);
}
