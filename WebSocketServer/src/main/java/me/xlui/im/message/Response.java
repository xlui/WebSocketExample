package me.xlui.im.message;

/**
 * 服务器向客户端发送此类消息
 */
public class Response {
    private String response;

    public Response(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
