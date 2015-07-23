package com.bbcow.platform;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class BaiduPing {
        public static void ping() {
                HttpClient client = new DefaultHttpClient();
                try {
                        HttpPost post = new HttpPost("http://ping.baidu.com/ping/RPC2");
                        post.setHeader("Connection", "close");
                        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        sb
                                .append("<methodCall>")
                                .append("<methodName>weblogUpdates.extendedPing</methodName>")
                                .append("<params>")
                                .append("<param>")
                                .append("<value><string>八牛号外</string></value>")
                                .append("</param>")
                                .append("<param>")
                                .append("<value><string>http://grownbook.com/</string></value>")
                                .append("</param>")
                                .append("<param>")
                                .append("<value><string>http://grownbook.com/</string></value>")
                                .append("</param>")
                                .append("<param>")
                                .append("<value><string>http://grownbook.com/rss</string></value>")
                                .append("</param>")
                                .append("</params>")
                                .append("</methodCall>");

                        HttpEntity entity = new StringEntity(sb.toString());

                        post.setEntity(entity);
                        client.execute(post);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
