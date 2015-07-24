package com.bbcow.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.BusCache;
import com.bbcow.server.po.DailyMain;
import com.bbcow.server.po.Paper;
import com.bbcow.server.po.ShareHost;
import com.bbcow.util.RequestParam;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

/**
 * mogodb
 * 
 * @author 大辉Face
 */
public class MongoPool {
        private static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
        private static MongoClient mongoClient;
        private static MongoDatabase db;

        public static void init() {
                mongoClient = new MongoClient("127.0.0.1", 27017);
                db = mongoClient.getDatabase("okaywit");
        }

        /**
         * 获取热门信息+当天信息
         */
        public static List<String> findIndex() {
                FindIterable<Document> top = db.getCollection("paper").find().sort(BsonDocument.parse("{goodCount:-1}")).limit(2);

                FindIterable<Document> current = db.getCollection("paper").find(BsonDocument.parse("{createDate:{$gte:ISODate('" + sFormat.format(new Date()) + "T00:00:00.000Z')}}"));

                final List<String> jsons = new LinkedList<String>();

                top.forEach(new Block<Document>() {
                        @Override
                        public void apply(final Document document) {

                                jsons.add(document.toJson());
                        }
                });

                current.forEach(new Block<Document>() {

                        @Override
                        public void apply(final Document document) {
                                jsons.add(document.toJson());
                        }
                });

                return jsons;
        }

        /**
         * 获取分享主机
         */
        public static List<String> findHost() {
                FindIterable<Document> top = db.getCollection("share_host").find();
                final List<String> jsons = new LinkedList<String>();

                top.forEach(new Block<Document>() {
                        @Override
                        public void apply(final Document document) {
                                jsons.add("{\"name\":\""
                                        + document.getString("name")
                                        + "\",\"id\":\""
                                        + document.getObjectId("_id")
                                        + "\",\"status\":\""
                                        + document.getString("status")
                                        + "\",\"path\":\""
                                        + document.getString("path")
                                        + "\",\"clickCount\":\""
                                        + (document.getInteger("clickCount") == null ? 0 : document.getInteger("clickCount"))
                                        + "\"}");
                        }
                });

                return jsons;
        }

        public static List<ShareHost> findBBHost() {
                FindIterable<Document> top = db.getCollection("share_host").find();//BsonDocument.parse("{\"type\":\"0\"}")

                final List<ShareHost> hosts = new LinkedList<ShareHost>();

                top.forEach(new Block<Document>() {
                        @Override
                        public void apply(final Document document) {
                                ShareHost host = new ShareHost();
                                host.setIp(document.getString("ip"));
                                host.setEmail(document.getString("email"));
                                host.setName(document.getString("name"));
                                host.setPoint(document.getString("point"));
                                host.setPort(document.getString("port"));
                                host.setPath(document.getString("path"));
                                host.setType(Integer.parseInt(document.getString("type")));
                                host.setStatus(Integer.parseInt(document.getString("status")));

                                hosts.add(host);
                        }
                });

                return hosts;
        }

        public static void insertHostTrend(ShareHost host) {
                db.getCollection("share_host").updateOne(BsonDocument.parse("{\"path\":\"" + host.getPath() + "\"}"), BsonDocument.parse("{$inc:{clickCount:1}}"));
        }

        public static ShareHost findOneHost(String path) {
                FindIterable<Document> top = db.getCollection("share_host").find(BsonDocument.parse("{\"path\":\"" + path + "\"}"));
                Document document = top.first();
                if (document != null) {
                        ShareHost host = new ShareHost();
                        host.setIp(document.getString("ip"));
                        host.setEmail(document.getString("email"));
                        host.setName(document.getString("name"));
                        host.setPoint(document.getString("point"));
                        host.setPort(document.getString("port"));
                        host.setPath(document.getString("path"));
                        host.setType(Integer.parseInt(document.getString("type")));
                        host.setStatus(Integer.parseInt(document.getString("status")));
                        return host;
                } else {
                        return null;
                }
        }

        /**
         * 获取当天头条
         */
        public static String findDailyFirst() {
                FindIterable<Document> current =
                        db
                                .getCollection("paper")
                                .find(BsonDocument.parse("{createDate:{$gte:ISODate('" + sFormat.format(new Date()) + "T00:00:00.000Z')}}"))
                                .sort(BsonDocument.parse("{goodCount:-1}"))
                                .limit(1);
                if (current.first() != null) {
                        return current.first().toJson();
                } else {
                        return null;
                }
        }

        /**
         * 获取前100
         */
        public static List<String> findTop100() {
                FindIterable<Document> top = db.getCollection("paper").find().sort(BsonDocument.parse("{goodCount:-1}")).limit(100);
                final List<String> jsons = new LinkedList<String>();

                top.forEach(new Block<Document>() {
                        @Override
                        public void apply(final Document document) {
                                jsons.add(RequestParam.returnJson(BusCache.MESSAGE_TYPE_TOP100, document.toJson()));
                        }
                });
                return jsons;
        }

