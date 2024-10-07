package org.pogonin;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@code MultiThreadCounter} class demonstrates the use of multiple threads,
 * which alternately output numbers in a given range in cyclic order.
 * <p>
 * To synchronize threads, an array of semaphores is used, allowing transmission
 * control between threads in a circle. Each thread prints the current counter value
 * and passes control to the next thread.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * MultiThreadCounter counter = new MultiThreadCounter();
 * counter.startCounting(3, 0, 10);
 * }</pre>
 *
 * Expected output:
 * <pre>
 * pool-1-thread-1: 0
 * pool-1-thread-2: 1
 * pool-1-thread-3: 2
 * pool-1-thread-1: 3
 * pool-1-thread-2: 4
 * pool-1-thread-3: 5
 * // and so on up to 10
 * </pre>
 */
public class MultiThreadCounter {
    /**
     * A counter used to output numbers in streams.
     */
    final AtomicInteger count = new AtomicInteger(0);

    /**
     * Starts the counting process in a given number of threads, which alternately output numbers
     * from {@code startCount} to {@code endCount}.
     *
     * @param threadCount number of threads to perform counting on
     * @param startCount the starting value of the counter
     * @param endCount the end value of the counter (inclusive)
     */
    public void startCounting(int threadCount, int startCount, int endCount) {
        count.set(startCount);
        Semaphore[] semaphores = new Semaphore[threadCount];

        semaphores[0] = new Semaphore(1);
        for (int i = 1; i < threadCount; i++)
            semaphores[i] = new Semaphore(0);

        try(var executor = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                final int finalI = i;

                executor.submit(() -> {
                    try {
                        while (semaphores[finalI].tryAcquire(40, TimeUnit.MILLISECONDS)) {
                            if (count.get() > endCount) return;
                            System.out.println(Thread.currentThread().getName() + ": " + count.getAndIncrement());

                            if (finalI < threadCount - 1)
                                semaphores[finalI + 1].release();
                            else semaphores[0].release();
                        }
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + ": " + e.getMessage());
                    }
                });
            }
        }
    }
}
