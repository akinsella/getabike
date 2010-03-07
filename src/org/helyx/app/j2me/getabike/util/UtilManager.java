package org.helyx.app.j2me.getabike.util;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.ui.theme.AppThemeConstants;
import org.helyx.app.j2me.getabike.lib.map.google.GoogleMapsView;
import org.helyx.app.j2me.getabike.lib.map.google.POIInfoAccessor;
import org.helyx.app.j2me.getabike.lib.model.list.IElementProvider;
import org.helyx.app.j2me.getabike.lib.pref.Pref;
import org.helyx.app.j2me.getabike.lib.pref.PrefHelper;
import org.helyx.app.j2me.getabike.lib.pref.PrefManager;
import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.getabike.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback.OkCancelResultCallback;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback.YesNoResultCallback;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.level.LevelInformationHolder;
import org.helyx.logging4me.level.LevelSwitcher;

public class UtilManager {
	
	private static final Logger logger = Logger.getLogger("UTIL_MANAGER");

	public static final String GOOGLE_MAPS_KEY = "GOOGLE_MAPS_KEY";

	public static final String DEFAULT_GOOGLE_MAPS_KEY = "ABQIAAAAm-1Nv9nWhCjQxC3a4v-lBBR9EQxSSSz8gi3qW-McXAXnYGN8YxSBOridiu5I3THzW-_0oY2DecnShA";
	
	private static LevelInformationHolder levelInformationHolder;
	
	private static boolean debugModeActive = false;
	
	private static String poiImgClasspath;
	private static String poiSelectedImgClasspath;
	
	public static void showGoogleMapsView(final AbstractView view, final String title, final POIInfoAccessor poiInfoAccessor, final Object elementSelected, final IElementProvider elementProvider, final int zoom) {
		try {
			poiImgClasspath = view.getTheme().getString(AppThemeConstants.MAP_POI_IMAGE_PATH);
			poiSelectedImgClasspath = view.getTheme().getString(AppThemeConstants.MAP_POI_SELECTED_IMAGE_PATH);
		}
		catch (Throwable t) {
			logger.warn(t);
		}

		Pref showGoogleMapsPref = PrefManager.readPref(PrefConstants.MAP_MODE_ENABLED);
		if (showGoogleMapsPref == null) {
			DialogUtil.showYesNoDialog(
				view, 
				"manager.util.google.maps", 
				view.getMessage("manager.util.google.maps.message"),
				new YesNoResultCallback() {
					public void onYes(DialogView dialogView, Object data) {
						PrefManager.writePrefBoolean(PrefConstants.MAP_MODE_ENABLED, true);
						showGoogleMapsViewInternal(view, title, poiInfoAccessor, elementSelected, elementProvider, zoom);
					}
		
					public void onNo(DialogView dialogView, Object data) {
						PrefManager.writePrefBoolean(PrefConstants.MAP_MODE_ENABLED, false);
						dialogView.showDisplayable(view);
					}
				});
		}
		else {
			if (PrefHelper.readPrefBoolean(showGoogleMapsPref)) {
				showGoogleMapsViewInternal(view, title, poiInfoAccessor, elementSelected, elementProvider, zoom);				
			}
			else {
				DialogUtil.showAlertMessage(view, "dialog.title.information", view.getMessage("manager.util.google.maps.message.2"));				
			}
		}
	}
	
	
	public static void showGoogleMapsViewInternal(final AbstractView view, String title, POIInfoAccessor poiInfoAccessor, Object elementSelected, IElementProvider elementProvider, int zoom) {
		String googleMapsKey = PrefManager.readPrefString(UtilManager.GOOGLE_MAPS_KEY);

		GoogleMapsView googleMapView = new GoogleMapsView(view.getMidlet(), title, googleMapsKey, poiInfoAccessor, poiInfoAccessor.getLocalization(elementSelected), zoom, poiImgClasspath, poiSelectedImgClasspath);

		googleMapView.setPreviousDisplayable(view);
		googleMapView.setSelectedPoi(elementSelected);
		googleMapView.setPoiItems(elementProvider);
		googleMapView.showDisplayable(googleMapView);
		googleMapView.updateMap();
	}

