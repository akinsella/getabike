/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.ui.view.support.list;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.model.list.IElementProvider;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Point;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Shade;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.background.ColorBackgroundPainter;
import org.helyx.app.j2me.getabike.lib.ui.view.support.list.ICellRenderer;
import org.helyx.logging4me.Logger;


public abstract class AbstractListView extends AbstractView {

	private static final Logger logger = Logger.getLogger("ABSTRACT_LIST_VIEW");

	protected IElementProvider elementProvider = null;
	
	protected int selectedOffset = 0;
	protected int topOffset = 0;
	protected int visibleItemCount = 0;
	
	private ICellRenderer cellRenderer;
	
	private boolean preRender = true;
	private boolean separatorVisible = false;

	public AbstractListView(AbstractMIDlet midlet, String title) {
		this(midlet, title, null);
	}
	
	public AbstractListView(AbstractMIDlet midlet, String title, ICellRenderer cellRenderer) {
		super(midlet);
		this.title = title;
		this.cellRenderer = cellRenderer;
		init();
	}

	private void init() {
		setFullScreenMode(true);
		setTitle(title);
		setClientBackgroundPainter(new ColorBackgroundPainter(getTheme().getColor(ThemeConstants.WIDGET_LIST_BACKGROUND).intValue()));
		setPaintScrollBar(true);
		setScreenDragging(true);
		setTopOffset(0);
	}
	
	protected abstract void initActions();
	
	protected abstract void initData();
	
	protected abstract void initComponents();


	public void setItems(IElementProvider elementProvider) {
		this.elementProvider = elementProvider;
		repaint();
	}
	
	public boolean isSeparatorVisible() {
		return separatorVisible;
	}
	
	public void setSeparatorVisible(boolean separatorVisible) {
		this.separatorVisible = separatorVisible;
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
	
	protected void setTopOffset(int topOffset) {
		this.topOffset = topOffset;
	}
	
	protected int getVisibleItemCount() {
		return visibleItemCount;
	}

	public int getScrollMaximum() {
		return getItemArrayLength();
	}
	
	public int getScrollValue() {
		return selectedOffset;
	}
	
	public int getScrollVisible() {
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
		setTopOffset(0);
	}
	
	private int getVisibleItemOffsetByPointer(Point pointPosition) {

		Rectangle clientArea = computeViewPortArea();
		int height = 0;
		for (int offset = topOffset ; offset < topOffset + visibleItemCount + 1 ; offset++) {
			if (offset >= elementProvider.length()) {
				return -1;
			}
			int cellHeight = cellRenderer == null ? getDefaultItemHeight() : cellRenderer.computeHeight(this, elementProvider.get(offset), offset);
			Rectangle cellRect = new Rectangle(clientArea.location.x, clientArea.location.y + height, clientArea.size.width, cellHeight);
			
			if (cellRect.contains(pointPosition)) {
				return offset;
			}
			
			height = height + cellHeight;
		}
		
		return -1;
	}
	
	protected void paint(Graphics g) {
		
		Rectangle screenRect = computeScreenArea();
		Rectangle viewPortRect = computeViewPortArea();
		
		g.setClip(viewPortRect.location.x, viewPortRect.location.y, viewPortRect.size.width, viewPortRect.size.height);

		try {
			int length = getItemArrayLength();
			boolean isEmpty = isItemArrayEmpty();
			
			if (!isEmpty && topOffset > length) {
				setTopOffset(length - 1);
			}
			if (!isEmpty && selectedOffset > length) {
				selectedOffset = length - 1;
			}
				
			Rectangle clientArea = computeViewPortArea();
			
	        if (isEmpty) {
	        	g.setColor(getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT).intValue());
	            g.setFont(FontUtil.SMALL);
	        	g.drawString(getMessage("view.list.abstract.no.element"), clientArea.location.x + clientArea.size.width / 2, clientArea.location.y + clientArea.size.height / 2 - FontUtil.SMALL.getHeight() / 2, Graphics.HCENTER | Graphics.TOP);
	        	return ;
	        }
	        
	        Rectangle itemsClientArea = computeViewPortArea();
	        int itemCount = 0;
	        int visibleItemCount = 0;
	        int paintedHeight = 0;
	        while (paintedHeight < clientArea.size.height) {
	           	int offset = topOffset + itemCount;
	           	
	        	int itemHeight = 0;
	        	
	           	// No more cell
	        	if (offset >= length) {
	       	        itemHeight = getDefaultItemHeight();
	            	int y = clientArea.location.y + paintedHeight;
	            	Rectangle itemClientArea = new Rectangle(itemsClientArea.location.x, y, itemsClientArea.size.width, itemHeight);

	            	try { prePaintItem(g, offset, itemClientArea, null); } catch(Throwable t) { logger.warn(t); }

	            	paintedHeight += itemHeight;
	    		}
	        	else {
	               	Object itemObject = elementProvider.get(offset);
	               	
	            	try { 
	            		if (cellRenderer != null) {
	            			itemHeight = cellRenderer.computeHeight(this, itemObject, offset);
	            		}
	            		else { 		   	
	            	        itemHeight = getDefaultItemHeight();
	            		}
	            	}
	            	catch(Throwable t) {
	            		itemHeight = getDefaultItemHeight();
	            		logger.warn(t); 
	            	}
	            	

	            	int y = clientArea.location.y + paintedHeight;
	            	Rectangle itemClientArea = new Rectangle(itemsClientArea.location.x, y, itemsClientArea.size.width, itemHeight);

	            	try { prePaintItem(g, offset, itemClientArea, itemObject); } catch(Throwable t) { logger.warn(t); }

	            	paintedHeight += itemHeight;
	            	if (paintedHeight <= clientArea.size.height) {
	                   	visibleItemCount++;
	            	}
	        	}
	        	itemCount++;
	        }
	        this.visibleItemCount = visibleItemCount;
		}
		finally {
			g.setClip(screenRect.location.x, screenRect.location.y, screenRect.size.width, screenRect.size.height);
		}
	}
	
