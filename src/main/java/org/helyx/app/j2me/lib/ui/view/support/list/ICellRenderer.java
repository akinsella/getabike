package org.helyx.app.j2me.lib.ui.view.support.list;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.ui.geometry.Rectangle;

public interface ICellRenderer {

	public void paintItem(AbstractListView view, Graphics g, int offset, Rectangle itemClientArea, Object itemObject);
	
}
