package me.xlui.im.message;

public class ChatMessage {
	private int userID;
	private int fromUserID;
	private String message;

	public ChatMessage() {
		super();
	}

	public ChatMessage(int userID, int fromUserID, String message) {
		this.userID = userID;
		this.fromUserID = fromUserID;
		this.message = message;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getFromUserID() {
		return fromUserID;
	}

	public void setFromUserID(int fromUserID) {
		this.fromUserID = fromUserID;
	}
}
