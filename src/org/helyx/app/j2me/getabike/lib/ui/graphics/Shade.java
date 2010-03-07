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

import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.util.ColorUtil;

public class Shade {
	
	public Color src;
	public Color dest;

	/**
	* Crée un dégradé de noir sur blanc
	*/
	public Shade() {
		src = new Color(0, 0, 0);
		dest = new Color(255, 255, 255);
	}

	public Shade(int srcColor, int destColor) {
		src = ColorUtil.intToColor(srcColor);
		dest = ColorUtil.intToColor(destColor);
	}
	
	/**
	* Dégradé vers le blanc avec composantes Décimales
	* @param r Composante rouge
	* @param v Composante verte
	* @param b Composante bleu
	*/
	public Shade(Color srcColor){
		src = srcColor;
		dest = new Color(0, 0, 0);
	}

	/**
	 * Obtenir un dégradé depuis deux couleurs de la classe Color
	 * 
	 * @param c1 Couleur 1
	 * @param c2 Couleur 2
	 */
	public Shade(Color srcColor, Color destColor){
		src = srcColor;
		dest = destColor;
	
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[Shade] ")
		.append(" src=").append(src)
		.append(", dest=").append(dest);

		return sb.toString();
	}
	
}
