package com.bbcow.command;

import java.util.List;

import javax.websocket.Session;

/**
 * 指令接口
 * 
 * @author 大辉Face
 */
public interface ICommand {
        List<String> process(String message, Session session);
}
