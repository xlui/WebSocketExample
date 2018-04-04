package me.xlui.im.util;

import android.annotation.SuppressLint;
import android.util.Log;

import me.xlui.im.conf.Const;
import ua.naiksoftware.stomp.client.StompClient;

public class StompUtils {
	@SuppressLint("CheckResult")
    public static void lifecycle(StompClient stompClient) {
		stompClient.lifecycle().subscribe(lifecycleEvent -> {
			switch (lifecycleEvent.getType()) {
				case OPENED:
					Log.i(Const.TAG, "Connect: stomp connection opened!");
					break;
				case ERROR:
					Log.e(Const.TAG, "Connect: Error occured!", lifecycleEvent.getException());
					break;
				case CLOSED:
					Log.i(Const.TAG, "Connect: stomp connection closed!");
					break;
			}
		});
	}
}
