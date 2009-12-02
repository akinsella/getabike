package org.helyx.app.j2me.getabike.ui.view.contact;

import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.contact.comparator.ContactNameComparator;
import org.helyx.app.j2me.getabike.data.contact.domain.Contact;
import org.helyx.app.j2me.getabike.data.contact.task.ContactLoadTask;
import org.helyx.app.j2me.getabike.ui.view.renderer.ContactItemRenderer;
import org.helyx.app.j2me.getabike.util.ErrorManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.comparator.Comparator;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.model.list.IDynamicFilterableSortableElementProvider;
import org.helyx.helyx4me.model.list.IElementProvider;
import org.helyx.helyx4me.model.list.impl.ArrayElementProvider;
import org.helyx.helyx4me.model.list.impl.DynamicFilterableSortableElementProvider;
import org.helyx.helyx4me.model.list.impl.RefElementProvider;
import org.helyx.helyx4me.task.AbstractProgressTask;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.displayable.callback.BasicReturnCallback;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.YesNoResultCallback;
import org.helyx.helyx4me.ui.view.support.list.AbstractListView;
import org.helyx.helyx4me.ui.view.support.task.LoadTaskView;
import org.helyx.helyx4me.ui.view.transition.BasicTransition;
import org.helyx.helyx4me.ui.widget.command.Command;
import org.helyx.logging4me.Logger;


public class ContactListView extends AbstractListView {
	
	private static final Logger logger = Logger.getLogger("CONTACT_LIST_VIEW");
	
	private RefElementProvider refContactProvider;
	private IDynamicFilterableSortableElementProvider filteredSortedElementProvider;

	public ContactListView(AbstractMIDlet midlet, String title) {
		super(midlet, title);
		setTitle(title);
		init();
	}
	
	private void init() {
		initActions();
		initData();
		initComponents();
	}

	protected void initActions() {

		setPrimaryCommand(new Command("command.validate", true, getMidlet().getI18NTextRenderer(), new IAction() {
			public void run(Object data) {
				checkSelectedItem();
			}
		}));
	
		setSecondaryCommand(new Command("command.back", true, getMidlet().getI18NTextRenderer(), new IAction() {
			public void run(Object data) {
				fireReturnCallback();
			}
		}));

	}
	
	protected Contact getContactSelected() {
		if (getElementProvider().length() <= 0) {
			return null;
		}
		return (Contact)getElementProvider().get(selectedOffset);
	}
	
	protected void checkSelectedItem() {
		if (getElementProvider().length() <= 0) {
			return ;
		}		
		onShowItemSelected(getElementProvider().get(selectedOffset));
	}

	protected void initData() {
		setCellRenderer(new ContactItemRenderer());
		IElementProvider arrayStationsProvider = new ArrayElementProvider(new Station[0]);
		refContactProvider = new RefElementProvider(arrayStationsProvider);
		filteredSortedElementProvider = new DynamicFilterableSortableElementProvider(refContactProvider);
		filteredSortedElementProvider.setComparator(new ContactNameComparator());
		setItems((IElementProvider)filteredSortedElementProvider);
	}

	protected void initComponents() {
		
	}
	
	public Contact[] getAllContacts() {
		return (Contact[])refContactProvider.getElementProvider().getElements();
	}
	
	public void filterAndSort() {
		
		resetPosition();
		filteredSortedElementProvider.filterAndSort();
	}

	protected void onShowItemSelected(Object object) {
		showItemSelected((Contact)object);
	}

	protected void showItemSelected(final Contact contact) {
		
		if (contact == null) {
			return;
		}

		DialogUtil.showYesNoDialog(
				ContactListView.this, 
				"dialog.title.question", 
				getMessage("view.contact.sms.send"), 
				new YesNoResultCallback() {
					
					public void onYes(DialogView dialogView, Object data) {
						sendSmsToContact(contact);
					}
					
					public void onNo(DialogView dialogView, Object data) {
						fireReturnCallback();
					}
				});
	}
	

