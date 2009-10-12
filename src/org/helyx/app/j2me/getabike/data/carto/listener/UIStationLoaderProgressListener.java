package org.helyx.app.j2me.getabike.data.carto.listener;

import org.helyx.app.j2me.getabike.ui.view.StationListView;
import org.helyx.helyx4me.comparator.Comparator;
import org.helyx.helyx4me.filter.IObjectFilter;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.list.ArrayElementProvider;
import org.helyx.helyx4me.ui.view.support.list.FilterableSortableElementProvider;
import org.helyx.helyx4me.ui.view.support.list.IElementProvider;
import org.helyx.helyx4me.ui.view.transition.BasicTransition;
import org.helyx.logging4me.Logger;

import com.sun.perseus.parser.ViewBoxParser;


public class UIStationLoaderProgressListener extends ProgressAdapter {

	private static final Logger logger = Logger.getLogger("STATION_LOADING_PROGRESS_LISTENER");
	
	private StationListView stationListView;
	private IObjectFilter objectFilter;
	private Comparator comparator;
	
	public UIStationLoaderProgressListener(StationListView stationListView) {
		super(logger.getCategory().getName());
		this.stationListView = stationListView;
	}
	
	public UIStationLoaderProgressListener(StationListView stationListView, IObjectFilter objectFilter, Comparator comparator) {
		super(logger.getCategory().getName());
		this.stationListView = stationListView;
		this.objectFilter = objectFilter;
		this.comparator = comparator;
	}
	
	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
		switch (eventType) {
			case EventType.ON_SUCCESS:
				IElementProvider elementProvider = new ArrayElementProvider((Object[])eventData);
				if (objectFilter != null || comparator != null) {
					IElementProvider filteredSortedElementProvider = new FilterableSortableElementProvider(elementProvider, objectFilter, comparator);
					stationListView.setItems(filteredSortedElementProvider);
				}
				else {
					stationListView.setItems(elementProvider);					
				}
				
				stationListView.showDisplayable(stationListView, new BasicTransition());
				break;

			case EventType.ON_ERROR:
				Throwable throwable = (Throwable)eventData;
				getLogger().warn(throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
				DialogUtil.showAlertMessage(stationListView, "dialog.title.error", stationListView.getMessage("dialog.error.unexpected") + ": " + throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
				stationListView.showDisplayable(stationListView, new BasicTransition());
				break;
				
			default:
				DialogUtil.showAlertMessage(stationListView, "dialog.title.error", stationListView.getMessage("dialog.result.unexpected"));
				stationListView.showDisplayable(stationListView, new BasicTransition());
				break;
		}
	}
	
};

