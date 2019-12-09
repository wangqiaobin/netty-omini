package com.wei.omini.register.register;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-19 23:44
 */
@Slf4j
public class ZooKeeperWrapper implements Zookeeper {

    private ZooKeeper zooKeeper;
    private String address;
    private ReentrantLock lock = new ReentrantLock();
    private Integer timeout;


    private final ConcurrentMap<String, List<String>> children = Maps.newConcurrentMap();


    @Override
    public void connection(String address, Integer timeout, Watcher watcher) {
        try {
            zooKeeper = new ZooKeeper(address, timeout, watcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean createPath(String path) {
        try {
            log.info("create node path {} success~!", zooKeeper.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
            return true;
        } catch (KeeperException | InterruptedException e) {
            String parent = path.substring(0, path.lastIndexOf(47));
            try {
                log.info("create node path {} success~!", zooKeeper.create(parent, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
                log.info("create node path {} success~!", zooKeeper.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
            } catch (KeeperException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String writeData(String path, String data) {
        try {
            String node = zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("update node path {} success~!", node);
            return node;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String readData(String path) {
        try {
            byte[] data = zooKeeper.getData(path, false, null);
            return new String(data);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteNode(String path) {
        try {
            zooKeeper.delete(path, -1);
            log.info("delete node path {} success~!", path);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(String path) {
        try {
            if (children.containsKey(path)) {
                return true;
            }
            return zooKeeper.exists(path, false) != null;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> getChildren(String path) {
        try {
            if (children.containsKey(path) && !children.get(path).isEmpty()) {
                return children.get(path);
            }
            List<String> list = zooKeeper.getChildren(path, false);
            children.put(path, list);
            return list;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }

    @Override
    public void deleteAllPath() {
        children.forEach(new BiConsumer<String, List<String>>() {
            @Override
            public void accept(String path, List<String> strings) {
                System.out.println(path);
            }
        });
        children.clear();
    }
}
