package me.xlui.im;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import okhttp3.WebSocket;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "xlui";
	// hello 是在服务器配置中注册的节点
	// 如果使用 Android Studio 自带的 AVD，地址应该是 10.0.2.2
//	private static final String address = "ws://10.0.2.2:8080/hello/websocket";
	// 如果使用 Genymotion，地址应该是 10.0.3.2
	private static final String address = "ws://10.0.3.2:8080/hello/websocket";

	private EditText mEditText;
	private Button mButton;
	private TextView mTextView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mEditText = findViewById(R.id.name);
		mButton = findViewById(R.id.send);
		mTextView = findViewById(R.id.show);

		StompClient stompClient = Stomp.over(WebSocket.class, address);
		// 连接服务器
		stompClient.connect();
		Toast.makeText(this, "开始连接", Toast.LENGTH_SHORT).show();
		stompClient.lifecycle().subscribe(lifecycleEvent -> {
			switch (lifecycleEvent.getType()) {
				case OPENED:
					Log.i(TAG, "Connect: stomp connection opened");
					break;
				case ERROR:
					Log.e(TAG, "Connect: Error occured!", lifecycleEvent.getException());
					break;
				case CLOSED:
					Log.i(TAG, "Connect: stomp connection closed");
					break;
			}
		});

		// 订阅消息
		stompClient.topic("/topic/getResponse").subscribe(stompMessage -> {
			// 解析 JSON
			JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
			Log.i(TAG, "Receive: " + stompMessage.getPayload());
			runOnUiThread(() -> {
				try {
					mTextView.append(jsonObject.getString("responseMessage") + "\n");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			});
		});

		// 以 json 格式发送数据
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", mEditText.getText());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// 设置点击按钮发送事件
		mButton.setOnClickListener(v -> stompClient.send("/welcome", jsonObject.toString()).subscribe(new Subscriber<Void>() {
			@Override
			public void onSubscribe(Subscription s) {

			}

			@Override
			public void onNext(Void aVoid) {

			}

			@Override
			public void onError(Throwable t) {
				Log.e(TAG, "Send Error!", t);
			}

			@Override
			public void onComplete() {
				Log.i(TAG, "Send complete!");
			}
		}));
	}
}
