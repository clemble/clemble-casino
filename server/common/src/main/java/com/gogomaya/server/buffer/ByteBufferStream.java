package com.gogomaya.server.buffer;

import java.nio.ByteBuffer;

public interface ByteBufferStream<T> {

    public ByteBuffer write(T value, ByteBuffer writeBuffer);

    public T read(ByteBuffer readBuffer);

}
