package com.wei.omini.model;

import lombok.Data;

import java.util.Objects;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-12-05 10:48
 */
@Data
public class RemoteServer {
    private String name;
    private String host;
    private Integer port;

    public RemoteServer() {
    }

    public RemoteServer(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public RemoteServer(String name, String host, Integer port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteServer that = (RemoteServer) o;
        return host.equals(that.host) &&
                port.equals(that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
