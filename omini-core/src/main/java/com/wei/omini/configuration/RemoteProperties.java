package com.wei.omini.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
@Data
@ConfigurationProperties(prefix = "spring.remote.rpc")
public class RemoteProperties {


    private String name;

    /**
     * spring.remote.rpc.port
     */
    private Integer port;

    private String zookeeper;

}
