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
package org.helyx.app.j2me.getabike.lib.ui.view.painter.zone.command;

import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Point;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.AbstractViewCanvasZone;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.zone.command.CommandZone;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.logging4me.Logger;


public abstract class AbstractCommandZone extends AbstractViewCanvasZone implements CommandZone {

	private static final Logger logger = Logger.getLogger("ABSTRACT_MENU_ZONE");
	
	protected Command currentCommandPressed = null;

	private boolean commandPressed = false;
	
	public AbstractCommandZone(AbstractView view) {
		super(view);
	}
	public boolean isCommandPressed() {
		return commandPressed;
	}
	
	public void executePendingCommand() {
		try {
			if (currentCommandPressed != null && currentCommandPressed.isInteractive()) {
				view.fireCommandEvent(currentCommandPressed);
			}
		}
		catch(Throwable t) {
			logger.warn(t);
		}
	}
	
	public Command getCurrentCommandPressed() {
		return currentCommandPressed;
	}

	public void setCurrentCommandPressed(Command currentCommandPressed) {
		boolean stateChanged = this.currentCommandPressed != currentCommandPressed;
		commandPressed = currentCommandPressed != null;
		this.currentCommandPressed = currentCommandPressed;
		
		if (stateChanged) {
			view.getViewCanvas().repaint();
		}
	}

	public void onPointerPressed(int x, int y) {
		setCurrentCommandPressed(computeCurrentCommandPressed(new Point(x, y)));
	}

	public void onPointerDragged(int x, int y) {
		
	}

	public void onPointerReleased(int x, int y) {
		Command currentCommandPressed = computeCurrentCommandPressed(new Point(x, y));
		if (currentCommandPressed == getCurrentCommandPressed()) {
			setCurrentCommandPressed(currentCommandPressed);
			executePendingCommand();
		}
		setCurrentCommandPressed(null);
	}
	
	protected Rectangle computePrimaryCommandArea(Rectangle zoneRect) {
		return computeCommandArea(zoneRect, view.getPrimaryCommand());
	}
	
	protected Rectangle computeSecondaryCommandArea(Rectangle zoneRect) {
		return computeCommandArea(zoneRect, view.getSecondaryCommand());
	}
	
	protected Rectangle computeThirdCommandArea(Rectangle zoneRect) {
		return computeCommandArea(zoneRect, view.getThirdCommand());
	}
	
	public abstract Rectangle computeCommandArea(Rectangle zoneRect, Command command);
	
	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.getabike.lib.ui.view.painter.zone.menu.MenuZone#getCommandPressed(org.helyx.app.j2me.getabike.lib.ui.view.AbstractView, org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle)
	 */

	public Command computeCurrentCommandPressed(Point pointerPosition) {
		Rectangle zoneRect = computeArea();

		if (pointerPosition == null) {
			return null;
		}
		
		if (!zoneRect.contains(pointerPosition)) {
			return null;
		}
		
		Command commandPressed = null;
		
		if (view.getPrimaryCommand() != null && computePrimaryCommandArea(zoneRect).contains(pointerPosition)) {
			commandPressed = view.getPrimaryCommand();
		}
		else if (view.getSecondaryCommand() != null && computeSecondaryCommandArea(zoneRect).contains(pointerPosition)) {
			commandPressed = view.getSecondaryCommand();
		}
		else if (view.getThirdCommand() != null && computeThirdCommandArea(zoneRect).contains(pointerPosition)) {
			commandPressed = view.getThirdCommand();
		}
		else {
			return null;
		}
		
		if (commandPressed != null && !commandPressed.isInteractive()) {
			commandPressed = null;
		}
		
		return commandPressed;
	}

}
