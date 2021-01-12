package com.giants3.thread;

import com.giants3.concurrent.ListenableFutureTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池定制类
 *  Created by davidleen29 on 2017/11/15.
 */
public class ThreadConst {


    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "WorkThread #" + mCount.getAndIncrement());
        }
    };

//    /**
//     * An {@link Executor} that can be used to execute tasks in parallel.
//     */
//    public static final Executor THREAD_POOL_EXECUTOR
//            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
//            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, new  ThreadPoolExecutor.DiscardOldestPolicy());


    public static Executor create(int queMaxSize)
    {

          return  new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS,  new LinkedBlockingQueue<Runnable>(queMaxSize), sThreadFactory, new  ThreadPoolExecutor.DiscardOldestPolicy());

    }


    public static Executor create()
    {

        return create(Integer.MAX_VALUE);


    }


    public static  void  test()
    {

    }

}