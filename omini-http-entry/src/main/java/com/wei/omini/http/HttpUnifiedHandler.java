package com.wei.omini.http;

import com.wei.omini.common.util.IPv4Util;
import com.wei.omini.common.util.IdUtil;
import com.wei.omini.configuration.RemoteProperties;
import com.wei.omini.constants.Constants;
import com.wei.omini.handler.ServerContextHandler;
import com.wei.omini.model.Context;
import com.wei.omini.model.InnerContext;
import com.wei.omini.model.RemoteServer;
import com.wei.omini.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-12-10 14:08
 */
@Slf4j
@EnableAsync
@RestController
@RequestMapping("/")
public class HttpUnifiedHandler {

    @Resource
    private RemoteProperties properties;
    private final static String host = IPv4Util.getAddressIp();

    @RequestMapping(value = "get", method = RequestMethod.GET)

    public Callable<Object> unified(@RequestParam("_name") String name,
                                    @RequestParam("_cmd") String cmd,
                                    @RequestParam("_sub") String sub,
                                    @RequestParam(value = "_version", required = false, defaultValue = Constants.DEFAULT_VERSION) String version,
                                    @RequestParam Map object) {
        object.remove("_name");
        object.remove("_cmd");
        object.remove("_sub");
        object.remove("_version");
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                RemoteServer info = buildServerInfo(name, host, properties.getPort());
                Context param = buildRequestContext(name, cmd, sub, version, object);
                InnerContext context = new InnerContext(System.currentTimeMillis());
                context.setContext(param);
                context.setState(1);
                context.setServer(info);
                ServerContextHandler.getInstance().putContext(context);
                RemoteHttpHandler router = (RemoteHttpHandler) ServerContextHandler.getInstance().getRemoteServer("http-entry", "router", Constants.DEFAULT_VERSION);
                if (Objects.isNull(router)) {
                    Map<String, RemoteHttpHandler> beans = ApplicationContextUtil.getBeansOfType(RemoteHttpHandler.class);
                    router = (RemoteHttpHandler) beans.values().toArray()[0];
                    ServerContextHandler.getInstance().putRemoteServer("http-entry", "router", Constants.DEFAULT_VERSION, router);
                }
                log.info("unified {}", context);
                router.onRequest(info, param);
                synchronized (context.getTime()) {
                    context.getTime().wait();
                }
                return context.getContext();
            }
        };
    }

    private RemoteServer buildServerInfo(String name, String host, Integer port) {
        RemoteServer info = new RemoteServer();
        info.setName(name);
        info.setHost(host);
        info.setPort(port);
        return info;
    }

    private Context buildRequestContext(String name, String cmd, String sub, String version, Map object) {
        Context context = new Context();
        context.setState(0);
        context.setReq(IdUtil.buildHax());
        context.setCmd(cmd);
        context.setSub(sub);
        context.setVersion(version);
        context.setContent(object);
        return context;
    }

}
