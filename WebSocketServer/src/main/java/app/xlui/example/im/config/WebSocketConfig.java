package app.xlui.example.im.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
// Enable WebSocket's message broker
// 启用 Websocket 的消息代理
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    /**
     * Register STOMP's endpoint
     * We don't need to implement WebSocketHandler, STOMP has do the thins what it need to do.
     *
     * 注册 STOMP 协议的节点（Endpoint），并映射为指定的 URL
     * 我们使用 STOMP，所以不需要再实现 WebSocketHandler。
     * 实现 WebSocketHandler 的目的是接收和处理消息，STOMP 已经为我们做了这些。
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        // Register the endpoint of STOMP, and use SockJS protocol
        // 注册 STOMP 协议的节点，并指定使用 SockJS 协议
        stompEndpointRegistry.addEndpoint("/im").addInterceptors(new HandshakeInterceptor()).withSockJS();
    }

    // 配置使用消息代理
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configure global message broker, client will subscribe these message brokers to receive messages
        // 统一配置消息代理，消息代理即订阅点，客户端通过订阅消息代理点接受消息
        registry.enableSimpleBroker("/b", "/g", "/user");

        // configure point to point message's prefix
        // 配置点对点消息的前缀
        registry.setUserDestinationPrefix("/user");
    }
}
