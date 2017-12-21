package me.xlui.im.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	/**
	 * 注册 STOMP 协议的节点（Endpoint），并映射为指定的 URL
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
		// 注册 STOMP 协议的节点，并指定使用 SockJS 协议
		stompEndpointRegistry.addEndpoint("/hello").withSockJS();
	}

	/**
	 * 配置消息代理
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 广播式应配置一个 /topic 消息代理
		// 配置 /user 消息代理
		registry.enableSimpleBroker("/topic", "/user");

		// 配置点对点消息的前缀
		registry.setUserDestinationPrefix("/user");
	}
}
