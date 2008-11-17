package org.helyx.app.j2me.velocite.data.carto.provider;

import java.io.InputStream;

import org.helyx.app.j2me.lib.constant.EncodingConstants;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.app.j2me.lib.xml.xpp.XppAttributeProcessor;
import org.helyx.app.j2me.lib.xml.xpp.XppUtil;
import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.carto.util.LocalizationUtil;
import org.helyx.basics4me.io.BufferedInputStream;
import org.xmlpull.v1.XmlPullParser;

public class VeloPlusStationContentProvider extends AbstractStationContentProvider {
	
	private static final Log log = LogFactory.getLog("VELO_PLUS_STATION_CONTENT_PROVIDER");

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
		
		log.debug("Loading carto info ...");
		
		InputStream inputStream = null;
		InputStreamProvider cartoInputStreamProvider = null;
		
		try {

			progressDispatcher.fireEvent(EventType.ON_START);
			try {
				
				cartoInputStreamProvider = stationContentAccessor.getInputStreamProvider();
				inputStream = new BufferedInputStream(cartoInputStreamProvider.createInputStream());
				
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
					
					stationNameNormalizer.normalizeName(station);

					progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, station);
				}
			}
			finally {
				cartoInputStreamProvider.dispose();
			}
			progressDispatcher.fireEvent(EventType.ON_SUCCESS);
		}
		catch (Throwable t) {
    		log.warn(t);
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
