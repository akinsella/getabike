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
package org.helyx.app.j2me.getabike.lib.ui.view.support.dialog;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.WordWrapUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogCommand;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.DialogResultConstants;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.logging4me.Logger;


public class DialogView extends AbstractView {
	
	private static final Logger logger = Logger.getLogger("DIALOG_VIEW");
		
	private String text;
	
	private int resultCode = DialogResultConstants.NULL;
	
	private DialogCommand primaryDialogCommand;
	private DialogCommand secondaryDialogCommand;
	private DialogCommand thirdDialogCommand;

	public DialogView(AbstractMIDlet midlet, String title, String text, DialogCommand primaryCommand) {
		this(midlet, title, text, primaryCommand, null, null);
	}

	public DialogView(AbstractMIDlet midlet, String title, String text, DialogCommand primaryCommand, DialogCommand secondaryCommand) {
		this(midlet, title, text, primaryCommand, secondaryCommand, null);
	}
	
	public DialogView(AbstractMIDlet midlet, String title, String text, DialogCommand primaryDialogCommand, DialogCommand secondaryDialogCommand, DialogCommand thirdDialogCommand) {
		super(midlet);
	    setTitle(title);
//	    setPaintScrollBar(true);
//	    setScreenDragging(true);
	    this.text = text;
	    this.primaryDialogCommand = primaryDialogCommand;
	    this.secondaryDialogCommand = secondaryDialogCommand;
	    this.thirdDialogCommand = thirdDialogCommand;
		init();
	}

	private void init() {
		setFullScreenMode(true);
		if (primaryDialogCommand != null) {
			setPrimaryCommand(createCommand(getMidlet(), primaryDialogCommand));
		}
		if (secondaryDialogCommand != null) {
			setSecondaryCommand(createCommand(getMidlet(), secondaryDialogCommand));
		}
		if (thirdDialogCommand != null) {
			setThirdCommand(createCommand(getMidlet(), thirdDialogCommand));
		}
	}
	
	private Command createCommand(AbstractMIDlet midlet, final DialogCommand dialogCommand) {
		Command command = new Command(dialogCommand.getText(), midlet.getI18NTextRenderer(), new IAction() {

			public void run(Object data) {
				resultCode = dialogCommand.getResultCode();
				fireReturnCallback();
			}
			
		});
		
		return command;
	}

	protected void paint(Graphics g) {
		Rectangle clientArea = computeClientArea();
        
		int x = clientArea.location.x;
		int y = clientArea.location.y;
		int width = clientArea.size.width;
        int height = clientArea.size.height;

        g.setColor(getTheme().getColor(ThemeConstants.WIDGET_FONT).intValue());
        g.setFont(FontUtil.MEDIUM_BOLD);
        WordWrapUtil.paint(g, text, x + width / 2, y + height / 2, width - 10, Graphics.HCENTER | Graphics.VCENTER);
	}

	public int getResultCode() {
		return resultCode;
	}

}
