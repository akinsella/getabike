package org.helyx.app.j2me.lib.content.provider;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.task.AbstractProgressTask;


public class ContentProviderProgressTaskAdapter extends AbstractProgressTask {
	
	private static final Logger logger = LoggerFactory.getLogger("CONTENT_PROVIDER_PROGRESS_TASK_ADAPTER");
		
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
