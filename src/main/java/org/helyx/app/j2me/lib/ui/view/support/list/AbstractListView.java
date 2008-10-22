package org.helyx.app.j2me.lib.ui.view.support.list;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.graphics.Shade;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.lib.ui.view.AbstractView;

public abstract class AbstractListView extends AbstractView {

	private static final Log log = LogFactory.getLog("ABSTRACT_LIST_VIEW");

	protected IElementProvider elementProvider = null;
	
	protected int selectedOffset = 0;
	protected int topOffset = 0;
	protected int visibleItemCount = 0;

	public AbstractListView(AbstractMIDlet midlet, String title) {
		super(midlet);
		this.title = title;
		init();
	}

	private void init() {
		setFullScreenMode(true);
		setTitle(title);
	}
	
	protected abstract void initActions();
	
	protected abstract void initData();
	
	protected abstract void initComponents();


	public void setItems(IElementProvider elementProvider) {
		this.elementProvider = elementProvider;
		repaint();
	}
	
	protected boolean isItemArrayEmpty() {
		int length = elementProvider == null ? 0 : elementProvider.length();
		boolean isEmpty = length == 0;
		
		return isEmpty;
	}
	
	protected int getItemArrayLength() {
		int length = elementProvider == null ? 0 : elementProvider.length();
		
		return length;
	}
	
	protected int getSelectedOffset() {
		return selectedOffset;
	}
	
	protected int getTopOffset() {
		return topOffset;
	}
	
	protected int getVisibleItemCount() {
		return visibleItemCount;
	}
	
	protected IElementProvider getElementProvider() {
		return elementProvider;
	}
	
	protected void setElementProvider(IElementProvider elementprovider) {
		this.elementProvider = elementprovider;
		repaint();
	}
	
	protected void resetPosition() {
		selectedOffset = 0;
		topOffset = 0;
	}

	protected void paint(Graphics g) {
		int length = getItemArrayLength();
		boolean isEmpty = isItemArrayEmpty();
		
		if (!isEmpty && topOffset > length) {
			topOffset = length - 1;
		}
		if (!isEmpty && selectedOffset > length) {
			selectedOffset = length - 1;
		}
			
		Rectangle clientArea = computeClientArea(g);
		
        g.setFont(FontUtil.SMALL);
        
        if (isEmpty) {
        	g.drawString("Aucun élément chargé", clientArea.location.x + clientArea.size.width / 2, clientArea.location.y + clientArea.size.height / 2, Graphics.HCENTER | Graphics.BASELINE);
        	return ;
        }
        
        int smallFontHeight = FontUtil.SMALL.getHeight();
        int smallBoldFontHeight = FontUtil.SMALL_BOLD.getHeight();
        
        int itemHeight = 1 + smallFontHeight + 1 + smallBoldFontHeight + 1 + 1;
        
        visibleItemCount = clientArea.size.height / itemHeight;

        int elementProviderLength = elementProvider.length();
        for (int i = 0 ; i < visibleItemCount + 1 ; i++) {
        	int y = clientArea.location.y + i * itemHeight;
        	Rectangle itemClientArea = new Rectangle(clientArea.location.x, y, clientArea.size.width, itemHeight);
    		
        	int offset = topOffset + i;
        	if (offset >= elementProviderLength) {
    			break;
    		}
        	paintItem(g, offset, itemClientArea, elementProvider.get(offset));
        }
        paintScrollBar(g, length, topOffset, selectedOffset, visibleItemCount, clientArea);
	}
	
	private void paintScrollBar(Graphics g, int length, int topOffset, int selectedOffset, int visibleItemCount, Rectangle clientArea) {
		if (length == 0 || length <= visibleItemCount) {
			return;
		}
		Color scrollBackgroundColor = getTheme().getColor(ThemeConstants.WIDGET_SCROLL_BACKGROUND);
		g.setColor(scrollBackgroundColor.intValue());
		g.fillRect(clientArea.location.x + clientArea.size.width - 4, clientArea.location.y, 4, clientArea.size.height);
		
		int yHeight = Math.max(clientArea.size.height / 8, clientArea.size.height * visibleItemCount / length);
		int yPos = clientArea.location.y + (int)((clientArea.size.height - yHeight) * ((double)selectedOffset / ((double)length - 1)));
		
		Color shadeColor1 = getTheme().getColor(ThemeConstants.WIDGET_SCROLL_SHADE_DARK);
		Color shadeColor2 = getTheme().getColor(ThemeConstants.WIDGET_SCROLL_SHADE_LIGHT);
		Shade shade = new Shade(shadeColor1.intValue(), shadeColor2.intValue());
		Rectangle area = new Rectangle(clientArea.location.x + clientArea.size.width - 4, yPos, 4, yHeight);
		GraphicsUtil.fillShade(g, area, shade, true);

		Color scrollBorderColor = getTheme().getColor(ThemeConstants.WIDGET_SCROLL_BORDER);
		g.setColor(scrollBorderColor.intValue());
		g.drawLine(clientArea.location.x + clientArea.size.width - 5, clientArea.location.y, clientArea.location.x + clientArea.size.width - 5, clientArea.location.y + clientArea.size.height);
	}
	
