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
package org.helyx.app.j2me.getabike.lib.ui.geometry;

import org.helyx.app.j2me.getabike.lib.ui.geometry.Point;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Size;

public class Rectangle {

	public Point location;
	public Size size;
	
	public Rectangle(Point location, Size size) {
		super();
		this.location = location;
		this.size = size;
	}
	
	public Rectangle(int x, int y, int width, int height) {
		this(new Point(x, y), new Size(width, height));
	}

	public Rectangle() {
		super();
	}
	
	public boolean contains(Point point) {
		if (point == null) {
			return false;
		}
		
		return 
			point.x >= this.location.x && 
			point.x <= this.location.x + this.size.width &&
			point.y >= this.location.y && 
			point.y <= this.location.y + this.size.height;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[Rectangle] ")
		.append(" location=").append(location)
		.append(", size=").append(size);

		return sb.toString();
	}

}
