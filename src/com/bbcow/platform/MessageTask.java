package com.bbcow.platform;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.db.MongoPool;
import com.bbcow.server.po.ShareHost;
import com.bbcow.server.util.RequestParam;

public class MessageTask implements Runnable {
        private String message;
        private String path;

        public MessageTask(String message, String path) {
                this.message = message;
                this.path = path;
        }

        @Override
        public void run() {
                JSONObject object = JSONObject.parseObject(message);
                int mode = object.getIntValue("mode");
                if (mode == 1) {

                        if (!HostCache.queue.offer(message)) {
                                HostCache.queue.poll();
                                HostCache.queue.offer(message);
                        }
                        for (Iterator<Session> it = HostCache.userMap.values().iterator(); it.hasNext();) {
                                Session session = it.next();

                                try {

                                        session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_PUSH, this.message));
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                        return;
                }
                if (mode == 2) {
                        ShareHost host = MongoPool.findOneHost(this.path);
                        object = object.getJSONObject("paperTrend");
                        object.put("path", this.path);
                        object.put("hostName", host.getName());
                        MongoPool.insertPaperTrend(object);
                        return;
                }

                /* */
        }

}
