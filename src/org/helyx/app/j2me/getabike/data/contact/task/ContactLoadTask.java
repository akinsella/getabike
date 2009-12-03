package org.helyx.app.j2me.getabike.data.contact.task;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.pim.ContactList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMItem;

import org.helyx.app.j2me.getabike.data.contact.domain.Contact;
import org.helyx.helyx4me.task.AbstractProgressTask;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.logging4me.Logger;

public class ContactLoadTask extends AbstractProgressTask {

	private static final Logger logger = Logger.getLogger("CONTACT_LOAD_TASK");

	protected static final String CONTACT_UNKNOWN = "UNKNONWN";

	private AbstractView view;

	public ContactLoadTask(AbstractView view) {
		super("CONTACT_LIST_VIEW");
		this.view = view;
	}

	public Runnable getRunnable() {
		return new Runnable() {
			
			public void run() {
				Vector contactList = new Vector();

				try {
					getProgressDispatcher().fireEvent(EventType.ON_START);
					getProgressDispatcher().fireEvent(EventType.ON_PROGRESS, "task.contact.load.data.read");
					ContactList pimContactList = (ContactList)PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY);
					Enumeration _enumContact = pimContactList.items();
					int count = 0;
					while (_enumContact.hasMoreElements()) {
						javax.microedition.pim.Contact pimContact = (javax.microedition.pim.Contact) _enumContact.nextElement();
						int phoneNumbers = pimContact.countValues(javax.microedition.pim.Contact.TEL);
						for (int i = 0; i < phoneNumbers; i++) {
							count++;
							String phoneNumber = pimContact.getString(javax.microedition.pim.Contact.TEL, i);

							Contact contact = new Contact();
							contact.setName(getContactName(pimContactList, pimContact));
							contact.setPhoneNumber(phoneNumber);
							contactList.addElement(contact);
							
							if (count % 5 == 0) {
								getProgressDispatcher().fireEvent(EventType.ON_PROGRESS, count + " " + view.getMessage("task.contact.load.data.loaded"));
							}
						}
					}

					Contact[] contacts = new Contact[contactList.size()];
					contactList.copyInto(contacts);
					getProgressDispatcher().fireEvent(EventType.ON_SUCCESS, contacts);

				}
				catch (Throwable t) {
					getProgressDispatcher().fireEvent(EventType.ON_ERROR, t);
					logger.warn(t);
				}
			}
			

		   private String getContactName(ContactList contactList, javax.microedition.pim.Contact item) {
			   try {
					if (contactList.isSupportedField(javax.microedition.pim.Contact.FORMATTED_NAME)) {
						String contactName = item.getString(javax.microedition.pim.Contact.FORMATTED_NAME, PIMItem.ATTR_NONE);

						if ((contactName != null) && (contactName.trim().length() > 0)) {
							return contactName;
						}
					}
					
					
					if (item.countValues(javax.microedition.pim.Contact.NAME) > 0) {
						String[] names = item.getStringArray(javax.microedition.pim.Contact.NAME, javax.microedition.pim.Contact.ATTR_NONE);
						
						String firstName = contactList.isSupportedArrayElement(javax.microedition.pim.Contact.NAME, javax.microedition.pim.Contact.NAME_GIVEN) ? 
								names[javax.microedition.pim.Contact.NAME_GIVEN] : null;
						String lastName = contactList.isSupportedArrayElement(javax.microedition.pim.Contact.NAME, javax.microedition.pim.Contact.NAME_GIVEN) ? 
								names[javax.microedition.pim.Contact.NAME_FAMILY] : null;

						if (firstName != null || lastName != null) {
							StringBuffer sb = new StringBuffer();
			
							if ( firstName != null ) {
								sb.append(firstName);
								if (firstName.length() > 0) {
									sb.append(" ");
								}
							}
			
							if (lastName != null && sb.length() > 0) {
								sb.append(lastName);
							}
			
							String contactName = sb.toString().trim();
			
							return contactName;
						}
					}
					
					if (contactList.isSupportedField(javax.microedition.pim.Contact.NICKNAME)) {
						String contactName = item.getString(javax.microedition.pim.Contact.NICKNAME, 0);

						if ((contactName != null) && (contactName.trim().length() > 0)) {
							return contactName;
						}
					}
				   
					return CONTACT_UNKNOWN;
			   }
			   catch(Throwable t) {
				   logger.warn(t);
				   return CONTACT_UNKNOWN;
			   }
			}

		};
	}

}
