# Spring Boot WebSocket And Android Client

Spring Boot WebSocket 服务器端，附带了浏览器客户端和安卓客户端，用于演示 WebSocket 的用法。

安卓客户端使用了 [StompProtocolAndroid](https://github.com/NaikSoftware/StompProtocolAndroid)，是 [STOMP](https://en.wikipedia.org/wiki/Streaming_Text_Oriented_Messaging_Protocol) 在安卓上的一个实现。

## 介绍

服务器端目前有三个接收消息的端点：

1. `/broadcast`

broadcast 会转发它接收到的所有消息到 `/broadcast/getResponse` 端点的订阅者。

2. `/group/{groupID}`

这个端点用于动态创建群组。举个例子，一个客户端发送消息给 `/group/1`，所有订阅了端点 `/g/1` 的客户端都会接收到消息。如果要改变接收消息的端点，需要同时改变 [Controller](WebSocketServer/src/main/java/me/xlui/im/web/WebSocketController.java#L29) 和 [WebSocketConfig](WebSocketServer/src/main/java/me/xlui/im/config/WebSocketConfig.java#L24) 的相关代码。

3. `/chat`

`/chat` 是用于点对点通信，也即 QQ 或者 微信 中的私聊。如果 Alice（userID 为 1）想和 Bob（userID 为 2）聊天，她需要发送消息到 `/chat`，并且在请求体中附带相关信息（json 化的 [ChatMessage](WebSocketServer/src/main/java/me/xlui/im/message/ChatMessage.java)）:

```js
// js code
function sendMessage() {
    var message = $('#message').val();
    stompClient.send('/chat', {}, JSON.stringify({
        'userID': 2, 
        'fromUserID': 1, 
        'message': "Hello Bob"})
    );
}
```

`userID` 是必须的，这个属性会被服务端用来判断转发的端点：

```java
simpMessagingTemplate.convertAndSendToUser(String.valueOf(chatMessage.getUserID()), "/msg", response);
```

通过以上的代码，Alice 的消息会被转发到 `/user/2/msg` 的订阅者。如果 Bob 订阅了他自己对应的端点，他将收到消息。

如果 Alice 也想收到发给她的消息，她也应当订阅她自己（本例中 Alice 应该订阅 `/user/1/msg`）：

```js
stompClient.subscribe('/user/' + 1 + '/msg', function (response) {
    showResponse(JSON.parse(response.body).responseMessage);
});
```

这样，当 Bob 给 Alice 发送消息的时候，Alice 会成功收到。

## 当前状态

Android 端目前只能发送消息给 `/broadcast`，并接受相应的回应。下一个 Feature 是对 `/group/{groupID}` 的支持。

浏览器端可以测试全部端点，运行服务器端代码，分别访问 `localhost:8080/broadcast`、`localhost:8080/group/{groupID}`、`localhost:8080/chat` 进行测试。

## 服务器端构建

![spring boot starter](Images/spring-boot-starter.png)

## Broadcast(Browser)

![test in browser](Images/websocket-browser-client.gif)

## Broadcast(Android)

![test in android](Images/websocket-android-client.gif)

## Dynamic Groups

![groups](Images/group.gif)

## Point-to-Point Message

![chat](Images/chat.gif)

## LICENSE

MIT
