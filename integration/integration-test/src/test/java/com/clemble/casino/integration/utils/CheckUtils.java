package com.clemble.casino.integration.utils;

import java.util.function.Function;

/**
 * Created by mavarazy on 11/26/14.
 */
public class CheckUtils {

    private CheckUtils() {
        throw new IllegalAccessError();
    }

    public static boolean check(Function<Integer, Boolean> check) {
        long timeout = System.currentTimeMillis() + 30_000;
        int i = 0;
        while(timeout > System.currentTimeMillis()) {
            boolean result = check.apply(i++);
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
}
