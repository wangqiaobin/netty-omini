package com.wei.omini.register.register;

import com.google.common.collect.Maps;
import com.wei.omini.register.ServiceRegister;
import com.wei.omini.util.RegisterUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.Map;

import static com.wei.omini.constants.Constants.ZK_SESSION_TIMEOUT;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
@Slf4j
public class ZookeeperRegister implements ServiceRegister, Watcher {

    private Zookeeper zookeeper;

    private Map<String, String> remote = Maps.newHashMap();

    public ZookeeperRegister(String address) {
        this.zookeeper = new ZooKeeperWrapper();
        zookeeper.connection(address, ZK_SESSION_TIMEOUT, this);
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
        } else if (Event.KeeperState.AuthFailed == event.getState()) {
        } else if (Event.KeeperState.ConnectedReadOnly == event.getState()) {
        } else if (Event.KeeperState.Disconnected == event.getState()) {
        } else if (Event.KeeperState.Expired == event.getState()) {
        }
        if (Event.EventType.None == event.getType()) {
        } else if (Event.EventType.NodeCreated == event.getType()) {
        } else if (Event.EventType.NodeDataChanged == event.getType()) {
        } else if (Event.EventType.NodeChildrenChanged == event.getType()) {
        } else if (Event.EventType.NodeDeleted == event.getType()) {
        }
    }

    @Override
    public void register(String name, String host, Integer port) {
        if (!remote.containsKey(name)) {
            remote.put(name, RegisterUtils.getServerNodeData(host, port));
        }
        String parent = RegisterUtils.getServiceParentPath();
        if (!zookeeper.exists(parent)) {
            zookeeper.createPath(parent);
        }
        String folder = RegisterUtils.getServiceFolderPath(name);
        if (!zookeeper.exists(folder)) {
            zookeeper.createPath(folder);
        }
        String server = RegisterUtils.getServerPath(name);
        String node = RegisterUtils.getServerNodeData(host, port);
        zookeeper.writeData(server, node);
        log.info("register zookeeper ip={} port={}", host, port);
    }

    @Override
    public void shutdown() {
        this.zookeeper.close();
    }
}
