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
import org.helyx.helyx4me.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;
import org.xmlpull.v1.XmlPullParser;


public class VelibStationDetailsContentProvider extends AbstractContentProvider {
	
	private static final Logger logger = Logger.getLogger("VELIB_STATION_DETAILS_CONTENT_PROVIDER");


	private static final String STATION = "station";
	
	private static final String AVAILABLE = "available";
	private static final String FREE = "free";
	private static final String TOTAL = "total";
	private static final String TICKET = "ticket";
	
	
	private static final String INVALID_CONTENT = "Xml content is invalid";
	
	private IContentAccessor stationDetailsContentAccessor;

	private City city;
	private Station station;
	
	public VelibStationDetailsContentProvider(IContentAccessor stationDetailsContentAccessor, City city, Station station) {
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

				if (!XppUtil.readToNextElement(xpp, STATION)) {
					throw new ContentProviderException(INVALID_CONTENT);
				}
	
				stationDetails.date = new Date();
				stationDetails.stationNumber = station.number;
				stationDetails.open = station.open;

				while (XppUtil.readNextElement(xpp)) {
					String elementName = xpp.getName();
					if (elementName.equals(AVAILABLE)) {
						stationDetails.available = Integer.parseInt(XppUtil.readNextText(xpp));
					}
					else if (elementName.equals(FREE)) {
						stationDetails.free = Integer.parseInt(XppUtil.readNextText(xpp));
					}
					else if (elementName.equals(TOTAL)) {
						stationDetails.total = Integer.parseInt(XppUtil.readNextText(xpp));
					}
					else if (elementName.equals(TICKET)) {
						stationDetails.ticket = Integer.parseInt(XppUtil.readNextText(xpp)) == 1;
					}
				}
				stationDetails.hs = stationDetails.total - stationDetails.free - stationDetails.available;
				
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
