package com.wei.omini.register.register;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wei.omini.exception.NotFoundRegistryException;
import com.wei.omini.model.RemoteClient;
import com.wei.omini.register.ServiceDiscover;
import com.wei.omini.util.RegisterUtils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.wei.omini.constants.Constants.ZK_SESSION_TIMEOUT;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
@Slf4j
public class ZookeeperDiscover implements ServiceDiscover, Watcher {

    private final Zookeeper zookeeper;

    private final Map<String, List<RemoteClient>> remote = Maps.newConcurrentMap();

    public ZookeeperDiscover(String address) {
        zookeeper = new ZooKeeperWrapper();
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
    public RemoteClient discover(String name) {
        String path = RegisterUtils.getServiceFolderPath(name);
        if (StringUtil.isNullOrEmpty(path) || !zookeeper.exists(path)) {
            throw new NotFoundRegistryException(String
                    .format("not found service <%s> in zookeeper registry <%s>", name, zookeeper));
        }
        List<RemoteClient> list = remote.getOrDefault(name, Lists.newArrayList());
        if (list.isEmpty()) {
            List<String> children = zookeeper.getChildren(path);
            if (Objects.isNull(children) || children.isEmpty()) {
                throw new NotFoundRegistryException(String
                        .format("not found available server for service <%s> in zookeeper registry <%s>", name, zookeeper));
            }
            for (String child : children) {
                String data = zookeeper.readData(path + "/" + child);
                if (StringUtil.isNullOrEmpty(data)) {
                    continue;
                }
                if (!list.contains(data)) {
                    String[] split = data.split(":");
                    RemoteClient client = new RemoteClient(name, split[0], Integer.valueOf(split[1]));
                    list.add(client);
                }
            }
            remote.put(name, list);
        }
        return list.get((int) (System.currentTimeMillis() % list.size()));
    }

    @Override
    public RemoteClient discover(String name, String host, Integer port) {
        List<RemoteClient> clients = remote.getOrDefault(name, Lists.newArrayList());
        for (RemoteClient client : clients) {
            if (client.getServer().getHost().equals(host) && client.getServer().getPort().equals(port)) {
                return client;
            }
        }
        RemoteClient client = new RemoteClient(name, host, port);
        clients.add(client);
        return client;
    }
}
