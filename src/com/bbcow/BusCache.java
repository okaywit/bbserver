package com.bbcow;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BusCache {
        public static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");

        public static ExecutorService threads = null;
        //服务器返回
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
        //客户端请求
        public final static int MESSAGE_MODE_AD = 1;//消息
        public final static int MESSAGE_MODE_VOTE = 2;//投票
        public final static int MESSAGE_MODE_CHAT = 3;//聊天
        public final static int MESSAGE_MODE_SEARCH = 5;//查询

        static {
                threads = Executors.newFixedThreadPool(8);
        }

}
