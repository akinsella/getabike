package org.helyx.app.j2me.lib.content.provider;

import org.helyx.app.j2me.lib.task.AbstractProgressTask;


public class ContentProviderProgressTaskAdapter extends AbstractProgressTask {
	
	private static final String CAT = "CONTENT_PROVIDER_PROGRESS_TASK_ADAPTER";
		
	private IContentProvider contentProvider;
	
	public ContentProviderProgressTaskAdapter(IContentProvider contentProvider) {
		super(CAT);
		this.cancellable = false;
		this.contentProvider = contentProvider;
		this.progressDispatcher = contentProvider.getProgressDispatcher();
	}

	public String getDescription() {
		return "Content provider adapter: '" + contentProvider.getDescription() + "'";
	}

	public void execute() {
		contentProvider.loadData();
	}

	public String getCat() {
		return CAT + "[" + contentProvider.getCat() + "]";
	}
	
}
