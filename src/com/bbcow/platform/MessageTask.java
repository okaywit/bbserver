package com.bbcow.platform;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.server.util.RequestParam;

public class MessageTask implements Runnable {
        private String message;

        public MessageTask(String message) {
                this.message = message;
        }

        @Override
        public void run() {
                for (Iterator<Session> it = HostCache.userMap.values().iterator(); it.hasNext();) {
                        Session session = it.next();

                        try {

                                JSONObject object = JSONObject.parseObject(message);

                                session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_PUSH, message));
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

}
