package org.helyx.app.j2me.getabike.data.carto.provider;

import java.io.InputStream;
import java.util.Date;

import org.helyx.app.j2me.getabike.data.carto.CartoConstants;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.domain.StationDetails;
import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.helyx4me.constant.EncodingConstants;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.localization.Point;
import org.helyx.helyx4me.stream.InputStreamProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.xml.xpp.XppAttributeProcessor;
import org.helyx.helyx4me.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;
import org.xmlpull.v1.XmlPullParser;


public class NextBikeStationContentProvider extends AbstractStationContentProvider {
	
	private static final Logger logger = Logger.getLogger("NEXT_BIKE_STATION_CONTENT_PROVIDER");
	
	private static final String MARKERS = "markers";
	private static final String COUNTRY = "country";
	private static final String CITY = "city";
	private static final String PLACE = "place";
	
	
	private static final String ATTR_UID = "uid";
	private static final String ATTR_LAT = "lat";
	private static final String ATTR_LNG = "lng";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_BIKES = "bikes";

	private IContentAccessor stationContentAccessor;

	private City city;

	public NextBikeStationContentProvider(City city, IContentAccessor stationContentAccessor) {
		super();
		this.stationContentAccessor = stationContentAccessor;
		this.city = city;
	}


	public void loadData() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Loading carto info ...");
		}
		
		InputStream inputStream = null;
		InputStreamProvider cartoInputStreamProvider = null;
		
		try {

			progressDispatcher.fireEvent(EventType.ON_START);
			try {
				
				cartoInputStreamProvider = stationContentAccessor.getInputStreamProvider();
				inputStream = cartoInputStreamProvider.createInputStream(true);
//				inputStream = new BufferedInputStream(cartoInputStreamProvider.createInputStream());
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);

				XppAttributeProcessor cityXppAttributeProcessor = new XppAttributeProcessor();
				cityXppAttributeProcessor.addAll(new String[] { ATTR_NAME });

				XppAttributeProcessor placeXppAttributeProcessor = new XppAttributeProcessor();
				placeXppAttributeProcessor.addAll(new String[] { ATTR_UID, ATTR_BIKES, ATTR_LAT, ATTR_LNG, ATTR_NAME });

				IStationInfoNormalizer stationNameNormalizer = getStationInfoNormalizer();
				
				boolean cityFound = false;

				while (!cityFound && XppUtil.readNextElement(xpp)) {
					
					String eltName = xpp.getName();
					
					if (eltName.equals(CITY)) {
						
						cityXppAttributeProcessor.processNode(xpp);
						
						if (city.name.equals(cityXppAttributeProcessor.getAttrValueAsString(ATTR_NAME))) {
						cityFound = true;
							
							while (XppUtil.readNextElement(xpp) && xpp.getName().equals(PLACE)) {
								
								Station station = new Station();
								station.localization = new Point();
								station.details = new StationDetails();
								station.details.date = new Date();
								
								placeXppAttributeProcessor.processNode(xpp);

								station.number = placeXppAttributeProcessor.getAttrValueAsInt(ATTR_UID);
								station.details.stationNumber = station.number;

								station.name = placeXppAttributeProcessor.getAttrValueAsString(ATTR_NAME);
							
								station.hasLocalization = true;
								station.localization.lat = placeXppAttributeProcessor.getAttrValueAsDouble(ATTR_LAT);
								station.localization.lng = placeXppAttributeProcessor.getAttrValueAsDouble(ATTR_LNG);
								try {
									station.details.available = Integer.parseInt(placeXppAttributeProcessor.getAttrValueAsString(ATTR_BIKES).replace('+', ' ').trim());
								}
								catch(Throwable t) {
									station.details.available = 0;
								}
								station.details.total = -1;

								station.details.free = -1;
								station.details.hs = -1;
								
								station.open = true;
								station.bonus = false;
								station.tpe = false;
								station.address = "";
								station.fullAddress = "";
								
								if (stationNameNormalizer != null) {
									stationNameNormalizer.normalizeName(station);
								}
								
								progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, station);
							}
						}
					}
					
				}
				
			}
			finally {
				cartoInputStreamProvider.dispose();
			}
			progressDispatcher.fireEvent(EventType.ON_SUCCESS);
		}
		catch (Throwable t) {
    		logger.warn(t);
			progressDispatcher.fireEvent(EventType.ON_ERROR, t);
		}
	}
	
	public String getDescription() {
		return "Fetchs station informations from path: '" + stationContentAccessor.getPath() + "'";
	}


}
