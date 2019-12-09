package com.wei.omini.model;

import lombok.Data;

import java.util.Objects;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-12-05 10:48
 */
@Data
public class ServerInfo {
    private String name;
    private String host;
    private Integer port;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerInfo that = (ServerInfo) o;
        return host.equals(that.host) &&
                port.equals(that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
