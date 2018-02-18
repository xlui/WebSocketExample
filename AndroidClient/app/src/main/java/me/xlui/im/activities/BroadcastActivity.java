package me.xlui.im.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import me.xlui.im.R;
import me.xlui.im.conf.Const;
import me.xlui.im.util.StompUtils;
import okhttp3.WebSocket;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class BroadcastActivity extends AppCompatActivity {
	private Button broadcast;
	private Button groups;
	private Button chat;

	private EditText name;
	private Button send;
	private TextView result;

	private void init() {
		broadcast = findViewById(R.id.broadcast);
		broadcast.setEnabled(false);
		groups = findViewById(R.id.groups);
		chat = findViewById(R.id.chat);
		name = findViewById(R.id.name);
		send = findViewById(R.id.send);
		result = findViewById(R.id.show);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broadcast);

		this.init();

		StompClient stompClient = Stomp.over(WebSocket.class, Const.address);
		// 连接服务器
		stompClient.connect();
		Toast.makeText(this, "开始连接", Toast.LENGTH_SHORT).show();
		StompUtils.connect(stompClient);

		// 订阅消息
		stompClient.topic(Const.broadcastResponse).subscribe(stompMessage -> {
			JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
			Log.i(Const.TAG, "Receive: " + stompMessage.getPayload());
			runOnUiThread(() -> {
				try {
					result.append(jsonObject.getString("response") + "\n");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			});
		});

		send.setOnClickListener(v -> {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("name", name.getText());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			stompClient.send(Const.broadcast, jsonObject.toString()).subscribe(new Subscriber<Void>() {
				@Override
				public void onSubscribe(Subscription s) {
					Log.i(Const.TAG, "onSubscribe: 订阅成功！");
				}

				@Override
				public void onNext(Void aVoid) {

				}

				@Override
				public void onError(Throwable t) {
					t.printStackTrace();
					Log.e(Const.TAG, "发生错误：", t);
				}

				@Override
				public void onComplete() {
					Log.i(Const.TAG, "onComplete: Send Complete!");
				}
			});
		});

		groups.setOnClickListener(v -> {
			Intent intent = new Intent();
			intent.setClass(BroadcastActivity.this, GroupActivity.class);
			startActivity(intent);
			this.finish();
		});
		chat.setOnClickListener(v -> {
			Intent intent = new Intent();
			intent.setClass(BroadcastActivity.this, ChatActivity.class);
			startActivity(intent);
			this.finish();
		});
	}
}
