package org.helyx.app.j2me.lib.reflect;

public class FieldInfo {

	public String name;
	public int type;
	public int index;
	
	public FieldInfo() {
		super();
	}

	public FieldInfo(int index, String name, int type) {
		super();
		this.index = index;
		this.name = name;
		this.type = type;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[FieldInfo] name='" + name + "', index='" + index + "', type='" + FieldType.getName(type) + "'");
		String result = sb.toString();
		
		return result;
	}
	
}
