package app.xlui.example.im.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Message sent to server by browser
 *
 * 封装浏览器发送到服务器的消息
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String name;
}
