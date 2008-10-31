package org.helyx.app.j2me.lib.task;

public class Event {

	private int type;
	private String message;
	private Object data;
	
	public Event(int type, String message, Object data) {
		super();
		this.type = type;
		this.message = message;
		this.data = data;
	}

	public int getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[Event]")
		.append(" type=").append(type)
		.append(", message=").append(message)
		.append(", data=").append(data);

		return sb.toString();
	}

}
