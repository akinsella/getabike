package org.helyx.app.j2me.velocite.util;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.ui.theme.AppThemeConstants;
import org.helyx.helyx4me.map.google.GoogleMapsView;
import org.helyx.helyx4me.map.google.POIInfoAccessor;
import org.helyx.helyx4me.pref.Pref;
import org.helyx.helyx4me.pref.PrefHelper;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.OkCancelResultCallback;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.YesNoResultCallback;
import org.helyx.helyx4me.ui.view.support.list.IElementProvider;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.level.LevelInformationHolder;
import org.helyx.logging4me.level.LevelSwitcher;

public class UtilManager {
	
	private static final Logger logger = Logger.getLogger("UTIL_MANAGER");

	public static final String DEBUG_MODE_ENABLED = "DEBUG_MODE_ENABLED";
	public static final String OPTIMIZED_HTTP_MODE_ENABLED = "OPTIMIZED_HTTP_MODE_ENABLED";
	public static final String MAP_MODE_ENABLED = "MAP_MODE_ENABLED";

	public static final String GOOGLE_MAPS_KEY = "GOOGLE_MAPS_KEY";

	public static final String DEFAULT_GOOGLE_MAPS_KEY = "ABQIAAAAm-1Nv9nWhCjQxC3a4v-lBBR9EQxSSSz8gi3qW-McXAXnYGN8YxSBOridiu5I3THzW-_0oY2DecnShA";

	public static final String PRE_LOAD_MODE_ENABLED = "PRE_LOAD_MODE_ENABLED";
	
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

		Pref showGoogleMapsPref = PrefManager.readPref(UtilManager.MAP_MODE_ENABLED);
		if (showGoogleMapsPref == null) {
			DialogUtil.showYesNoDialog(
				view, 
				"Google Maps", 
				"Activer la visualisation des stations avec Google Maps ?\n\nAttention: Un forfait data illimit� est  fortement recommand�.",
				new YesNoResultCallback() {
					public void onYes(DialogView dialogView, Object data) {
						PrefManager.writePrefBoolean(UtilManager.MAP_MODE_ENABLED, true);
						showGoogleMapsViewInternal(view, title, poiInfoAccessor, elementSelected, elementProvider, zoom);
					}
		
					public void onNo(DialogView dialogView, Object data) {
						PrefManager.writePrefBoolean(UtilManager.MAP_MODE_ENABLED, false);
						dialogView.showDisplayable(view);
					}
				});
		}
		else {
			if (PrefHelper.readPrefBoolean(showGoogleMapsPref)) {
				showGoogleMapsViewInternal(view, title, poiInfoAccessor, elementSelected, elementProvider, zoom);				
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

		DialogUtil.showYesNoDialog(currentDisplayable, "Question", 
			(!debugModeActive ? "Activer" : "D�sactiver") + " le mode Debug ?",
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
		final boolean mapModeActive = PrefManager.readPrefBoolean(MAP_MODE_ENABLED);

		DialogUtil.showYesNoDialog(currentDisplayable,  "Question", 
			(!mapModeActive ? "Activer" : "D�sactiver") + " la visualisation des informations de station avec Google Maps ?" + (!mapModeActive ? "\n\nAttention: Un forfait data illimit� est fortement recommand�." : ""), 
			new YesNoResultCallback() {
				public void onYes(DialogView dialogView, Object data) {
					PrefManager.writePrefBoolean(MAP_MODE_ENABLED, !mapModeActive);
					dialogView.showDisplayable(currentDisplayable);
				}

				public void onNo(DialogView dialogView, Object data) {
					dialogView.showDisplayable(currentDisplayable);
				}
			});

	}

	public static void changeHttpMode(final AbstractDisplayable currentDisplayable) {
		final boolean httpModeActive = PrefManager.readPrefBoolean(OPTIMIZED_HTTP_MODE_ENABLED);

		DialogUtil.showYesNoDialog(currentDisplayable,  "Question", 
				!httpModeActive ? 
					"Activer le mode optimis� de chargement des donn�es HTTP ?\nAttention: Certains t�l�phone peuvent rencontrer des probl�mes avec ce mode!" : 
					"D�sactiver le mode optimis� HTTP ?"
				, 			
				new YesNoResultCallback() {
					public void onYes(DialogView dialogView, Object data) {
						PrefManager.writePrefBoolean(OPTIMIZED_HTTP_MODE_ENABLED, !httpModeActive);
						dialogView.showDisplayable(currentDisplayable);
					}

					public void onNo(DialogView dialogView, Object data) {
						dialogView.showDisplayable(currentDisplayable);
					}
				});

	}

	public static void reset(final AbstractDisplayable currentDisplayable) {
		PrefManager.writePrefBoolean(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, true);
		DialogUtil.showConfirmDialog(currentDisplayable, "Confirmation", 
			"Etes-vous sur de vouloir reseter l'application ?", 
			new OkCancelResultCallback() {
				public void onOk(DialogView dialogView, Object data) {
					DialogUtil.showMessageDialog(currentDisplayable, "Attention", 
						"L'application va quitter. L'application doit �tre relanc�e.", 
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
