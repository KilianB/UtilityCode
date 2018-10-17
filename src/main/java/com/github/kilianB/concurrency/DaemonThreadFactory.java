package com.github.kilianB.concurrency;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Analogous implementation of the default thread factory used by java executors. This 
 * Factory spawns daemon threads in the same thread group.
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class DaemonThreadFactory implements ThreadFactory {

	private static final Logger LOGGER = Logger.getLogger(DaemonThreadFactory.class.getName());
	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;

	private UncaughtExceptionHandler handler = (thread,throwable) ->{
		LOGGER.severe("Uncaught exception in: " + thread + "Throwable: " + throwable);
	};
	
	/**
	 * A thread factory creating daemon threads. A default uncaught exception handler
	 * is attached to every thread created.
	 */
	public DaemonThreadFactory() {
		this("");
	}
	
	/**
	 * A thread factory creating daemon threads.
	 * @param handler Exception handler attached to every created thread
	 */
	public DaemonThreadFactory(UncaughtExceptionHandler handler) {
		this("");
		this.handler = handler;
	}
	
	/**
	 * A thread factory creating daemon threads.A default uncaught exception handler
	 * is attached  to every thread created.
	 * @param namePrefix prefixed to name threads created by this factory.
	 *  Threads will be named namePrefix-thread#.
	 *  If null or empty a pattern of pool-pool#-thread# is applied.
	 */
	public DaemonThreadFactory(String namePrefix) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		if(namePrefix == null || namePrefix.isEmpty()) {
			this.namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
		}else {
			this.namePrefix = namePrefix+"-";
		}
	}
	
	/**
	 * A thread factory creating daemon threads
	 * 
	 * @param namePrefix namePrefix prefixed to name threads created by this factory.
	 *  Threads will be named namePrefix-thread#.
	 *  If null or empty a pattern of pool-pool#-thread# is applied.
	 * @param handler Exception handler attached to every created thread
	 */
	public DaemonThreadFactory(String namePrefix,UncaughtExceptionHandler handler) {
		this(namePrefix);
		this.handler = handler;
	}
	

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		t.setDaemon(true);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		t.setUncaughtExceptionHandler(handler);
		return t;
	}

}