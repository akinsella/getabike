package org.helyx.app.j2me.velocite.data.city.provider;

import java.io.InputStream;

import org.helyx.app.j2me.lib.constant.EncodingConstants;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.content.provider.ContentProviderException;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.xml.XppAttributeProcessor;
import org.helyx.app.j2me.lib.xml.XppUtil;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.city.CityConstants;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.xmlpull.v1.XmlPullParser;

public class DefaultCityContentProvider extends AbstractContentProvider {
	
	private static final String CAT = "DEFAULT_CITY_CONTENT_PROVIDER";


	private static final String CITIES = "cities";
	private static final String CITY = "city";

	private static final String KEY = "key";
	private static final String NAME = "name";
	private static final String TYPE = "type";
	private static final String DEFAULT = "default";
	private static final String ACTIVE = "active";
	private static final String STATION_LIST = "stationList";
	private static final String STATION_DETAILS = "stationDetails";
	private static final String OFFLINE_STATION_LIST = "offlineStationList";
	private static final String WEB_SITE = "webSite";
	private static final String CONTENT_PROVIDER_FACTORY = "contentProviderFactory";
	
	
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
		
		Log.debug(CAT, "Loading cities informations ...");
		
		InputStream inputStream = null;
		InputStreamProvider cityInputStreamProvider = null;
		
		try {
			progressDispatcher.fireEvent(ProgressEventType.ON_START);
			try {			
				cityInputStreamProvider = cityContentAccessor.getInputStreamProvider();
				inputStream = cityInputStreamProvider.createInputStream();
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);
	
				Log.debug(CAT, "Parsing simple sample XML");
				
				if (!XppUtil.readToNextElement(xpp, CITIES)) {
					throw new ContentProviderException(INVALID_CONTENT);
				}
	
				XppAttributeProcessor xppAttributeProcessor0 = new XppAttributeProcessor();
				xppAttributeProcessor0.addAll(new String[] { DEFAULT });
				xppAttributeProcessor0.processNode(xpp);

				if (!xppAttributeProcessor0.attrExists(DEFAULT)) {
					PrefManager.removePref(PrefConstants.CITY_DEFAULT_KEY);
				}
				else {
					String defaultCityKey = xppAttributeProcessor0.getAttrValueAsString(DEFAULT);
					Log.debug(CAT, "Default city key: '" + defaultCityKey + "'");
					PrefManager.writePref(PrefConstants.CITY_DEFAULT_KEY, defaultCityKey);					
				}
				
				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { 
					KEY, NAME, TYPE, ACTIVE, WEB_SITE, CONTENT_PROVIDER_FACTORY, 
					STATION_DETAILS, STATION_LIST, OFFLINE_STATION_LIST
				});

				while (XppUtil.readToNextElement(xpp, CITY)) {
					if (cancel) {
						progressDispatcher.fireEvent(ProgressEventType.ON_CANCEL);
						return ;
					}
					City city = new City();
					xppAttributeProcessor.processNode(xpp);

					city.key = xppAttributeProcessor.getAttrValueAsString(KEY);
					city.name = xppAttributeProcessor.getAttrValueAsString(NAME);
					city.type = xppAttributeProcessor.getAttrValueAsString(TYPE);
					city.active = xppAttributeProcessor.getAttrValueAsBoolean(ACTIVE);
					city.webSite = xppAttributeProcessor.getAttrValueAsString(WEB_SITE);
					city.contentProviderFactory = xppAttributeProcessor.getAttrValueAsString(CONTENT_PROVIDER_FACTORY);
					city.offlineStationList = xppAttributeProcessor.getAttrValueAsString(OFFLINE_STATION_LIST);
					city.stationList = xppAttributeProcessor.getAttrValueAsString(STATION_LIST);
					city.stationDetails = xppAttributeProcessor.getAttrValueAsString(STATION_DETAILS);
	
					progressDispatcher.fireEvent(CityConstants.ON_CITY_LOADED, city);
				}

			}
			finally {
				cityInputStreamProvider.dispose();
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
		return "Fetchs city configurations from path: '" + cityContentAccessor.getPath() + "'";
	}

}
