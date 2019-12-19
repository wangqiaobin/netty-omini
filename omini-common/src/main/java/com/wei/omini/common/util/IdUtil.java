package com.wei.omini.common.util;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分布式ID算法 TODO 简单实现，并未解决分布式ID问题
 * Created by wangqiaobin on 2016/12/22.
 */
public class IdUtil {

    private static AtomicInteger integer = new AtomicInteger(0);
    private static long time = System.currentTimeMillis();

    public static long build() {
        return build(0L);
    }

    public static long build(long key) {
        long millis = System.currentTimeMillis();
        if (time != millis) {
            synchronized (integer) {
                if (time != millis) {
                    time = millis;
                    integer.set(0);
                }
            }
        }
        long t = time << 31;
        long k = (key & 0x000FF0) << 12;
        return t + k + integer.incrementAndGet();
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 10000000; i++) {
            String hax = IdUtil.buildHax(System.nanoTime());
            set.add(hax);
        }
        System.out.println(set.size());
        System.out.println(System.currentTimeMillis() - l);
    }

    public static String buildHax() {
        return ByteUtil.bytes2Hex(ByteUtil.long2Bytes(build()));
    }

    public static String buildHax(long key) {
        return ByteUtil.bytes2Hex(ByteUtil.long2Bytes(build(key)));
    }

    public static boolean check(long id) {
        return false;
    }
}
