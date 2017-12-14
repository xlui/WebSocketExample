package me.xlui.im.web;

import me.xlui.im.message.Message;
import me.xlui.im.message.Response;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    // 当客户端向服务器发送请求时，通过 `@MessageMapping` 映射 /welcome 这个地址
    @MessageMapping("/welcome")
    // 当服务器有消息时，会对订阅了 @SendTo 中的路径的客户端发送消息
    @SendTo("/topic/getResponse")
    public Response say(Message message) throws Exception {
        return new Response("Welcome, " + message.getName() + "!");
    }
}
