/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.test.list;

import org.helyx.app.j2me.getabike.lib.filter.Filter;
import org.helyx.app.j2me.getabike.lib.model.list.IElementProvider;
import org.helyx.app.j2me.getabike.lib.model.list.impl.ArrayElementProvider;
import org.helyx.app.j2me.getabike.lib.model.list.impl.FilterableSortableElementProvider;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.task.ProgressAdapter;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.transition.BasicTransition;
import org.helyx.logging4me.Logger;

public class UIStationLoaderProgressListener extends ProgressAdapter {

	private static final Logger logger = Logger.getLogger("STATION_LOADING_PROGRESS_LISTENER");
	
	private StationListView stationListView;
	private Filter objectFilter;
	
	public UIStationLoaderProgressListener(StationListView stationListView) {
		super(logger.getCategory().getName());
		this.stationListView = stationListView;
	}
	
	public UIStationLoaderProgressListener(StationListView stationListView, Filter objectFilter) {
		super(logger.getCategory().getName());
		this.stationListView = stationListView;
		this.objectFilter = objectFilter;
	}
	
	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
		switch (eventType) {
			case EventType.ON_SUCCESS:
				IElementProvider elementProvider = new ArrayElementProvider((Object[])eventData);
				if (objectFilter != null) {
					IElementProvider filteredElementProvider = new FilterableSortableElementProvider(elementProvider, objectFilter);
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

