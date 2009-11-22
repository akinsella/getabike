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
import org.helyx.helyx4me.text.TextUtil;
import org.helyx.helyx4me.xml.xpp.XppAttributeProcessor;
import org.helyx.helyx4me.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;
import org.xmlpull.v1.XmlPullParser;


public class OyBikeStationContentProvider extends AbstractStationContentProvider {
	
	private static final Logger logger = Logger.getLogger("OYBIKE_STATION_CONTENT_PROVIDER");
	
	private static final String VIEW_ENTRIES = "viewentries";
	private static final String VIEW_ENTRY = "viewentry";
	private static final String ENTRY_DATA = "entrydata";
	private static final String TEXT = "text";
	private static final String NUMBER = "number";
	private static final String NAME = "name";
	
	private static final String ATTR_STAND_ID = "standId";
	private static final String ATTR_STAND_NAME = "standName";
	private static final String ATTR_STAND_COMMENTS = "standComments";
	private static final String ATTR_LOCALIZATION = "$1";
	private static final String ATTR_CAPACITY = "Capacity";
	private static final String ATTR_BIKES_AVAILABLE = "BikesAvailable";
	private static final String ATTR_PORTS_FREE = "portsFree";
	private static final String ATTR_DATE = "$2";

	private IContentAccessor stationContentAccessor;

	private City city;

	public OyBikeStationContentProvider(City city, IContentAccessor stationContentAccessor) {
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
				xppAttributeProcessor.addAll(new String[] { NAME });

				IStationInfoNormalizer stationNameNormalizer = getStationInfoNormalizer();
				
				XppUtil.readToNextElement(xpp, VIEW_ENTRIES);
				
				while (true) {
					
					String eltName = xpp.getName();
					
					if (eltName.equals(VIEW_ENTRY)) {
						
						Station station = new Station();
						station.localization = new Point();
						station.details = new StationDetails();
						station.details.date = new Date();
						
						while (true) {
							
							if (!XppUtil.readNextElement(xpp)) {
								break;
							}
							
							if (xpp.getName().equals(VIEW_ENTRY)) {
								break;
							}
							
							xppAttributeProcessor.processNode(xpp);

							String attrName = xppAttributeProcessor.getAttrValueAsString(NAME);				
							
							if (attrName.equals(ATTR_STAND_ID)) {
								XppUtil.readNextElement(xpp);
								String content = XppUtil.readNextText(xpp).trim();
								station.number = Integer.parseInt(content);
								station.details.stationNumber = station.number;
							}
							else if (attrName.equals(ATTR_STAND_NAME)) {
								XppUtil.readNextElement(xpp);
								station.name = XppUtil.readNextText(xpp).trim();
							}
							else if (attrName.equals(ATTR_STAND_COMMENTS)) {
								XppUtil.readNextElement(xpp);
							}
							else if (attrName.equals(ATTR_LOCALIZATION)) {
								XppUtil.readNextElement(xpp);
								String localization = XppUtil.readNextText(xpp).trim();
								String[] localizationInfos = TextUtil.split(localization, ',');
								station.hasLocalization = true;
								station.localization.lat = Double.parseDouble(localizationInfos[0]);
								station.localization.lng = Double.parseDouble(localizationInfos[1]);
							}
							else if (attrName.equals(ATTR_CAPACITY)) {
								XppUtil.readNextElement(xpp);
								station.details.total = Integer.parseInt(XppUtil.readNextText(xpp).trim());
							}
							else if (attrName.equals(ATTR_BIKES_AVAILABLE)) {
								XppUtil.readNextElement(xpp);
								station.details.available = Integer.parseInt(XppUtil.readNextText(xpp).trim());
							}
							else if (attrName.equals(ATTR_PORTS_FREE)) {
								XppUtil.readNextElement(xpp);
								station.details.free = Integer.parseInt(XppUtil.readNextText(xpp).trim());
							}
							else if (attrName.equals(ATTR_DATE)) {
								XppUtil.readNextElement(xpp);
							}
							else {
								XppUtil.readNextElement(xpp);
							}
							
						}
						
						station.open = true;
						station.bonus = false;
						station.tpe = false;
						station.address = "";
						station.fullAddress = "";

						station.details.hs = station.details.total - station.details.free - station.details.available;
						
						if (stationNameNormalizer != null) {
							stationNameNormalizer.normalizeName(station);
						}
						
						progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, station);
					}
					if (xpp.getName() == null) {
						break;
					}
					
					if (xpp.getName().equals(VIEW_ENTRY)) {
						continue;
					}


					if (!XppUtil.readNextElement(xpp)) {
						break;
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
