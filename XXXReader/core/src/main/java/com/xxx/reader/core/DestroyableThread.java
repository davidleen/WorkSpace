package com.xxx.reader.core;



import com.giants3.android.frame.util.Log;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by HP on 2018/3/30.
 */

public abstract class DestroyableThread extends Thread {

    public AtomicBoolean destroy = new AtomicBoolean();

    private static final int SLEEP_DURATION = Integer.MAX_VALUE;

    private long sleepTime;

    public DestroyableThread()
    {
        this(SLEEP_DURATION);
    }
    public DestroyableThread(long sleepTime)
    {
        this.sleepTime = sleepTime;
    }
    /**
     * 标记 当前绘制界面是否销毁 是 直接退出死循环
     */
    public void setDestroy() {

        destroy.set(true);
        interrupt();
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onDestroy();
    }

    protected void onDestroy() {

    }


    public AtomicBoolean skip = new AtomicBoolean();
    public void setSkip(boolean skip) {
        this.skip.set(skip);
    }

    @Override
    public final void run() {


        while (true) {

            // 绘制停止 退出循环
            if (destroy.get()) {
                break;
            }

            skip.set(false);
            long time = Calendar.getInstance().getTimeInMillis();

            runOnThread();


            Log.i("time use in prepare:" + (Calendar.getInstance().getTimeInMillis() - time));


            //线程进入睡眠等待状态。调用update 中断睡眠，可以继续循环。
            try {
                if(!skip.get())
                    Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Log.d(e);
                Log.d(this);

            }


        }
    }



    protected abstract void runOnThread();
}
