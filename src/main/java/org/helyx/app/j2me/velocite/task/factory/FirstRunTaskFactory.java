package org.helyx.app.j2me.velocite.task.factory;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.task.ITask;
import org.helyx.app.j2me.lib.task.ITaskFactory;

public class FirstRunTaskFactory implements ITaskFactory {
	
	private static final Log log = LogFactory.getLog("FIRST_RUN_TASK_FACTORY");
	
	public FirstRunTaskFactory() {
		super();
	}
	
	public ITask[] getTasks() {
//		
//		IContentAccessor cityContentAccessor = new HttpContentAccessor("http://m.velocite.org/cities/v2/data.xml");
//		IContentProvider contentProvider = new DefaultCityContentProvider(cityContentAccessor);
//		IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);
		
		ITask[] tasks = new ITask[] {
//			progressTask
		};
		
		return tasks;
	}
}
