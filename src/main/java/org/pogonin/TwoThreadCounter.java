package org.pogonin;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * The {@code TwoThreadCounter} class demonstrates the use of two threads,
 * which alternately display numbers in a given range.
 * <p>
 * Two semaphores are used to synchronize threads: {@code firstSemaphore} and {@code secondSemaphore}.
 * This ensures that the output of numbers is interleaved between the two threads.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * TwoThreadCounter counter = new TwoThreadCounter();
 * counter.startCounting(0, 10);
 * }</pre>
 *
 * Expected output:
 * <pre>
 * pool-1-thread-1: 0
 * pool-1-thread-2: 1
 * pool-1-thread-1: 2
 * pool-1-thread-2: 3
 * // and so on
 * </pre>
 */
public class TwoThreadCounter {
    private final Semaphore firstSemaphore = new Semaphore(1);
    private final Semaphore secondSemaphore = new Semaphore(0);

    /**
     * Starts the counting process in two threads that alternately output numbers
     * from {@code startCount} to {@code endCount}.
     *
     * @param startCount the starting value of the counter
     * @param endCount final counter value
     */
    public void startCounting(int startCount, int endCount) {
        try (var executor = Executors.newFixedThreadPool(2)) {

            executor.execute(() -> {
                try {
                    for (int i = startCount; i <= endCount; i += 2) {
                        firstSemaphore.acquire();
                        System.out.println(Thread.currentThread().getName() + ": " + i);
                        secondSemaphore.release();
                    }
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ": " + e);
                }
            });

            executor.execute(() -> {
                try {
                    for (int i = startCount + 1; i <= endCount; i += 2) {
                        secondSemaphore.acquire();
                        System.out.println(Thread.currentThread().getName() + ": " + i);
                        firstSemaphore.release();
                    }
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ": " + e);
                }
            });
        }
    }
}
