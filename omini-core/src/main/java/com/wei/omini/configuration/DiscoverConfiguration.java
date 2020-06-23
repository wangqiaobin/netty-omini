package com.wei.omini.configuration;

import com.wei.omini.register.ServiceDiscover;
import com.wei.omini.register.register.ZookeeperDiscover;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-13 00:08
 */
@Configuration
@EnableConfigurationProperties(RemoteProperties.class)
public class DiscoverConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "spring.remote.rpc.zookeeper")
    public ServiceDiscover zookeeperDiscover(RemoteProperties properties) {
        return new ZookeeperDiscover(properties.getZookeeper());
    }
}
