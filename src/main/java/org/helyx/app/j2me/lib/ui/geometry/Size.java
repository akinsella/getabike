package org.helyx.app.j2me.lib.ui.geometry;

public class Size {

	public int width;
	public int height;

	public Size() {
		super();
	}
		
	public Size(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[Size] ")
		.append(" width=").append(width)
		.append(", height=").append(height);

		return sb.toString();
	}
	
}
