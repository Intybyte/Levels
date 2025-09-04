package com.thexfactor117.levels.architectury.util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class NetworkUtil {
    public static void writeString(ByteBuf buf, String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public static String readString(ByteBuf buf) {
        int len = buf.readInt();
        if (len < 0 || len > 32767) throw new IllegalArgumentException("Invalid string length: " + len);
        byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
