package com.bbcow.util;

import java.util.Timer;
import java.util.TimerTask;

import com.bbcow.db.MongoPool;

public class TimerControler {
        private static Timer t = new Timer();

        public static void init() {
                t.schedule(new MainTask(), 0, 24 * 60 * 60 * 1000);
                //t.schedule(new NewsTask(), 0, 60 * 60 * 1000);
        }

        static class MainTask extends TimerTask {

                @Override
                public void run() {
                        MongoPool.insertDailyMain(HtmlParser.getWikiMain());
                }

        }

        static class NewsTask extends TimerTask {

                @Override
                public void run() {
                        MongoPool.insertGoogleNews(HtmlParser.getNews());
                }

        }

}
