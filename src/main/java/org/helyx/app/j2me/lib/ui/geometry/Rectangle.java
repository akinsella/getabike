package org.helyx.app.j2me.lib.ui.geometry;

public class Rectangle {

	public Point location;
	public Size size;
	
	public Rectangle(Point location, Size size) {
		super();
		this.location = location;
		this.size = size;
	}
	
	public Rectangle(int x, int y, int width, int height) {
		this(new Point(x, y), new Size(width, height));
	}

	public Rectangle() {
		super();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[Rectangle] ")
		.append(" location=").append(location)
		.append(", size=").append(size);

		return sb.toString();
	}

}
