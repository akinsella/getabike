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

import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.task.ProgressListener;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.list.AbstractListView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.task.LoadTaskView;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.logging4me.Logger;


public class StationListView extends AbstractListView {
	
	private static final Logger logger = Logger.getLogger("STATION_LIST_VIEW");

	public StationListView(AbstractMIDlet midlet, String title) {
		super(midlet, title);
		init();
	}
	
	private void init() {
		initActions();
		initData();
		initComponents();
	}
	
	protected void initActions() {	

		setSecondaryCommand(new Command("Quitter", true, getMidlet().getI18NTextRenderer(), new IAction() {
			public void run(Object data) {
				fireReturnCallback();
			}
		}));

	}
	
	protected void showItemSelected() {
		onShowItemSelected(elementProvider.get(selectedOffset));
	}

	protected void initData() {
		// Nothing to do
	}

	protected void initComponents() {

	}
	
	protected void onShowItemSelected(Object object) {
		logger.debug("Shiw selected item");
		DialogUtil.showAlertMessage(this, "dialog.information", "Item selected offset: " + getSelectedOffset());
	}

	public void loadListContent(ProgressListener progressListener) {
		final StationLoadTask stationLoadTask = new StationLoadTask();
		stationLoadTask.addProgressListener(progressListener);
		LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "Chargement des stations", stationLoadTask);
		showDisplayable(loadTaskView, this);
		resetPosition();
		loadTaskView.startTask();
		logger.info("Load List Content...");
	}

}
