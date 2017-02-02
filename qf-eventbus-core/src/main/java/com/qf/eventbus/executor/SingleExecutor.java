package com.qf.eventbus.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.Dispatcher;
import com.qf.eventbus.Executor;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 单一线程池
 * <br>
 * File Name: SingleExecutor.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月2日 下午4:58:09 
 * @version: v1.0
 *
 */
public class SingleExecutor implements Executor {
	
	private final static int DEFAULT_THREAD_COUNT = 10;
	
	public SingleExecutor(Dispatcher dispatcher) {
		this(dispatcher, DEFAULT_THREAD_COUNT, DEFAULT_THREAD_COUNT, 0);
	}
	
	public SingleExecutor(Dispatcher dispatcher, int initThreadCount, int maxThreadCount) {
		this(dispatcher, initThreadCount, maxThreadCount, 0);
	}
	
	public SingleExecutor(Dispatcher dispatcher, int initThreadCount, int maxThreadCount, int taskLimit) {
		this.dispatcher = dispatcher;
		
		ThreadFactory threadFactory = new SingleThreadFactory();
		BlockingQueue<Runnable> queue = taskLimit <= 0 ? new LinkedBlockingQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(taskLimit);
		this.threadExecutor = new ThreadPoolExecutor(initThreadCount, maxThreadCount, 1L, TimeUnit.SECONDS, queue, threadFactory);
	}
	
    private class SingleThreadFactory implements ThreadFactory {
        
    	final ThreadGroup group;
        final AtomicInteger threadSequence = new AtomicInteger(1);
        final String threadPrefix = "SingleExecutor-thread-";

        SingleThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        public Thread newThread(final Runnable r) {
            Thread t = new DispatchThread(group, r, threadPrefix + threadSequence.getAndIncrement(), 0);
           	t.setDaemon(false);
           	t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
    
    private final class DispatchThread extends Thread {
    	
    	public DispatchThread(ThreadGroup group, Runnable runnable, String name, long stackSize) {
    		super(group, runnable, name, stackSize);
    	}    	
    }
    
    private Dispatcher dispatcher;
    
    private ThreadPoolExecutor threadExecutor;

	@Override
	public <T> void submit(final ActionData<T> data) {
		threadExecutor.submit(new Runnable() {			
			public void run() {
				dispatcher.dispatch(data);
			}
		});
	}

	@Override
	public int getAvailableTask() {
		return threadExecutor.getQueue().size();
	}
	
}
