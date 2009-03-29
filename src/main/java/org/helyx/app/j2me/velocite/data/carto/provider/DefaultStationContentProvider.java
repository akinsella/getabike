package org.helyx.app.j2me.velocite.data.carto.provider;

import java.io.InputStream;

import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.carto.util.LocalizationUtil;
import org.helyx.basics4me.io.BufferedInputStream;
import org.helyx.helyx4me.constant.EncodingConstants;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.localization.Point;
import org.helyx.helyx4me.stream.InputStreamProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.xml.xpp.XppAttributeProcessor;
import org.helyx.helyx4me.xml.xpp.XppUtil;
import org.helyx.log4me.Logger;
import org.helyx.log4me.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;


public class DefaultStationContentProvider extends AbstractStationContentProvider {
	
	private static final Logger logger = LoggerFactory.getLogger("DEFAULT_STATION_CONTENT_PROVIDER");

	private static final String MARKER = "marker";
	
	private static final String NUMBER = "number";
	private static final String NAME = "name";
	private static final String ADDRESS = "address";
	private static final String FULL_ADDRESS = "fullAddress";
	private static final String OPEN = "open";
	private static final String BONUS = "bonus";
	private static final String LNG = "lng";
	private static final String LAT = "lat";
	
	private static final int DEFAULT_OPEN_VALUE = 1;
	
	
	private boolean cancel = false;
	
	private int openValue = DEFAULT_OPEN_VALUE;

	private IContentAccessor stationContentAccessor;

	public DefaultStationContentProvider() {
		super();
	}

	public DefaultStationContentProvider(IContentAccessor stationContentAccessor) {
		super();
		this.stationContentAccessor = stationContentAccessor;
	}


	public void loadData() {
		
		logger.debug("Loading carto info ...");
		
		InputStream inputStream = null;
		InputStreamProvider cartoInputStreamProvider = null;
		
		try {

			progressDispatcher.fireEvent(EventType.ON_START);
			try {
				
				cartoInputStreamProvider = stationContentAccessor.getInputStreamProvider();
				inputStream = new BufferedInputStream(cartoInputStreamProvider.createInputStream());
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);

				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { NUMBER, NAME, OPEN, BONUS, ADDRESS, FULL_ADDRESS, LNG, LAT });

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
					station.open = xppAttributeProcessor.getAttrValueAsInt(OPEN) == openValue;
					station.bonus = xppAttributeProcessor.getAttrValueAsBoolean(BONUS);
					station.address = xppAttributeProcessor.getAttrValueAsString(ADDRESS);
					station.fullAddress = xppAttributeProcessor.getAttrValueAsString(FULL_ADDRESS);
					station.localization.lat = xppAttributeProcessor.getAttrValueAsDouble(LAT);
					station.localization.lng = xppAttributeProcessor.getAttrValueAsDouble(LNG);
					station.hasLocalization = LocalizationUtil.isSet(station.localization);
					processComplementaryInfo(station);

					stationNameNormalizer.normalizeName(station);

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

	private void processComplementaryInfo(Station station) {
		int index = station.fullAddress.indexOf(" - 750");
		
		if (index >= 0) {
			station.zipCode = station.fullAddress.substring(index + 2 + 1, index + 2 + 5 + 1);
			station.city = station.fullAddress.substring(index + 2 + 5 + 1 + 1);
		}
		else if ((index = station.fullAddress.indexOf(" 750")) > 0) {
			station.zipCode = station.fullAddress.substring(index + 1, index + 5 + 1);
			station.city = station.fullAddress.substring(index + 5 + 1 + 1);
		}
	}

	public void cancel() {
		cancel = true;
	}
	
	public String getDescription() {
		return "Fetchs station informations from path: '" + stationContentAccessor.getPath() + "'";
	}

	public void setOpenValue(int openValue) {
		this.openValue = openValue;
	}


}
