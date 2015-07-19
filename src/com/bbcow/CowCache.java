package com.bbcow;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.bbcow.command.Command01;
import com.bbcow.command.Command02;
import com.bbcow.command.Command03;
import com.bbcow.command.Command04;
import com.bbcow.command.Command05;
import com.bbcow.command.Command06;
import com.bbcow.command.ICommand;
import com.bbcow.controller.client.HostConnectCenter;
import com.bbcow.db.MongoPool;
import com.bbcow.po.ShareHost;

/**
 * 缓存
 * 
 * @author 大辉Face
 */

public class CowCache {
        public static ExecutorService threads = null;
        public static Map<String, Session> userMap = new HashMap<String, Session>();
        public static Map<Integer, ICommand> commandMap = new HashMap<Integer, ICommand>();
        public static Map<String, Session> hostMap = new HashMap<String, Session>();
        private static WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        public static void init() {
        	
                commandMap.put(1, new Command01());
                commandMap.put(2, new Command02());
                commandMap.put(3, new Command03());
                commandMap.put(4, new Command04());
                commandMap.put(5, new Command05());
                commandMap.put(6, new Command06());

                threads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                //初始化主机连接
                initHosts();
        }
        
        //TODO 主机未启动处理
        public static void initHosts(){
        	List<ShareHost> hosts = MongoPool.findBBHost();
        	for(ShareHost host : hosts){
        		String uri = "ws://" + host.getIp() + ":" + host.getPort() + "/" + host.getPoint();
        		try {
        			hostMap.put(host.getPath(), container.connectToServer(new HostConnectCenter(), new URI(uri)));
				} catch (DeploymentException | IOException | URISyntaxException e) {
					hostMap.put(host.getPath(),null);
				}
        	}
        }
        public static boolean initHost(String path){
    		boolean flag = false;
        	ShareHost host = MongoPool.findOneHost(path);
        	if(host==null)
        		return flag;
    		String uri = "ws://" + host.getIp() + ":" + host.getPort() + "/" + host.getPoint();
    		try {
    			hostMap.put(host.getPath(), container.connectToServer(new HostConnectCenter(), new URI(uri)));
    			flag = true;
			} catch (DeploymentException | IOException | URISyntaxException e) {
				e.printStackTrace();
				hostMap.put(host.getPath(),null);
			}
    		return flag;
        }
}
