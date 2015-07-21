package com.bbcow.platform;

import java.lang.reflect.Field;

import com.bbcow.server.po.Paper;

/**
 * 插入 {1:{""}}
 */
public class FirstFilter {

        public static void filter(String message) {
                message = message.replaceAll("[\\p{Punct}&&[^,:]]|[\\p{Space}]", "");
                System.out.println(message);
                Field[] fs = Paper.class.getDeclaredFields();
                for (Field f : fs) {
                        System.out.println(f.getName());
                }
                /* 
                 JSON
                 
                 message = message.replaceAll("\"\\',", "");
                 String type = message.substring(1, message.indexOf(":"));
                 if ("1".equals(type)) {
                         String data = message.substring(message.indexOf(":") + 2, message.length() - 2);
                         System.out.println(data);
                 }*/
        }

        class UserMessage {

        }

        public static void main(String[] args) {
                FirstFilter.filter("{title1:d:,\"f d,title2:dfd}");
        }
}
