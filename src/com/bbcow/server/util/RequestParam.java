package com.bbcow.server.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Json解析
 * 
 * @author 大辉Face
 */
public class RequestParam {
        public final static int MESSAGE_TYPE_ERROR = 0; //错误信息类型
        public final static int MESSAGE_TYPE_SUCCESS = 100; //错误信息类型
        public final static int MESSAGE_TYPE_AD = 1;//推广类型
        public final static int MESSAGE_TYPE_CHAT = 2;//聊天类型
        public final static int MESSAGE_TYPE_PUSH = 3;//推送类型
        public final static int MESSAGE_TYPE_YESTERDAY = 4;//昨天
        public final static int MESSAGE_TYPE_TOP100 = 5;//前百
        public final static int MESSAGE_TYPE_DAILYMAIN = 6;//每日
        public final static int MESSAGE_TYPE_SHAREHOST = 7;//分享
        public final static int MESSAGE_TYPE_LIKETREND = 8;//喜爱日志

        public static void toParam(String message) {
                JSONObject object = JSONObject.parseObject(message);
        }

        /**
         * 返回Json
         * 格式：{"type":"","data":"{}"}
         */
        public static String returnJson(int type, String message) {
                return "{\"type\":" + type + ",\"data\":" + message + "}";
        }

        public static String returnJson(int type, String sId, String message) {
                return "{\"type\":" + type + ",\"sId\":" + sId + ",\"data\":" + message + "}";
        }
}
