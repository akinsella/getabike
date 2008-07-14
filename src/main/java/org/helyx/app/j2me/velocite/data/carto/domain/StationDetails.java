package org.helyx.app.j2me.velocite.data.carto.domain;

import java.util.Date;


public class StationDetails {

	public int id;
	
	public int stationNumber;
	
	public Date dateCreation;
	
	public int available;
	public int free;
	public int total;
	public boolean ticket;
	
	public StationDetails() {
		super();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[StationDetails]")
		.append(" available=").append(available)
		.append(", free=").append(free)
		.append(", total=").append(total)
		.append(", ticket=").append(ticket);

		return sb.toString();
	}
	
}
