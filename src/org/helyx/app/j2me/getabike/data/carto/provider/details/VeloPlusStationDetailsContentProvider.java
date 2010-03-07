package org.helyx.app.j2me.getabike.data.carto.provider.details;

import java.io.InputStream;
import java.util.Date;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.domain.StationDetails;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.basics4me.io.BufferedInputStream;
import org.helyx.app.j2me.getabike.lib.constant.EncodingConstants;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.getabike.lib.content.provider.exception.ContentProviderException;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;
import org.xmlpull.v1.XmlPullParser;

public class VeloPlusStationDetailsContentProvider extends AbstractContentProvider {
	
	private static final Logger logger = Logger.getLogger("VELO_PLUS_STATION_DETAILS_CONTENT_PROVIDER");


	private static final String STATION = "station";
	
	private static final String STATUS = "status";
	private static final String BIKES = "bikes";
	private static final String ATTACHS = "attachs";
	private static final String PAIEMENT = "paiement";
	
	private static final String EN_SERVICE = "En service";
	private static final String HORS_SERVICE = "Hors service";
	
	private static final String SANS_TPE = "SANS_TPE";
	private static final String AVEC_TPE = "AVEC_TPE";
	
	
	private static final String INVALID_CONTENT = "Xml content is invalid";
	
	private City city;
	private Station station;

	private IContentAccessor stationDetailsContentAccessor;

	public VeloPlusStationDetailsContentProvider(IContentAccessor stationDetailsContentAccessor, City city, Station station) {
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
					if (elementName.equals(STATUS)) {
						stationDetails.open = EN_SERVICE.equals(XppUtil.readNextText(xpp));
					}
					else if (elementName.equals(BIKES)) {
						stationDetails.available = Integer.parseInt(XppUtil.readNextText(xpp));
					}					
					else if (elementName.equals(ATTACHS)) {
						stationDetails.free = Integer.parseInt(XppUtil.readNextText(xpp));
					}
					else if (elementName.equals(PAIEMENT)) {
						stationDetails.tpe = AVEC_TPE.equals(XppUtil.readNextText(xpp));
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
