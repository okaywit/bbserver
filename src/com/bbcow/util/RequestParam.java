package com.bbcow.util;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bbcow.BusCache;
import com.bbcow.platform.PlatformCache;

/**
 * Json解析
 * 
 * @author 大辉Face
 */
public class RequestParam {
        private static Logger log = Logger.getLogger(RequestParam.class);

        /**
         * 返回Json
         * 格式：{"type":"","data":"{}"}
         */
        public static String returnJson(int type, String message) {
                return "{\"type\":" + type + ",\"data\":" + message + "}";
        }

        /**
         * 聊天
         */
        public static void sendChat(String message) {
                try {
                        for (Iterator<Session> it = PlatformCache.userMap.values().iterator(); it.hasNext();) {
                                it.next().getBasicRemote().sendText(returnJson(BusCache.MESSAGE_TYPE_CHAT, message));
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public static void sendSmartChat(String message) {
                JSONObject ro = JSONObject.parseObject(message);
                int code = ro.getIntValue("code");

                JSONArray ros = ro.getJSONArray("list");

                if (ros != null) {
                        StringBuffer rs = new StringBuffer();
                        if (code == 308000) {
                                for (int i = 0; i < ros.size(); i++) {
                                        JSONObject rod = JSONObject.parseObject(ros.get(i).toString());
                                        rs.append("<a href='"
                                                + rod.getString("detailurl")
                                                + "'><img width='64px' height='64px' src='"
                                                + rod.getString("icon")
                                                + "' alt='baidu'>"
                                                + rod.getString("name")
                                                + "</a>");
                                }
                                sendChat("{\"fakeName\":\"八牛大厨\",\"msg\":\"" + rs.toString() + "\"}");
                        } else if (code == 302000) {
                                for (int i = 0; i < ros.size(); i++) {
                                        JSONObject rod = JSONObject.parseObject(ros.get(i).toString());
                                        rs.append("<p class='small'><a href='" + rod.getString("detailurl") + "'>" + rod.getString("article").replaceAll("\"", "”") + "</a></p>");
                                }
                                sendChat("{\"fakeName\":\"八牛号外\",\"msg\":\"" + rs.toString() + "\"}");
                        } else if (code == 305000) {
                                for (int i = 0; i < ros.size(); i++) {
                                        JSONObject rod = JSONObject.parseObject(ros.get(i).toString());
                                        rs
                                                .append("<p class='small'>")
                                                .append(rod.getString("trainnum"))
                                                .append(rod.getString("start"))
                                                .append(rod.getString("starttime"))
                                                .append(rod.getString("terminal"))
                                                .append(rod.getString("endtime"))
                                                .append("</p>");
                                }
                                sendChat("{\"fakeName\":\"八牛火车\",\"msg\":\"" + rs.toString() + "\"}");
                        } else if (code == 306000) {
                                for (int i = 0; i < ros.size(); i++) {
                                        JSONObject rod = JSONObject.parseObject(ros.get(i).toString());
                                        rs.append("<p class='small'>").append(rod.getString("flight")).append(rod.getString("starttime")).append("</p>");
                                }
                                sendChat("{\"fakeName\":\"八牛飞机\",\"msg\":\"" + rs.toString() + "\"}");
                        }

                } else {
                        if (code == 200000) {
                                sendChat("{\"fakeName\":\"八牛图片\",\"msg\":\"图片网址：" + ro.getString("url") + "\"}");
                        } else {
                                sendChat("{\"fakeName\":\"八牛大王\",\"msg\":\"" + ro.getString("text") + "\"}");
                        }
                }
        }

        /**
         * 推送文本消息
         * 格式：{"type":"","data":"{}"}
         */
        public static void sendText(int type, String message, Session session) {
                try {
                        session.getBasicRemote().sendText(returnJson(type, message));
                } catch (IOException e) {
                        e.printStackTrace();
                        log.error("Send message error : " + message);
                }
        }

        /**
         * 推送成功消息
         * 格式：{"type":"100","message":""}
         */
        public static void sendSuccess(String message, Session session) {
                try {
                        session.getBasicRemote().sendText("{\"type\":" + BusCache.MESSAGE_TYPE_SUCCESS + ",\"message\":\"" + message + "\"}");
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        /**
         * 推送失败消息
         * 格式：{"type":"0","message":""}
         */
        public static void sendError(String message, Session session) {
                try {
                        session.getBasicRemote().sendText("{\"type\":" + BusCache.MESSAGE_TYPE_ERROR + ",\"message\":\"" + message + "\"}");
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
