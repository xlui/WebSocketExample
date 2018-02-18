package me.xlui.im.conf;

public class Const {
	public static final String TAG = "xlui";
	public static final String placeholder = "placeholder";

	// im 是在服务器配置中注册的节点
	// 如果使用 Android Studio 自带的 AVD，地址应该是 10.0.2.2
//	private static final String address = "ws://10.0.2.2:8080/im/websocket";
	// 如果使用 Genymotion，地址应该是 10.0.3.2
	public static final String address = "ws://10.0.3.2:8080/im/websocket";

	public static final String broadcast = "/broadcast";
	public static final String broadcastResponse = "/b";
//	replace placeholder with group id
	public static final String group = "/group/" + placeholder;
	public static final String groupResponse = "/g/" + placeholder;
	public static final String chat = "/chat";
//	replace placeholder with user id
	public static final String chatResponse = "/user/" + placeholder + "/msg";
}
