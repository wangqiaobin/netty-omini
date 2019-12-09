package com.wei.omini.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPv4Util {
    public static String getAddressIp() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ip = addr.getHostAddress();
        return ip;
    }
}
