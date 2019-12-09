package com.wei.omini.util;

import com.google.common.base.Strings;

/**
 * @author qiaobinwang@qq.com
 * /remote-server/services/{name}/server-{index}
 */
public class RegisterUtils {

    private static final String ZOOKEEPER_ROOT_PATH = "/remote-server";

    private static final String SERVICES = "services";

    private static final String SERVER = "server-";

    /**
     * @param serviceName
     * @return /remote-server/services/{name}
     */
    public static String getServiceFolderPath(String serviceName) {
        if (Strings.isNullOrEmpty(serviceName)) return null;
        return getServiceParentPath() + "/" + serviceName;
    }

    /**
     * @return /remote-server/services
     */
    public static String getServiceParentPath() {
        return ZOOKEEPER_ROOT_PATH + "/" + SERVICES;
    }

    public static String getServerPath(String serviceName) {
        if (Strings.isNullOrEmpty(serviceName)) return null;
        return getServiceFolderPath(serviceName) + "/" + SERVER;
    }

    public static String getServerNodeData(String host, Integer port) {
        return host + ":" + port + ":0";
    }
}
