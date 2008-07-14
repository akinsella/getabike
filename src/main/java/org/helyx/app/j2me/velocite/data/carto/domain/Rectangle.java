package org.helyx.app.j2me.velocite.data.carto.domain;


public class Rectangle {

	public Point min;
	public Point max;
	
	public Rectangle() {
		super();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[Rectangle]")
		.append(" min=").append(min)
		.append(", max=").append(max);

		return sb.toString();
	}
	
}