	private int getDefaultItemHeight() {
		return 1 + FontUtil.SMALL.getHeight() + 1 + FontUtil.SMALL_BOLD.getHeight() + 1 + 1;
	}
	
	public boolean isItemSelected(int offset) {
    	boolean isSelected = selectedOffset == offset;
    	return isSelected;
	}

	protected void prePaintItem(Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {
    	
		boolean isSelected = isItemSelected(offset);
		
		boolean isEvenItem = offset %2 == 0;
		if (isPreRender()) {
	    	if (isSelected) {
	//    		g.setColor(ColorUtil.WIDGET_LIST_FONT_SELECTED);
	    		Color shadeColor1 = getTheme().getColor(ThemeConstants.WIDGET_LIST_SELECTED_SHADE_LIGHT);
	    		Color shadeColor2 = getTheme().getColor(ThemeConstants.WIDGET_LIST_SELECTED_SHADE_DARK);
	    		Shade shade = new Shade(shadeColor1.intValue(), shadeColor2.intValue());
	    		GraphicsUtil.fillShade(g, itemClientArea, shade, false);
	     	}
	    	else {
	    		Color listColor = getTheme().getColor(isEvenItem ? ThemeConstants.WIDGET_LIST_EVEN : ThemeConstants.WIDGET_LIST_ODD);
	    		g.setColor(listColor.intValue());
	    		g.fillRect(itemClientArea.location.x, itemClientArea.location.y, itemClientArea.size.width, itemClientArea.size.height);
	    	}
	    	if (separatorVisible) {
				Color listSeparatorColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_SEPARATOR);
		    	g.setColor(listSeparatorColor.intValue());
		    	g.drawLine(itemClientArea.location.x, itemClientArea.location.y + itemClientArea.size.height - 1, itemClientArea.location.x + itemClientArea.size.width, itemClientArea.location.y + itemClientArea.size.height - 1);
	    	}
	    	
	    	if (isSelected) {
				Color listTopSeparatorColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_SELECTED_TOP);
		    	g.setColor(listTopSeparatorColor.intValue());
		    	g.drawLine(itemClientArea.location.x, itemClientArea.location.y, itemClientArea.location.x + itemClientArea.size.width, itemClientArea.location.y);
				
		    	Color listBottomSeparatorColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_SELECTED_BOTTOM);
		    	g.setColor(listBottomSeparatorColor.intValue());
		    	g.drawLine(itemClientArea.location.x, itemClientArea.location.y + itemClientArea.size.height - 1, itemClientArea.location.x + itemClientArea.size.width, itemClientArea.location.y + itemClientArea.size.height - 1);
	    	}
	    	
