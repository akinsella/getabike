package org.helyx.app.j2me.getabike.data.app.manager;

import java.util.Enumeration;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.content.accessor.HttpGetABikeContentAccessor;
import org.helyx.app.j2me.getabike.data.app.domain.ConfigurationMetadata;
import org.helyx.app.j2me.getabike.data.app.domain.Version;
import org.helyx.app.j2me.getabike.data.app.domain.VersionComparator;
import org.helyx.app.j2me.getabike.data.app.domain.VersionRange;
import org.helyx.app.j2me.getabike.data.app.exception.ApplicationManagerException;
import org.helyx.app.j2me.getabike.data.app.exception.VersionException;
import org.helyx.app.j2me.getabike.data.app.provider.ApplicationDataContentProvider;
import org.helyx.app.j2me.getabike.data.app.util.PropertiesHelper;
import org.helyx.app.j2me.getabike.data.provider.PropertiesContentProvider;
import org.helyx.app.j2me.getabike.util.UtilManager;
import org.helyx.basics4me.util.Properties;
import org.helyx.helyx4me.concurrent.Future;
import org.helyx.helyx4me.content.accessor.HttpContentAccessor;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.logging4me.Logger;

public class AppManager {
	
	private static final Logger logger = Logger.getLogger("APP_MANAGER");
	
	private static final String APPLICATION_UID = "GETABIKE";
	private static final String APPLICATION_DATA_URL = "http://m.helyx.org/getabike/data/config.xml?uuid=${app.uuid}";
	
	private static Properties applicationProperties;
	
	private AppManager() {
		super();
	}

	public static String getProperty(String key) {
		return getProperties().getProperty(key);
	}
	
	public static Properties getProperties() {
		try {
			if (applicationProperties == null) {
				String applicationPropertiesStr = PrefManager.readPrefString(PrefConstants.APPLICATION_DATA);
				if (applicationPropertiesStr == null) {
					updateConfigurationMetaData();
				}
				else {
					Properties applicationProperties = PropertiesHelper.convertStringToProperties(applicationPropertiesStr);
					AppManager.applicationProperties = applicationProperties;
				}
			}
			
			return applicationProperties;
		}
		catch(ApplicationManagerException ame) {
			logger.warn(ame);
			throw ame;
		}
		catch(Throwable t) {
			logger.warn(t);
			throw new ApplicationManagerException(t);
		}
	}
	
	private static void updateConfigurationMetaData() {
		try {
			IContentAccessor dataCitiesContentAccessor = new HttpGetABikeContentAccessor(APPLICATION_DATA_URL);
			IContentProvider contentProvider = new ApplicationDataContentProvider(dataCitiesContentAccessor);
			IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);
			
			ConfigurationMetadata configurationMetaData = (ConfigurationMetadata)Future.get(progressTask);
			
			String appVersionStr = PrefManager.readPrefString(PrefConstants.MIDLET_VERSION);
			Version appVersion = new Version(appVersionStr);
			
			String propertiesUrl = getPropertiesUrl(configurationMetaData, appVersion);
			IContentAccessor httpContentAccessor = new HttpContentAccessor(propertiesUrl, true);
			IContentProvider propertiesContentProvider = new PropertiesContentProvider(httpContentAccessor);
			IProgressTask propertiesContentProviderProgressTask = new ContentProviderProgressTaskAdapter(propertiesContentProvider);
			
			Properties applicationProperties = (Properties)Future.get(propertiesContentProviderProgressTask);
			String applicationPropertiesStr = PropertiesHelper.convertPropertiesToString(applicationProperties);
			PrefManager.writePref(PrefConstants.APPLICATION_DATA, applicationPropertiesStr);
			AppManager.applicationProperties = applicationProperties;
		}
		catch(Throwable t) {
			logger.warn(t);
			throw new ApplicationManagerException(t);
		}
	}
	
	private static String getPropertiesUrl(ConfigurationMetadata configurationMetaData, Version version) throws VersionException {
		VersionComparator versionComparator = new VersionComparator();
		Enumeration versionRangeEnum = configurationMetaData.getVersionRangeList().elements();
		while(versionRangeEnum.hasMoreElements()) {
			VersionRange versionRange = (VersionRange)versionRangeEnum.nextElement();
			
			if (versionComparator.compare(version, versionRange.getMinVersion()) >= 0 && (versionRange.getMaxVersion() == null || versionComparator.compare(version, versionRange.getMaxVersion()) <= 0)) {
				String propertiesUrl = versionRange.getUrl();
				
				return propertiesUrl;
			}
		}
		
		throw new VersionException("No properties found for version : '" + version.getVersion() + "'");
	}
}
