package org.helyx.app.j2me.velocite.data.city.domain;

import java.util.Vector;

public class City {

	public City() {
		super();
	}

	public String key;
	
	public String name;
	
	public String serviceName;
	
	public String type;
	
	public boolean active;
	
	public String webSite;

	public String stationList;
	
	public String stationDetails;
	
	public String offlineStationList;
	
	public Vector quartierList = new Vector();

	public String toString() {
		StringBuffer sb = new StringBuffer("[City]")
		.append(" key=").append(key)
		.append(", name=").append(name)
		.append(", serviceName=").append(serviceName)
		.append(", type=").append(type)
		.append(", active=").append(active)
		.append(", webSite=").append(webSite)
		.append(", stationList=").append(stationList)
		.append(", stationDetails=").append(stationDetails)
		.append(", offlineStationList=").append(offlineStationList)
		.append(", quartiers: [");
		
		int quartierCount = quartierList.size();
		for (int i = 0 ; i < quartierCount ; i++) {
			Quartier quartier = (Quartier)quartierList.elementAt(i);
			sb.append(quartier.toString());
			if (i + 1 < quartierCount) {
				sb.append(", ");
			}
		}
		sb.append("]");
		
		return sb.toString();
	}

}
