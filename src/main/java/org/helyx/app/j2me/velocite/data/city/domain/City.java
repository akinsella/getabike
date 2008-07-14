package org.helyx.app.j2me.velocite.data.city.domain;

public class City {

	public City() {
		super();
	}

	public String key;
	
	public String name;
	
	public String type;
	
	public boolean active;
	
	public String webSite;

	public String stationList;
	
	public String stationDetails;
	
	public String offlineStationList;
	
	public String contentProviderFactory;

	public String toString() {
		StringBuffer sb = new StringBuffer("[City]")
		.append(" key=").append(key)
		.append(", name=").append(name)
		.append(", type=").append(type)
		.append(", active=").append(active)
		.append(", webSite=").append(webSite)
		.append(", stationList=").append(stationList)
		.append(", stationDetails=").append(stationDetails)
		.append(", offlineStationList=").append(offlineStationList)
		.append(", contentProviderFactory=").append(contentProviderFactory);
		
		return sb.toString();
	}

}
