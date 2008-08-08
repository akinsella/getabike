package org.helyx.app.j2me.velocite.midlet;

import org.helyx.app.j2me.lib.i18n.Locale;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.velocite.ui.view.SplashScreenView;


public class VelociteMIDlet extends AbstractMIDlet {

	private static final String CAT = "VELOCITE_MIDLET";
	
	public VelociteMIDlet() {
		super();
	}

	protected void onStart() {
		setThemeConfiguration("default", "org.helyx.app.j2me.velocite.theme");
		setI18nConfiguration("messages", "org.helyx.app.j2me.velocite.i18n");
		setLocale(Locale.FRANCE);
		SplashScreenView splashScreenView = new SplashScreenView(this);
		splashScreenView.show();	
	}

}
