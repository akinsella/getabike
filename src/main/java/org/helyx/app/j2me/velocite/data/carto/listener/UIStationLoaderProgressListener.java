package org.helyx.app.j2me.velocite.data.carto.listener;

import org.helyx.app.j2me.lib.filter.IObjectFilter;
import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.lib.ui.view.support.list.ArrayElementProvider;
import org.helyx.app.j2me.lib.ui.view.support.list.FilterableElementProvider;
import org.helyx.app.j2me.lib.ui.view.support.list.IElementProvider;
import org.helyx.app.j2me.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.velocite.ui.view.StationListView;

public class UIStationLoaderProgressListener extends ProgressAdapter {

	private static final Logger logger = LoggerFactory.getLogger("STATION_LOADING_PROGRESS_LISTENER");
	
	private StationListView stationListView;
	private IObjectFilter objectFilter;
	
	public UIStationLoaderProgressListener(StationListView stationListView) {
		super(logger.getCategory());
		this.stationListView = stationListView;
	}
	
	public UIStationLoaderProgressListener(StationListView stationListView, IObjectFilter objectFilter) {
		super(logger.getCategory());
		this.stationListView = stationListView;
		this.objectFilter = objectFilter;
	}
	
	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
		switch (eventType) {
			case EventType.ON_SUCCESS:
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

			case EventType.ON_ERROR:
				Throwable throwable = (Throwable)eventData;
				getLogger().warn(throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
				DialogUtil.showAlertMessage(stationListView, "Erreur", throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
				stationListView.showDisplayable(stationListView, new BasicTransition());
				break;
				
			default:
				DialogUtil.showAlertMessage(stationListView, "Erreur", "Unsupported result");
				stationListView.showDisplayable(stationListView, new BasicTransition());
				break;
		}
	}
	
};