	protected boolean isItemSelected(int offset) {
    	boolean isSelected = selectedOffset == offset;
    	return isSelected;
	}

	protected void paintItem(Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {
    	
		boolean isSelected = isItemSelected(offset);
		
    	if (isSelected) {
//    		g.setColor(ColorUtil.WIDGET_LIST_FONT_SELECTED);
    		Color shadeColor1 = getTheme().getColor(ThemeConstants.WIDGET_LIST_SELECTED_SHADE_LIGHT);
    		Color shadeColor2 = getTheme().getColor(ThemeConstants.WIDGET_LIST_SELECTED_SHADE_DARK);
    		Shade shade = new Shade(shadeColor1.intValue(), shadeColor2.intValue());
    		GraphicsUtil.fillShade(g, itemClientArea, shade, false);
     	}
    	else {
    		Color listColor = getTheme().getColor(ThemeConstants.WIDGET_LIST);
    		g.setColor(listColor.intValue());
    		g.fillRect(itemClientArea.location.x, itemClientArea.location.y, itemClientArea.size.width, itemClientArea.size.height);
    	}
    	
		Color listSeparatorColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_SEPARATOR);
    	g.setColor(listSeparatorColor.intValue());
    	g.drawLine(itemClientArea.location.x, itemClientArea.location.y + itemClientArea.size.height - 1, itemClientArea.location.x + itemClientArea.size.width, itemClientArea.location.y + itemClientArea.size.height - 1);
	
     	if (isSelected) {
    		Color listFontSelectedColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SELECTED);
    		g.setColor(listFontSelectedColor.intValue());
    	}
    	else {
    		Color listFontColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT);
    		g.setColor(listFontColor.intValue());
    	}
	}

	protected void onKeyPressed(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
		log.debug("[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
	    if (gameAction == GameCanvas.DOWN) {
	    	scrollDown();
	    }
	    else if (gameAction == GameCanvas.UP) {
			scrollUp();
		}
	    else if (gameAction == GameCanvas.LEFT) {
			returnToPreviousDisplayable();
		}
	    else if (gameAction == GameCanvas.RIGHT) {
			onRightKeyPressed();
		}
	    else if (gameAction == GameCanvas.FIRE) {
			onFirePressed();
		}
	}

	protected void onRightKeyPressed() {
		showCurrentItemSelected();
	}

	protected void onFirePressed() {
		showCurrentItemSelected();
	}
	
	private void showCurrentItemSelected() {
		int length = elementProvider == null ? 0 : elementProvider.length();
		boolean isEmpty = length == 0;
		
		if (!isEmpty) {
			onShowItemSelected(elementProvider.get(selectedOffset));
		}
	}
	
	protected void onShowItemSelected(Object object) {
		// TODO Auto-generated method stub
		
	}

	protected void onKeyRepeated(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
		log.debug("[onKeyRepeated] gameAction: " + gameAction + ", keyCode: " + keyCode);
	    if (gameAction == GameCanvas.DOWN) {
	    	scrollDown();
	    }
	    else if (gameAction == GameCanvas.UP) {
			scrollUp();
		}
	}

	private void scrollUp() {
		if (selectedOffset > 0) {
			selectedOffset--;
		}
		if (selectedOffset < topOffset) {
			topOffset = selectedOffset;
		}
		log.debug("Scroll Up - topOffset: " + topOffset + ", selectedOffset: " + selectedOffset);
		onScrollDown();
		repaint();
	}

	protected void onScrollUp() {
		// TODO Auto-generated method stub
		
	}

	private void scrollDown() {
		int length = elementProvider == null ? 0 : elementProvider.length();
		if (selectedOffset + 1 < length) {
			selectedOffset++;
		}
		if (length <= visibleItemCount) {
			topOffset = 0;
		}
		else {
			if (topOffset >= length - visibleItemCount) {
				topOffset = length - visibleItemCount;
			}
			if (topOffset + visibleItemCount <= selectedOffset) {
				topOffset = selectedOffset - visibleItemCount + 1;
			}	
		}
		onScrollDown();
		log.debug("Scroll Down - topOffset: " + topOffset + ", selectedOffset: " + selectedOffset + ", visibleItemCount: " + visibleItemCount + ", length: " + length);
		repaint();
	}

	protected void onScrollDown() {
		
	}

}
