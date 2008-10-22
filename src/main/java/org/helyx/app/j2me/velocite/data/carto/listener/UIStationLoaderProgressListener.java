package org.helyx.app.j2me.velocite.data.carto.listener;

import org.helyx.app.j2me.lib.filter.IObjectFilter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.lib.ui.view.support.list.ArrayElementProvider;
import org.helyx.app.j2me.lib.ui.view.support.list.FilterableElementProvider;
import org.helyx.app.j2me.lib.ui.view.support.list.IElementProvider;
import org.helyx.app.j2me.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.velocite.ui.view.StationListView;

public class UIStationLoaderProgressListener extends ProgressAdapter {

	private static final Log log = LogFactory.getLog("STATION_LOADING_PROGRESS_LISTENER");
	
	private StationListView stationListView;
	private IObjectFilter objectFilter;
	
	public UIStationLoaderProgressListener(StationListView stationListView) {
		super();
		this.stationListView = stationListView;
	}
	
	public UIStationLoaderProgressListener(StationListView stationListView, IObjectFilter objectFilter) {
		super();
		this.stationListView = stationListView;
		this.objectFilter = objectFilter;
	}
	
	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
		switch (eventType) {
			case ProgressEventType.ON_SUCCESS:
				IElementProvider elementProvider = new ArrayElementProvider((Object[])eventData);
				if (objectFilter != null) {
					IElementProvider filteredElementProvider = new FilterableElementProvider(elementProvider, objectFilter);
					stationListView.setItems(filteredElementProvider);
				}
				else {
					stationListView.setItems(elementProvider);					
				}
				stationListView.showDisplayable(stationListView, new BasicTransition());
				break;
				
			case ProgressEventType.ON_CANCEL:
				stationListView.showDisplayable(stationListView, new BasicTransition());
				break;

			case ProgressEventType.ON_ERROR:
				Throwable throwable = (Throwable)eventData;
				getLog().warn(throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
				DialogUtil.showAlertMessage(stationListView.getMidlet(), stationListView, "Erreur", throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
				stationListView.showDisplayable(stationListView, new BasicTransition());
				break;
				
			default:
				break;
		}
	}
	
};

