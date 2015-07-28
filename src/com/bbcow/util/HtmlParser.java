package com.bbcow.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.db.MongoPool;
import com.bbcow.server.po.DailyMain;

public class HtmlParser {
        private static Logger log = Logger.getLogger(HtmlParser.class);
/**静态页面生成*/
        public static void staticHtml(String date) {

                StringBuffer allcon = new StringBuffer();
                InputStream is = null;
                BufferedReader br = null;
                OutputStream os = null;
                BufferedWriter bw = null;

                HttpClient client = new DefaultHttpClient();
                try {
                        HttpResponse response = client.execute(new HttpGet("http://localhost/page/template.html"));
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();
                        br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                        String con = "";
                        while ((con = br.readLine()) != null) {
                                allcon.append(con + "\n");
                        }
                        //MongoPool.init();
                        List<String> ss = MongoPool.findDailyTOP5();
                        String str = allcon.toString();

                        str = str.replaceFirst("\\$title", date + " TOP 5");
                        for (String s : ss) {

                                JSONObject js = JSONObject.parseObject(s);
                                js = js.getJSONObject("data");
                                str =
                                        str
                                                .replaceFirst("\\$description", js.getString("title"))
                                                .replaceFirst("hidden", "show")
                                                .replaceFirst("\\$post_title", js.getString("title"))
                                                .replaceFirst("\\$post_imgUrl", "".equals(js.getString("imgUrl")) ? "/img/bbcow.png" : js.getString("imgUrl"))
                                                .replaceFirst("\\$post_content", js.getString("content"))
                                                .replaceFirst("\\$post_linkUrl", "".equals(js.getString("linkUrl").trim()) ? "/" : js.getString("linkUrl"));
                        }

                        File f = new File("/usr/share/nginx/html/page/" + date + ".html");
                        os = new FileOutputStream(f);
                        bw = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
                        bw.write(str);
                        log.error("Create static html " + date + " has finished!");
                } catch (IOException e) {
                        e.printStackTrace();
                        log.error(e);
                } finally {
                        try {
                                bw.close();
                                os.close();
                                is.close();
                                br.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        public static void main(String[] args) {
                HtmlParser.staticHtml("2015-07-28");
        }

        public static String getNews() {
                StringBuffer allcon = new StringBuffer();
                InputStream is = null;
                BufferedReader br = null;

                HttpClient client = new DefaultHttpClient();
                try {
                        HttpResponse response = client.execute(new HttpGet("http://imgur.com/"));
                        HttpEntity entity = response.getEntity();

                        is = entity.getContent();

                        br = new BufferedReader(new InputStreamReader(is, "utf-8"));

                        String con = "";

                        while ((con = br.readLine()) != null) {
                                allcon.append(con);
                                System.out.println(con);
                        }

                } catch (IOException e) {
                        e.printStackTrace();
                } finally {
                        try {
                                is.close();
                                br.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                return allcon.toString();
        }

        public static DailyMain getWikiMain() {
                InputStream is = null;
                BufferedReader br = null;
                DailyMain wm = new DailyMain();

                HttpClient client = new DefaultHttpClient();
                try {
                        HttpGet get = new HttpGet("https://en.m.wikipedia.org/wiki/Main_Page");
                        get.setHeader("Connection", "close");
                        HttpResponse response = client.execute(get);
                        HttpEntity entity = response.getEntity();

                        is = entity.getContent();

                        br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                        br.skip(8721l);

                        char[] cs = new char[1000];
                        br.read(cs);

                        StringBuffer str = new StringBuffer();
                        for (char c : cs) {
                                str.append(c);
                        }

                        str.delete(0, str.indexOf("src") + 5);
                        wm.setImgUrl("https:" + str.substring(0, str.indexOf("\"")));
                        str.delete(0, str.indexOf("href") + 6);
                        wm.setLinkUrl("https://en.m.wikipedia.org" + str.substring(0, str.indexOf("\"")));
                        str.delete(0, str.indexOf("title") + 7);
                        wm.setTitle(str.substring(0, str.indexOf("\"")));
                } catch (IOException e) {
                        e.printStackTrace();
                } finally {
                        try {
                                is.close();
                                br.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                return wm;
        }

}
