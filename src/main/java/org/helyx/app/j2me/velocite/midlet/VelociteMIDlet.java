package org.helyx.app.j2me.velocite.midlet;

import org.helyx.app.j2me.lib.concurrent.Future;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.i18n.ClasspathResourceBundleContentProviderFactory;
import org.helyx.app.j2me.lib.i18n.Locale;
import org.helyx.app.j2me.lib.i18n.ResourceBundle;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.theme.Theme;
import org.helyx.app.j2me.velocite.ui.view.SplashScreenView;


public class VelociteMIDlet extends AbstractMIDlet {

	private static final String CAT = "VELOCITE_MIDLET";
	
	public VelociteMIDlet() {
		super();
	}

	protected void onStart() {
		setLocale(Locale.FRANCE);
		setTheme(getDefaultTheme());
		SplashScreenView splashScreenView = new SplashScreenView(this);
		splashScreenView.show();	
	}
	
	protected Theme getDefaultTheme() {
		ClasspathResourceBundleContentProviderFactory cprbcpf = new ClasspathResourceBundleContentProviderFactory(getLocale(), "org.helyx.app.j2me.velocite.theme", "default");
		ResourceBundle resourceBundle = (ResourceBundle)Future.get(new ContentProviderProgressTaskAdapter(cprbcpf.getContentProviderFactory()));
		Theme theme = new Theme(resourceBundle);
		
		return theme;
	}

}
