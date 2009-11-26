package org.helyx.app.j2me.getabike.data.contact.comparator;

import org.helyx.app.j2me.getabike.data.contact.domain.Contact;
import org.helyx.helyx4me.comparator.Comparator;

public class ContactNameComparator implements Comparator {

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

		return ((Contact)object1).renderDefaultText().compareTo(((Contact)object2).renderDefaultText());
	}

}
