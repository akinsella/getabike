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
import org.helyx.app.j2me.getabike.util.ErrorManager;
import org.helyx.basics4me.util.Properties;
import org.helyx.helyx4me.concurrent.Future;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.displayable.callback.BasicReturnCallback;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.YesNoResultCallback;
import org.helyx.helyx4me.ui.view.support.task.LoadTaskView;
import org.helyx.logging4me.Logger;

public class AppManager {
	
	private static final Logger logger = Logger.getLogger("APP_MANAGER");
	
	private static final String APPLICATION_UID = "GETABIKE";
	private static final String APPLICATION_DATA_URL = "http://m.helyx.org/getabike/data/config.xml?key=${app.key}&amp;version=${app.version}&amp;uuid=${app.uuid}";
	private static final String APPLICATION_UPDATE_CHECK_URL = "http://m.helyx.org/getabike/binaries/release.properties?key=${app.key}&amp;version=${app.version}&amp;uuid=${app.uuid}";
	
	private static final String APP_RELEASE_LAST_VERSION = "release.last.version";
	private static final String APP_RELEASE_LAST_DATE = "release.last.date";
	private static final String APP_RELEASE_LAST_URL = "release.last.url";
	
	private static Properties applicationProperties;
	
	private AppManager() {
		super();
	}

