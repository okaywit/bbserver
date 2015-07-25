package com.bbcow.platform.controller;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.ServerConfigurator;
import com.bbcow.api.TuLing;
import com.bbcow.platform.FirstFilter;
import com.bbcow.platform.PlatformCache;
import com.bbcow.util.RequestParam;

/**
 * 聊天
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/chat", configurator = ServerConfigurator.class)
public class ChatController {
        @OnOpen
        public void open(Session session) {
                PlatformCache.userMap.put(session.getId(), session);
        }

        @OnMessage
        public void chatMessage(String message, Session session) {
                message = FirstFilter.filter(message);
                JSONObject object = JSONObject.parseObject(message);

                String msg = object.getString("msg");
                if (msg != null && msg.startsWith("@")) {
                        msg = msg.substring(1);
                        String json = TuLing.request(msg);
                        //返回数据
                        RequestParam.sendSmartChat(json);
                }

                RequestParam.sendChat(message);
        }

        @OnClose
        public void chatQuit(Session session) {
                PlatformCache.userMap.remove(session.getId());
        }
}
