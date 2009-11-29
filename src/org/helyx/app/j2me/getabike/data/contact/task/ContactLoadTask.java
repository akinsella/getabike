package org.helyx.app.j2me.getabike.data.contact.task;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.pim.ContactList;
import javax.microedition.pim.PIM;

import org.helyx.app.j2me.getabike.data.contact.domain.Contact;
import org.helyx.helyx4me.task.AbstractProgressTask;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.logging4me.Logger;

public class ContactLoadTask extends AbstractProgressTask {

	private static final Logger logger = Logger.getLogger("CONTACT_LOAD_TASK");

	private AbstractView view;

	public ContactLoadTask(AbstractView view) {
		super("CONTACT_LIST_VIEW");
	}

	public Runnable getRunnable() {
		return new Runnable() {
			
			public void run() {
				Vector contactList = new Vector();

				try {
					getProgressDispatcher().fireEvent(EventType.ON_START);

					Enumeration _enumContact = ((ContactList)PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY)).items();

					while (_enumContact.hasMoreElements()) {
						javax.microedition.pim.Contact pimContact = (javax.microedition.pim.Contact) _enumContact.nextElement();
						int phoneNumbers = pimContact.countValues(javax.microedition.pim.Contact.TEL);
						for (int i = 0; i < phoneNumbers; i++) {
							String phoneNumber = pimContact.getString(javax.microedition.pim.Contact.TEL, i);

							Contact contact = new Contact();
							contact.setName(getContactName(pimContact));
							contact.setPhoneNumber(phoneNumber);
							contactList.addElement(contact);
							
						}
					}

					Contact[] contacts = new Contact[contactList.size()];
					contactList.copyInto(contacts);
					getProgressDispatcher().fireEvent(EventType.ON_SUCCESS, contacts);

				} catch (Throwable t) {
					getProgressDispatcher().fireEvent(EventType.ON_ERROR, t);
					logger.warn(t);
				}
			}
			

		   private String getContactName(javax.microedition.pim.Contact item) {
		        int fieldCode = javax.microedition.pim.Contact.FORMATTED_NAME;

				if (item.countValues(fieldCode) != 0) {
					String contactName = item.getString(fieldCode, 0);

					if ((contactName != null) && (contactName.trim().length() > 0)) {
						return contactName;
					}
				}

				if (item.countValues(javax.microedition.pim.Contact.NAME) != 0) {
					String[] contactNames = item.getStringArray(javax.microedition.pim.Contact.NAME, 0);

					if (contactNames != null) {
						StringBuffer sb = new StringBuffer();

						if (contactNames[javax.microedition.pim.Contact.NAME_GIVEN] != null) {
							sb.append(contactNames[javax.microedition.pim.Contact.NAME_GIVEN]);
						}

						if (contactNames[javax.microedition.pim.Contact.NAME_FAMILY] != null) {
							if (sb.length() > 0) {
								sb.append(" ");
							}

							sb.append(contactNames[javax.microedition.pim.Contact.NAME_FAMILY]);
						}

						String contactName = sb.toString().trim();

						return contactName;
					}
				}
				
				return null;

			}

		};
	}

}
