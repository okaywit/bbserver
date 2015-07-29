package com.bbcow.platform;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.BusCache;
import com.bbcow.db.MongoPool;
import com.bbcow.server.po.ShareHost;
import com.bbcow.util.HtmlParser;
import com.bbcow.util.RequestParam;

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
                if (mode == BusCache.MESSAGE_MODE_AD) {

                        if (!PlatformCache.queue.offer(message)) {
                                PlatformCache.queue.poll();
                                PlatformCache.queue.offer(message);
                        }
                        for (Iterator<Session> it = PlatformCache.userMap.values().iterator(); it.hasNext();) {
                                Session session = it.next();

                                try {

                                        session.getBasicRemote().sendText(RequestParam.returnJson(BusCache.MESSAGE_TYPE_PUSH, this.message));
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                        //新文章更新首页
                        HtmlParser.staticIndex();
                        return;
                }
                if (mode == BusCache.MESSAGE_MODE_VOTE) {
                        ShareHost host = MongoPool.findOneHost(this.path);
                        object = object.getJSONObject("paperTrend");
                        object.put("path", this.path);
                        object.put("hostName", host.getName());
                        MongoPool.insertPaperTrend(object);
                        //投票更新首页
                        HtmlParser.staticIndex();
                        return;
                }

                /* */
        }

}
