package com.bbcow.platform;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class BaiduPing {
	
		public static void site() throws IOException{
			
				// 定义HttpClient
				HttpClient client = new DefaultHttpClient();
	
				// 实例化HTTP方法
				HttpPost request = new HttpPost("http://data.zz.baidu.com/urls?site=www.grownbook.com&token=hqjEuHTIHexJiPFt");
	
				// 创建UrlEncodedFormEntity对象
				StringEntity formEntiry = new StringEntity("http://www.grownbook.com/index.html \n http://www.grownbook.com/share.html");
				request.setEntity(formEntiry);
	
				// 执行请求
				client.execute(request);
	
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
