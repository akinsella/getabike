package org.helyx.app.j2me.getabike.data.contact.task;

import java.util.Enumeration;
import java.util.Vector;

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
		Vector contactList = new Vector();
		
		try {
			getProgressDispatcher().fireEvent(EventType.ON_START);
			
			Contact contact = new Contact();
			
			Enumeration _enumContact = PIM.getInstance().;
			while(_enumContact.hasMoreElements()) {
				javax.microedition.pim.Contact pimContact = (javax.microedition.pim.Contact)_enumContact.nextElement();
			}
			
			getProgressDispatcher().fireEvent(EventType.ON_START);
			
		}
		catch(Throwable t) {
			getProgressDispatcher().fireEvent(EventType.ON_ERROR, t);
			logger.warn(t);
		}
	}

}
