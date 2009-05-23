package org.helyx.app.j2me.velocite.data.carto.domain;

import org.helyx.helyx4me.localization.Point;



public class Station {
	
	public String name;
	
	public int number;
	
	public String address;
	
	public String fullAddress;
	
	public String zipCode;
	
	public String city;
	
	public boolean open;
	
	public boolean hasLocalization;
	
	public Point localization;
	
	public boolean bonus;

	public Station() {
		super();
	}	

	public String toString() {
		StringBuffer sb = new StringBuffer("[Station]")
		.append(" name=").append(name)
		.append(", number=").append(number)
		.append(", address=").append(address)
		.append(", fullAddress=").append(fullAddress)
		.append(", zipCode=").append(zipCode)
		.append(", city=").append(city)
		.append(", open=").append(open)
		.append(", bonus").append(bonus)
		.append(", hasLocalization=").append(hasLocalization)
		.append(", localization=").append(localization);

		return sb.toString();
	}

}
