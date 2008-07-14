package org.helyx.app.j2me.lib.pref;

public class Pref {

	public String key;
	public String value;
	
	public Pref() {
		super();
	}
	
	public Pref(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[Pref]")
		.append(" key=").append(key)
		.append(", value=").append(value);
		return sb.toString();
	}
	
}
