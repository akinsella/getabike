package org.helyx.app.j2me.lib.ui.displayable.transition;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.ui.displayable.AbstractView;

public interface IViewTransition {
	
	void doTransition(Graphics graphics, AbstractView srcAbstractCanvas, AbstractView targetAbstractCanvas);
	
}
