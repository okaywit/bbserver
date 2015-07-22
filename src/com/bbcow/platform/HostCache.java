package com.bbcow.platform;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.bbcow.db.MongoPool;
import com.bbcow.platform.controller.HostConnectCenter;
import com.bbcow.server.po.ShareHost;

public class HostCache {
        //首页用户
        public static Map<String, Session> userMap = new HashMap<String, Session>();
        //各主机用户
        public static Map<String, List<Session>> hostUserMap = new HashMap<String, List<Session>>();

        public static Map<String, Session> hostMap = new HashMap<String, Session>();
        private static WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        //随机访问
        public static Queue<String> queue = new LinkedBlockingQueue<String>(5);

        //TODO 主机未启动处理
        public static void init() {
                List<ShareHost> hosts = MongoPool.findBBHost();
                for (ShareHost host : hosts) {
                        String uri = "ws://" + host.getIp() + ":" + host.getPort() + "/" + host.getPoint();
                        try {
                                hostMap.put(host.getPath(), container.connectToServer(HostConnectCenter.class, new URI(uri)));
                        } catch (DeploymentException | IOException | URISyntaxException e) {
                                hostMap.put(host.getPath(), null);
                        }
                }
        }

        public static boolean initHost(String path) {
                boolean flag = false;
                ShareHost host = MongoPool.findOneHost(path);
                if (host == null)
                        return flag;
                String uri = "ws://" + host.getIp() + ":" + host.getPort() + "/" + host.getPoint();
                try {
                        hostMap.put(host.getPath(), container.connectToServer(HostConnectCenter.class, new URI(uri)));
                        flag = true;
                } catch (DeploymentException | IOException | URISyntaxException e) {
                        e.printStackTrace();
                        hostMap.put(host.getPath(), null);
                }
                return flag;
        }

        public static Session connect(String path) {
                ShareHost host = MongoPool.findOneHost(path);
                Session session = null;
                if (host == null)
                        return session;

                String uri = "ws://" + host.getIp() + ":" + host.getPort() + "/" + host.getPoint();
                try {
                        session = container.connectToServer(HostConnectCenter.class, new URI(uri));
                } catch (DeploymentException | IOException | URISyntaxException e) {
                        e.printStackTrace();
                }
                return session;
        }
}
