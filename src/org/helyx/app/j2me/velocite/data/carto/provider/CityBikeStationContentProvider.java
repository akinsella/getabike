package org.helyx.app.j2me.velocite.data.carto.provider;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.carto.util.LocalizationUtil;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.localization.Point;
import org.helyx.helyx4me.stream.InputStreamProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.xml.xpp.XppAttributeProcessor;
import org.helyx.helyx4me.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;

import au.com.bytecode.opencsv.CSVReader;


public class CityBikeStationContentProvider extends AbstractStationContentProvider {
	
	private static final Logger logger = Logger.getLogger("CITY_BIKE_STATION_CONTENT_PROVIDER");
	
	private static final int POS_NUMBER = 0;
	private static final int POS_NAME = 3;
	private static final int POS_ADDRESS = 4;
	private static final int POS_TOTAL = 5;	
	
	private boolean cancel = false;

	private IContentAccessor stationContentAccessor;

	private City city;

	public CityBikeStationContentProvider(City city, IContentAccessor stationContentAccessor) {
		super();
		this.stationContentAccessor = stationContentAccessor;
		this.city = city;
	}


	public void loadData() {
		
		logger.debug("Loading carto info ...");
		
		Reader reader = null;
		CSVReader csvReader = null;
		InputStream inputStream = null;
		InputStreamProvider cartoInputStreamProvider = null;
		
		try {

			progressDispatcher.fireEvent(EventType.ON_START);
			try {
				
				cartoInputStreamProvider = stationContentAccessor.getInputStreamProvider();
				inputStream = cartoInputStreamProvider.createInputStream(true);
//				inputStream = new BufferedInputStream(cartoInputStreamProvider.createInputStream());
				
				reader = new InputStreamReader(inputStream);
				
				csvReader = new CSVReader(reader);

				IStationInfoNormalizer stationNameNormalizer = getStationInfoNormalizer();
				
				String[] line = null;
				
				csvReader.readNext();
				
				while ((line = csvReader.readNext()) != null) {
					if (cancel) {
						progressDispatcher.fireEvent(EventType.ON_CANCEL);
						return ;
					}
					
					Station station = new Station();
					station.localization = new Point();

					station.number = Integer.parseInt(line[POS_NUMBER]);
					station.name = line[POS_NAME];
					station.address = line[POS_ADDRESS];
					station.fullAddress = line[POS_ADDRESS];
					station.hasLocalization = false;
					station.tpe = false;
					station.bonus = false;

					if (stationNameNormalizer != null) {
						stationNameNormalizer.normalizeName(station);
					}
					
					progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, station);
				}
				
			}
			finally {
				if (csvReader != null) {
					try { csvReader.close(); } catch(Throwable t) { logger.warn(t); }
				}
				if (reader != null) {
					try { reader.close(); } catch(Throwable t) { logger.warn(t); }
				}
				cartoInputStreamProvider.dispose();
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
		return "Fetchs station informations from path: '" + stationContentAccessor.getPath() + "'";
	}

}
