package com.qf.eventbus.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.qf.eventbus.Executor;

/**
 * <p>
 * Project Name: 买到手抽筋
 * <br>
 * Description: 简单线程池
 * <br>
 * File Name: SingleExecutor.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017-02-07 21:51:35 
 * @version: v1.0
 *
 */
public class SingleExecutor implements Executor {
	
	private final static int DEFAULT_THREAD_COUNT = 10;
	
	public SingleExecutor() {
		this(DEFAULT_THREAD_COUNT, DEFAULT_THREAD_COUNT, 0);
	}
	
	public SingleExecutor(int initThreadCount, int maxThreadCount) {
		this(initThreadCount, maxThreadCount, 0);
	}
	
	public SingleExecutor(int initThreadCount, int maxThreadCount, int taskLimit) {
		ThreadFactory threadFactory = new AdjustableThreadFactory();
		BlockingQueue<Runnable> queue = taskLimit <= 0 ? new LinkedBlockingQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(taskLimit);
		this.threadExecutor = new ThreadPoolExecutor(initThreadCount, maxThreadCount, 1L, TimeUnit.SECONDS, queue, threadFactory);
	}
	
    private class AdjustableThreadFactory implements ThreadFactory {
        
    	final ThreadGroup group;
        final AtomicInteger threadSequence = new AtomicInteger(1);
        final String threadPrefix = "SingleExecutor-thread-";

        AdjustableThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        public Thread newThread(final Runnable r) {
            Thread t = new SingleThread(group, r, threadPrefix + threadSequence.getAndIncrement(), 0);
           	t.setDaemon(false);
           	t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
    
    private final class SingleThread extends Thread {    	
    	public SingleThread(ThreadGroup group, Runnable runnable, String name, long stackSize) {
    		super(group, runnable, name, stackSize);
    	}    	
    }
    
    private ThreadPoolExecutor threadExecutor;

	@Override
	public <T> void submit(Runnable runnable) {
		if (runnable != null) {
			threadExecutor.submit(runnable);
		}
	}

	@Override
	public int getAvailableTask() {
		return threadExecutor.getQueue().size();
	}

	@Override
	public void shutDown() {
		threadExecutor.shutdown();
	}

}
