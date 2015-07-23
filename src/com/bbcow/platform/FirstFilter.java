package com.bbcow.platform;

import org.apache.log4j.Logger;

import com.bbcow.BusCache;

/**
 * 
 */
public class FirstFilter {
        private static Logger log = Logger.getLogger(FirstFilter.class);

        //[1]==" [2]==: [3]==,
        public static String filter(String message) {
                long t1 = System.currentTimeMillis();
                message = message.replaceAll("\\[1\\]", "@@1@@").replaceAll("\\[2\\]", "@@2@@").replaceAll("\\[3\\]", "@@3@@");

                StringBuffer s = new StringBuffer("{[1]mode[1][2]");
                int mode = Integer.parseInt(message.substring(8, message.indexOf(",")));
                s.append(mode);

                switch (mode) {
                case BusCache.MESSAGE_MODE_AD:
                        s
                                .append("[3][1]paper[1][2]{")
                                .append("[1]title[1][2][1]")
                                .append(message.substring(message.indexOf("\"title\"") + 9, message.indexOf("\"content\"") - 2))
                                .append("[1][3][1]content[1][2][1]")
                                .append(message.substring(message.indexOf("\"content\"") + 11, message.indexOf("\"contactName\"") - 2))
                                .append("[1][3][1]contactName[1][2][1]")
                                .append(message.substring(message.indexOf("\"contactName\"") + 15, message.indexOf("\"contactTel\"") - 2))
                                .append("[1][3][1]contactTel[1][2][1]")
                                .append(message.substring(message.indexOf("\"contactTel\"") + 14, message.indexOf("\"tag\"") - 2))
                                .append("[1][3][1]tag[1][2][1]")
                                .append(message.substring(message.indexOf("\"tag\"") + 7, message.indexOf("\"imgUrl\"") - 2))
                                .append("[1][3][1]imgUrl[1][2][1]")
                                .append(message.substring(message.indexOf("\"imgUrl\"") + 10, message.indexOf("\"linkUrl\"") - 2))
                                .append("[1][3][1]linkUrl[1][2][1]")
                                .append(message.substring(message.indexOf("\"linkUrl\"") + 11, message.indexOf("}}") - 1));

                        s.append("[1]}");
                        break;
                case BusCache.MESSAGE_MODE_VOTE:
                        s
                                .append("[3][1]paperTrend[1][2]{")
                                .append("[1]id[1][2][1]")
                                .append(message.substring(message.indexOf("\"id\"") + 6, message.indexOf("\"type\"") - 2))
                                .append("[1][3][1]type[1][2]")
                                .append(message.substring(message.indexOf("\"type\"") + 8, message.indexOf("\"title\"") - 2))
                                .append("[3][1]title[1][2][1]")
                                .append(message.substring(message.indexOf("\"title\"") + 9, message.indexOf("\"content\"") - 2))
                                .append("[1][3][1]content[1][2][1]")
                                .append(message.substring(message.indexOf("\"content\"") + 11, message.indexOf("\"imgUrl\"") - 2))
                                .append("[1][3][1]imgUrl[1][2][1]")
                                .append(message.substring(message.indexOf("\"imgUrl\"") + 10, message.indexOf("\"linkUrl\"") - 2))
                                .append("[1][3][1]linkUrl[1][2][1]")
                                .append(message.substring(message.indexOf("\"linkUrl\"") + 11, message.indexOf("}}") - 1));

                        s.append("[1]}");
                        break;
                case BusCache.MESSAGE_MODE_SEARCH:
                        s.append("[3][1]conditionType[1][2]").append(message.substring(message.indexOf("\"conditionType\"") + 16, message.indexOf("}")));
                        break;
                default:
                        break;
                }

                s.append("}");
                String filterMessage = s.toString().replaceAll("\"", "”").replaceAll(",", "，").replaceAll(":", "：").replaceAll("\\[1\\]", "\"").replaceAll("\\[2\\]", ":").replaceAll("\\[3\\]", ",");
                filterMessage = filterMessage.replaceAll("@@1@@", "[1]").replaceAll("@@2@@", "[2]").replaceAll("@@3@@", "[3]");
                log.error("Decode message use " + (System.currentTimeMillis() - t1) + " mils");
                return filterMessage;
        }

}
