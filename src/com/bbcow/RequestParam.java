package com.bbcow;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.Session;

import org.apache.log4j.Logger;

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
