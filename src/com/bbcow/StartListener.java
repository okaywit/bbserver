package com.bbcow;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bbcow.util.TimerControler;

public class StartListener implements ServletContextListener {

        @Override
        public void contextDestroyed(ServletContextEvent arg0) {

        }

        @Override
        public void contextInitialized(ServletContextEvent arg0) {
                CowCache.init();
                TimerControler.init();
        }

}