	     	if (isSelected) {
	    		Color listFontSelectedColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SELECTED);
	    		g.setColor(listFontSelectedColor.intValue());
	    	}
	    	else {
	    		Color listFontColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT);
	    		g.setColor(listFontColor.intValue());
	    	}
		}
		if (itemObject != null) {
			paintItem(g, offset, itemClientArea, itemObject);
			if (cellRenderer != null) {
				cellRenderer.paintItem(this, g, offset, itemClientArea, itemObject);
			}
			else {
			}
     	}
	}

	protected void paintItem(Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {
		
	}
	
	public void onKeyPressed(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
		if (logger.isDebugEnabled()) {
			logger.debug("[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
		}
	    if (gameAction == GameCanvas.DOWN) {
	    	onBottomKeyPressed();
	    }
	    else if (gameAction == GameCanvas.UP) {
	    	onTopKeyPressed();
		}
	    else if (gameAction == GameCanvas.LEFT) {
	    	onLeftKeyPressed();
		}
	    else if (gameAction == GameCanvas.RIGHT) {
			onRightKeyPressed();
		}
	    else if (gameAction == GameCanvas.FIRE) {
			onFirePressed();
		}
	}

	protected void onTopKeyPressed() {
		selectPreviousItem();
	}

	protected void onBottomKeyPressed() {
		selectNextItem();
	}

	protected void onLeftKeyPressed() {
		fireReturnCallback();
	}

	protected void onRightKeyPressed() {
		showCurrentItemSelected();
	}

	protected void onFirePressed() {
		if (getPrimaryCommand() != null && (!getPrimaryCommand().isInteractive() || !isPrimaryCommandEnabled())) {
			showCurrentItemSelected();
		}
	}
	
	private void showCurrentItemSelected() {
		int length = elementProvider == null ? 0 : elementProvider.length();
		boolean isEmpty = length == 0;
		
		if (!isEmpty) {
			try { onShowItemSelected(elementProvider.get(selectedOffset)); } catch(Throwable t) { logger.warn(t); }
		}
	}
	
	protected void onShowItemSelected(Object object) {
		// TODO Auto-generated method stub
		
	}
	
	long previousPointerReleased = 0;
	int previousOffsetSelected = -1;
	
	public void onPointerReleased(int x, int y) {
		if (elementProvider.length() <= 0) {
			super.onPointerReleased(x, y);
			return;
		}
		if (!pointerDragged) {
			int pointerPressedOffset = getVisibleItemOffsetByPointer(pointerPressedPosition);
			
			if (pointerPressedOffset < 0) {
				return;
			}
			
			pointerReleasedPosition = new Point(x, y);
			
			if (!computeClientArea(false).contains(pointerReleasedPosition)) {
				return ;
			}

			int pointerReleasedOffset = getVisibleItemOffsetByPointer(pointerReleasedPosition);

			if (logger.isDebugEnabled()) {
				logger.debug("pointerPressedPosition: " + pointerPressedPosition
						+ ", pointerReleasedPosition: " + pointerReleasedPosition
						+ ", pointerPressedOffset: " + pointerPressedOffset
						+ ", pointerReleasedOffset: " + pointerReleasedOffset);
			}
			
			if (pointerPressedOffset == pointerReleasedOffset && pointerPressedOffset >= 0) {
				selectedOffset = pointerPressedOffset;
			}
		}
		super.onPointerReleased(x, y);
		long now = System.currentTimeMillis();
		if (/* now - previousPointerReleased < 750 && */ selectedOffset == previousOffsetSelected) {
			showCurrentItemSelected();
		}
		previousOffsetSelected = selectedOffset;
		previousPointerReleased = now;
	}
	
	public void onKeyRepeated(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
		if (logger.isDebugEnabled()) {
			logger.debug("[onKeyRepeated] gameAction: " + gameAction + ", keyCode: " + keyCode);
		}
	    if (gameAction == GameCanvas.DOWN) {
	    	selectNextItem();
	    }
	    else if (gameAction == GameCanvas.UP) {
	    	selectPreviousItem();
		}
	}
	
	protected void scrollDown(int height) {
			selectNextItem();
			hitScrollbar();
			repaint();
	}

	protected void scrollUp(int height) {
		selectPreviousItem();
		hitScrollbar();
		repaint();
	}


	protected void selectPreviousItem() {
		if (selectedOffset > 0) {
			selectedOffset--;
		}
		if (selectedOffset < topOffset) {
			setTopOffset(selectedOffset);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Select Previous Item - topOffset: " + topOffset + ", selectedOffset: " + selectedOffset);
		}
		
		hitScrollbar();
		repaint();
	}

	protected int getViewPortTop() {
		Rectangle titleArea = getTitleZone().computeArea();
		return titleArea.size.height;
	}
	
	protected int getViewPortHeight() {
		return getClientAreaHeight();
	}	
	
	protected int getViewPortLeft() {
		return 0;
	}
	
	protected int getViewPortWidth() {
		return viewCanvas.getWidth();
	}	

	public Rectangle computeViewPortArea() {
		return new Rectangle(getViewPortLeft(), getViewPortTop(), getViewPortWidth(), getViewPortHeight());
	}

	protected void selectNextItem() {
		int length = elementProvider == null ? 0 : elementProvider.length();
		if (selectedOffset + 1 < length) {
			selectedOffset++;
		}
		if (length <= visibleItemCount) {
			setTopOffset(0);
		}
		else {
			if (topOffset >= length - visibleItemCount) {
				setTopOffset(length - visibleItemCount);
			}
			if (topOffset + visibleItemCount <= selectedOffset) {
				setTopOffset(selectedOffset - visibleItemCount + 1);
			}	
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Scroll Next Item - topOffset: " + topOffset + ", selectedOffset: " + selectedOffset + ", visibleItemCount: " + visibleItemCount + ", length: " + length);
		}
		
		hitScrollbar();
		repaint();
	}
	
	public void setCellRenderer(ICellRenderer cellRenderer) {
		this.cellRenderer = cellRenderer;
	}
	
	public boolean isPreRender() {
		return preRender;
	}
	
	public void setPreRender(boolean preRender) {
		this.preRender = preRender;
	}

}
