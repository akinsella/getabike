package org.helyx.app.j2me.velocite.data.carto.provider;

import java.io.InputStream;
import java.util.Date;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.content.provider.ContentProviderException;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.xml.xpp.XppUtil;
import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;
import org.xmlpull.v1.XmlPullParser;


public class DefaultStationDetailsContentProvider extends AbstractContentProvider {
	
	private static final String CAT = "DEFAULT_STATION_CONTENT_PROVIDER";


	private static final String STATION = "station";
	
	private static final String AVAILABLE = "available";
	private static final String FREE = "free";
	private static final String TOTAL = "total";
	private static final String TICKET = "ticket";
	
	private static final String UTF_8 = "UTF-8";
	
	
	private static final String INVALID_CONTENT = "Xml content is invalid";
	
	private int stationNumber;

	private IContentAccessor stationDetailsContentAccessor;

	public DefaultStationDetailsContentProvider(int stationNumber) {
		super();
		this.stationNumber = stationNumber;
	}

	public DefaultStationDetailsContentProvider(IContentAccessor stationDetailsContentAccessor) {
		super();
		this.stationDetailsContentAccessor = stationDetailsContentAccessor;
	}
	
	public void loadData() {
		
		if (Log.isLoggable(CAT, Log.DEBUG)) {
			Log.debug(CAT, "Loading station '" + stationNumber + "' infos ...");
		}
		
		InputStream inputStream = null;
		InputStreamProvider stationDetailsInputStreamReaderProvider = null;
	
		try {
				
			try {

				progressDispatcher.fireEvent(ProgressEventType.ON_START);

				stationDetailsInputStreamReaderProvider = stationDetailsContentAccessor.getInputStreamProvider();
				
				inputStream = stationDetailsInputStreamReaderProvider.createInputStream();
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, UTF_8);
				
				StationDetails stationDetails = new StationDetails();
				stationDetails.dateCreation = new Date();
				
				if (!XppUtil.readToNextElement(xpp, STATION)) {
					throw new ContentProviderException(INVALID_CONTENT);
				}
	
				while (XppUtil.readNextElement(xpp)) {
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
				}
				
				if (Log.isLoggable(CAT, Log.DEBUG)) {
					Log.debug(CAT, "Station loaded: " + stationDetails);
				}

				progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS, stationDetails);
			}
			finally {
				stationDetailsInputStreamReaderProvider.dispose();
			}
		}
		catch (Throwable t) {
    		Log.warn(CAT, t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
		}

	}

	protected String getCat() {
		return CAT;
	}
	
	public String getDescription() {
		return "Fetchs station details from path: '" + stationDetailsContentAccessor.getPath() + "'";
	}

}
