package org.helyx.app.j2me.velocite.data.carto.provider.details;

import java.io.InputStream;
import java.util.Date;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.basics4me.io.BufferedInputStream;
import org.helyx.helyx4me.constant.EncodingConstants;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.content.provider.AbstractContentProvider;
import org.helyx.helyx4me.content.provider.exception.ContentProviderException;
import org.helyx.helyx4me.stream.InputStreamProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;


public class VeloVStationDetailsContentProvider extends AbstractContentProvider {
	
	private static final Logger logger = LoggerFactory.getLogger("VELO_PLUS_STATION_DETAILS_CONTENT_PROVIDER");
	
	private static final String ID = "id";
	private static final String LABEL = "label";
	private static final String STATE = "state";
	private static final String TOTAL_BIKE_BASE = "totalBikeBase";
	private static final String AVAILABLE_BIKE = "availableBike";
	private static final String FREE_BIKE_BASE = "freeBikeBase";
	
	private static final String ENABLED = "enabled";
	private static final String DISABLED = "disabled";

	
	private static final String INFOS = "Infos";
	
	
	private static final String INVALID_CONTENT = "Xml content is invalid";
	
	private City city;
	private Station station;

	private IContentAccessor stationDetailsContentAccessor;

	public VeloVStationDetailsContentProvider(IContentAccessor stationDetailsContentAccessor, City city, Station station) {
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

				logger.info("Path to station details: '" + stationDetailsContentAccessor.getPath() + "'");
				
				progressDispatcher.fireEvent(EventType.ON_START);

				stationDetailsInputStreamReaderProvider = stationDetailsContentAccessor.getInputStreamProvider();
				
				inputStream = new BufferedInputStream(stationDetailsInputStreamReaderProvider.createInputStream());
				
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);
				if (!XppUtil.readToNextElement(xpp, INFOS)) {
					throw new ContentProviderException(INVALID_CONTENT);
				}
	
				stationDetails.date = new Date();
				stationDetails.stationNumber = station.number;
				stationDetails.open = station.open;

				while (XppUtil.readNextElement(xpp)) {
					String elementName = xpp.getName();
					if (elementName.equals(STATE)) {
						stationDetails.open = ENABLED.equals(XppUtil.readNextText(xpp));
					}
					else if (elementName.equals(AVAILABLE_BIKE)) {
						stationDetails.available = Integer.parseInt(XppUtil.readNextText(xpp));
					}					
					else if (elementName.equals(FREE_BIKE_BASE)) {
						stationDetails.free = Integer.parseInt(XppUtil.readNextText(xpp));
					}
					else if (elementName.equals(TOTAL_BIKE_BASE)) {
						stationDetails.total = Integer.parseInt(XppUtil.readNextText(xpp));
					}
				}
				stationDetails.total = stationDetails.available + stationDetails.free;
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
