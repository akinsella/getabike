package org.helyx.app.j2me.getabike.data.carto.provider;

import java.io.InputStream;

import org.helyx.app.j2me.getabike.data.carto.CartoConstants;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.getabike.data.carto.util.LocalizationUtil;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.lib.constant.EncodingConstants;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.localization.Point;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.xml.xpp.XppAttributeProcessor;
import org.helyx.app.j2me.getabike.lib.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;
import org.xmlpull.v1.XmlPullParser;


public class LeVeloStarStationContentProvider extends AbstractStationContentProvider {
	
	private static final Logger logger = Logger.getLogger("VELIB_STATION_CONTENT_PROVIDER");

	private static final String MARKER = "marker";
	private static final String ADDRESS = "t";
	
	private static final String CAT = "cat";
	private static final String UID = "uid";
	private static final String NAME = "name";
	private static final String LNG = "lng";
	private static final String LAT = "lat";
	
	private IContentAccessor stationContentAccessor;

	private City city;

	public LeVeloStarStationContentProvider(City city, IContentAccessor stationContentAccessor) {
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

				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { CAT, UID, LNG, LAT });

				IStationInfoNormalizer stationNameNormalizer = getStationInfoNormalizer();
				
				while (XppUtil.readToNextElement(xpp, MARKER)) {

					Station station = new Station();
					station.localization = new Point();
					
					xppAttributeProcessor.processNode(xpp);

					station.number = xppAttributeProcessor.getAttrValueAsInt(UID);
					station.open = xppAttributeProcessor.getAttrValueAsInt(CAT) == 1 ? true : false;
					station.bonus = false;
					station.tpe = false;
					station.address = "";
					station.fullAddress = "";
					station.localization.lat = xppAttributeProcessor.getAttrValueAsDouble(LAT);
					station.localization.lng = xppAttributeProcessor.getAttrValueAsDouble(LNG);
					station.hasLocalization = LocalizationUtil.isSet(station.localization);
					
					XppUtil.readNextElement(xpp);
					
					station.name = XppUtil.readNextText(xpp);
						
					if (stationNameNormalizer != null) {
						stationNameNormalizer.normalizeName(station);
					}
					
					progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, station);
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
