package com.bbcow.util;

import org.apache.log4j.Logger;

public class NotFindCommandException extends Exception {
        private static Logger logger = Logger.getLogger(NotFindCommandException.class);
        /**
         * @description 属性说明
         */
        private static final long serialVersionUID = 1L;

        public NotFindCommandException(String message) {
                super(message);
        }

        @Override
        public String getMessage() {
                logger.warn("Command " + super.getMessage() + " not found");
                return super.getMessage() + " not found";
        }

}
