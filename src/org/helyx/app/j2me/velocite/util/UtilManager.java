package org.helyx.app.j2me.velocite.util;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.view.support.dialog.AbstractDialogResultCallback;
import org.helyx.helyx4me.ui.view.support.dialog.DialogResultConstants;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
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
	
	private static LevelInformationHolder levelInformationHolder;
	
	private static boolean debugModeActive = false;

	public static void changeDebugMode(final AbstractDisplayable currentDisplayable) {

		DialogUtil.showYesNoDialog(currentDisplayable, "Question", (!debugModeActive ? "Activer" : "Désactiver") + " le mode Debug ?", new AbstractDialogResultCallback() {

			public void onResult(DialogView dialogView, Object data) {
				int resultValue = dialogView.getResultCode();
				switch (resultValue) {
					case DialogResultConstants.YES:
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
						break;
					case DialogResultConstants.NO:
						dialogView.showDisplayable(currentDisplayable);
						break;
					default: 
						dialogView.showDisplayable(currentDisplayable);
						break;
				}
			}
		});

	}

	public static void changeMapMode(final AbstractDisplayable currentDisplayable) {
		final boolean mapModeActive = PrefManager.readPrefBoolean(MAP_MODE_ENABLED);

		DialogUtil.showYesNoDialog(currentDisplayable, "Question", (!mapModeActive ? "Activer" : "Désactiver") + " la visualisation des informations de station avec Google Maps ?" + (!mapModeActive ? "\n\nAttention: Un forfait data illimité est fortement recommandé." : ""), new AbstractDialogResultCallback() {

			public void onResult(DialogView dialogView, Object data) {
				int resultValue = dialogView.getResultCode();
				switch (resultValue) {
					case DialogResultConstants.YES:
						PrefManager.writePrefBoolean(MAP_MODE_ENABLED, !mapModeActive);
						
						dialogView.showDisplayable(currentDisplayable);
						break;
					case DialogResultConstants.NO:
						dialogView.showDisplayable(currentDisplayable);
						break;
					default :
						dialogView.showDisplayable(currentDisplayable);
						break;						
				}
			}
		});
	}

	public static void changeHttpMode(final AbstractDisplayable currentDisplayable) {
		final boolean httpModeActive = PrefManager.readPrefBoolean(OPTIMIZED_HTTP_MODE_ENABLED);

		DialogUtil.showYesNoDialog(currentDisplayable, 
				"Question", 
				!httpModeActive ? 
						"Activer le mode optimisé de chargement des données HTTP ?\nAttention: Certains téléphone peuvent rencontrer des problèmes avec ce mode!" : 
						"Désactiver le mode optimisé HTTP ?"
				, new AbstractDialogResultCallback() {

			public void onResult(DialogView dialogView, Object data) {
				int resultValue = dialogView.getResultCode();
				switch (resultValue) {
					case DialogResultConstants.YES:
						PrefManager.writePrefBoolean(OPTIMIZED_HTTP_MODE_ENABLED, !httpModeActive);
						dialogView.showDisplayable(currentDisplayable);
						break;
					case DialogResultConstants.NO:
						dialogView.showDisplayable(currentDisplayable);
						break;
					default :
						dialogView.showDisplayable(currentDisplayable);
						break;						
				}
			}
		});
	}

	public static void reset(final AbstractDisplayable currentDisplayable) {
		PrefManager.writePrefBoolean(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, true);
		DialogUtil.showConfirmDialog(currentDisplayable, "Confirmation", "Etes-vous sur de vouloir reseter l'application ?", new AbstractDialogResultCallback() {

			public void onResult(DialogView dialogView, Object data) {
				int resultValue = dialogView.getResultCode();
				switch (resultValue) {
					case DialogResultConstants.OK:
						DialogUtil.showMessageDialog(currentDisplayable, "Attention", "L'application va quitter. L'application doit être relancée.", new AbstractDialogResultCallback() {

							public void onResult(DialogView dialogView, Object data) {
								currentDisplayable.getMidlet().exit();								
							}
						});

						break;
					case DialogResultConstants.CANCEL:
						dialogView.showDisplayable(currentDisplayable);
						break;
				}
			}
			
		});
	}

	public static boolean isDebugModeActive() {
		return debugModeActive;
	}
	
}
