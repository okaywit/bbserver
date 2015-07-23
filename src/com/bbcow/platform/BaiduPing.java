package com.bbcow.platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class BaiduPing {
        public static void site() {
                //TODO
                URL url;
                StringBuffer sbf = new StringBuffer();
                try {
                        url = new URL("http://data.zz.baidu.com/urls?site=www.grownbook.com&token=hqjEuHTIHexJiPFt");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "text/plain");
                        connection.setRequestProperty("User-Agent", "curl/7.12.1");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);
                        connection.connect();

                        OutputStream outStrm = connection.getOutputStream();
                        ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);
                        objOutputStrm.writeChars(new String("http://www.grownbook.com/share.html"));
                        objOutputStrm.writeChars(new String("http://www.grownbook.com/index.html"));
                        objOutputStrm.flush();

                        objOutputStrm.close();

                        InputStream inStrm = connection.getInputStream();

                        System.out.println(sbf.toString());
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public static void main(String[] args) {
                BaiduPing.site();
        }

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
                                .append("<value><string>http://www.grownbook.com/</string></value>")
                                .append("</param>")
                                .append("<param>")
                                .append("<value><string>http://www.grownbook.com/</string></value>")
                                .append("</param>")
                                .append("<param>")
                                .append("<value><string>http://www.grownbook.com/rss</string></value>")
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
