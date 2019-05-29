package app.xlui.example.im.util;

import android.annotation.SuppressLint;
import android.util.Log;

import app.xlui.example.im.conf.Const;
import ua.naiksoftware.stomp.StompClient;

public class StompUtils {
	@SuppressLint("CheckResult")
    public static void lifecycle(StompClient stompClient) {
		stompClient.lifecycle().subscribe(lifecycleEvent -> {
			switch (lifecycleEvent.getType()) {
				case OPENED:
					Log.d(Const.TAG, "Stomp connection opened");
					break;

				case ERROR:
					Log.e(Const.TAG, "Error", lifecycleEvent.getException());
					break;

				case CLOSED:
					Log.d(Const.TAG, "Stomp connection closed");
					break;
			}
		});
	}
}
