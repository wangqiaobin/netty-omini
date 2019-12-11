package com.wei.omini.model;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-15 16:32
 */
public interface IRemoteServer {
    int onRequest(RemoteServer server, RemoteRequest param);

    int onTimeout(RemoteServer server, RemoteRequest param);

    int onReceive(RemoteServer server, RemoteRequest param);
}