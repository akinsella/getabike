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
package org.helyx.app.j2me.getabike.lib.test.list;

import org.helyx.app.j2me.getabike.lib.localization.Point;



public class Station {
	
	public String name;
	
	public int number;
	
	public String address;
	
	public String fullAddress;
	
	public String zipCode;
	
	public String city;
	
	public boolean open;
	
	public boolean hasLocalization;
	
	public Point localization;
	
	public boolean bonus;

	public Station() {
		super();
	}	

	public String toString() {
		StringBuffer sb = new StringBuffer("[Station]")
		.append(" name=").append(name)
		.append(", number=").append(number)
		.append(", address=").append(address)
		.append(", fullAddress=").append(fullAddress)
		.append(", zipCode=").append(zipCode)
		.append(", city=").append(city)
		.append(", open=").append(open)
		.append(", bonus").append(bonus)
		.append(", hasLocalization=").append(hasLocalization)
		.append(", localization=").append(localization);

		return sb.toString();
	}

}
