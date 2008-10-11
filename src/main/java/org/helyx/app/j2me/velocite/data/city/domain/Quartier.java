package org.helyx.app.j2me.velocite.data.city.domain;

public class Quartier {

	public int id;
	public String city;
	public String name;
	public String zipCode;

	public String toString() {
		StringBuffer sb = new StringBuffer("[Quartier]")
		.append(" id=").append(id)
		.append(" city=").append(city)
		.append(", name=").append(name)
		.append(", zipCode=").append(zipCode);		
		return sb.toString();
	}
	
}
