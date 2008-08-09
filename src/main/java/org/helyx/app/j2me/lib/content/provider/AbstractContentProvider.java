package org.helyx.app.j2me.lib.content.provider;

import org.helyx.app.j2me.lib.task.IProgressDispatcher;
import org.helyx.app.j2me.lib.task.ProgressDispatcher;
import org.helyx.app.j2me.lib.task.ProgressListener;

public abstract class AbstractContentProvider implements IContentProvider {
	
	private static final String CAT = "ABSTRACT_CONTENT_PROVIDER";

	protected IProgressDispatcher progressDispatcher;

	public AbstractContentProvider() {
		super();
		this.progressDispatcher = new ProgressDispatcher();
		this.progressDispatcher.setName(getCat());
	}

	public void addProgressListener(ProgressListener progressListener) {
		progressDispatcher.addProgressListener(progressListener);
	}

	public void removeProgressListener(ProgressListener progressListener) {
		progressDispatcher.removeProgressListener(progressListener);		
	}

	public IProgressDispatcher getProgressDispatcher() {
		return progressDispatcher;
	}

	public void cancel() {
	
	}
	
	public abstract String getCat();

}
