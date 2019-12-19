package com.wei.omini.model;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-15 16:32
 */
public interface IRemoteServer {
    int onRequest(RemoteServer server, Context param);

    int onTimeout(RemoteServer server, Context param);

    int onReceive(RemoteServer server, Context param);
}