package org.helyx.app.j2me.lib.ui.geometry;

public class Point {

	public int x;
	public int y;
	
	public Point() {
		super();
	}
	
	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[Point] ")
		.append(" x=").append(x)
		.append(", y=").append(y);

		return sb.toString();
	}

}
