package org.helyx.app.j2me.velocite.data.carto.domain;


public class Point {

	public double lng;
	
	public double lat;
	
	public Point() {
		super();
	}
	
	public Point(double lng, double lat) {
		super();
		this.lng = lng;
		this.lat = lat;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[Point]")
		.append(" lng=").append(lng)
		.append(", lat=").append(lat);

		return sb.toString();
	}
	
}
