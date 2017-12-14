package me.xlui.im.message;

/**
 * 服务器向客户端发送此类消息
 */
public class Response {
    private String responseMessage;

    public Response(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
