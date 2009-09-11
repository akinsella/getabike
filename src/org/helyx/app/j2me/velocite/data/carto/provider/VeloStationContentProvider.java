package org.helyx.app.j2me.velocite.data.carto.provider;

import java.io.InputStream;

import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.carto.util.LocalizationUtil;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.helyx4me.constant.EncodingConstants;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.localization.Point;
import org.helyx.helyx4me.stream.InputStreamProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.xml.xpp.XppAttributeProcessor;
import org.helyx.helyx4me.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;
import org.xmlpull.v1.XmlPullParser;


public class VeloStationContentProvider extends AbstractStationContentProvider {
	
	private static final Logger logger = Logger.getLogger("VELO_STATION_CONTENT_PROVIDER");

	private static final String MARKER = "marker";
	
	private static final String NUMBER = "number";
	private static final String NAME = "name";
	private static final String ADDRESS = "address";
	private static final String FULL_ADDRESS = "fullAddress";
	private static final String OPEN = "open";
	private static final String LNG = "lng";
	private static final String LAT = "lat";
	
	private City city;
	
	private boolean cancel = false;

	private IContentAccessor stationContentAccessor;

	public VeloStationContentProvider(City city, IContentAccessor stationContentAccessor) {
		super();
		this.stationContentAccessor = stationContentAccessor;
		this.city = city;
	}


	public void loadData() {
		
		logger.debug("Loading carto info ...");
		
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
				xppAttributeProcessor.addAll(new String[] { NUMBER, NAME, OPEN, ADDRESS, FULL_ADDRESS, LNG, LAT });

				IStationInfoNormalizer stationNameNormalizer = getStationInfoNormalizer();

				while (XppUtil.readToNextElement(xpp, MARKER)) {
					if (cancel) {
						progressDispatcher.fireEvent(EventType.ON_CANCEL);
						return ;
					}
					
					Station station = new Station();
					station.localization = new Point();
					
					xppAttributeProcessor.processNode(xpp);

					station.number = xppAttributeProcessor.getAttrValueAsInt(NUMBER);
					station.name = xppAttributeProcessor.getAttrValueAsString(NAME);
					station.open = true;
					station.bonus = false;
					station.tpe = false;
					station.address = xppAttributeProcessor.getAttrValueAsString(ADDRESS);
					station.fullAddress = xppAttributeProcessor.getAttrValueAsString(FULL_ADDRESS).trim();
					station.localization.lat = xppAttributeProcessor.getAttrValueAsDouble(LAT);
					station.localization.lng = xppAttributeProcessor.getAttrValueAsDouble(LNG);
					station.hasLocalization = LocalizationUtil.isSet(station.localization);

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

	public void cancel() {
		cancel = true;
	}
	
	public String getDescription() {
		return "Fetchs station informations from path: '" + stationContentAccessor.getPath() + "'";
	}

}
