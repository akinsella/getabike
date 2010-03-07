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
package org.helyx.app.j2me.getabike.lib.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Point;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Shade;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.ColorUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.KeyUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.view.ViewCanvas;
import org.helyx.app.j2me.getabike.lib.ui.view.listener.KeyEventListener;
import org.helyx.app.j2me.getabike.lib.ui.view.listener.PointerEventListener;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.Painter;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.ViewCanvasZone;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.background.ColorBackgroundPainter;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.background.DefaultBackgroundPainter;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.zone.command.CommandZone;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.zone.command.DefaultCommandZone;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.zone.title.DefaultTitleZone;
import org.helyx.app.j2me.getabike.lib.ui.view.transition.IViewTransition;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.event.CommandEvent;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.event.listener.CommandSelectionListener;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.support.HourCommand;
import org.helyx.logging4me.Logger;

public abstract class AbstractView extends AbstractDisplayable implements PointerEventListener, KeyEventListener, CommandSelectionListener {

	private static final Logger logger = Logger.getLogger("ABSTRACT_VIEW");
	
	protected String title;
	protected boolean fullScreenMode = false;
	
	private ViewCanvasZone titleZone = new DefaultTitleZone(this);
	private CommandZone commandZone;
//	private Painter screenBackgroundPainter = new ShadeBackgroundPainter(ColorUtil.colorToInt(174, 187, 203), ColorUtil.colorToInt(111, 134, 165), false);
	private Painter screenBackgroundPainter = new ColorBackgroundPainter(ColorUtil.colorToInt(174, 187, 203));
	private Painter clientBackgroundPainter = new DefaultBackgroundPainter();
	
	private Command primaryCommand;
	private Command secondaryCommand;
	private Command thirdCommand;
	
	private boolean primaryCommandEnabled = true;
	private boolean secondaryCommandEnabled = true;
	private boolean thirdCommandEnabled = true;
	
	protected boolean screenBackgroundEnabled = true;
	protected boolean clientBackgroundEnabled = true;
	protected boolean titleEnabled = true;
	protected boolean commandEnabled = true;
	
	private boolean paintScrollbar = false;
	
	private HourCommand hourCommand = new HourCommand();
	private boolean hourEnabled = true;

	protected static ViewCanvas viewCanvas = new ViewCanvas();
	
	private Vector pointerEventListenerList = new Vector();
	
	private Vector keyEventListenerList = new Vector();
		
	private long scrollbarHitTimeout = 1000;
	
	private int screenTop = 0;
	
	private boolean screenDragging = false;

	public AbstractView(AbstractMIDlet midlet) {
		super(midlet);
		init();
	}

