package app.xlui.example.im.activities;

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

import app.xlui.example.im.R;
import app.xlui.example.im.conf.Const;
import app.xlui.example.im.util.StompUtils;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})
public class GroupActivity extends AppCompatActivity {
    private Button broadcastButton;
    private Button groupButton;
    private Button chatButton;

    private EditText groupIdText;
    private Button submitButton;
    private EditText nameText;
    private Button sendButton;
    private TextView showText;

    private String groupId;

    private void init() {
        broadcastButton = findViewById(R.id.broadcast);
        groupButton = findViewById(R.id.groups);
        groupButton.setEnabled(false);
        chatButton = findViewById(R.id.chat);

        groupIdText = findViewById(R.id.group_id);
        submitButton = findViewById(R.id.submit);
        submitButton.setEnabled(false);

        nameText = findViewById(R.id.name);
        sendButton = findViewById(R.id.send);
        sendButton.setEnabled(false);

        showText = findViewById(R.id.show);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        this.init();

        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address);
        Toast.makeText(this, "Start connecting to server", Toast.LENGTH_SHORT).show();
        stompClient.connect();
        StompUtils.lifecycle(stompClient);

        groupIdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!submitButton.isEnabled())
                    submitButton.setEnabled(true);
            }
        });

        submitButton.setOnClickListener(v -> {
            groupId = groupIdText.getText().toString();
            if (groupId.length() == 0) {
                return;
            }
            stompClient.topic(Const.groupResponse.replace(Const.placeholder, groupId)).subscribe(stompMessage -> {
                JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
                Log.i(Const.TAG, "Receive: " + stompMessage.getPayload());
                runOnUiThread(() -> {
                    try {
                        showText.append(jsonObject.getString("response") + "\n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            });
            submitButton.setEnabled(false);
            sendButton.setEnabled(true);
        });

        sendButton.setOnClickListener(v -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", nameText.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (groupId == null || groupId.length() == 0) {
                groupId = groupIdText.getText().toString();
            }
            stompClient.send(Const.group.replace(Const.placeholder, groupId), jsonObject.toString()).subscribe();
            nameText.setText("");
        });

        broadcastButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(GroupActivity.this, BroadcastActivity.class);
            startActivity(intent);
            this.finish();
        });
        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(GroupActivity.this, ChatActivity.class);
            startActivity(intent);
            this.finish();
        });
    }
}
