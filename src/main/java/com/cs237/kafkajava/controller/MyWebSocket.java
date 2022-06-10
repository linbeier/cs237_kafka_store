package com.cs237.kafkajava.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@ServerEndpoint(value = "/websocket") //接受websocket请求路径
@Component  //注册到spring容器中
public class MyWebSocket {


    // Keep all active sessions
    public static final Map<String, Session> webSocketMap = new LinkedHashMap<>();

    public static final Map<String, HashSet<String>> topicToWebSocketIdMap = new LinkedHashMap<>();

    //记录当前在线数目
    private static int count=0;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    //处理连接建立
    @OnOpen
    public void onOpen(Session session){
        webSocketMap.put(session.getId(), session);
        addCount();
        log.info("New connection on：{}", session.getId());
    }

    //接受消息
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("Message from client{}：{}", session.getId(), message);
        // parse message
        HashMap<String, String> messageMap = new ObjectMapper().readValue(message, HashMap.class);
        String messageType = messageMap.get("type");
        if (messageType.equals("QueryRequest")) {
            // Remove previous subscription
            for (String key : topicToWebSocketIdMap.keySet()) {
                topicToWebSocketIdMap.get(key).remove(session.getId());
            }
            // add new subscription
            if (!topicToWebSocketIdMap.containsKey(message)) {
                topicToWebSocketIdMap.put(message, new HashSet());
            } else {
                topicToWebSocketIdMap.get(message).add(session.getId());
            }
            sendMessage(makeTypedMessage("ProductUpdateEvent", "Empty Record"), session);
        }
    }

    //处理错误
    @OnError
    public void onError(Throwable error,Session session){
        log.info("Error occurred {},{}", session.getId(), error.getMessage());
    }

    //处理连接关闭
    @OnClose
    public void onClose(Session session){
        for (String key : topicToWebSocketIdMap.keySet()) {
            topicToWebSocketIdMap.get(key).remove(session.getId());
        }
        webSocketMap.remove(session.getId());
        reduceCount();
        log.info("Connection closed:{}", session.getId());
    }

    //群发消息

    //发送消息
    public static void sendMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    private String makeTypedMessage(String type, String content) {
        SortedMap<String, String> elements = new TreeMap<>();
        elements.put("type", type);
        elements.put("productUpdateRecord", content);
        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        return gson.toJson(elements,gsonType);
    }

    //广播消息
//    public static void broadcast(){
//        MyWebSocket.webSocketMap.forEach((k,v)->{
//            try{
//                v.sendMessage("这是一条测试广播");
//            }catch (Exception e){
//            }
//        });
//    }

    //获取在线连接数目
    public static int getCount(){
        return count;
    }

    //操作count，使用synchronized确保线程安全
    public static synchronized void addCount(){
        MyWebSocket.count++;
    }

    public static synchronized void reduceCount(){
        MyWebSocket.count--;
    }
}
