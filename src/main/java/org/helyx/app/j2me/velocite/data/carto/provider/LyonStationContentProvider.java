package org.helyx.app.j2me.velocite.data.carto.provider;

import java.io.InputStream;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.json.me.JSONArray;
import org.json.me.JSONObject;


public class LyonStationContentProvider extends AbstractContentProvider {
	
	private static final String CAT = "LYON_STATION_CONTENT_PROVIDER";
	
	private static final String NUM_STATION = "numStation";
	private static final String NOM_STATION = "nomStation";
	private static final String INFO_STATION = "infoStation";
	private static final String LATITUDE = "lat";
	private static final String LONGITUDE = "longitude";
	private static final String LYON = "Lyon";
	
	private boolean cancel = false;

	private IContentAccessor[] stationContentAccessors;

	public LyonStationContentProvider() {
		super();
	}

	public LyonStationContentProvider(IContentAccessor[] stationContentAccessors) {
		super();
		this.stationContentAccessors = stationContentAccessors;
	}
	
	public void loadData() {
		
		Log.debug(CAT, "Loading Lyon carto info ...");

		try {

			progressDispatcher.fireEvent(ProgressEventType.ON_START);
			int scaLength = stationContentAccessors.length;
			for (int i = 0 ; i < scaLength ; i++) {
				InputStream inputStream = null;
				InputStreamProvider cartoInputStreamProvider = null;

				if (cancel) {
					progressDispatcher.fireEvent(ProgressEventType.ON_CANCEL);
					return ;
				}

				try {
					cartoInputStreamProvider = stationContentAccessors[i].getInputStreamProvider();
					inputStream = cartoInputStreamProvider.createInputStream();

					Log.debug(CAT, "Parsing simple sample XML for id: " + i);
					
					int length = -1;
					StringBuffer sb = new StringBuffer();
					byte[] bytes = new byte[1024];
					while((length = inputStream.read(bytes)) >= 0) {
						if (cancel) {
							progressDispatcher.fireEvent(ProgressEventType.ON_CANCEL);
							return ;
						}

						if (length == 0) {
							continue;
						}
						String line = new String(bytes);
						sb.append(line);
					}
					String content = sb.toString();
					Log.info(CAT, "JSON content: " + content);
					JSONObject jsonContent = new JSONObject(content);
					JSONArray markerArray = jsonContent.getJSONArray("markers");
					int markerCount = markerArray.length();
					
					for (int markerOffset = 0 ; markerOffset < markerCount ; markerOffset++) {
						if (cancel) {
							progressDispatcher.fireEvent(ProgressEventType.ON_CANCEL);
							return ;
						}

						JSONObject jsonMarker = markerArray.getJSONObject(markerOffset);
						Station station = new Station();
						station.number = jsonMarker.getInt(NUM_STATION);
						station.name = jsonMarker.getString(NOM_STATION);
						station.address = jsonMarker.optString(INFO_STATION, "");
						station.fullAddress = station.address;
						station.hasLocalization = true;
						station.localization = new Point();
						station.localization.lat = jsonMarker.optDouble(LATITUDE, 0);
						station.localization.lat = jsonMarker.optDouble(LONGITUDE, 0);
						station.open = true;
						station.city = LYON;
						station.zipCode = (markerOffset < 10 ? "6900": "690") + markerOffset;
						
						progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, station);
					}
				}
				finally {
					cartoInputStreamProvider.dispose();
				}
				
			}
			progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS);			
		}
		catch (Throwable t) {
    		Log.warn(CAT, t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
		}
	}

	public void cancel() {
		cancel = true;
	}

	public String getCat() {
		return CAT;
	}
	
	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Fetchs station informations from path: [");
		int length = stationContentAccessors.length;
		for (int i = 0 ; i < length ; i++) {
			sb.append("'" + stationContentAccessors[i].getPath() + "'");
			if (i + 1 < length) {
				sb.append(", ");
			}
		}
		sb.append("]");
		String result = sb.toString();
		
		return result;
	}

}
