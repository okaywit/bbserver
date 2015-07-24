package com.bbcow.platform.controller;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.BusCache;
import com.bbcow.ServerConfigurator;
import com.bbcow.db.MongoPool;
import com.bbcow.server.po.ShareHost;
import com.bbcow.util.RequestParam;

/**
 * 分享
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/share", configurator = ServerConfigurator.class)
public class ShareController {
        @OnOpen
        public void open(Session session) {

                for (String s : MongoPool.findHost()) {
                        RequestParam.sendText(BusCache.MESSAGE_TYPE_SHAREHOST, s, session);
                }
        }

        @OnMessage
        public void shareMessage(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                ShareHost host = new ShareHost();
                host.setIp(object.getString("ip"));
                host.setEmail(object.getString("email"));
                host.setPoint(object.getString("point"));
                host.setPort(object.getString("port"));
                host.setName(object.getString("name"));
                host.setPath(object.getString("path"));
                if (MongoPool.findOneHost(object.getString("path")) == null) {
                        MongoPool.insertHost(host);
                        RequestParam.sendSuccess("分享完成", session);
                } else {
                        RequestParam.sendError("英文别名已存在", session);
                }
        }
}
