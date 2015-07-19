package com.bbcow;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bbcow.db.MongoPool;
import com.bbcow.util.TimerControler;

public class StartListener implements ServletContextListener {

        @Override
        public void contextDestroyed(ServletContextEvent arg0) {
        	
        }

        @Override
        public void contextInitialized(ServletContextEvent arg0) {
        		MongoPool.init();
        		CowCache.init();
                TimerControler.init();
        }

}
