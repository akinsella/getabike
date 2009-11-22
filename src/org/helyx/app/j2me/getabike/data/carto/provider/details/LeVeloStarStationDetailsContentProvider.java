package org.helyx.app.j2me.getabike.data.carto.provider.details;

import java.io.InputStream;
import java.util.Date;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.domain.StationDetails;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.basics4me.io.BufferedInputStream;
import org.helyx.helyx4me.constant.EncodingConstants;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.content.provider.AbstractContentProvider;
import org.helyx.helyx4me.content.provider.exception.ContentProviderException;
import org.helyx.helyx4me.stream.InputStreamProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.xml.xpp.XppAttributeProcessor;
import org.helyx.helyx4me.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;
import org.xmlpull.v1.XmlPullParser;


public class LeVeloStarStationDetailsContentProvider extends AbstractContentProvider {
	
	private static final Logger logger = Logger.getLogger("VELIB_STATION_DETAILS_CONTENT_PROVIDER");


	private static final String DIV = "div";
	
	private static final String ATTR_CLASS = "class";
	
	private static final String TXT_VELO_DISPO = "velo_dispo";
	private static final String TXT_PLACE_DISPO = "place_dispo";
	private static final String TXT_CARTE_CREDIT = "carte_credit";
	
	private IContentAccessor stationDetailsContentAccessor;

	private City city;
	private Station station;
	
	public LeVeloStarStationDetailsContentProvider(IContentAccessor stationDetailsContentAccessor, City city, Station station) {
		super();
		this.stationDetailsContentAccessor = stationDetailsContentAccessor;
		this.city = city;
		this.station = station;
	}
	
	public void loadData() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Loading station '" + station.number + "' infos ...");
		}
		
		InputStream inputStream = null;
		InputStreamProvider stationDetailsInputStreamReaderProvider = null;
	
		try {
				
			StationDetails stationDetails = new StationDetails();
			try {
				if (logger.isInfoEnabled()) {
					logger.info("Path to station details: '" + stationDetailsContentAccessor.getPath() + "'");
				}
				
				progressDispatcher.fireEvent(EventType.ON_START);

				stationDetailsInputStreamReaderProvider = stationDetailsContentAccessor.getInputStreamProvider();
				
				inputStream = new BufferedInputStream(stationDetailsInputStreamReaderProvider.createInputStream());
								
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);
	
				stationDetails.date = new Date();
				stationDetails.stationNumber = station.number;

				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { ATTR_CLASS });

				while (XppUtil.readNextElement(xpp)) {
					String elementName = xpp.getName();
					if (elementName.equals(DIV)) {
						xppAttributeProcessor.processNode(xpp);
						if (xppAttributeProcessor.attrExists(ATTR_CLASS)) {
							
							if (TXT_VELO_DISPO.equals(xppAttributeProcessor.getAttrValueAsString(ATTR_CLASS))) {
								XppUtil.readNextElement(xpp);
								
								try {
									stationDetails.available = Integer.parseInt(XppUtil.readNextText(xpp));
								}
								catch(Throwable t) {
									stationDetails.available = -1;
								}
							}
							else if (TXT_PLACE_DISPO.equals(xppAttributeProcessor.getAttrValueAsString(ATTR_CLASS))) {
								XppUtil.readNextElement(xpp);
								
								try {
									stationDetails.free = Integer.parseInt(XppUtil.readNextText(xpp));
								}
								catch(Throwable t) {
									stationDetails.free = -1;
								}
							}
							else if (TXT_CARTE_CREDIT.equals(xppAttributeProcessor.getAttrValueAsString(ATTR_CLASS))) {
								stationDetails.tpe = true;
							}
							
						}
					}
				}
				stationDetails.hs = -1;
				stationDetails.total = (stationDetails.available >= 0 && stationDetails.free >= 0) ? stationDetails.available + stationDetails.free : 1;
				
				if (logger.isDebugEnabled()) {
					logger.debug("Station loaded: " + stationDetails);
				}

			}
			finally {
				stationDetailsInputStreamReaderProvider.dispose();
			}
			progressDispatcher.fireEvent(EventType.ON_SUCCESS, stationDetails);
		}
		catch (Throwable t) {
    		logger.warn(t);
			progressDispatcher.fireEvent(EventType.ON_ERROR, t);
		}

	}
	
	public String getDescription() {
		return "Fetchs station details from path: '" + stationDetailsContentAccessor.getPath() + "'";
	}

}
