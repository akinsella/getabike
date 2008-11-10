package org.helyx.app.j2me.velocite.data.carto.domain;


public class Rectangle {

	public Point min;
	public Point max;
	
	public Rectangle() {
		super();
	}
	
	public Rectangle(Point min, Point max) {
		super();
		this.min = min;
		this.max = max;
	}	
	
	public Rectangle(double lat1, double lng1, double lat2, double lng2) {
		super();
		this.min = new Point(lng1, lat1);
		this.max = new Point(lng2, lat2);
	}	

	public String toString() {
		StringBuffer sb = new StringBuffer("[Rectangle]")
		.append(" min=").append(min)
		.append(", max=").append(max);

		return sb.toString();
	}
	
}
