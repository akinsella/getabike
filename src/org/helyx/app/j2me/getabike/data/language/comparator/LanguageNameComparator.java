package org.helyx.app.j2me.velocite.data.language.comparator;

import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.helyx4me.comparator.Comparator;
import org.helyx.logging4me.Logger;



public class LanguageNameComparator implements Comparator {

	private static final Logger logger = Logger.getLogger("LANGUAGE_NAME_COMPARATOR");
	
	public int compare(Object object1, Object object2) {
		if (object1 == null && object2 == null) {
			return 0;
		}
		if (object1 == null) {
			return -1;
		}
		if (object2 == null) {
			return 1;
		}
		
		int result = ((Language)object1).name.compareTo(((Language)object2).name);
		
		return result;
	}

}
