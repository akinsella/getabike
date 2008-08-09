package org.helyx.app.j2me.lib.ui.view.transition;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.ui.view.AbstractView;

public interface IViewTransition {
	
	void doTransition(Graphics graphics, AbstractView srcAbstractCanvas, AbstractView targetAbstractCanvas);
	
}