	private void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("Has pointer events: " + viewCanvas.hasPointerEvents());
			logger.debug("Has pointer motion events: " + viewCanvas.hasPointerMotionEvents());
			logger.debug("Has repeat events: " + viewCanvas.hasRepeatEvents());
		}
		addPointEventListener(this);
		addKeyEventListener(this);
		setCommandZone(new DefaultCommandZone(this));
	}
	
	public boolean isScreenDragging() {
		return screenDragging;
	}

	public void setScreenDragging(boolean screenDragging) {
		this.screenDragging = screenDragging;
	}

	public boolean isHourEnabled() {
		return hourEnabled;
	}

	public void setHourEnabled(boolean hourEnabled) {
		this.hourEnabled = hourEnabled;
	}

	public ViewCanvasZone getTitleZone() {
		return titleZone;
	}

	public void setTitleZone(ViewCanvasZone titleZone) {
		this.titleZone = titleZone;
	}

	public ViewCanvasZone getCommandZone() {
		return commandZone;
	}

	public void setCommandZone(CommandZone commandZone) {
		if (this.commandZone != null) {
			removePointEventListener(this.commandZone);
		}
		this.commandZone = commandZone;
		if (this.commandZone != null) {
			addPointEventListener(this.commandZone);
		}
	}

	public Painter getScreenBackgroundPainter() {
		return screenBackgroundPainter;
	}

	public void setScreenBackgroundPainter(Painter screenBackgroundPainter) {
		this.screenBackgroundPainter = screenBackgroundPainter;
	}

	public Painter getClientBackgroundPainter() {
		return clientBackgroundPainter;
	}

	public void setClientBackgroundPainter(Painter clientBackgroundPainter) {
		this.clientBackgroundPainter = clientBackgroundPainter;
	}
	
	public void addKeyEventListener(KeyEventListener keyEventListener) {
		keyEventListenerList.addElement(keyEventListener);
	}
	
	public void removeKeyEventListener(KeyEventListener keyEventListener) {
		keyEventListenerList.removeElement(keyEventListener);
	}

	public void removeAllKeyEventListener() {
		keyEventListenerList.removeAllElements();		
	}

	public void addPointEventListener(PointerEventListener pointerEventListener) {
		pointerEventListenerList.addElement(pointerEventListener);
	}

	public void removePointEventListener(PointerEventListener pointerEventListener) {
		pointerEventListenerList.removeElement(pointerEventListener);		
	}

	public void removeAllPointEventListener() {
		pointerEventListenerList.removeAllElements();		
	}

	protected void sizeChanged(int w, int h) {
		if (logger.isDebugEnabled()) {
			logger.debug("Screen size changed [" + w + ", " + h + "]");
		}
	}

	protected void pointerPressed(int x, int y) {
		if (logger.isDebugEnabled()) {
			logger.debug("Pointer pressed: [x:" + x + ", y:" + y + "]");
		}
		Enumeration pointerEventListenerEnum = pointerEventListenerList.elements();
		while (pointerEventListenerEnum.hasMoreElements()) {
			try {
				PointerEventListener pel = (PointerEventListener)pointerEventListenerEnum.nextElement();
				pel.onPointerPressed(x, y);
			}
			catch(Throwable t) {
				logger.warn(t);
			}
		}
		if (getViewCanvas().isDebug()) {
			repaint();
		}
	}

	protected void pointerDragged(int x, int y) {
		if (logger.isDebugEnabled()) {
			logger.debug("Pointer dragged: [x:" + x + ", y:" + y + "]");
		}
		Enumeration pointerEventListenerEnum = pointerEventListenerList.elements();
		while (pointerEventListenerEnum.hasMoreElements()) {
			try {
				PointerEventListener pel = (PointerEventListener)pointerEventListenerEnum.nextElement();
				pel.onPointerDragged(x, y);
			}
			catch(Throwable t) {
				logger.warn(t);
			}
		}
		if (getViewCanvas().isDebug()) {
			
		}
	}

	protected void pointerReleased(int x, int y) {
		if (logger.isDebugEnabled()) {
			logger.debug("Pointer released: [x:" + x + ", y:" + y + "]");
		}
		Enumeration pointerEventListenerEnum = pointerEventListenerList.elements();
		while (pointerEventListenerEnum.hasMoreElements()) {
			try {
				PointerEventListener pel = (PointerEventListener)pointerEventListenerEnum.nextElement();
				pel.onPointerReleased(x, y);
			}
			catch(Throwable t) {
				logger.warn(t);
			}
		}
		if (getViewCanvas().isDebug()) {
			repaint();
		}
	}

	protected void keyPressed(int keyCode) {
		
		int gameAction = viewCanvas.getGameAction(keyCode);

	    if (gameAction == GameCanvas.FIRE && primaryCommand != null) {
	    	commandZone.setCurrentCommandPressed(primaryCommand);
	    	repaint();
		}
	    else if (KeyUtil.isRightSoftKey(keyCode) && secondaryCommand != null) {
	    	commandZone.setCurrentCommandPressed(secondaryCommand);
	    	repaint();
		}
	    else if (KeyUtil.isLeftSoftKey(keyCode) && thirdCommand != null) {
	    	commandZone.setCurrentCommandPressed(thirdCommand);
	    	repaint();
	    }
		
		if (logger.isDebugEnabled()) {
			logger.debug("Key pressed: '" + viewCanvas.getKeyName(keyCode) + "' - Key code: '" + keyCode + "'");
		}
		
		Enumeration keyEventListenerEnum = keyEventListenerList.elements();
		while (keyEventListenerEnum.hasMoreElements()) {
			try {
				KeyEventListener kel = (KeyEventListener)keyEventListenerEnum.nextElement();
				kel.onKeyPressed(keyCode);
			}
			catch(Throwable t) {
				logger.warn(t);
			}
		}
	}

	protected void keyRepeated(int keyCode) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Key repeated: '" + viewCanvas.getKeyName(keyCode) + "' - Key code: '" + keyCode + "'");
		}
		Enumeration keyEventListenerEnum = keyEventListenerList.elements();
		while (keyEventListenerEnum.hasMoreElements()) {
			try {
				KeyEventListener kel = (KeyEventListener)keyEventListenerEnum.nextElement();
				kel.onKeyRepeated(keyCode);
			}
			catch(Throwable t) {
				logger.warn(t);
			}
		}
	}

	protected void keyReleased(int keyCode) {
		
		commandZone.executePendingCommand();
		commandZone.setCurrentCommandPressed(null);
		
		Enumeration keyEventListenerEnum = keyEventListenerList.elements();
		while (keyEventListenerEnum.hasMoreElements()) {
			try {
				KeyEventListener kel = (KeyEventListener)keyEventListenerEnum.nextElement();
				kel.onKeyReleased(keyCode);
			}
			catch(Throwable t) {
				logger.warn(t);
			}
		}
	}
	
	public void onCommandSelection(CommandEvent commandEvent) {
		
		if (commandEvent == null) {
			return ;
		}
		
		Command command = commandEvent.getCommand();
		
		if (command == null) {
			return ;
		}
		
		IAction commandAction = command.getAction();
		
		if (commandAction == null) {
			return ;
		}
		
		commandAction.run(null);
	}
	
	public void fireCommandEvent(Command command) {
		try {
			CommandEvent commandEvent = new CommandEvent(command);
			onCommandSelection(commandEvent);
		}
		catch(Throwable t) {
			logger.warn(t);
		}
	}
	
	public void flushGraphics() {
		viewCanvas.flushGraphics();
	}

	public void onKeyReleased(int keyCode) {
		
	}

	public void onKeyPressed(int keyCode) {
		
	}

	public void onKeyRepeated(int keyCode) {
		
	}

	public Displayable getDisplayable() {
		return viewCanvas;
	}

	private int scrollMaximum;
	private int scrollValue;
	private int scrollVisible;
	
	public int getScrollMaximum() {

		if (screenDragging) {
			int scrollMaximum = getViewHeight() - computeClientArea(false).size.height;
		
			return scrollMaximum;
		}
		
		return scrollMaximum;
	}

	public int getScrollValue() {
		if (screenDragging) {
			if (getScreenTop() <= 0) {
				return Math.min(getScrollMaximum(), Math.abs(getScreenTop()));
			}
			else {
				return 0;
			}
		}
		
		return scrollValue;
	}
	
	public int getScrollVisible() {
		if (screenDragging) {	
			return (int)(getScrollMaximum() * (double)computeClientArea(false).size.height / (double)getViewHeight());
		}
		return scrollVisible;
	}
	
	private void paintScrollbar(Graphics g) {
		
		if (isScrollbarTimestampExpired()) {
			return ;
		}
	
		Rectangle clientArea = computeClientArea(false);
		
		int x = clientArea.location.x;
        int y = clientArea.location.y + 5;
        int width = clientArea.size.width - 3;
        int height = clientArea.size.height - 10;
        int scrollBarWidth = getTheme().getInt(ThemeConstants.WIDGET_SCROLL_WIDTH);
		
        if (logger.isDebugEnabled()) {
            logger.debug("scrollBarWidth: " + scrollBarWidth);
       }

        g.setColor(getTheme().getColor(ThemeConstants.WIDGET_SCROLL_BACKGROUND).intValue());
		g.fillRect(x + width - scrollBarWidth - 2, y, scrollBarWidth, height);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Scroll visible: " + getScrollVisible());
			logger.debug("Scroll maximum: " + getScrollMaximum());
			logger.debug("Scroll value: " + getScrollValue());			
		}
		
		int yHeight = height;
		int yPos = y;
		if (getScrollMaximum() > 0) {
			yHeight = Math.max(height / 8, (int)(height * (double)getScrollVisible() / (double)getScrollMaximum()));
			yPos = y + (int)((height - yHeight) * ((double)getScrollValue() / ((double)getScrollMaximum() - 1)));
		}
	
		Color shadeColor1 = getTheme().getColor(ThemeConstants.WIDGET_SCROLL_SHADE_DARK);
		Color shadeColor2 = getTheme().getColor(ThemeConstants.WIDGET_SCROLL_SHADE_LIGHT);
		Shade shade = new Shade(shadeColor1.intValue(), shadeColor2.intValue());
		Rectangle sbArea = new Rectangle(x + width - scrollBarWidth - 2, yPos, scrollBarWidth, yHeight);
		
        if (logger.isDebugEnabled()) {
            logger.debug("sbArea: " + sbArea);
        }
 		GraphicsUtil.fillShade(g, sbArea, shade, false);
		
		g.setColor(getTheme().getColor(ThemeConstants.WIDGET_SCROLL_BORDER).intValue());
		g.drawLine(sbArea.location.x, sbArea.location.y - 1, sbArea.location.x + sbArea.size.width - 1, sbArea.location.y - 1);
		g.drawLine(sbArea.location.x, sbArea.location.y + sbArea.size.height, sbArea.location.x + sbArea.size.width - 1, sbArea.location.y + sbArea.size.height);

		g.drawLine(sbArea.location.x - 1, sbArea.location.y, sbArea.location.x - 1, sbArea.location.y + sbArea.size.height - 1);
		g.drawLine(sbArea.location.x + sbArea.size.width, sbArea.location.y, sbArea.location.x + sbArea.size.width, sbArea.location.y + sbArea.size.height - 1);
	}

	protected boolean pointerPressed = false;
	protected boolean pointerDragged = false;
	protected Point pointerPressedPosition = null;
	protected Point pointerReleasedPosition = null;
	protected Point lastPointPosition = null;
	protected Point pointerDraggedPosition = null;
	protected Vector pointList = new Vector();
	protected long lastPointerHitTimestamp = 0;
	protected long minPointerHitIntervalTimestamp = 0;
	protected long minPointerHitDistance = 20;
	
	public long getMinPointerHitIntervalTimestamp() {
		return minPointerHitIntervalTimestamp;
	}

	public void setMinPointerHitIntervalTimestamp(
			long minPointerHitIntervalTimestamp) {
		this.minPointerHitIntervalTimestamp = minPointerHitIntervalTimestamp;
	}

	public long getMinPointerHitDistance() {
		return minPointerHitDistance;
	}

	public void setMinPointerHitDistance(long minPointerHitDistance) {
		this.minPointerHitDistance = minPointerHitDistance;
	}

	public void onPointerPressed(int x, int y) {
//		if (!screenDragging) {
//			return ;
//		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Point pressed: x=" + x  + ", y=" + y);
		}
		pointerDragged = false;
		pointerPressed = true;
		pointerPressedPosition = new Point(x, y);
		pointerDraggedPosition = null;
		lastPointPosition = pointerPressedPosition;
		lastPointerHitTimestamp = System.currentTimeMillis();
		pointList = new Vector();
		pointList.addElement(pointerPressedPosition);
	}
	
	public void onPointerDragged(int x, int y) {
		long timestamp = System.currentTimeMillis();
		
		if (!screenDragging) {
			return ;
		}
		
		if (timestamp - lastPointerHitTimestamp < minPointerHitIntervalTimestamp) {
			if (logger.isDebugEnabled()) {
				logger.debug("Filtering pointer drag event - Delta ms: " + (timestamp - lastPointerHitTimestamp) + " - min interval: " + minPointerHitIntervalTimestamp);
			}
			return ;
		}
		else if (Math.abs(y - lastPointPosition.y) < minPointerHitDistance) {
			if (logger.isDebugEnabled()) {
				logger.debug("Filtering pointer drag event - Last Y pos: '" + lastPointPosition.y + ", Actual Y pos: '" + y);
			}
			return ;
		}
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("Drag event - Delta ms: " + (timestamp - lastPointerHitTimestamp) + " - min interval: " + minPointerHitIntervalTimestamp);
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Pointer dragged: x=" + x  + ", y=" + y);
		}
		
		pointerDragged = true;
		pointerDraggedPosition = new Point(x, y);

		pointerHit(pointerDraggedPosition, timestamp);
	}

	public void onPointerReleased(int x, int y) {
//		if (!screenDragging) {
//			return ;
//		}
		
		long timestamp = System.currentTimeMillis();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Point released: x=" + x  + ", y=" + y);
		}
		
		pointerReleasedPosition = new Point(x, y);
		
		if (screenDragging && Math.abs(y - lastPointPosition.y) >= minPointerHitDistance && timestamp - lastPointerHitTimestamp >= minPointerHitIntervalTimestamp) {
			pointerHit(pointerReleasedPosition, timestamp);
		}
		else {
			repaintNow();
		}
		
		pointList.removeAllElements();
		pointerPressedPosition = null;
		pointerDraggedPosition = null;
		pointerPressed = false;
		pointerDragged = false;
	