	public static boolean checkUpdateApplication(final AbstractView view, final boolean silent, boolean checkAppStats) {

		try {
			if (checkAppStats) {
				if (logger.isDebugEnabled()) {
					logger.debug("Checking using app stats");
				}
				long updateRunTimestamp = 0;
				try { updateRunTimestamp = Long.parseLong(PrefManager.readPrefString(PrefConstants.UPDATE_RUN_TIMESTAMP)); } catch(Throwable t) { logger.info("No 'RUN_COUNT' information"); }

				long now = System.currentTimeMillis();
				if (now - updateRunTimestamp > 7 * 24 * 60 * 60 * 1000) {
					if (logger.isDebugEnabled()) {
						logger.debug("Update check is needed");
					}
					PrefManager.writePref(PrefConstants.UPDATE_RUN_TIMESTAMP, String.valueOf(now));
				}
				else {
					return false;
				}				
			}
			
			String propertiesUrl = APPLICATION_UPDATE_CHECK_URL;
			IContentAccessor httpContentAccessor = new HttpGetABikeContentAccessor(propertiesUrl, true);
			IContentProvider propertiesContentProvider = new PropertiesContentProvider(httpContentAccessor);
			IProgressTask propertiesContentProviderProgressTask = new ContentProviderProgressTaskAdapter(propertiesContentProvider);
			final LoadTaskView loadTaskView = new LoadTaskView(view.getMidlet(), "view.station.detail.load.message", propertiesContentProviderProgressTask);
			loadTaskView.setReturnCallback(new BasicReturnCallback(view));
			propertiesContentProviderProgressTask.addProgressListener(new ProgressAdapter("Loading station details") {

				public void onError(String eventMessage, Object eventData) {
					if (AppManager.logger.isInfoEnabled()) {
						AppManager.logger.info("Error: " + eventMessage + ", data: " + eventData);
					}
					
					Throwable t = (Throwable)eventData;
					AppManager.logger.warn(t);
					if (!silent) {
						DialogUtil.showMessageDialog(
								view, 
								"dialog.title.error", 
								view.getMessage("connection.error") + ": " + ErrorManager.getErrorMessage(view.getMidlet(), t), 
								new OkResultCallback() {
									public void onOk(DialogView dialogView, Object data) {
										loadTaskView.fireReturnCallback();
									}
								});
					}
					else {
						loadTaskView.fireReturnCallback();
					}
				}

				public void onSuccess(String eventMessage, Object eventData) {
					try {
						final Properties updateProperties = (Properties)eventData;
						AppManager.logger.info(updateProperties);
						if (updateProperties.containsKey(APP_RELEASE_LAST_VERSION) && updateProperties.containsKey(APP_RELEASE_LAST_URL)) {
							String appReleaseLastVersionStr = updateProperties.getProperty(APP_RELEASE_LAST_VERSION);
							String currentVersionStr = PrefManager.readPrefString(PrefConstants.APP_VERSION);
							Version appReleaseLastVersion = new Version(appReleaseLastVersionStr);
							Version currentVersion = new Version(currentVersionStr);
							VersionComparator vc = new VersionComparator();
							if (vc.compare(appReleaseLastVersion, currentVersion) > 0) {
								DialogUtil.showYesNoDialog(view, "dialog.title.question", view.getMessage("manager.app.check.update.question", new String[] { appReleaseLastVersionStr, currentVersionStr }), new YesNoResultCallback() {
									
									public void onYes(DialogView dialogView, Object data) {
										
										try {
											String targetUrl = updateProperties.getProperty(APP_RELEASE_LAST_URL);
											view.getMidlet().platformRequest(targetUrl);
											view.getMidlet().exit();
										}
										catch(Throwable t) {
											AppManager.logger.warn(t);
											if (!silent) {
												DialogUtil.showMessageDialog(
													view, 
													"dialog.title.error", 
													view.getMessage("dialog.error.unexpected") + ": " + ErrorManager.getErrorMessage(view.getMidlet(), t),
													new OkResultCallback() {
														
														public void onOk(DialogView dialogView, Object data) {
															loadTaskView.fireReturnCallback();
														}
													});
											}
											else {
												loadTaskView.fireReturnCallback();
											}
										}
									}
									
									public void onNo(DialogView dialogView, Object data) {
											DialogUtil.showMessageDialog(
												view, 
												"dialog.title.information", 
												view.getMessage("manager.app.check.update.another.time"),
												new OkResultCallback() {
													
													public void onOk(DialogView dialogView, Object data) {
														loadTaskView.fireReturnCallback();
													}
												});
									}
								});
							}
							else {
								if (!silent) {
									DialogUtil.showMessageDialog(
											view, 
											"dialog.title.information", 
											view.getMessage("manager.app.check.update.no.new.version"),
											new OkResultCallback() {
												
												public void onOk(DialogView dialogView, Object data) {
													loadTaskView.fireReturnCallback();
												}
											});
								}
								else {
									loadTaskView.fireReturnCallback();
								}
							}
						}
						else {
							if (!silent) {
							DialogUtil.showMessageDialog(
									view, 
									"dialog.title.error", 
									view.getMessage("manager.app.check.update.recheck.later"),
									new OkResultCallback() {
										
										public void onOk(DialogView dialogView, Object data) {
											loadTaskView.fireReturnCallback();
										}
									});
							}
							else {
								loadTaskView.fireReturnCallback();
							}
						}
					}
					catch (Throwable t) {
						AppManager.logger.warn(t);
						if (!silent) {
							DialogUtil.showMessageDialog(
									view, 
									"dialog.title.error", 
									view.getMessage("dialog.error.unexpected") + ": " + ErrorManager.getErrorMessage(view.getMidlet(), t),
									new OkResultCallback() {
										
										public void onOk(DialogView dialogView, Object data) {
											loadTaskView.fireReturnCallback();
										}
									});
						}
						else {
							loadTaskView.fireReturnCallback();							
						}
					}
				}
				
			});
			view.showDisplayable(loadTaskView);
			loadTaskView.startTask();
			return true;
		}
		catch (Throwable t) {
			logger.warn(t);
			DialogUtil.showAlertMessage(
					view, 
					"dialog.title.error", 
					view.getMessage("dialog.error.unexpected") + ": " + ErrorManager.getErrorMessage(view.getMidlet(), t));
		}
		return false;
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
			
			String appVersionStr = PrefManager.readPrefString(PrefConstants.APP_VERSION);
			Version appVersion = new Version(appVersionStr);
			
			String propertiesUrl = getPropertiesUrl(configurationMetaData, appVersion);
			IContentAccessor httpContentAccessor = new HttpGetABikeContentAccessor(propertiesUrl, true);
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
