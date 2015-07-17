package com.bbcow.command;

import javax.websocket.Session;

/**
 * 指令接口
 * 
 * @author 大辉Face
 */
public interface ICommand {
        void process(String message, Session session);
}
