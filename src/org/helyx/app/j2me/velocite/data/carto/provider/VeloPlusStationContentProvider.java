package org.helyx.app.j2me.velocite.data.carto.provider;

import java.io.InputStream;

import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.carto.util.LocalizationUtil;
import org.helyx.basics4me.io.BufferedInputStream;
import org.helyx.helyx4me.constant.EncodingConstants;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.localization.Point;
import org.helyx.helyx4me.stream.InputStreamProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.xml.xpp.XppAttributeProcessor;
import org.helyx.helyx4me.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;

import org.xmlpull.v1.XmlPullParser;

public class VeloPlusStationContentProvider extends AbstractStationContentProvider {
	
	private static final Logger logger = Logger.getLogger("VELO_PLUS_STATION_CONTENT_PROVIDER");

	private static final String MARKER = "marker";

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String LNG = "lng";
	private static final String LAT = "lat";
	
	private boolean cancel = false;

	private IContentAccessor stationContentAccessor;

	public VeloPlusStationContentProvider() {
		super();
	}

	public VeloPlusStationContentProvider(IContentAccessor stationContentAccessor) {
		super();
		this.stationContentAccessor = stationContentAccessor;
	}


	public void loadData() {
		
		logger.debug("Loading carto info ...");
		
		InputStream inputStream = null;
		InputStreamProvider cartoInputStreamProvider = null;
		
		try {

			progressDispatcher.fireEvent(EventType.ON_START);
			try {
				
				cartoInputStreamProvider = stationContentAccessor.getInputStreamProvider();
				inputStream = cartoInputStreamProvider.createInputStream(true);
//				inputStream = new BufferedInputStream(cartoInputStreamProvider.createInputStream());
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);

				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { ID, NAME, LNG, LAT });

				IStationInfoNormalizer stationNameNormalizer = getStationInfoNormalizer();
				
				while (XppUtil.readToNextElement(xpp, MARKER)) {
					if (cancel) {
						progressDispatcher.fireEvent(EventType.ON_CANCEL);
						return ;
					}
					Station station = new Station();
					station.localization = new Point();
					
					xppAttributeProcessor.processNode(xpp);

					station.number = xppAttributeProcessor.getAttrValueAsInt(ID);
					station.name = xppAttributeProcessor.getAttrValueAsString(NAME);
					station.open = true;
					station.bonus = false;
					station.address = "";
					station.fullAddress = "";
					station.localization.lat = xppAttributeProcessor.getAttrValueAsDouble(LAT);
					station.localization.lng = xppAttributeProcessor.getAttrValueAsDouble(LNG);
					station.hasLocalization = LocalizationUtil.isSet(station.localization);
					processComplementaryInfo(station);
					
					if (stationNameNormalizer != null) {
						stationNameNormalizer.normalizeName(station);
					}
					
					progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, station);
				}
			}
			finally {
				cartoInputStreamProvider.dispose();
			}
			progressDispatcher.fireEvent(EventType.ON_SUCCESS);
		}
		catch (Throwable t) {
    		logger.warn(t);
			progressDispatcher.fireEvent(EventType.ON_ERROR, t);
		}
	}

	private void processComplementaryInfo(Station station) {

	}

	public void cancel() {
		cancel = true;
	}

	public String getDescription() {
		return "Fetchs station informations from path: '" + stationContentAccessor.getPath() + "'";
	}

}
