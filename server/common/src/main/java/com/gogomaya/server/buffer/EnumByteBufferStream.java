package com.gogomaya.server.buffer;

import java.nio.ByteBuffer;

public class EnumByteBufferStream<T extends Enum<?>> implements ByteBufferStream<T> {

    final private static byte NULL_ENUM = -1;

    final Class<T> enumClass;

    public EnumByteBufferStream(Class<T> enumClass) {
        if (enumClass == null)
            throw new IllegalArgumentException("Class can't be null");
        if (!enumClass.isEnum())
            throw new IllegalArgumentException("Class must be enum");

        this.enumClass = enumClass;
    }

    @Override
    public ByteBuffer write(T value, ByteBuffer writeBuffer) {
        if (value == null) {
            writeBuffer.put(NULL_ENUM);
        } else {
            writeBuffer.put((byte) value.ordinal());
        }
        return writeBuffer;
    }

    @Override
    public T read(ByteBuffer readBuffer) {
        int value = readBuffer.get();
        if (value == NULL_ENUM)
            return null;
        return enumClass.getEnumConstants()[value];
    }

}
