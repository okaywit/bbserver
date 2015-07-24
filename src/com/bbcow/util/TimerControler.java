package com.bbcow.util;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.bbcow.db.MongoPool;

public class TimerControler {
        private static Timer t = new Timer();

        public static void init() {
                t.schedule(new MainTask(), 0, 12 * 60 * 60 * 1000);
                t.schedule(new BaiduTask(), 0, 60 * 60 * 1000);
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
                                BaiduPing.site();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

        }

}
