package com.bbcow.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnector {

        /**
         * @param urlAll
         *                :请求接口
         * @param httpArg
         *                :参数
         * @return 返回结果
         */
        public static String request(String httpUrl, String httpArg) {
                BufferedReader reader = null;
                String result = null;
                StringBuffer sbf = new StringBuffer();
                httpUrl = httpUrl + "?" + httpArg;
                System.out.println(httpArg);
                try {
                        URL url = new URL(httpUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        // 填入apikey到HTTP header
                        connection.setRequestProperty("apikey", "7c26d66e853ae1f2aa6ce32d281d1ac8");
                        connection.connect();
                        InputStream is = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String strRead = null;
                        while ((strRead = reader.readLine()) != null) {
                                sbf.append(strRead);
                                sbf.append("\r\n");
                        }
                        reader.close();
                        result = sbf.toString();
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return result;
        }

        public static String request1(String httpUrl, String httpArg) {
                BufferedReader reader = null;
                String result = null;
                StringBuffer sbf = new StringBuffer();
                httpUrl = httpUrl + "?" + httpArg;
                System.out.println(httpArg);
                try {
                        URL url = new URL(httpUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        // 填入apikey到HTTP header
                        connection.setRequestProperty("apikey", "7c26d66e853ae1f2aa6ce32d281d1ac8");
                        connection.connect();
                        InputStream is = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String strRead = null;
                        while ((strRead = reader.readLine()) != null) {
                                sbf.append(strRead);
                                sbf.append("\r\n");
                        }
                        reader.close();
                        result = sbf.toString();
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return result;
        }

}
