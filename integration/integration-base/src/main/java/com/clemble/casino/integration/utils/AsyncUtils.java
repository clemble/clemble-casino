package com.clemble.casino.integration.utils;

import org.junit.Assert;
import java.util.concurrent.Callable;

/**
 * Created by mavarazy on 11/26/14.
 */
public class AsyncUtils {

    private AsyncUtils() {
        throw new IllegalAccessError();
    }

    public static void verify(Callable<Boolean> check) {
        Assert.assertTrue(check(check));
    }

    public static <T> void verifyEquals(Callable<T> A, Callable<T> B) throws Exception {
        boolean check = check(() -> {
            try {
                return A.call().equals(B.call());
            } catch (Exception e) {
                return false;
            }
        });
        if (!check) {
            Assert.assertEquals(A.call(), B.call());
        }
    }

    public static boolean check(Callable<Boolean> check) {
        return check(check, 30_000);
    }

    public static boolean check(Callable<Boolean> check, long checkTimeout) {
        long timeout = System.currentTimeMillis() + checkTimeout;
        while(timeout > System.currentTimeMillis()) {
            boolean result = false;
                try {
                    result = check.call();
                } catch (Throwable throwable) {
                }
            if (result) {
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static <T> boolean checkNotNull(Callable<T> f) {
        return check(() -> {
            try {
                return f.call() != null;
            } catch (Throwable throwable) {
                return false;
            }
        });
    }

    public static <T> T get(Callable<T> get, long getTimeout) {
        long timeout = System.currentTimeMillis() + getTimeout;
        while(timeout > System.currentTimeMillis()) {
            try {
                T candidate = get.call();
                if (candidate != null)
                    return candidate;
            } catch (Throwable throwable) {
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
