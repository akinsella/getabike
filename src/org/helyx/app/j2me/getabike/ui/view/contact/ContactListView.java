package org.helyx.app.j2me.getabike.ui.view.contact;

import org.helyx.app.j2me.getabike.data.carto.comparator.ContactNameComparator;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.contact.domain.Contact;
import org.helyx.app.j2me.getabike.data.contact.task.ContactLoadTask;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.comparator.Comparator;
import org.helyx.helyx4me.filter.FilterBuilder;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.model.list.IDynamicFilterableSortableElementProvider;
import org.helyx.helyx4me.model.list.IElementProvider;
import org.helyx.helyx4me.model.list.impl.ArrayElementProvider;
import org.helyx.helyx4me.model.list.impl.DynamicFilterableSortableElementProvider;
import org.helyx.helyx4me.model.list.impl.RefElementProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.list.AbstractListView;
import org.helyx.helyx4me.ui.view.support.menu.MenuListView;
import org.helyx.helyx4me.ui.view.support.task.LoadTaskView;
import org.helyx.helyx4me.ui.view.transition.BasicTransition;
import org.helyx.helyx4me.ui.widget.command.Command;
import org.helyx.logging4me.Logger;


public class ContactListView extends AbstractListView {
	
	private static final Logger logger = Logger.getLogger("CONTACT_LIST_VIEW");
	
	private RefElementProvider refContactProvider;
	private IDynamicFilterableSortableElementProvider filteredSortedElementProvider;

	private FilterBuilder filterBuilder;

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
		
		setThirdCommand(new Command("command.menu", true, getMidlet().getI18NTextRenderer(), new IAction() {
			public void run(Object data) {
				showMenuView();
			}
		}));

		setPrimaryCommand(new Command("command.select", true, getMidlet().getI18NTextRenderer(), new IAction() {
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

	protected void showMenuView() {

		MenuListView menuListView = new MenuListView(getMidlet(), "view.menu.title", false);
		menuListView.setPreviousDisplayable(ContactListView.this);
		showDisplayable(menuListView, new BasicTransition());	
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

	protected void onCheckItemSelected(Object object) {
		checkItemSelected((Contact)object);
	}

	protected void checkItemSelected(Contact contact) {
		
		if (contact == null) {
			return;
		}
		
	}

	public void loadListContent() {
		ContactLoadTask contactLoadTask = new ContactLoadTask(this);
		contactLoadTask.addProgressListener(new ProgressAdapter("UI_CONTACT_PROGRESS_TAKS_LISTENER") {
			
			public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
				ContactListView contactListView = ContactListView.this;
				switch (eventType) {
					case EventType.ON_SUCCESS:
						setContactProvider(new ArrayElementProvider((Contact[])eventData));
						
						contactListView.showDisplayable(contactListView, new BasicTransition());

						break;

					case EventType.ON_ERROR:
						Throwable throwable = (Throwable)eventData;
						getLogger().warn(throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
						DialogUtil.showAlertMessage(contactListView, "dialog.title.error", contactListView.getMessage("dialog.error.unexpected") + ": " + throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
						contactListView.showDisplayable(contactListView, new BasicTransition());
						break;
						
					default:
						DialogUtil.showAlertMessage(contactListView, "dialog.title.error", contactListView.getMessage("dialog.result.unexpected"));
						contactListView.showDisplayable(contactListView, new BasicTransition());
						break;
				}
			}
		});
		
		LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "view.contact.list.load.contact", contactLoadTask);
		showDisplayable(loadTaskView, this);
		resetPosition();
		logger.info("Load List Content...");
		loadTaskView.startTask();
	}

	public void setComparator(Comparator comparator) {
		filteredSortedElementProvider.setComparator(comparator);
	}

	public void setContactProvider(IElementProvider stationsProvider) {
		refContactProvider.setElementProvider(stationsProvider);
		filterAndSort();
	}

	public void setFilterBuilder(FilterBuilder filterBuilder) {
		this.filterBuilder = filterBuilder;
	}

	public boolean isEmpty() {
		return getElementProvider().length() <= 0;
	}
	
}
