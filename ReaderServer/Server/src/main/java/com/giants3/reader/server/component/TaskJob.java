package com.giants3.reader.server.component;

import com.giants3.reader.server.interf.Job;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TaskJob implements Job {

    @Async
    public void doTask (int i ) {
        String threadName = Thread.currentThread().getName(); 
        System.out.println("   " + threadName + " beginning work on " + i);
        try {
            Thread.sleep(5000); // simulates work
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   " + threadName + " completed work on " + i);
    }
}