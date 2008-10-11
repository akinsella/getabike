package org.helyx.app.j2me.velocite.data.carto.provider;

import java.io.InputStream;

import org.helyx.app.j2me.lib.constant.EncodingConstants;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
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


public class VeloStationContentProvider extends AbstractContentProvider {
	
	private static final Log log = LogFactory.getLog("VELO_STATION_CONTENT_PROVIDER");

	private static final String MARKER = "marker";
	
	private static final String NUMBER = "number";
	private static final String NAME = "name";
	private static final String ADDRESS = "address";
	private static final String FULL_ADDRESS = "fullAddress";
	private static final String OPEN = "open";
	private static final String LNG = "lng";
	private static final String LAT = "lat";
	
	
	private boolean cancel = false;

	private IContentAccessor stationContentAccessor;

	public VeloStationContentProvider() {
		super();
	}

	public VeloStationContentProvider(IContentAccessor stationContentAccessor) {
		super();
		this.stationContentAccessor = stationContentAccessor;
	}


	public void loadData() {
		
		log.debug("Loading carto info ...");
		
		InputStream inputStream = null;
		InputStreamProvider cartoInputStreamProvider = null;
		
		try {

			progressDispatcher.fireEvent(ProgressEventType.ON_START);
			try {
				
				cartoInputStreamProvider = stationContentAccessor.getInputStreamProvider();
				inputStream = new BufferedInputStream(cartoInputStreamProvider.createInputStream());
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);

				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { NUMBER, NAME, OPEN, ADDRESS, FULL_ADDRESS, LNG, LAT });

				while (XppUtil.readToNextElement(xpp, MARKER)) {
					if (cancel) {
						progressDispatcher.fireEvent(ProgressEventType.ON_CANCEL);
						return ;
					}
					
					Station station = new Station();
					station.localization = new Point();
					
					xppAttributeProcessor.processNode(xpp);

					station.number = xppAttributeProcessor.getAttrValueAsInt(NUMBER);
					station.name = xppAttributeProcessor.getAttrValueAsString(NAME);
					station.open = true;
					station.address = xppAttributeProcessor.getAttrValueAsString(ADDRESS);
					station.fullAddress = xppAttributeProcessor.getAttrValueAsString(FULL_ADDRESS);
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
    		log.warn(t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
		}
	}

	private void processComplementaryInfo(Station station) {
		int index = station.fullAddress.indexOf(" - 750");
		
		if (index >= 0) {
			station.zipCode = station.fullAddress.substring(index + 2 + 1, index + 2 + 5 + 1);
			station.city = station.fullAddress.substring(index + 2 + 5 + 1 + 1);
		}
		else if ((index = station.fullAddress.indexOf(" 750")) > 0) {
			station.zipCode = station.fullAddress.substring(index + 1, index + 5 + 1);
			station.city = station.fullAddress.substring(index + 5 + 1 + 1);
		}
	}

	public void cancel() {
		cancel = true;
	}
	
	public String getDescription() {
		return "Fetchs station informations from path: '" + stationContentAccessor.getPath() + "'";
	}

}
