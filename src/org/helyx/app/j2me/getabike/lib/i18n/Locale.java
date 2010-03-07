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
package org.helyx.app.j2me.getabike.lib.i18n;

import org.helyx.app.j2me.getabike.lib.i18n.Locale;
import org.helyx.logging4me.Logger;


public class Locale {

	private static final Logger logger = Logger.getLogger("LOCALE");
	
	public static final Locale FRANCE = new Locale("fr", "FR");
	public static final Locale ENGLAND = new Locale("en", "EN");
	public static final Locale SPAIN = new Locale("es", "ES");

	private String country;
	private String language;
	private String name;
	
	public Locale(String country, String language) {
		super();
		this.country = country;
		this.language = language;
		name = country + "_" + language;
	}

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}

	public String getLanguage() {
		return language;
	}

}
