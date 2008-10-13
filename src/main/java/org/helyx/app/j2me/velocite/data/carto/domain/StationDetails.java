package org.helyx.app.j2me.velocite.data.carto.domain;

import java.util.Date;


public class StationDetails {
	

	public int stationNumber;
	
	public Date date;
	
	public int available;
	public int free;
	public int total;
	public int hs;
	public boolean ticket;
	public boolean open;
	public boolean tpe;
	
	
	public StationDetails() {
		super();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[StationDetails]")
		.append(" date=").append(date)
		.append(", available=").append(available)
		.append(", free=").append(free)
		.append(", total=").append(total)
		.append(", hs=").append(hs)
		.append(", ticket=").append(ticket);

		return sb.toString();
	}
	
}