        /**
         * 获取昨日
         */
        public static List<String> findYesterday() {
                Date yesterday = new Date();
                yesterday.setDate(yesterday.getDate() - 1);

                FindIterable<Document> top =
                        db
                                .getCollection("paper")
                                .find(
                                        BsonDocument.parse("{$and : [{createDate:{$gte:ISODate('"
                                                + sFormat.format(yesterday)
                                                + "T00:00:00.000Z')}},{createDate:{$lt:ISODate('"
                                                + sFormat.format(new Date())
                                                + "T00:00:00.000Z')}}] }"))
                                .limit(100);
                final List<String> jsons = new LinkedList<String>();

                top.forEach(new Block<Document>() {
                        @Override
                        public void apply(final Document document) {
                                jsons.add(RequestParam.returnJson(BusCache.MESSAGE_TYPE_YESTERDAY, document.toJson()));
                        }
                });
                return jsons;
        }

        /**
         * 根据主键
         */
        public static String findOne(long paperId) {
                FindIterable<Document> iterable = db.getCollection("paper").find(BsonDocument.parse("{id:" + paperId + "}"));
                Document document = iterable.first();

                return document.toJson();
        }

        public static void insertHost(ShareHost host) {
                db.getCollection("share_host").insertOne(
                        new Document("ip", host.getIp())
                                .append("port", host.getPort())
                                .append("point", host.getPoint())
                                .append("email", host.getEmail())
                                .append("name", host.getName())
                                .append("path", host.getPath())
                                .append("type", "0")
                                .append("status", "0"));
        }

        public static void insertPaper(Paper paper) {
                db.getCollection("paper").insertOne(
                        new Document("id", paper.getId())
                                .append("title", paper.getTitle())
                                .append("content", paper.getContent())
                                .append("contactName", paper.getContactName())
                                .append("contactTel", paper.getContactTel())
                                .append("tag", paper.getTag())
                                .append("imgUrl", paper.getImgUrl())
                                .append("linkUrl", paper.getLinkUrl())
                                .append("createDate", new Date())
                                .append("goodCount", paper.getGoodCount())
                                .append("badCount", paper.getBadCount()));
        }

        /**
         * 每日重点
         */
        public static void insertDailyMain(DailyMain main) {
                FindIterable<Document> iterable = db.getCollection("daily_main").find(BsonDocument.parse("{title:\"" + main.getTitle() + "\"}"));
                if (iterable.first() == null)
                        db.getCollection("daily_main").insertOne(
                                new Document("title", main.getTitle()).append("linkUrl", main.getLinkUrl()).append("imgUrl", main.getImgUrl()).append("createDate", new Date()));
        }

        public static String findMain() {
                FindIterable<Document> iterable = db.getCollection("daily_main").find().sort(BsonDocument.parse("{createDate:-1}"));
                Document document = iterable.first();
                return document.toJson();
        }

        public static String findGoogleNews() {
                FindIterable<Document> iterable = db.getCollection("google_news").find().sort(BsonDocument.parse("{createDate:-1}"));
                Document document = iterable.first();
                return document.getString("content");
        }

        public static void insertGoogleNews(String news) {
                db.getCollection("google_news").insertOne(new Document("content", news).append("createDate", new Date()));

        }

        public static void insertPaperTrend(JSONObject object) {
                db.getCollection("paper_trend").insertOne(
                        new Document("paper_id", object.getLongValue("id"))
                                .append("type", object.getIntValue("type"))
                                .append("ip", "")
                                .append("title", object.getString("title"))
                                .append("content", object.getString("content"))
                                .append("linkUrl", object.getString("linkUrl"))
                                .append("imgUrl", object.getString("imgUrl"))
                                .append("path", object.getString("path"))
                                .append("hostName", object.getString("hostName"))
                                .append("createDate", new Date()));

        }

        public static List<String> findPaperTrend() {
                List<BsonDocument> bs = new ArrayList<BsonDocument>();
                bs.add(BsonDocument.parse("{$match:{type : 1}}"));
                bs
                        .add(BsonDocument
                                .parse("{$group:{_id : \"$paper_id\",title:{$first:\"$title\"},content:{$first:\"$content\"},linkUrl:{$first:\"$linkUrl\"},imgUrl:{$first:\"$imgUrl\"},path:{$first:\"$path\"},hostName:{$first:\"$hostName\"},total: {$sum: 1} }}"));

                bs.add(BsonDocument.parse("{$sort:{total:-1}}"));
                bs.add(BsonDocument.parse("{$limit:20}"));
                AggregateIterable<Document> iterable = db.getCollection("paper_trend").aggregate(bs);
                final List<String> jsons = new LinkedList<String>();
                iterable.forEach(new Block<Document>() {
                        @Override
                        public void apply(final Document document) {
                                jsons.add(document.toJson());
                        }
                });
                return jsons;
        }

        public static void doNotLike(long id) {
                db.getCollection("paper").updateOne(BsonDocument.parse("{id:" + id + "}"), BsonDocument.parse("{$inc:{badCount:1}}"));

                FindIterable<Document> iterable = db.getCollection("paper").find(BsonDocument.parse("{id:" + id + ",badCount:{$gt:10}}"));
                Document document = iterable.first();
                if (document != null) {
                        long gc = document.getLong("goodCount");
                        long bc = document.getLong("badCount");
                        if (gc < bc) {
                                db.getCollection("paper_rubbish").insertOne(document.append("inDate", new Date()));
                                db.getCollection("paper").deleteOne(BsonDocument.parse("{id:" + id + "}"));
                        }
                }
        }

        public static void doLike(long id) {
                db.getCollection("paper").updateOne(BsonDocument.parse("{id:" + id + "}"), BsonDocument.parse("{$inc:{goodCount:1}}"));
        }
}