	void sendSmsToContact(final Contact contact) {
		if (logger.isDebugEnabled()) {
			logger.debug("Try to send SMS to contact: '" + contact.getName() + "'");
		}
		
		try {
			IProgressTask progressTask = new AbstractProgressTask("") {
				
				public Runnable getRunnable() {
					return new Runnable() {
						public void run() {

							getProgressDispatcher().fireEvent(EventType.ON_START);

							String message = getMessage("view.contact.sms.message");

							MessageConnection conn = null;
							try {
								String addr = "sms://" + contact.getFormattedPhoneNumber();

								conn = (MessageConnection)Connector.open(addr);

								TextMessage msg = (TextMessage)conn.newMessage(MessageConnection.TEXT_MESSAGE);
								msg.setPayloadText(message);

								conn.send(msg);

								getProgressDispatcher().fireEvent(EventType.ON_SUCCESS);
							}
							catch (Throwable t){
								logger.warn(t);
								getProgressDispatcher().fireEvent(EventType.ON_ERROR, t);
							}
							finally {
								try { if (conn != null) { conn.close(); } } catch(Throwable t) { logger.warn(t); }
							}
						}
					};
				}
				
			};
			
			final LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "view.contact.sms.send", progressTask);
			loadTaskView.setReturnCallback(new BasicReturnCallback(this));
			progressTask.addProgressListener(new ProgressAdapter("Loading station details") {

				public void onSuccess(String eventMessage, Object eventData) {
					DialogUtil.showMessageDialog(
							ContactListView.this, 
							"dialog.title.error", 
							getMessage("view.contact.sms.sent.to", new Object[] { contact.getName() }), 
							new OkResultCallback() {
								public void onOk(DialogView dialogView, Object data) {
									loadTaskView.fireReturnCallback();
								}
							});
				}

				public void onError(String eventMessage, Object eventData) {
					if (ContactListView.logger.isInfoEnabled()) {
						ContactListView.logger.info("Error: " + eventMessage + ", data: " + eventData);
					}
					
					Throwable t = (Throwable)eventData;

					DialogUtil.showMessageDialog(
							ContactListView.this, 
							"dialog.title.error", 
							getMessage("dialog.title.error") + ": " + ErrorManager.getErrorMessage(getMidlet(), t), 
							new OkResultCallback() {
								public void onOk(DialogView dialogView, Object data) {
									loadTaskView.fireReturnCallback();
								}
							});
				}
				
			});
			showDisplayable(loadTaskView);
			loadTaskView.startTask();
		}
		catch (Throwable t) {
			logger.warn(t);
			DialogUtil.showAlertMessage(
					ContactListView.this, 
					"dialog.title.error", 
					getMessage("dialog.error.unexpected") + ": " + ErrorManager.getErrorMessage(getMidlet(), t));
		}
	}

	

	public void loadListContent() {
		ContactLoadTask contactLoadTask = new ContactLoadTask(this);
		final LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "view.contact.list.load.contact", contactLoadTask);
		loadTaskView.setReturnCallback(new BasicReturnCallback(this));
		contactLoadTask.addProgressListener(new ProgressAdapter("UI_CONTACT_PROGRESS_TAKS_LISTENER") {

			public void onSuccess(String eventMessage, Object eventData) {
				setContactProvider(new ArrayElementProvider((Contact[])eventData));
				
				loadTaskView.showDisplayable(ContactListView.this, new BasicTransition());
			}
			
			public void onError(String eventMessage, Object eventData) {
				if (ContactListView.logger.isInfoEnabled()) {
					ContactListView.logger.info("Error: " + eventMessage + ", data: " + eventData);
				}
				
				Throwable t = (Throwable)eventData;

				DialogUtil.showMessageDialog(
						ContactListView.this, 
						"dialog.title.error", 
						getMessage("dialog.title.error") + ": " + ErrorManager.getErrorMessage(getMidlet(), t), 
						new OkResultCallback() {
							public void onOk(DialogView dialogView, Object data) {
								loadTaskView.fireReturnCallback();
							}
						});
			}

		});

		showDisplayable(loadTaskView);
		
		resetPosition();

		logger.info("Load List Content...");
		loadTaskView.startTask();
	}

	public void setComparator(Comparator comparator) {
		filteredSortedElementProvider.setComparator(comparator);
	}

	public void setContactProvider(IElementProvider contactProvider) {
		refContactProvider.setElementProvider(contactProvider);
		filterAndSort();
	}

	public boolean isEmpty() {
		return getElementProvider().length() <= 0;
	}
	
}
