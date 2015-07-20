package com.bbcow.platform.controller;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.platform.HostCache;

@ClientEndpoint
public class HostConnectCenter {
        @OnOpen
        public void hostOpen(Session hostSession) {

        }

        @OnMessage
        public void hostMessage(String message, Session hostSession) {
                JSONObject object = JSONObject.parseObject(message);
                //用户ID
                String usi = object.getString("sId");
                try {
                        HostCache.userMap.get(usi).getBasicRemote().sendText(message);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        @OnClose
        public void hostQuit(Session userSession) {

        }
}