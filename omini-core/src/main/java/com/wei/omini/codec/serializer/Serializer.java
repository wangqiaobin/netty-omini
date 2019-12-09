package com.wei.omini.codec.serializer;

public interface Serializer {
    byte[] serialize(Object data);

    <T> T deserialize(byte[] dataBytes);
}
