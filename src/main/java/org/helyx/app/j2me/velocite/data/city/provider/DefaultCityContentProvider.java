package org.helyx.app.j2me.velocite.data.city.provider;

import java.io.InputStream;
import java.util.Vector;

import org.helyx.app.j2me.lib.constant.EncodingConstants;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.content.provider.ContentProviderException;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.app.j2me.lib.xml.xpp.XppAttributeProcessor;
import org.helyx.app.j2me.lib.xml.xpp.XppUtil;
import org.helyx.app.j2me.velocite.data.city.CityConstants;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.domain.Quartier;
import org.helyx.basics4me.io.BufferedInputStream;
import org.xmlpull.v1.XmlPullParser;

public class DefaultCityContentProvider extends AbstractContentProvider {
	
	private static final Log log = LogFactory.getLog("DEFAULT_CITY_CONTENT_PROVIDER");


	private static final String CITIES = "cities";
	private static final String CITY = "city";
	private static final String QUARTIERS = "quartiers";
	private static final String QUARTIER = "quartier";

	private static final String KEY = "key";
	private static final String NAME = "name";
	private static final String SERVICE_NAME = "serviceName";
	private static final String TYPE = "type";
	private static final String DEFAULT = "default";
	private static final String ACTIVE = "active";
	private static final String STATION_LIST = "stationList";
	private static final String STATION_DETAILS = "stationDetails";
	private static final String WEB_SITE = "webSite";
	private static final String BONUS = "bonus";
	private static final String TPE = "tpe";
	private static final String STATE = "state";
	
	private static final String ID = "id";
	private static final String ZIP_CODE = "zipCode";
	
	
	private static final String INVALID_CONTENT = "Xml content is invalid";
	
	private boolean cancel = false;

	private IContentAccessor cityContentAccessor;

	public DefaultCityContentProvider() {
		super();
	}

	public DefaultCityContentProvider(IContentAccessor cityContentAccessor) {
		super();
		this.cityContentAccessor = cityContentAccessor;
	}


	public void loadData() {
		
		log.debug("Loading cities informations ...");
		
		InputStream inputStream = null;
		InputStreamProvider cityInputStreamProvider = null;
		
		Vector cityList = new Vector();

		try {
			progressDispatcher.fireEvent(EventType.ON_START);
			try {			
				cityInputStreamProvider = cityContentAccessor.getInputStreamProvider();
				inputStream = new BufferedInputStream(cityInputStreamProvider.createInputStream());
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);
				
				if (!XppUtil.readToNextElement(xpp, CITIES)) {
					throw new ContentProviderException(INVALID_CONTENT);
				}
	
				XppAttributeProcessor xppAttributeProcessor0 = new XppAttributeProcessor();
				xppAttributeProcessor0.addAll(new String[] { DEFAULT });
				xppAttributeProcessor0.processNode(xpp);

				progressDispatcher.fireEvent(
						CityConstants.ON_DEFAULT_CITY, 
						xppAttributeProcessor0.attrExists(DEFAULT) ? 
								xppAttributeProcessor0.getAttrValueAsString(DEFAULT) : 
								null );
				
				XppAttributeProcessor cityXppAttributeProcessor = new XppAttributeProcessor();
				cityXppAttributeProcessor.addAll(new String[] { 
					KEY, NAME, SERVICE_NAME, TYPE, ACTIVE, WEB_SITE, 
					STATION_DETAILS, STATION_LIST, BONUS, TPE, STATE
				});

				XppAttributeProcessor quartierXppAttributeProcessor = new XppAttributeProcessor();
				quartierXppAttributeProcessor.addAll(new String[] { 
					ID, NAME, ZIP_CODE, CITY
				});

				while (XppUtil.readToNextElement(xpp, CITY, false)) {
					if (cancel) {
						progressDispatcher.fireEvent(EventType.ON_CANCEL);
						return ;
					}
					City city = new City();
					cityXppAttributeProcessor.processNode(xpp);

					city.key = cityXppAttributeProcessor.getAttrValueAsString(KEY);
					city.name = cityXppAttributeProcessor.getAttrValueAsString(NAME);
					city.serviceName = cityXppAttributeProcessor.getAttrValueAsString(SERVICE_NAME);
					city.type = cityXppAttributeProcessor.getAttrValueAsString(TYPE);
					city.active = cityXppAttributeProcessor.getAttrValueAsBoolean(ACTIVE);
					city.webSite = cityXppAttributeProcessor.getAttrValueAsString(WEB_SITE);
					city.bonus = cityXppAttributeProcessor.getAttrValueAsBoolean(BONUS);
					city.tpe = cityXppAttributeProcessor.getAttrValueAsBoolean(TPE);
					city.state = cityXppAttributeProcessor.getAttrValueAsBoolean(STATE);
					city.stationList = cityXppAttributeProcessor.getAttrValueAsString(STATION_LIST);
					city.stationDetails = cityXppAttributeProcessor.getAttrValueAsString(STATION_DETAILS);
					
					if (XppUtil.readNextElement(xpp) && xpp.getName().equals(QUARTIERS)) {
						while (XppUtil.readNextElement(xpp) && xpp.getName().equals(QUARTIER)) {
							Quartier quartier = new Quartier();
							quartierXppAttributeProcessor.processNode(xpp);
							
							quartier.id = quartierXppAttributeProcessor.getAttrValueAsInt(ID);
							quartier.city = quartierXppAttributeProcessor.getAttrValueAsString(CITY);
							quartier.name = quartierXppAttributeProcessor.getAttrValueAsString(NAME);
							quartier.zipCode = quartierXppAttributeProcessor.getAttrValueAsString(ZIP_CODE);
							city.quartierList.addElement(quartier);
						}
					}
					
					cityList.addElement(city);
					progressDispatcher.fireEvent(CityConstants.ON_CITY_LOADED, city);				
				}
			}
			finally {
				cityInputStreamProvider.dispose();
			}
			progressDispatcher.fireEvent(CityConstants.ON_CITIES_LOADED);				
			progressDispatcher.fireEvent(EventType.ON_SUCCESS, cityList);
		}
		catch (Throwable t) {
    		log.warn(t);
			progressDispatcher.fireEvent(EventType.ON_ERROR, t);
		}
	}

	public void cancel() {
		cancel = true;
	}

	public String getDescription() {
		return "Fetchs city configurations from path: '" + cityContentAccessor.getPath() + "'";
	}

}
