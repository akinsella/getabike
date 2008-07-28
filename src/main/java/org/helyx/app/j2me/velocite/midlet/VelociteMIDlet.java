package org.helyx.app.j2me.velocite.midlet;

import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.velocite.view.SplashScreenView;


public class VelociteMIDlet extends AbstractMIDlet {

	private static final String CAT = "VELOCITE_MIDLET";
	
	public VelociteMIDlet() {
		super();
	}

	protected void onStart() {
		SplashScreenView splashScreenView = new SplashScreenView(this);
		splashScreenView.show();	
	}

}
