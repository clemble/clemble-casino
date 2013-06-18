package com.gogomaya.server.utils;

import java.nio.ByteBuffer;

public class NumberUtils {

    public static byte[] toByteArray(long longValue) {
        return ByteBuffer.allocate(8).putLong(longValue).array();
    }

    public static <T extends Enum<T>> byte[] toByteArray(T value) {
        return new byte[] { (byte) value.ordinal() };
    }

}
