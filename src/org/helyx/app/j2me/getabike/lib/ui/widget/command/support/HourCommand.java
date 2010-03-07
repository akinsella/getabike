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
package org.helyx.app.j2me.getabike.lib.ui.widget.command.support;

import java.util.Calendar;

import org.helyx.app.j2me.getabike.lib.renderer.text.DefaultTextRenderer;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.logging4me.format.date.DateFormatter;

public class HourCommand extends Command {

	private static final DateFormatter df = new DateFormatter("HH:mm");
	
	public HourCommand() {
		super(null, false, new DefaultTextRenderer(), null);
	}

	public String getText() {
		return df.format(Calendar.getInstance());
	}

}
