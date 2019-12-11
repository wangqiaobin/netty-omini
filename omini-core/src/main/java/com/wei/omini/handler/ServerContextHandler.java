package com.wei.omini.handler;

import com.wei.omini.common.util.ThreadUtil;
import com.wei.omini.model.Context;
import com.wei.omini.model.IRemoteServer;
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
                                if (millis - context.getTime() > 3000) {
                                    IRemoteServer server = instance.getRemoteServer(entry.getValue().getParam().getCmd(), entry.getValue().getParam().getSub(), entry.getValue().getParam().getVersion());
                                    server.onTimeout(entry.getValue().getServer(), entry.getValue().getParam());
                                    Context buffer = instance.removeContext(entry.getKey());
                                    log.info("timeout request={}", entry.getValue());
                                }
                            }
                            ThreadUtil.sleep(10);
                        }
                    });
//                    thread.start();
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
        String key = buildHandlerKey(cmd, sub, version);
        if (this.beans.containsKey(key)) {
            return this.beans.get(key);
        }
        return null;
    }

    public String buildHandlerKey(String cmd, String sub, String version) {
        return cmd + sub + version;
    }

    public void putContext(Context context) {
        if (hasContext(context.getParam().getReq())) return;
        this.context.put(context.getParam().getReq(), context);
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