//		if (screenTop < 0) {
//			for (int i = Math.abs(screenTop) ; i > 0 ; i = i / 2) {
//				setScreenTop(-i);
//				getViewCanvas().paint(getViewCanvas().getGraphics());
//				getViewCanvas().flushGraphics();
//			}
//			if (screenTop < 0) {
//				setScreenTop(0);
//				getViewCanvas().paint(getViewCanvas().getGraphics());
//				getViewCanvas().flushGraphics();	
//			}
//		}
//	
//		Rectangle rect = computeClientArea(false);
//		int maxHeight = rect.size.height;
//		
//		if (screenTop > 0) {
//			for (int i = Math.abs(screenTop) ; i > 0 ; i = i / 2) {
//				setScreenTop(i);
//				getViewCanvas().paint(getViewCanvas().getGraphics());
//				getViewCanvas().flushGraphics();
//			}
//			if (screenTop > 0) {
//				setScreenTop(0);
//				getViewCanvas().paint(getViewCanvas().getGraphics());
//				getViewCanvas().flushGraphics();
//			}
//		}
		// getViewHeight()
		if (screenDragging) {

			int tmpHeight = (getViewHeight() - computeClientArea(false).size.height);
			if (screenTop + tmpHeight < 0) {
				for (int i = Math.abs(screenTop + tmpHeight) ; i > 0 ; i = i / 2) {
					setScreenTop(- (tmpHeight + i));
					repaintNow();
				}
				if (screenTop + tmpHeight < 0) {
					setScreenTop(-tmpHeight);
					repaintNow();
				}
			}
			
			if (screenTop > 0) {
				for (int i = Math.abs(screenTop) ; i > 0 ; i = i / 2) {
					setScreenTop(i);
					repaintNow();
				}
				if (screenTop > 0) {
					setScreenTop(0);
					repaintNow();
				}
			}
		}
	}

	protected void pointerHit(Point pointerPosition, long timestamp) {
		int scroll = pointerPosition.y - lastPointPosition.y;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Scroll value: " + scroll);
		}

		pointList.addElement(pointerPosition);
		lastPointPosition = pointerPosition;		
		lastPointerHitTimestamp = timestamp;
		
		if (scroll > 0) {
			scrollDown(scroll);
		}
		else if (scroll < 0) {
			scrollUp(scroll);			
		}

	}

	protected void scrollDown(int height) {
		if (screenDragging) {
			logger.debug("Scroll down: " + height);
			Rectangle rect = computeClientArea(false);		
			int maxScreenTop = rect.size.height / 3;
			setScreenTop(Math.min(maxScreenTop, screenTop + height));
			hitScrollbar();
			repaint();
		}
		else {
			hitScrollbar();
		}
	}

	protected void scrollUp(int height) {
		if (screenDragging) {
			logger.debug("Scroll up: " + height);
			Rectangle rect = computeClientArea(false);		
			int minScreenTop = - (getViewHeight() - rect.size.height + rect.size.height / 3);
			setScreenTop(Math.max(minScreenTop, screenTop + height));
			hitScrollbar();
			repaint();
		}
		else {
			hitScrollbar();
		}
	}

	private long scrollbarHitTimestamp = 0;
	
	private boolean isScrollbarTimestampExpired() {
		long timestamp = System.currentTimeMillis();
		return timestamp >= scrollbarHitTimestamp + scrollbarHitTimeout;
	}
	
	protected void hitScrollbar() {
		if (!isPaintScrollBar()) {
			return ;
		}
		long timestamp = System.currentTimeMillis();
		
		if (logger.isDebugEnabled()) {
			logger.debug("timestamp: " + timestamp);
			logger.debug("scrollbarHitTimeout: " + scrollbarHitTimeout);
		}
		
		if (isScrollbarTimestampExpired()) {
			scrollbarHitTimestamp = timestamp;
			repaint();
			Thread scrollbarThread = new Thread() {
				public void run() {
					while (!isScrollbarTimestampExpired()) {
						try { Thread.sleep(250); } catch (InterruptedException e) { logger.warn(e); }
					}
					repaint();
				}
			};
			scrollbarThread.start();
		}
		else {
			scrollbarHitTimestamp = timestamp;
		}
	}

	public void repaintNow() {
		getViewCanvas().paint(getViewCanvas().getGraphics());
		getViewCanvas().flushGraphics();
	}

	public void repaint() {
		getViewCanvas().repaint();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isFullScreenMode() {
		return fullScreenMode;
	}
	
	public void setFullScreenMode(boolean fullScreenMode) {
		this.fullScreenMode = fullScreenMode;
		viewCanvas.setFullScreenMode(fullScreenMode);
	}

	public Command getPrimaryCommand() {
		return primaryCommand != null ? primaryCommand : (hourEnabled ? hourCommand : null);
	}

	public Command getSecondaryCommand() {
		return secondaryCommand;
	}

	public Command getThirdCommand() {
		return thirdCommand;
	}
	
	public boolean isTitleEnabled() {
		return titleEnabled;
	}

	public void setTitleEnabled(boolean titleEnabled) {
		this.titleEnabled = titleEnabled;
	}

	public boolean isCommandEnabled() {
		return commandEnabled;
	}

	public void setCommandEnabled(boolean commandEnabled) {
		this.commandEnabled = commandEnabled;
	}

	public boolean isClientBackgroundEnabled() {
		return clientBackgroundEnabled;
	}

	public void setClientBackgroundEnabled(boolean paintBackgroundColor) {
		this.clientBackgroundEnabled = paintBackgroundColor;
	}

	public boolean isScreenBackgroundEnabled() {
		return clientBackgroundEnabled;
	}

	public void setScreenBackgroundEnabled(boolean paintBackgroundColor) {
		this.screenBackgroundEnabled = paintBackgroundColor;
	}
	
	private int getScreenTop() {
		return screenTop;
	}	

	private void setScreenTop(int screenTop) {
		this.screenTop = screenTop;
		if (logger.isDebugEnabled()) {
			logger.debug("Screen top : " + screenTop);
		}
	}
	
	protected int getViewHeight() {
		return getClientAreaHeight();
	}	
	
	protected int getClientAreaHeight() {
		Rectangle titleArea = getTitleZone().computeArea();
		Rectangle commandArea = getCommandZone().computeArea();
		int clientAreaHeight = viewCanvas.getHeight() - titleArea.size.height - commandArea.size.height;
		return clientAreaHeight;
	}
	
	public Rectangle computeScreenArea() {
		return new Rectangle(0, 0, viewCanvas.getWidth(), viewCanvas.getHeight());
	}
	
	public Rectangle computeClientArea() {
		return computeClientArea(true);
	}
	
	public Rectangle computeClientArea(boolean useViewPort) {
		Rectangle titleArea = titleZone.computeArea();
		Rectangle rect = new Rectangle(0, titleArea.size.height + (useViewPort ? screenTop : 0), viewCanvas.getWidth(), useViewPort ? getViewHeight() : getClientAreaHeight());
//		logger.info("computeClientArea(useViewPort=" + useViewPort + ", screenTop=" + screenTop + "): " + rect);
		return rect;
	}
	
	protected boolean isPaintScrollBar() {
		return paintScrollbar;
	}
	
	protected void setPaintScrollBar(boolean paintScrollbar) {
		this.paintScrollbar = paintScrollbar;
	}

	public void onPaint(Graphics graphics) {
		
		try { paintScreenBackground(graphics); } catch(Throwable t) { logger.warn(t); }
		
		try { paintClientBackground(graphics); } catch(Throwable t) { logger.warn(t); }
			try { paint(graphics); } catch(Throwable t) { logger.warn(t); }

		    if (isPaintScrollBar()) {
				try { paintScrollbar(graphics); } catch(Throwable t) { logger.warn(t); }
		    }


		try { paintTitleArea(graphics); } catch(Throwable t) { logger.warn(t); }
		try { paintCommandArea(graphics); } catch(Throwable t) { logger.warn(t); }

		try { postPaint(graphics); } catch(Throwable t) { logger.warn(t); }
		if (viewCanvas.isDebug()) {
			try { paintDebug(graphics); } catch(Throwable t) { logger.warn(t); }
		}
	}

	protected void postPaint(Graphics graphics) {
		
	}

//	protected void paintClipped(Graphics graphics) {
//		
//	}

	protected void paintDebug(Graphics graphics) {
		int lastKeyCode = viewCanvas.getLastKeyCode();
		if (viewCanvas != null && viewCanvas.getLastKeyCode() != 0) {
			graphics.setColor(ColorUtil.BLACK);
			graphics.setFont(FontUtil.SMALL_BOLD);
			graphics.drawString("Code: " + lastKeyCode + ", Name: " + viewCanvas.getKeyName(lastKeyCode) + ", : " + viewCanvas.getGameAction(lastKeyCode), 0, 0, Graphics.TOP | Graphics.LEFT);
		}
		
		long start = System.currentTimeMillis();
		
		Enumeration pointListEnum = pointList.elements();
		
		graphics.setColor(ColorUtil.DARK_RED);
		Point previousPoint = null;
		while(pointListEnum.hasMoreElements()) {
			Point point = (Point)pointListEnum.nextElement();

			graphics.drawLine(point.x - 2, point.y - 2, point.x + 2, point.y + 2);
			graphics.drawLine(point.x - 2, point.y + 2, point.x + 2, point.y - 2);
			
			if (previousPoint != null) {
				graphics.drawLine(previousPoint.x, previousPoint.y, point.x, point.y);
			}
			
			previousPoint = point;
		}

		long end = System.currentTimeMillis();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Paint debug - Time ellapsed: " + Math.max(0, end - start) + "ms");
		}
	}

	public boolean shouldPaintScreenBackground() {
		return screenBackgroundEnabled;
	}

	public boolean shouldPaintClientBackground() {
		return clientBackgroundEnabled;
	}
	
	public boolean shouldPaintCommand() {
		return commandEnabled && (thirdCommand != null || primaryCommand != null || secondaryCommand != null);
	}
	
	public boolean shouldPaintTitle() {
		return titleEnabled && title != null;
	}
	
	public ViewCanvas getViewCanvas() {
		return viewCanvas;
	}
	
	protected void changeDisplayable(AbstractDisplayable srcDisplayable, AbstractDisplayable targetDisplayable, IViewTransition viewTransition) {
		if (srcDisplayable == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Current displayable: [" + null + "]");
			}
		}
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("Current displayable: [" + srcDisplayable.getClass().getName() + "] title='" + srcDisplayable.getTitle() + "'");
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Target displayable: [" + targetDisplayable.getClass().getName() + "] title='" + targetDisplayable.getTitle() + "'");
		}
		if (srcDisplayable != null && (srcDisplayable instanceof AbstractView) && (targetDisplayable instanceof AbstractView)) {
			AbstractView srcView = (AbstractView)srcDisplayable;
			AbstractView targetView = (AbstractView)targetDisplayable;

			try { viewCanvas.doCanvasTransition(srcView, targetView, viewTransition); }
			catch(Throwable t) { logger.warn(t); }
		}
		
		
		super.changeDisplayable(srcDisplayable, targetDisplayable, viewTransition);
	}
	
	protected void onSelection() {
		viewCanvas.setView(this);
	}
	
	protected void onLeave() {
		viewCanvas.setView(null);
	}

	protected void paintScreenBackground(Graphics graphics) {
		if (!shouldPaintScreenBackground()) {
			return;
		}
		screenBackgroundPainter.paintArea(graphics, this, computeScreenArea());
	}

	protected void paintClientBackground(Graphics graphics) {
		if (!shouldPaintClientBackground()) {
			return;
		}
		
		clientBackgroundPainter.paintArea(graphics, this, computeClientArea(true));
	}

	protected void paintTitleArea(Graphics graphics) {
		if (!shouldPaintTitle()) {
			return;
		}
		
		titleZone.paintArea(graphics);
	}

	protected void paintCommandArea(Graphics graphics) {
		if (!shouldPaintCommand()) {
			return;
		}

		commandZone.paintArea(graphics);
	}

	protected abstract void paint(Graphics graphics);
	
	public void setPrimaryCommand(Command primaryCommand) {
		this.primaryCommand = primaryCommand;
	}

	public void setSecondaryCommand(Command secondaryCommand) {
		this.secondaryCommand = secondaryCommand;
	}

	public void setThirdCommand(Command thirdCommand) {
		this.thirdCommand = thirdCommand;
	}

	public void afterDisplayableSelection(AbstractDisplayable previous, AbstractDisplayable current) {
		if (current == this) {
			if (logger.isDebugEnabled()) {
				logger.debug("afterDisplayableSelection[previous]: " + previous);
				logger.debug("afterDisplayableSelection[current]: " + current);
				logger.debug("fullScreenMode: " + fullScreenMode);
			}
			viewCanvas.setFullScreenMode(fullScreenMode);
		}
	}

	public boolean isPrimaryCommandEnabled() {
		return primaryCommandEnabled;
	}

	public void setPrimaryCommandEnabled(boolean primaryCommandEnabled) {
		boolean changed = primaryCommandEnabled != this.primaryCommandEnabled;
		this.primaryCommandEnabled = primaryCommandEnabled;
		if (changed) {
			repaint();
		}
	}

	public boolean isSecondaryCommandEnabled() {
		return secondaryCommandEnabled;
	}

	public void setSecondaryCommandEnabled(boolean secondaryCommandEnabled) {
		boolean changed = secondaryCommandEnabled != this.secondaryCommandEnabled;
		this.secondaryCommandEnabled = secondaryCommandEnabled;
		if (changed) {
			repaint();
		}
	}

	public boolean isThirdCommandEnabled() {
		return thirdCommandEnabled;
	}

	public void setThirdCommandEnabled(boolean thirdCommandEnabled) {
		boolean changed = thirdCommandEnabled != this.thirdCommandEnabled;
		this.thirdCommandEnabled = thirdCommandEnabled;
		if (changed) {
			repaint();
		}
	}
}
