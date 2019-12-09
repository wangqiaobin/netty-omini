package com.wei.omini.handler;

import com.wei.omini.common.util.ThreadUtil;
import com.wei.omini.model.IRemoteServer;
import com.wei.omini.model.RequestContext;
import com.wei.omini.model.ServerInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-19 11:22
 */
@Slf4j
public class ServerContextHandler {
    @Data
    public static class Context {
        private RequestContext request;
        private ServerInfo server;
    }

    private static final Map<String, Context> context = new ConcurrentHashMap<>();
    private static final Map<String, IRemoteServer> beans = new ConcurrentHashMap<>();
    private static ServerContextHandler instance;

    private ServerContextHandler() {
    }

    public static ServerContextHandler getInstance() {
        if (instance == null) {
            synchronized (ServerContextHandler.class) {
                if (instance == null) {
                    instance = new ServerContextHandler();
                    Thread thread = new Thread(() -> {
                        while (true) {
                            if (context.isEmpty()) {
                                ThreadUtil.sleep(10);
                                continue;
                            }
                            long millis = System.currentTimeMillis();
                            for (Map.Entry<String, Context> entry : context.entrySet()) {
                                Context context = entry.getValue();
                                if (millis - context.getRequest().getTime() > 3000) {
                                    IRemoteServer server = instance.getRemoteServer(entry.getValue().getRequest().getCmd(), entry.getValue().getRequest().getSub(), entry.getValue().getRequest().getVersion());
                                    server.onTimeout(entry.getValue().getServer(), entry.getValue().getRequest());
                                    Context buffer = instance.removeContext(entry.getKey());
                                    log.info("timeout request={}", entry.getValue());
                                    buffer.setServer(null);
                                    buffer.setRequest(null);
                                }
                            }
                            ThreadUtil.sleep(10);
                        }
                    });
                    thread.start();
                }
            }
        }
        return instance;
    }

    public void putRemoteServer(String cmd, String sub, String version, IRemoteServer bean) {
        String key = cmd + sub + version;
        beans.put(key, bean);
    }

    public boolean hasRemoteServer(String cmd, String sub, String version) {
        return beans.containsKey(cmd + sub + version);
    }

    public IRemoteServer getRemoteServer(String cmd, String sub, String version) {
        String key = cmd + sub + version;
        if (this.beans.containsKey(key)) {
            return this.beans.get(key);
        }
        return null;
    }

    public void putContext(ServerInfo server, RequestContext context) {
        if (hasContext(context.getReq())) return;
        Context buffer = new Context();
        buffer.setServer(server);
        buffer.setRequest(context);
        this.context.put(context.getReq(), buffer);
    }

    public boolean hasContext(String req) {
        return context.containsKey(req);
    }

    public Context removeContext(String req) {
        return context.remove(req);
    }


    public Context getContext(String req) {
        if (!context.containsKey(req)) {
            return null;
        }
        return context.get(req);
    }

}
