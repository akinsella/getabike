package org.helyx.app.j2me.getabike.ui.view.renderer;

import org.helyx.app.j2me.getabike.data.city.accessor.ICityAcessor;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.renderer.text.I18NTextRenderer;

public class StationTitleRenderer extends I18NTextRenderer {

	private ICityAcessor cityAccessor;
	
	private static StationTitleRenderer instance;
	
	private static String stationSeparator = " - ";
	private static String stationSeparatorEmpty = "";
	
	public StationTitleRenderer(AbstractMIDlet midlet, ICityAcessor cityAccessor) {
		super(midlet);
		this.cityAccessor = cityAccessor;
	}	

	public String renderText(String textValue) {
		City city =  cityAccessor.getCity();
		String cityName = city != null ? city.name : null;
		String title = (cityName != null ? (cityName + stationSeparator) : stationSeparatorEmpty) + super.renderText(textValue);
		 
		return title;
	}
	
}
