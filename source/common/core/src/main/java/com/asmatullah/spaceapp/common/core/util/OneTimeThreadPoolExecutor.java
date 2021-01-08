package com.asmatullah.spaceapp.common.core.util;

import android.util.Log;

import com.asmatullah.spaceapp.common.core.BuildConfig;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class OneTimeThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Object lock = new Object();
    private static OneTimeThreadPoolExecutor instance;

    public static OneTimeThreadPoolExecutor getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new OneTimeThreadPoolExecutor();
            }
            return instance;
        }
    }

    public OneTimeThreadPoolExecutor() {
        this("One", 4, (4 * 2));
    }

    public static OneTimeThreadPoolExecutor create(String threadPoolName, int defaultThreadPoolSize, int maximumPoolSize) {
        return new OneTimeThreadPoolExecutor(threadPoolName, defaultThreadPoolSize, maximumPoolSize);
    }

    private OneTimeThreadPoolExecutor(String threadPoolName, int defaultThreadPoolSize, int maximumPoolSize) {
        // For one time tasks we have a smaller core but larger max because the number of tasks queued can vary quite a lot.
        // We therefore also hold a very large queue.
        super(defaultThreadPoolSize,
                maximumPoolSize,
                20,
                TimeUnit.SECONDS,
                new ThreadPoolExecutorPriorityQueue(1, new PriorityTaskComparator()));
        setThreadFactory(new NamedThreadFactory(threadPoolName));
        setRejectedExecutionHandler(new BlockingQueuePut());
    }


    private static class PriorityTaskComparator
            implements Comparator<Runnable> {
        @Override
        public int compare(final Runnable left, final Runnable right) {
            int leftPriority = Thread.NORM_PRIORITY;
            int rightPriority = Thread.NORM_PRIORITY;
            if (left instanceof PriorityRunnable) {
                leftPriority = ((PriorityRunnable) left).priority;
            }
            if (right instanceof PriorityRunnable) {
                rightPriority = ((PriorityRunnable) right).priority;
            }
            return leftPriority - rightPriority;
        }
    }

    private static final class PriorityRunnable implements Runnable {
        private final int priority;
        private final Runnable runnable;

        PriorityRunnable(final int priority, final Runnable runnable) {
            this.priority = priority;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            runnable.run();
        }
    }

    private static final Random random = new Random();

    private static class ThreadPoolExecutorPriorityQueue extends PriorityBlockingQueue<Runnable> {
        public ThreadPoolExecutorPriorityQueue(int initialCapacity, PriorityTaskComparator priorityTaskComparator) {
            super(initialCapacity, priorityTaskComparator);
        }

        // NOTE: See http://stackoverflow.com/questions/19528304/how-to-get-the-threadpoolexecutor-to-increase-threads-to-max-before-queueing
        @Override
        public boolean offer(Runnable runnable) {
            /*
             * Offer it to the queue if there is 1 or 0 items already queued, else
             * return false so the TPE will add another thread. If we return false
             * and max threads have been reached then the RejectedExecutionHandler
             * will be called which will do the put into the queue.
             *
             * NOTE: I chose 1 to protect against race conditions where a task had
             * been added to the queue but the threads had not dequeued it yet. But
             * if there were more than 1, chances were greater that the current
             * threads were not keeping up with the load.
             */
            if (size() <= 0) {
                return super.offer(runnable);
            } else {
                return false;
            }
        }

        public void forceOffer(Runnable runnable) {
            super.offer(runnable);
        }
    }

    private class BlockingQueuePut implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (OneTimeThreadPoolExecutor.this.isShutdown()) {
                if (BuildConfig.DEBUG) {
                    Log.d(OneTimeThreadPoolExecutor.class.getSimpleName(), "Rejecting task because OneTimeThreadPoolExecutor has been shut down.");
                }
                return;
            }
            ((ThreadPoolExecutorPriorityQueue) executor.getQueue()).forceOffer(r);
        }
    }

    public void execute(final Runnable command) {
        try {
            super.execute(() -> {
                try {
                    command.run();
                } catch (Throwable ex) {
                    if (BuildConfig.DEBUG) {
                        Log.d("Unable to execute", "Fake exception to enable tracing call");
                    }
                    Timber.e(ex);
                }
            });
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    public void executeWithPriority(Runnable runnable, int priority) {
        if (random.nextInt(200) == 0) {
            // We sometimes give min-prio tasks normal prio in order to avoid starvation.
            execute(runnable);
        } else {
            execute(new PriorityRunnable(priority, runnable));
        }
    }
}
