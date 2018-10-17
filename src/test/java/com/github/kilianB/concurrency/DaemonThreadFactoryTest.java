package com.github.kilianB.concurrency;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class DaemonThreadFactoryTest {

	@Test
	void createsDaemonThread() {
		DaemonThreadFactory factory = new DaemonThreadFactory();
		Thread t = factory.newThread(() -> {
		});
		assertTrue(t.isDaemon());
	}

	@Test
	void defaultExceptionIsCaught() {
		DaemonThreadFactory factory = new DaemonThreadFactory();
		Thread t = factory.newThread(() -> {
			throw new IllegalArgumentException();
		});
		t.start();
		// Fails if an exception is thrown
	}

	@Test
	void exceptionHandler() {
		
		CountDownLatch latch = new CountDownLatch(1);
		DaemonThreadFactory factory = new DaemonThreadFactory((thread, throwable) -> {
			latch.countDown();
		});
		
		Thread t = factory.newThread(() -> {
			throw new IllegalArgumentException();
		});
		t.start();
		
		try {
			assertTrue(latch.await(20, TimeUnit.MILLISECONDS));
		}catch(InterruptedException e) {
			fail("Interrupted");
		}
	}
}
