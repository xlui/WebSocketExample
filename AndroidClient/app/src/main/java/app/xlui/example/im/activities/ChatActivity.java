package app.xlui.example.im.activities;

import android.annotation.SuppressLint;
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

import java.util.Random;

import app.xlui.example.im.R;
import app.xlui.example.im.conf.Const;
import app.xlui.example.im.util.StompUtils;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class ChatActivity extends AppCompatActivity {
    private Button broadcast;
    private Button groups;
    private Button chat;

    private TextView userId;
    private EditText chatUserId;
    private Button submit;
    private EditText chatMessage;
    private Button send;
    private TextView show;

    private String user_id;
    private String chat_user_id;

    private void init() {
        broadcast = findViewById(R.id.broadcast);
        groups = findViewById(R.id.groups);
        chat = findViewById(R.id.chat);
        chat.setEnabled(false);

        userId = findViewById(R.id.id);
        user_id = String.valueOf(new Random().nextInt(100));
        userId.setText(user_id);

        chatUserId = findViewById(R.id.chat_user_id);
        submit = findViewById(R.id.submit);
        submit.setEnabled(false);

        chatMessage = findViewById(R.id.chat_message);
        send = findViewById(R.id.send);
        send.setEnabled(false);

        show = findViewById(R.id.show);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.init();

        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address);
        stompClient.connect();
        Toast.makeText(this, "Connect start", Toast.LENGTH_SHORT).show();
        StompUtils.lifecycle(stompClient);

        stompClient.topic(Const.chatResponse.replace(Const.placeholder, user_id)).subscribe(stompMessage -> {
            JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
            Log.i(Const.TAG, "Receive: " + jsonObject.toString());
            runOnUiThread(() -> {
                try {
                    show.append(jsonObject.getString("response") + "\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        chatUserId.addTextChangedListener(new TextWatcher() {
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
            chat_user_id = chatUserId.getText().toString();
            if (chat_user_id.length() == 0) {
                return;
            }
            submit.setEnabled(false);
            send.setEnabled(true);
        });

        send.setOnClickListener(v -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userID", chat_user_id);
                jsonObject.put("fromUserID", userId.getText().toString());
                jsonObject.put("message", chatMessage.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (chat_user_id == null || chat_user_id.length() == 0) {
                chat_user_id = chatUserId.getText().toString();
            }
            stompClient.send(Const.chat, jsonObject.toString()).subscribe();
        });

        broadcast.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(ChatActivity.this, BroadcastActivity.class);
            startActivity(intent);
            this.finish();
        });
        groups.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(ChatActivity.this, GroupActivity.class);
            startActivity(intent);
            this.finish();
        });
    }
}
