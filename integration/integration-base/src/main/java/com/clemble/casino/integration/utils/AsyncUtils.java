package com.clemble.casino.integration.utils;

import org.junit.Assert;

import java.util.function.Function;

/**
 * Created by mavarazy on 11/26/14.
 */
public class AsyncUtils {

    private AsyncUtils() {
        throw new IllegalAccessError();
    }

    public static void verify(Function<Integer, Boolean> check) {
        Assert.assertTrue(check(check));
    }

    public static boolean check(Function<Integer, Boolean> check) {
        return check(check, 30_000);
    }

    public static boolean check(Function<Integer, Boolean> check, long checkTimeout) {
        long timeout = System.currentTimeMillis() + checkTimeout;
        int i = 0;
        while(timeout > System.currentTimeMillis()) {
            boolean result = false;
                try {
                    result = check.apply(i++);
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

    public static <T> boolean checkNotNull(Function<Integer, T> f) {
        return check((i) -> {
            try {
                return f.apply(i) != null;
            } catch (Throwable throwable) {
                return false;
            }
        });
    }

    public static <T> T get(Function<Integer, T> get, long getTimeout) {
        long timeout = System.currentTimeMillis() + getTimeout;
        int i = 0;
        while(timeout > System.currentTimeMillis()) {
            try {
                T candidate = get.apply(i++);
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
