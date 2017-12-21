# Spring Boot WebSocket And Android Client

Spring Boot WebSocket Server, with a browser client and Android client. Android client uses [StompProtocolAndroid](https://github.com/NaikSoftware/StompProtocolAndroid) which implements STOMP on Android, to subscribe or send message to server.

## Introduction

Server now include three endpoints to receive message from client:

- `/welcome`
- `/group/{groupID}`
- `/chat`

1. `/welcome`

This endpoint will simply transfer all messages it received to subscriber of `/topic/getResponse`.

2. `/group/{groupID}`

This endpoint is used to dynamicly create groups. For example, client can send a message to `/group/1`, and all subscriber of `/topic/group/1` will receiver the message. Also, you can change the subscribe endpoint by change the [controller code](WebSocketServer/src/main/java/me/xlui/im/web/WebSocketController.java).

3. `/chat`

Endpoint `/chat` is used to enable point-to-point chat. If Alice(with userID 1) want to chat with Bob(with userID 2), she should send a message to endpoint `/chat` with a special json object defined in [ChatMessage](WebSocketServer/src/main/java/me/xlui/im/message/ChatMessage.java):

```js
function sendMessage() {
    var message = $('#message').val();
    stompClient.send('/chat', {}, JSON.stringify({
        'userID': 2, 
        'fromUserID': 1, 
        'message': "Hello Bob"})
    );
}
```

`userID` is necessary, this field will be used in server code to transfer message:

```java
simpMessagingTemplate.convertAndSendToUser(String.valueOf(chatMessage.getUserID()), "/msg", response);
```

Throught code above, the message from Alice will be sent to `/user/2/msg`, and if Bob subscribe himself, he will receive the message.

And Alice should also subscribe herself to receive message sent to her:

```js
stompClient.subscribe('/user/' + 1 + '/msg', function (response) {
    showResponse(JSON.parse(response.body).responseMessage);
});
```

So when Bob send a message to Alice, she will receive it correctly.

<br/>

Android client now can only send message to endpoint `/welcome`, support for endpoint `/group/{groupID}` is the next feature.

Browser client can send message to all endpoints, just see the GIFs bellow. 

## Server

![spring boot starter](Images/spring-boot-starter.png)

## Browser

![test in browser](Images/websocket-browser-client.gif)

## Android

![test in android](Images/websocket-android-client.gif)

## Dynamic Groups

![groups](Images/group.gif)

## Point-to-Point Message

![chat](Images/chat.gif)

## LICENSE

MIT
