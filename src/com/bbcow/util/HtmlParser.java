package com.bbcow.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
        final static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        //private static final String PATH = "D://html/bbhtml/";

        private static final String PATH = "/usr/share/nginx/html/";

        public static void staticWiki() {
                StringBuffer allcon = new StringBuffer();
                InputStream is = null;
                BufferedReader br = null;
                OutputStream os = null;
                BufferedWriter bw = null;

                HttpClient client = new DefaultHttpClient();
                try {
                        HttpResponse response = client.execute(new HttpGet("http://localhost/template/wiki_template.html"));
                        HttpEntity entity = response.getEntity();

                        is = entity.getContent();
                        br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                        String con = "";
                        while ((con = br.readLine()) != null) {
                                allcon.append(con + "\n");
                        }

                        StringBuffer content = new StringBuffer();
                        //爬取有效主机
                        for (String s : MongoPool.findAllMain()) {
                                JSONObject o = JSONObject.parseObject(s);
                                content
                                        .append("<div style=\"height:300px\" class=\"thumbnail col-lg-3 col-md-3 col-xs-12 col-sm-12\"><a href=\"")
                                        .append(o.getString("linkUrl"))
                                        .append("\" target=\"_blank\" role=\"button\"><img src=\"")
                                        .append(o.getString("imgUrl"))
                                        .append("\" alt=\"")
                                        .append(o.getString("title"))
                                        .append("\"><div class=\"caption\"><h1>")
                                        .append(o.getString("title"))
                                        .append("</h1></div></a></div>");
                        }

                        allcon.insert(allcon.indexOf("#1") + 10, content.toString());

                        File f = new File(PATH + "wiki.html");

                        os = new FileOutputStream(f);
                        bw = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
                        bw.write(allcon.toString());

                        //通知抓取
                        BaiduPing.site();

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

        /**
         * 静态首页生成
         * 1.发表信息 2.投票
         */
        public static void staticIndex() {

                StringBuffer allcon = new StringBuffer();
                InputStream is = null;
                BufferedReader br = null;
                OutputStream os = null;
                BufferedWriter bw = null;

                HttpClient client = new DefaultHttpClient();
                try {
                        HttpResponse response = client.execute(new HttpGet("http://localhost/template/index_template.html"));
                        HttpEntity entity = response.getEntity();

                        is = entity.getContent();
                        br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                        String con = "";
                        while ((con = br.readLine()) != null) {
                                allcon.append(con + "\n");
                        }

                        StringBuffer content = new StringBuffer();
                        StringBuffer hosts = new StringBuffer();
                        StringBuffer pages = new StringBuffer();
                        //爬取有效主机
                        for (String s : MongoPool.findHost()) {
                                JSONObject o = JSONObject.parseObject(s);
                                hosts
                                        .append("<li><a href=\"/host.html?id=")
                                        .append(o.getString("path"))
                                        .append("\" class=\"btn btn-primary btn-sm col-md-12\">")
                                        .append(o.getString("name"))
                                        .append("<span class=\"navbar-new\">")
                                        .append(o.getString("clickCount"))
                                        .append("</span></a></li>");
                        }
                        //爬取推荐
                        for (String s : MongoPool.findPaperTrend()) {
                                JSONObject o = JSONObject.parseObject(s);
                                content
                                        .append("<li class=\"list-group-item\">")
                                        .append("<span class=\"navbar-new\">")
                                        .append(RequestParam.isNull(o.getString("total")) ? o.getJSONObject("goodCount").getString("$numberLong") : o.getString("total"))
                                        .append("</span>")
                                        .append("<dd><div class=\"media\"><div class=\"media-left\"><img src=\"")
                                        .append(RequestParam.isNull(o.getString("imgUrl")) ? "img/bbcow.png" : o.getString("imgUrl"))
                                        .append("\" width=\"64px\" height=\"64px\"></div><div class=\"media-body\">")
                                        .append(RequestParam.isNull(o.getString("linkUrl")) ? "" : "<a class=\"text-info\" href=\"/middle.html?url=" + o.getString("linkUrl") + "\">")
                                        .append("<p class=\"media-heading\">")
                                        .append(o.getString("content"))
                                        .append("</p>")
                                        .append(RequestParam.isNull(o.getString("linkUrl")) ? "" : "</a>")
                                        .append("<a class=\"text-muted\" href=\"/host.html?id=")
                                        .append(RequestParam.isNull(o.getString("path")) ? "bb" : o.getString("path"))
                                        .append("\">《")
                                        .append(o.getString("title"))
                                        .append("》  分享自：  <cite>")
                                        .append(RequestParam.isNull(o.getString("hostName")) ? "八牛" : o.getString("hostName"))
                                        .append("</cite></a></div></div></dd></li>");
                        }
                        //爬取静态文件
                        File pf = new File(PATH + "page/");
                        String[] pfs = pf.list(new FilenameFilter() {

                                @Override
                                public boolean accept(File dir, String name) {
                                        return !name.startsWith("template");
                                }
                        });
                        List<String> list = Arrays.asList(pfs);
                        Collections.sort(list,new Comparator<String>() {
							@Override
							public int compare(String o1, String o2) {
                        		try {
                        			
									Date d1 = HtmlParser.sf.parse(o1.substring(0,o1.lastIndexOf(".")));
									Date d2 = HtmlParser.sf.parse(o2.substring(0,o2.lastIndexOf(".")));
									if(d1.after(d2)){
										return -1;
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
								return 0;
							}
                        	
						});
                        for (String s : list) {
                                pages.append("<li><a href=\"/page/").append(s).append("\" class=\"\">").append(s).append("</a></li>");
                        }

                        allcon.insert(allcon.indexOf("#1") + 10, content.toString());
                        allcon.insert(allcon.indexOf("#2") + 10, hosts.toString());
                        allcon.insert(allcon.indexOf("#3") + 10, pages.toString());

                        File f = new File(PATH + "index.html");

                        os = new FileOutputStream(f);
                        bw = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
                        bw.write(allcon.toString());

                        //通知抓取
                        BaiduPing.site();

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

        /**
         * 静态页面生成
         */
        public static void staticHtml(String date) {

                StringBuffer allcon = new StringBuffer();
                InputStream is = null;
                BufferedReader br = null;
                OutputStream os = null;
                BufferedWriter bw = null;

                HttpClient client = new DefaultHttpClient();
                try {
                        HttpResponse response = client.execute(new HttpGet("http://localhost/template/page_template.html"));
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();
                        br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                        String con = "";
                        while ((con = br.readLine()) != null) {
                                allcon.append(con + "\n");
                        }

                        List<String> ss = MongoPool.findDailyTOP5();
                        String str = allcon.toString();
                        if(ss!=null && ss.size()>0){
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
	                        str = str.replaceFirst("\\$title", "八牛头条  - "+date);
	
	                        File f = new File(PATH + "page/" + date + ".html");
	                        os = new FileOutputStream(f);
	                        bw = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
	                        bw.write(str);
	                        log.error("Create static html " + date + " has finished!");
                        }else{
                        	log.error("Didn't find news " + date);
                        }
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
        public static void main(String[] args) {
        	String[] pfs = {"2015-08-10.html","2015-07-10.html","2015-09-10.html"};
        	List<String> list = Arrays.asList(pfs);
        	Collections.sort(list,new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
            		try {
						Date d1 = sf.parse(o1.substring(0,o1.lastIndexOf(".")));
						Date d2 = sf.parse(o2.substring(0,o2.lastIndexOf(".")));
						
						if(d1.after(d2)){
							return 1;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return 0;
				}
            	
			});
            for (String s : list) {
            	System.out.println(s);
            }
		}

}
