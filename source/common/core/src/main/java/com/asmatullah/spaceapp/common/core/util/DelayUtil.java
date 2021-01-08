package com.asmatullah.spaceapp.common.core.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import timber.log.Timber;

public class DelayUtil {

    private static final Object lock = new Object();

    private volatile static Handler mainHandler = null;

    private volatile static Handler backgroundHandler;

    public static Runnable postDelayedInUIThread(Runnable runnable, long delayMillis) {
        return postDelayed(getMainHandler(), runnable, delayMillis);
    }

    public static Runnable postDelayedInBackgroundThread(Runnable runnable, long delayMillis) {
        return postDelayed(getBackgroundHandler(), runnable, delayMillis);
    }

    private static Handler getMainHandler() {
        if (mainHandler == null) {
            synchronized (lock) {
                mainHandler = new Handler(Looper.getMainLooper());
            }
        }
        return mainHandler;
    }

    public static Handler getBackgroundHandler() {
        if (backgroundHandler == null) {
            synchronized (lock) {
                final HandlerThread mHandlerThread = new HandlerThread("HandlerThread");
                mHandlerThread.start();
                backgroundHandler = new Handler(mHandlerThread.getLooper());
            }
        }
        return backgroundHandler;
    }

    private static Runnable postDelayed(Handler handler, final Runnable runnable, long delayMillis) {
        if (handler == null) {
            return null;
        }

        try {
            Runnable errorHandlingRunnable = () -> {
                try {
                    runnable.run();
                } catch (Throwable throwable) {
                    Timber.e(throwable);
                }
            };
            handler.postDelayed(errorHandlingRunnable, delayMillis);
            return errorHandlingRunnable;
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
        return null;
    }

    public static void removeCallbacks(Runnable runnable) {
        try {
            removeCallbacks(getMainHandler(), runnable);
            removeCallbacks(getBackgroundHandler(), runnable);
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    private static void removeCallbacks(Handler handler, Runnable runnable) {
        if (handler == null) {
            return;
        }

        // If runnable is null everything is removed so we don't want that..!
        if (runnable == null) {
            return;
        }
        handler.removeCallbacks(runnable);
    }

    public static void removeAllCallbacks() {
        if (backgroundHandler != null) {
            backgroundHandler.removeCallbacks(null);
        }
        if (mainHandler != null) {
            mainHandler.removeCallbacks(null);
        }
    }

    public static void cleanUp() {
        if (mainHandler != null || backgroundHandler != null) {
            synchronized (lock) {
                removeAllCallbacks();
                mainHandler = null;
                backgroundHandler = null;
            }
        }
    }
}