package me.xlui.im.web;

import me.xlui.im.message.ChatMessage;
import me.xlui.im.message.Message;
import me.xlui.im.message.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	// 当客户端向服务器发送请求时，通过 `@MessageMapping` 映射 /welcome 这个地址
	@MessageMapping("/welcome")
// 当服务器有消息时，会对订阅了 @SendTo 中的路径的客户端发送消息
	@SendTo("/topic/getResponse")
	public Response say(Message message) throws Exception {
		return new Response("Welcome, " + message.getName() + "!");
	}

	@MessageMapping("/group/{groupID}")
	public void say(@DestinationVariable int groupID, Message message) throws Exception {
		Response response = new Response("Welcome to group " + groupID + ", " + message.getName() + "!");
		simpMessagingTemplate.convertAndSend("/topic/group/" + groupID, response);
	}

	@MessageMapping("/chat")
	public void chat(ChatMessage chatMessage) {
		Response response = new Response("Receive message from user " + chatMessage.getFromUserID() + ": " + chatMessage.getMessage());
		simpMessagingTemplate.convertAndSendToUser(String.valueOf(chatMessage.getUserID()), "/msg", response);
	}
}
