package me.xlui.im.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class GroupActivity extends AppCompatActivity {
	private Button broadcast;
	private Button groups;
	private Button chat;

	private EditText groupId;
	private Button submit;
	private EditText name;
	private Button send;
	private TextView show;

	private String group_id;

	private void init() {
		broadcast = findViewById(R.id.broadcast);
		groups = findViewById(R.id.groups);
		groups.setEnabled(false);
		chat = findViewById(R.id.chat);

		groupId = findViewById(R.id.group_id);
		submit = findViewById(R.id.submit);
		submit.setEnabled(false);

		name = findViewById(R.id.name);
		send = findViewById(R.id.send);
		send.setEnabled(false);

		show = findViewById(R.id.show);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);

		this.init();

		StompClient stompClient = Stomp.over(WebSocket.class, Const.address);
		stompClient.connect();
		Toast.makeText(this, "开始连接", Toast.LENGTH_SHORT).show();
		StompUtils.connect(stompClient);

		groupId.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!submit.isEnabled())
					submit.setEnabled(true);
			}
		});

		submit.setOnClickListener(v -> {
			group_id = groupId.getText().toString();
			if (group_id.length() == 0) {
				return;
			}
			stompClient.topic(Const.groupResponse.replace("placeholder", group_id)).subscribe(stompMessage -> {
				JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
				Log.i(Const.TAG, "Receive: " + stompMessage.getPayload());
				runOnUiThread(() -> {
					try {
						show.append(jsonObject.getString("response") + "\n");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				});
			});
			submit.setEnabled(false);
			send.setEnabled(true);
		});

		send.setOnClickListener(v -> {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("name", name.getText().toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (group_id == null || group_id.length() == 0) {
				group_id = groupId.getText().toString();
			}
			stompClient.send(Const.group.replace("placeholder", group_id), jsonObject.toString()).subscribe(new Subscriber<Void>() {
				@Override
				public void onSubscribe(Subscription s) {
					Log.i(Const.TAG, "订阅成功");
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
					Log.i(Const.TAG, "发送消息成功！");
				}
			});
		});

		broadcast.setOnClickListener(v -> {
			Intent intent = new Intent();
			intent.setClass(GroupActivity.this, BroadcastActivity.class);
			startActivity(intent);
			this.finish();
		});
		chat.setOnClickListener(v -> {
			Intent intent = new Intent();
			intent.setClass(GroupActivity.this, ChatActivity.class);
			startActivity(intent);
			this.finish();
		});
	}
}
