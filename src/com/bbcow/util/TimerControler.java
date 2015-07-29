package com.bbcow.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bson.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bbcow.BusCache;
import com.bbcow.db.MongoPool;

public class TimerControler {
        private static Timer t = new Timer();

        public static void init() {
                t.schedule(new MainTask(), 0, 12 * 60 * 60 * 1000);
                t.schedule(new BaiduTask(), 0, 6 * 60 * 60 * 1000);
                t.schedule(new WeiboTask(), 0, 1 * 60 * 60 * 1000);
        }

        static class MainTask extends TimerTask {

                @Override
                public void run() {
                        MongoPool.insertDailyMain(HtmlParser.getWikiMain());
                }

        }

        static class BaiduTask extends TimerTask {

                @Override
                public void run() {
                        try {
                                String date = BusCache.sFormat.format(new Date());
                                HtmlParser.staticHtml(date);
                                BaiduPing.site(date);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

        }

        /**
         * 每小时扫描授权用户微博
         */
        static class WeiboTask extends TimerTask {
                Pattern p = Pattern.compile("http://t.cn/[\\w]{4,}");
                List<Document> returnPaper = new LinkedList<Document>();
                HttpClient client = new DefaultHttpClient();

                @Override
                public void run() {

                        try {
                                returnPaper.clear();
                                //统计一小时内
                                long time = System.currentTimeMillis() - 1 * 60 * 60 * 1000;
                                List<String> users = MongoPool.findUser();

                                for (String userj : users) {
                                        JSONObject user = JSONObject.parseObject(userj);
                                        String token = user.getString("token");
                                        String uid = user.getString("uid");
                                        HttpGet get = new HttpGet("https://api.weibo.com/2/statuses/user_timeline.json?access_token=" + token + "&uid=" + uid);

                                        HttpResponse response = client.execute(get);
                                        HttpEntity backEntity = response.getEntity();
                                        InputStream is = backEntity.getContent();
                                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                                        String returnJson = reader.readLine();

                                        JSONObject object = JSONObject.parseObject(returnJson);
                                        //获取微博列表
                                        JSONArray ss = object.getJSONArray("statuses");

                                        for (int i = 0; i < ss.size(); i++) {
                                                JSONObject s = (JSONObject) ss.get(i);
                                                Date date = new Date(s.getString("created_at"));//创建时间（转发微博为转发时间）
                                                //是否转发
                                                s = s.getString("retweeted_status") != null ? s.getJSONObject("retweeted_status") : s;

                                                String content = s.getString("text");

                                                if (date.getTime() > time) {
                                                        Matcher matcher = p.matcher(content);
                                                        returnPaper.add(new Document("id", System.currentTimeMillis())
                                                                .append("title", content.substring(0, 20))
                                                                .append("content", content)
                                                                .append("contactName", "新浪微博")
                                                                .append("contactTel", uid)
                                                                .append("tag", "")
                                                                .append("imgUrl", s.getString("original_pic") == null ? "" : s.getString("original_pic"))
                                                                .append("linkUrl", matcher.find() ? matcher.group(0) : "")
                                                                .append("createDate", date)
                                                                .append("goodCount", s.getLongValue("comments_count"))
                                                                .append("badCount", 0));

                                                } else {
                                                        continue;
                                                }

                                        }

                                        try {
                                                is.close();
                                                reader.close();
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }
                                }
                                if (returnPaper.size() > 0) {
                                        MongoPool.batchInsertPaper(returnPaper);
                                        //新文章更新首页
                                        HtmlParser.staticIndex();
                                }
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        public static void main(String[] args) {
                String s = "Mon Jul 27 15:33:04 +0800 2015";

                Date d = new Date(s);
                System.out.println(d.getTime());

        }
}