	public static void changeDebugMode(final AbstractDisplayable currentDisplayable) {

		DialogUtil.showYesNoDialog(currentDisplayable, "dialog.title.question", 
			currentDisplayable.getMessage(!debugModeActive ? "manager.util.debug.mode.message.activate" : "manager.util.debug.mode.message.deactivate"),
			new YesNoResultCallback() {
				public void onYes(DialogView dialogView, Object data) {
					try {
						if (!debugModeActive) {
							debugModeActive = true;
							levelInformationHolder = LevelSwitcher.getLevelInformations();
							LevelSwitcher.switchToLevel(Logger.DEBUG);
						}
						else if (debugModeActive && levelInformationHolder != null) {
							try {
								debugModeActive = false;
								LevelSwitcher.restoreLevel(levelInformationHolder);
							}
							finally {
								levelInformationHolder.dispose();
								levelInformationHolder = null;
							}
						}
					}
					catch(Throwable t) {
						logger.warn(t);
					}
					
					dialogView.showDisplayable(currentDisplayable);
				}
				public void onNo(DialogView dialogView, Object data) {
					dialogView.showDisplayable(currentDisplayable);
				}
			});
	}

	public static void changeMapMode(final AbstractDisplayable currentDisplayable) {
		final boolean mapModeActive = PrefManager.readPrefBoolean(PrefConstants.MAP_MODE_ENABLED);

		DialogUtil.showYesNoDialog(currentDisplayable,  "dialog.title.question", 
				
			currentDisplayable.getMessage(!mapModeActive ? "manager.util.map.mode.message.activate" : "manager.util.map.mode.message.deactivate"), 
			new YesNoResultCallback() {
				public void onYes(DialogView dialogView, Object data) {
					PrefManager.writePrefBoolean(PrefConstants.MAP_MODE_ENABLED, !mapModeActive);
					dialogView.showDisplayable(currentDisplayable);
				}

				public void onNo(DialogView dialogView, Object data) {
					dialogView.showDisplayable(currentDisplayable);
				}
			});
	}

	public static void changeGeoLocMode(final AbstractDisplayable currentDisplayable) {
		changeGeoLocMode(currentDisplayable, null);
	}

	public static void changeGeoLocMode(final AbstractDisplayable currentDisplayable, final IReturnCallback returnCallback) {

		DialogUtil.showYesNoDialog(currentDisplayable,  "dialog.title.question", 
				
			currentDisplayable.getMessage("view.menu.item.location.cost.allow"), 
			new YesNoResultCallback() {
				public void onYes(DialogView dialogView, Object data) {
					PrefManager.writePrefBoolean(PrefConstants.COST_ALLOWED_GEO_LOCALIZATION, true);
					
					if (returnCallback != null) {
						returnCallback.onReturn(currentDisplayable, null);
					}
					else {
						dialogView.showDisplayable(currentDisplayable);
					}
				}

				public void onNo(DialogView dialogView, Object data) {
					PrefManager.writePrefBoolean(PrefConstants.COST_ALLOWED_GEO_LOCALIZATION, false);
					if (returnCallback != null) {
						returnCallback.onReturn(currentDisplayable, null);
					}
					else {
						dialogView.showDisplayable(currentDisplayable);
					}
				}
			});

	}

	public static void changeHttpMode(final AbstractDisplayable currentDisplayable) {
		final boolean httpModeActive = PrefManager.readPrefBoolean(PrefConstants.OPTIMIZED_HTTP_MODE_ENABLED);

		DialogUtil.showYesNoDialog(currentDisplayable, "dialog.title.question", 
				currentDisplayable.getMessage(!httpModeActive ? "manager.util.http.mode.message.activate" : "manager.util.http.mode.message.deactivate"), 			
				new YesNoResultCallback() {
					public void onYes(DialogView dialogView, Object data) {
						PrefManager.writePrefBoolean(PrefConstants.OPTIMIZED_HTTP_MODE_ENABLED, !httpModeActive);
						dialogView.showDisplayable(currentDisplayable);
					}

					public void onNo(DialogView dialogView, Object data) {
						dialogView.showDisplayable(currentDisplayable);
					}
				});

	}

	public static void reset(final AbstractDisplayable currentDisplayable) {
		PrefManager.writePrefBoolean(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, true);
		DialogUtil.showConfirmDialog(currentDisplayable, "dialog.title.confirm", 
			currentDisplayable.getMessage("manager.util.reset.message.1"), 
			new OkCancelResultCallback() {
				public void onOk(DialogView dialogView, Object data) {
					DialogUtil.showMessageDialog(currentDisplayable, "dialog.title.warn", 
						currentDisplayable.getMessage("manager.util.reset.message.2"), 
						new OkResultCallback() {
							public void onOk(DialogView dialogView, Object data) {
								currentDisplayable.getMidlet().exit();								
							}
						});
				}

				public void onCancel(DialogView dialogView, Object data) {
					dialogView.showDisplayable(currentDisplayable);
				}
			});

	}

	public static boolean isDebugModeActive() {
		return debugModeActive;
	}

	
}
