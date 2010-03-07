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
package org.helyx.app.j2me.getabike.lib.ui.graphics;

import org.helyx.app.j2me.getabike.lib.ui.util.ColorUtil;



public class Color {

	public int red;
	public int green;
	public int blue;
	
	public Color() {
		super();
	}

	public Color(int red, int green, int blue) {
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Color(int intColor) {
		super();
		ColorUtil.intToColor(this, intColor);
	}

	public Color(String colorValue) {
		super();
		if (colorValue.indexOf('#') == 0) {
			ColorUtil.parseHtmlColor(this, colorValue);
		}
		else {
			ColorUtil.parseHexaColor(this, colorValue);
		}
	}

	public int intValue() {
        return ColorUtil.colorToInt(this);
	}
		
	public String toString() {
		StringBuffer sb = new StringBuffer("[Color] ")
		.append(" red=").append(red)
		.append(", green=").append(green)
		.append(", blue=").append(blue);

		return sb.toString();
	}
	
}
