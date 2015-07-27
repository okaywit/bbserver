package com.bbcow.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TuLing {
        /**
         * @return 返回结果
         */
        public static String request(String keyword) {
                StringBuffer sb = new StringBuffer();
                String APIKEY = "c03c292775150bbb2399f0743f5f6098";
                String INFO;
                try {
                        INFO = URLEncoder.encode(keyword, "utf-8");
                        String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO;
                        URL getUrl = new URL(getURL);
                        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
                        connection.connect();
                        // 取得输入流，并使用Reader读取 
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

                        String line = "";
                        while ((line = reader.readLine()) != null) {
                                sb.append(line);
                        }
                        reader.close();
                        // 断开连接 
                        connection.disconnect();
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return sb.toString();
        }

        public static void main(String[] args) {
                System.out.println(TuLing.request("新闻"));
        }

}
