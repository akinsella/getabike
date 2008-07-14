package org.helyx.app.j2me.velocite.data.carto.provider;

import java.io.InputStream;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.stream.IInputStreamProvider;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.xml.XppUtil;
import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.xmlpull.v1.XmlPullParser;

public class OrleansStationContentProvider extends AbstractContentProvider {
	
	private static final String CAT = "DEFAULT_STATION_CONTENT_PROVIDER";

	private static final String MARKER = "marker";

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String LNG = "lng";
	private static final String LAT = "lat";
	
	private static final String UTF_8 = "UTF-8";
	
	
	private static final String INVALID_CONTENT = "Xml content is invalid";
	
	private boolean cancel = false;

	private IContentAccessor stationContentAccessor;

	public OrleansStationContentProvider() {
		super();
	}

	public OrleansStationContentProvider(IContentAccessor stationContentAccessor) {
		super();
		this.stationContentAccessor = stationContentAccessor;
	}


	public void loadData() {
		
		Log.debug(CAT, "Loading carto info ...");
		
		InputStream inputStream = null;
		IInputStreamProvider cartoInputStreamProvider = null;
		
		try {

			progressDispatcher.fireEvent(ProgressEventType.ON_START);
			try {
				
				cartoInputStreamProvider = stationContentAccessor.getInputStreamProvider();
				inputStream = cartoInputStreamProvider.createInputStream();
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, UTF_8);
	
				Log.debug(CAT, "Parsing simple sample XML");
				
				while (XppUtil.readToNextElement(xpp, MARKER)) {
					if (cancel) {
						progressDispatcher.fireEvent(ProgressEventType.ON_CANCEL);
						return ;
					}
					Station station = new Station();
					station.hasLocalization = true;
					Point localization = new Point();
	
					for (int i = 0 ; i < xpp.getAttributeCount() ; i++) {
						String attributeName = xpp.getAttributeName(i);
						if (attributeName.equals(ID)) {
							station.number = Integer.parseInt(xpp.getAttributeValue(i));
						}
						else if (attributeName.equals(NAME)) {
							String name = xpp.getAttributeValue(i);
	
							station.name = name;
						}
						else if (attributeName.equals(LNG)) {
							try {
								localization.lng = Double.parseDouble(xpp.getAttributeValue(i));
							}
							catch(Throwable t) {
								station.hasLocalization = false;
								Log.warn(CAT, t);
							}
						}
						else if (attributeName.equals(LAT)) {
							try {
								localization.lat = Double.parseDouble(xpp.getAttributeValue(i));
							}
							catch(Throwable t) {
								station.hasLocalization = false;
								Log.warn(CAT, t);
							}
						}
					}
	
					station.localization = localization;
									
					if (station.hasLocalization) {
						station.localization.lat = 0;
						station.localization.lng = 0;
					}
	
					progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, station);
				}
			}
			finally {
				cartoInputStreamProvider.dispose();
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

	protected String getCat() {
		return CAT;
	}
	
	public String getDescription() {
		return "Fetchs station informations from path: '" + stationContentAccessor.getPath() + "'";
	}

}
