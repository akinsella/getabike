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

import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogCommand;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.DialogResultConstants;
import org.helyx.logging4me.Logger;


public class DialogCommand {

	private static final Logger logger = Logger.getLogger("DIALOG_COMMAND");

	public static final DialogCommand YES = new DialogCommand("command.yes", DialogResultConstants.YES);
	public static final DialogCommand NO = new DialogCommand("command.no", DialogResultConstants.NO);

	public static final DialogCommand OK = new DialogCommand("command.ok", DialogResultConstants.OK);
	public static final DialogCommand CANCEL = new DialogCommand("command.cancel", DialogResultConstants.CANCEL);

	private String text;
	
	private int resultCode;
	
	public DialogCommand(String text, int resultCode) {
		super();
		this.text = text;
		this.resultCode = resultCode;
	}

	public String getText() {
		return text;
	}

	public int getResultCode() {
		return resultCode;
	}
	
}
