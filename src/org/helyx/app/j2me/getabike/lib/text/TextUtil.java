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
package org.helyx.app.j2me.getabike.lib.text;

import java.util.Vector;

public class TextUtil {

	
	private TextUtil()  {
		super();
	}
	
	
	public static String[] split(final String str, final char c) {

		final Vector result = new Vector();
		
		int pos = 0;
		while (true) {
			final int cut = str.indexOf(c, pos);
			if (cut == -1) {
				result.addElement(str.substring(pos));
				break;
			}
			result.addElement(str.substring(pos, cut));
			pos = cut + 1;
		}

		String[] stringArray = new String[result.size()];
		result.copyInto(stringArray);
		
		return stringArray;
	}

	public static String urlEncode(String str) {
		StringBuffer buf = new StringBuffer();
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
				buf.append(c);
			}
			else {
				buf.append("%").append(Integer.toHexString((int)str.charAt(i)));
			}
		}
		return buf.toString();
	}

	public static String replaceAll(String haystack, char needle, char replacement) {
		int length = haystack.length();
		
		StringBuffer sb = new StringBuffer( length );

		for (int i = 0 ; i < length ; i++) {
	
			char c = haystack.charAt(i);
			if (c == needle) {
				sb.append( replacement );
			}
			else {
				sb.append( c );
			}
		}

		return sb.toString();
	}
	
	public static String replaceAll(String haystack, String needle, String replacement) {
		
		if (needle == null) {
			return haystack;
		}
		
		if (replacement == null) {
			replacement = "";
		}
		
		int needleLength = needle.length();
		
		if (needleLength == 0) {
			return haystack;
		}

		StringBuffer result = new StringBuffer();

		int searchPos = 0;

		while (true) {
			final int pos = haystack.indexOf(needle, searchPos);
			if (pos == -1) { 
				break;
			}

			result.append(haystack.substring(searchPos, pos));
			result.append(replacement);

			searchPos = pos + needle.length();
		}

		result.append(haystack.substring(searchPos));

		return result.toString();
	}


}
