package org.helyx.app.j2me.velocite.task.factory;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.BasicTask;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ITask;
import org.helyx.app.j2me.lib.task.ITaskFactory;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.provider.DefaultCityContentProvider;

public class DataCleanUpTaskFactory implements ITaskFactory {
	
	private static final String CAT = "DATA_CLEAN_UP_TASK_FACTORY";
		
	public DataCleanUpTaskFactory() {
		super();
	}

	public ITask[] getTasks() {

		ITask[] tasks = new ITask[] {
			new BasicTask("Cleaning up cities related data") {
				public void execute() {
					CityManager.cleanUpSavedData();
				}
			}, 
			
			new BasicTask("Cleaning up station related data") {
				public void execute() {
					CartoManager.cleanUpSavedData();
				}
			}, 
			
			new BasicTask("Cleaning up preference related data") {
				public void execute() {
					PrefManager.cleanUpSavedData();
				}
			}
			
		};
		
		return tasks;
	}

}
