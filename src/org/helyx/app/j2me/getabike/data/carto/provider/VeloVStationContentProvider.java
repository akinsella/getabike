package org.helyx.app.j2me.getabike.data.carto.provider;

import java.io.InputStream;

import org.helyx.app.j2me.getabike.data.carto.CartoConstants;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.getabike.data.carto.util.LocalizationUtil;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.city.domain.Quartier;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessorFactory;
import org.helyx.app.j2me.getabike.lib.localization.Point;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.stream.StreamUtil;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.logging4me.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONObject;


public class VeloVStationContentProvider extends AbstractStationContentProvider {
	
	private static final Logger logger = Logger.getLogger("VELOV_STATION_CONTENT_PROVIDER");
	
	private static final String NUM_STATION = "numStation";
	private static final String NOM_STATION = "nomStation";
	private static final String INFO_STATION = "infoStation";
	private static final String LATITUDE = "x";
	private static final String LONGITUDE = "y";
	
	private boolean cancel = false;

	private IContentAccessorFactory contentAccessorFactory;
	private City city;

	public VeloVStationContentProvider(City city, IContentAccessorFactory contentAccessorFactory) {
		super();
		this.contentAccessorFactory = contentAccessorFactory;
		this.city = city;
	}
	
	public void loadData() {
		if (logger.isDebugEnabled()) {
			logger.debug("Loading carto info ...");
		}

		try {
			progressDispatcher.fireEvent(EventType.ON_START);
			int quartierLength = city.quartierList.size();
			for (int i = 0 ; i < quartierLength ; i++) {
				Quartier quartier = (Quartier)city.quartierList.elementAt(i);
				InputStream inputStream = null;
				InputStreamProvider cartoInputStreamProvider = null;

				if (cancel) {
					progressDispatcher.fireEvent(EventType.ON_CANCEL);
					return ;
				}

				try {
					IContentAccessor contentAccessor = contentAccessorFactory.createContentAccessorFactory(quartier);
					if (logger.isDebugEnabled()) {
						logger.debug("contentAccessor: '" + contentAccessor.getPath() + "'");
					}
					cartoInputStreamProvider = contentAccessor.getInputStreamProvider();
					inputStream = cartoInputStreamProvider.createInputStream(true);

					if (logger.isDebugEnabled()) {
						logger.debug("Parsing JSON for quartier: " + i);
					}
					String jsonStreamContent = StreamUtil.readStream(inputStream, false);
					if (logger.isDebugEnabled()) {
						logger.debug("JSON content: " + jsonStreamContent);
					}
					JSONObject jsonContent = new JSONObject(jsonStreamContent);
					JSONArray markerArray = jsonContent.getJSONArray("markers");
					int markerCount = markerArray.length();

					IStationInfoNormalizer stationNameNormalizer = getStationInfoNormalizer();
					
					for (int markerOffset = 0 ; markerOffset < markerCount ; markerOffset++) {
						if (cancel) {
							progressDispatcher.fireEvent(EventType.ON_CANCEL);
							return ;
						}

						JSONObject jsonMarker = markerArray.getJSONObject(markerOffset);
						Station station = new Station();
						station.number = jsonMarker.getInt(NUM_STATION);
						station.name = jsonMarker.getString(NOM_STATION);
						station.address = jsonMarker.optString(INFO_STATION, "");
						station.fullAddress = station.address;
						station.bonus = false;
						station.tpe = false;
						station.hasLocalization = true;
						station.localization = new Point();
						station.localization.lat = jsonMarker.optDouble(LATITUDE, 0);
						station.localization.lng = jsonMarker.optDouble(LONGITUDE, 0);
						station.hasLocalization = LocalizationUtil.isSet(station.localization);

						station.open = true;
						station.city = quartier.city;
						station.zipCode = quartier.zipCode;

						if (stationNameNormalizer != null) {
							stationNameNormalizer.normalizeName(station);
						}

						progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, station);
					}
				}
				finally {
					cartoInputStreamProvider.dispose();
				}
				
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
		StringBuffer sb = new StringBuffer();
		sb.append("Fetchs station informations from path: [");
		int length = city.quartierList.size();
		for (int i = 0 ; i < length ; i++) {
			sb.append("'" + contentAccessorFactory.createContentAccessorFactory(city.quartierList.elementAt(i)).getPath() + "'");
			if (i + 1 < length) {
				sb.append(", ");
			}
		}
		sb.append("]");
		String result = sb.toString();
		
		return result;
	}

}
