package org.helyx.app.j2me.velocite.data.carto.provider.details;

import java.io.InputStream;
import java.util.Date;

import org.helyx.app.j2me.lib.constant.EncodingConstants;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.content.provider.ContentProviderException;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.xml.xpp.XppAttributeProcessor;
import org.helyx.app.j2me.lib.xml.xpp.XppUtil;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.basics4me.io.BufferedInputStream;
import org.xmlpull.v1.XmlPullParser;


public class DefaultStationDetailsContentProvider extends AbstractContentProvider {
	
	private static final Log log = LogFactory.getLog("DEFAULT_STATION_DETAILS_CONTENT_PROVIDER");


	private static final String STATION = "station";
	
	private static final String AVAILABLE = "available";
	private static final String FREE = "free";
	private static final String TOTAL = "total";
	private static final String TICKET = "ticket";
	
	
	private static final String INVALID_CONTENT = "Xml content is invalid";
	
	private IContentAccessor stationDetailsContentAccessor;

	private City city;
	private Station station;
	
	public DefaultStationDetailsContentProvider(IContentAccessor stationDetailsContentAccessor, City city, Station station) {
		super();
		this.stationDetailsContentAccessor = stationDetailsContentAccessor;
		this.city = city;
		this.station = station;
	}
	
	public void loadData() {
		
		if (log.isDebugEnabled()) {
			log.debug("Loading station '" + station.number + "' infos ...");
		}
		
		InputStream inputStream = null;
		InputStreamProvider stationDetailsInputStreamReaderProvider = null;
	
		try {
				
			StationDetails stationDetails = new StationDetails();
			try {
				log.info("Path to station details: '" + stationDetailsContentAccessor.getPath() + "'");
				
				progressDispatcher.fireEvent(ProgressEventType.ON_START);

				stationDetailsInputStreamReaderProvider = stationDetailsContentAccessor.getInputStreamProvider();
				
				inputStream = new BufferedInputStream(stationDetailsInputStreamReaderProvider.createInputStream());
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);
				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { AVAILABLE, FREE, TOTAL, TICKET });

				if (XppUtil.readToNextElement(xpp, STATION)) {
					throw new ContentProviderException(INVALID_CONTENT);
				}
				
				xppAttributeProcessor.processNode(xpp);
				stationDetails.date = new Date();
				
				stationDetails.stationNumber = station.number;
				stationDetails.open = station.open;
				stationDetails.available = xppAttributeProcessor.getAttrValueAsInt(AVAILABLE);
				stationDetails.free = xppAttributeProcessor.getAttrValueAsInt(FREE);
				stationDetails.total = xppAttributeProcessor.getAttrValueAsInt(TOTAL);
				stationDetails.hs = stationDetails.total - stationDetails.free - stationDetails.available;
				stationDetails.ticket = xppAttributeProcessor.getAttrValueAsInt(TICKET) == 1;

				
				
				String elementName = xpp.getName();
				if (elementName.equals(AVAILABLE)) {
					stationDetails.available = Integer.parseInt(XppUtil.readNextText(xpp));
				}
				else if (elementName.equals(FREE)) {
					stationDetails.free = Integer.parseInt(XppUtil.readNextText(xpp));
				}
				else if (elementName.equals(TICKET)) {
					stationDetails.ticket = Integer.parseInt(XppUtil.readNextText(xpp)) == 1;
				}
				else if (elementName.equals(TOTAL)) {
					stationDetails.total = Integer.parseInt(XppUtil.readNextText(xpp));
				}
				
				if (log.isDebugEnabled()) {
					log.debug("Station loaded: " + stationDetails);
				}

			}
			finally {
				stationDetailsInputStreamReaderProvider.dispose();
			}
			progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS, stationDetails);
		}
		catch (Throwable t) {
    		log.warn(t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
		}

	}
	
	public String getDescription() {
		return "Fetchs station details from path: '" + stationDetailsContentAccessor.getPath() + "'";
	}

}
