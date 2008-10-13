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
import org.helyx.app.j2me.lib.stream.StreamUtil;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.xml.xpp.XppAttributeProcessor;
import org.helyx.app.j2me.lib.xml.xpp.XppUtil;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.basics4me.io.BufferedInputStream;
import org.json.me.JSONObject;
import org.xmlpull.v1.XmlPullParser;


public class VeloVStationDetailsContentProvider extends AbstractContentProvider {
	
	private static final Log log = LogFactory.getLog("VELO_PLUS_STATION_DETAILS_CONTENT_PROVIDER");


	private static final String STATION = "station";
	
	private static final String ID = "id";
	private static final String LABEL = "label";
	private static final String STATE = "state";
	private static final String TOTAL_BIKE_BASE = "totalBikeBase";
	private static final String AVAILABLE_BIKE = "availableBike";
	private static final String FREE_BIKE_BASE = "freeBikeBase";

	
	private static final String INFOS = "Infos";
	
	
	private static final String INVALID_CONTENT = "Json content is invalid";
	
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
				
				String jsonStreamContent = StreamUtil.readStream(inputStream, false);
				log.info("JSON content: " + jsonStreamContent);
				JSONObject jsonContent = new JSONObject(jsonStreamContent);
				JSONObject jsonMarker = jsonContent.getJSONObject(INFOS);
				
				
				stationDetails.date = new Date();
				stationDetails.stationNumber = station.number;
				stationDetails.open = jsonMarker.getBoolean(STATE);
				stationDetails.available = jsonMarker.getInt(AVAILABLE_BIKE);
				stationDetails.free = jsonMarker.getInt(FREE_BIKE_BASE);
				stationDetails.total = jsonMarker.getInt(TOTAL_BIKE_BASE);
				stationDetails.hs = stationDetails.total - stationDetails.free - stationDetails.available;

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
