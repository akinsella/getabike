package org.helyx.app.j2me.lib.ui.util;

public class KeyMapConfig {

	public String modelKey;
	public KeyMap[] keyMapArray;
	
	public KeyMapConfig() {
		super();
	}
	
	public KeyMapConfig(String modelKey, KeyMap[] keyMapArray) {
		super();
		this.modelKey = modelKey;
		this.keyMapArray = keyMapArray;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[KeyMapConfig]");
		sb.append(" modelKey=" + modelKey);
		sb.append(", keyMapArray= [");
		int length = keyMapArray.length;
		for (int i = 0 ; i < length ; i++) {
			sb.append("[").append(keyMapArray[i]).append("]");
			if (i + 1 < length) {
				sb.append(", ");
			}
		}
		sb.append("]");
		
		return sb.toString();
	}

}
