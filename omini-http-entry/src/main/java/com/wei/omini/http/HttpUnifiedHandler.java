package com.wei.omini.http;

import com.wei.omini.common.util.IdUtil;
import com.wei.omini.constants.Constants;
import com.wei.omini.handler.ServerContextHandler;
import com.wei.omini.model.RequestContext;
import com.wei.omini.model.ServerInfo;
import com.wei.omini.request.RemoteRequest;
import com.wei.omini.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private RemoteRequest remote;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public Callable<Object> unified(@RequestParam("_name") String name,
                                    @RequestParam("_cmd") String cmd,
                                    @RequestParam("_sub") String sub,
                                    @RequestParam(value = "_version", required = false, defaultValue = Constants.DEFAULT_VERSION) String version,
                                    @RequestParam Map object,
                                    HttpServletRequest request) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                ServerInfo info = buildServerInfo(name, request.getLocalAddr(), request.getLocalPort());
                RequestContext context = buildRequestContext(name, cmd, sub, version, object);
                ServerContextHandler.getInstance().putContext(info, context);
                RemoteHttpHandler router = (RemoteHttpHandler) ServerContextHandler.getInstance().getRemoteServer("http-entry", "router", Constants.DEFAULT_VERSION);
                if (Objects.isNull(router)) {
                    Map<String, RemoteHttpHandler> beans = ApplicationContextUtil.getBeansOfType(RemoteHttpHandler.class);
                    router = (RemoteHttpHandler) beans.values().toArray()[0];
                    ServerContextHandler.getInstance().putRemoteServer("http-entry", "router", Constants.DEFAULT_VERSION, router);
                }
                router.onRequest(info, context);
                synchronized (context) {
                    context.wait();
                }
                return context.getContent();
            }
        };
    }

    private ServerInfo buildServerInfo(String name, String host, Integer port) {
        ServerInfo info = new ServerInfo();
        info.setName(name);
        info.setHost(host);
        info.setPort(port);
        return info;
    }

    private RequestContext buildRequestContext(String name, String cmd, String sub, String version, Map object) {
        RequestContext context = new RequestContext();
        context.setState(0);
        context.setFrom(name);
        context.setAck(false);
        context.setReq(IdUtil.buildHax());
        context.setCmd(cmd);
        context.setSub(sub);
        context.setVersion(version);
        context.setTime(System.currentTimeMillis());
        context.setContent(object);
        return context;
    }

}
