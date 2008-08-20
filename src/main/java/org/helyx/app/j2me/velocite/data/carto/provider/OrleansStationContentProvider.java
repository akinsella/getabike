package org.helyx.app.j2me.velocite.data.carto.provider;

import java.io.InputStream;

import org.helyx.app.j2me.lib.constant.EncodingConstants;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.xml.xpp.XppAttributeProcessor;
import org.helyx.app.j2me.lib.xml.xpp.XppUtil;
import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.util.LocalizationUtil;
import org.helyx.basics4me.io.BufferedInputStream;
import org.xmlpull.v1.XmlPullParser;

public class OrleansStationContentProvider extends AbstractContentProvider {
	
	private static final String CAT = "DEFAULT_STATION_CONTENT_PROVIDER";

	private static final String MARKER = "marker";

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String LNG = "lng";
	private static final String LAT = "lat";
	
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
		InputStreamProvider cartoInputStreamProvider = null;
		
		try {

			progressDispatcher.fireEvent(ProgressEventType.ON_START);
			try {
				
				cartoInputStreamProvider = stationContentAccessor.getInputStreamProvider();
				inputStream = new BufferedInputStream(cartoInputStreamProvider.createInputStream());
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);

				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { ID, NAME, LNG, LAT });

				
				while (XppUtil.readToNextElement(xpp, MARKER)) {
					if (cancel) {
						progressDispatcher.fireEvent(ProgressEventType.ON_CANCEL);
						return ;
					}
					Station station = new Station();
					station.localization = new Point();
					
					xppAttributeProcessor.processNode(xpp);

					station.number = xppAttributeProcessor.getAttrValueAsInt(ID);
					station.name = xppAttributeProcessor.getAttrValueAsString(NAME);
					station.open = true;
					station.address = "";
					station.fullAddress = "";
					station.localization.lat = xppAttributeProcessor.getAttrValueAsDouble(LAT);
					station.localization.lng = xppAttributeProcessor.getAttrValueAsDouble(LNG);
					station.hasLocalization = LocalizationUtil.isSet(station.localization);
					processComplementaryInfo(station);
	
					progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, station);
				}
				progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS);
			}
			finally {
				cartoInputStreamProvider.dispose();
			}
		}
		catch (Throwable t) {
    		Log.warn(CAT, t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
		}
	}

	private void processComplementaryInfo(Station station) {

	}

	public void cancel() {
		cancel = true;
	}

	public String getCat() {
		return CAT;
	}
	
	public String getDescription() {
		return "Fetchs station informations from path: '" + stationContentAccessor.getPath() + "'";
	}

}
