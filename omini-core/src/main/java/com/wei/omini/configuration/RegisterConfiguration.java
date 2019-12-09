package com.wei.omini.configuration;

import com.wei.omini.register.ServiceRegister;
import com.wei.omini.register.register.ZookeeperRegister;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-19 17:40
 */
@Configuration
@EnableConfigurationProperties(RemoteProperties.class)
public class RegisterConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "spring.remote.rpc.zookeeper")
    public ServiceRegister zookeeperRegister(RemoteProperties properties) {
        return new ZookeeperRegister(properties.getZookeeper());
    }
}
