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
package org.helyx.app.j2me.getabike.lib.content.provider;

import org.helyx.app.j2me.getabike.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.getabike.lib.task.AbstractProgressTask;
import org.helyx.logging4me.Logger;



public class ContentProviderProgressTaskAdapter extends AbstractProgressTask {
	
	private static final Logger logger = Logger.getLogger("CONTENT_PROVIDER_PROGRESS_TASK_ADAPTER");
		
	private IContentProvider contentProvider;
	
	public ContentProviderProgressTaskAdapter(IContentProvider contentProvider) {
		super(contentProvider.getDescription());
		this.cancellable = false;
		this.contentProvider = contentProvider;
		this.progressDispatcher = contentProvider.getProgressDispatcher();
	}

	public String getDescription() {
		return "Content provider adapter: '" + contentProvider.getDescription() + "'";
	}

	public Runnable getRunnable() {
		
		return new Runnable() {

			public void run() {
				contentProvider.loadData();
			}
			
		};
	}
	
}
