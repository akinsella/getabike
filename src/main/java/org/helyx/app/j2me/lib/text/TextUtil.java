package org.helyx.app.j2me.lib.text;

import java.util.Vector;

public class TextUtil {

	private static final String CAT = "TEXT_UTIL";

	public static String[] split(final String str, final char c) {

		final Vector result = new Vector();
		
		int pos = 0;
		while (true) {
			final int cut = str.indexOf(c, pos);
			if (cut == -1) {
				break;
			}
			result.addElement(str.substring(pos, pos + cut));
			pos = cut + 1;
		}

		String[] stringArray = new String[result.size()];
		result.copyInto(stringArray);
		
		return stringArray;
	}

	public static String replaceAll(String haystack, char needle, char replacement) {
		int length = haystack.length();
		
		StringBuffer sb = new StringBuffer( length );

		for (int i=0 ; i < length ; i++) {
	
			char c = haystack.charAt(i);
			switch( c )
			{
				case '.':
					sb.append( '/' );
					break;
		
				default:
					sb.append( c );
			}
		}

		return sb.toString();
	}
	
	public static String replaceAll(String haystack, String needle, String replacement) {
		int needleLength = needle.length();
		
		if (needle == null || needleLength == 0) {
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

		if (result.length() == 0) {
			return haystack;
		}

		result.append(haystack.substring(searchPos));

		return result.toString();
	}

}
