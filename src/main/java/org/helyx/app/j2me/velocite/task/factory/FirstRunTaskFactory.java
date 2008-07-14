package org.helyx.app.j2me.velocite.task.factory;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ITask;
import org.helyx.app.j2me.lib.task.ITaskFactory;
import org.helyx.app.j2me.velocite.data.city.provider.DefaultCityContentProvider;

public class FirstRunTaskFactory implements ITaskFactory {
	
	private static final String CAT = "FIRST_RUN_TASK_FACTORY";
	
	public FirstRunTaskFactory() {
		super();
	}
	
	public ITask[] getTasks() {
		
		IContentAccessor cityContentAccessor = new HttpContentAccessor("http://www.velocite.org/cities.xml");
		IContentProvider contentProvider = new DefaultCityContentProvider(cityContentAccessor);
		IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);
		
		ITask[] tasks = new ITask[] {
			progressTask
		};
		
		return tasks;
	}
}
