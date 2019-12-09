package com.wei.omini.model;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-15 16:32
 */
public interface IRemoteServer {
    int onRequest(ServerInfo server, RequestContext context);

    int onTimeout(ServerInfo server, RequestContext context);

    int onReceive(ServerInfo server, RequestContext context);
}