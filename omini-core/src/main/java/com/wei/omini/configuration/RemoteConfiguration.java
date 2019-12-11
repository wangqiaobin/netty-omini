package com.wei.omini.configuration;

import com.wei.omini.common.util.IPv4Util;
import com.wei.omini.constants.Constants;
import com.wei.omini.model.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-18 16:01
 */
@Configuration
public class RemoteConfiguration {

    @Resource
    private RemoteProperties properties;


    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public Server server() {
        Server server = new Server();
        if (properties.getPort() == null) {
            properties.setPort(Constants.DEFAULT_PORT);
        }
        Thread thread = new Thread(() -> {
            try {
                String host = IPv4Util.getAddressIp();
                server.start(properties.getName(), host, properties.getPort());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        return server;
    }
}
