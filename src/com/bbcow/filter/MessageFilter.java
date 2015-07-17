package com.bbcow.filter;

/**
 * 指令检测链-消息
 */
public class MessageFilter extends AbstractFilter {

        @Override
        public void filter(Message message) {
                String originMessage = message.originMessage;
                String data = originMessage.substring(originMessage.indexOf("data") + 7, originMessage.length() - 2);
                //过滤特殊字符\和"
                message.dealMessage = "{\"" + data.replaceAll("[\\\\\"]", "*").replaceAll("(\\*\\:\\*)", "\":\"").replaceAll("(\\*\\,\\*)", "\",\"").substring(1, data.length() - 1) + "\"}";
        }

}
