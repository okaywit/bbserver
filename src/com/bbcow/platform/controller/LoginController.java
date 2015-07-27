package com.bbcow.platform.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.ServerConfigurator;
import com.bbcow.db.MongoPool;

/**
 * 访问指定主机
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/login/{code}", configurator = ServerConfigurator.class)
public class LoginController {
        /**
         * 用户连接
         */
        @OnOpen
        public void userConnect(@PathParam(value = "code") String code, Session session) {
                InputStream is = null;
                BufferedReader reader = null;
                try {
                        HttpPost post = new HttpPost("https://api.weibo.com/oauth2/access_token");
                        List<NameValuePair> params = new LinkedList<NameValuePair>();
                        params.add(new BasicNameValuePair("client_id", "4284001649"));
                        params.add(new BasicNameValuePair("client_secret", "a9c723ea405d32eb9adcc525c624e8e0"));
                        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
                        params.add(new BasicNameValuePair("redirect_uri", "http://www.grownbook.com/login.html"));
                        params.add(new BasicNameValuePair("code", code));
                        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);

                        post.setEntity(entity);

                        HttpClient client = new DefaultHttpClient();
                        HttpResponse response1 = client.execute(post);

                        HttpEntity backEntity1 = response1.getEntity();
                        is = backEntity1.getContent();
                        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                        String returnText = reader.readLine();
                        /*while ((returnText = reader.readLine()) != null) {
                                System.out.println(returnText);
                        }*/

                        try {
                                is.close();
                                reader.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }

                        String token = JSONObject.parseObject(returnText).getString("access_token");

                        //HttpGet get = new HttpGet("https://api.weibo.com/2/statuses/public_timeline.json?access_token=" + token);
                        HttpGet get = new HttpGet("https://api.weibo.com/2/account/get_uid.json?access_token=" + token);
                        HttpResponse response2 = client.execute(get);
                        HttpEntity backEntity2 = response2.getEntity();
                        is = backEntity2.getContent();
                        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String returnJson = "";

                        while ((returnJson = reader.readLine()) != null) {
                                session.getBasicRemote().sendText(returnJson);
                                MongoPool.insertUser(JSONObject.parseObject(returnJson).getString("uid"), token);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                        try {
                                is.close();
                                reader.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        /**
         * 用户发送消息
         */
        @OnMessage
        public void userMessage(@PathParam(value = "code") String code, String message, Session userSession) {

        }

}
