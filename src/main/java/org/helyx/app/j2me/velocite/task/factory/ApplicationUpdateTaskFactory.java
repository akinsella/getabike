package org.helyx.app.j2me.velocite.task.factory;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.task.BasicTask;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ITask;
import org.helyx.app.j2me.lib.task.ITaskFactory;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.provider.DefaultCityContentProvider;

// TODO: Fix Version Management
public class ApplicationUpdateTaskFactory implements ITaskFactory {

	private static final String CAT = "APPLICATION_UPDATE_TASK_FACTORY";
	private static final String V_1_0_83 = "1.0.83";
	private static final String V_1_0_82 = "1.0.82";
	private String oldVersion;
	private String newVersion;
	
	private Canvas canvas;
	
	public ApplicationUpdateTaskFactory(Canvas canvas,  String oldVersion, String newVersion) {
		super();
		this.canvas = canvas;
		this.oldVersion = oldVersion;
		this.newVersion = newVersion;
	}

	public ITask[] getTasks() {
		Log.info(CAT, "Old Version: " + oldVersion + ", New Version: " + newVersion);
		Vector tasksToRun = new Vector();
		if (V_1_0_82.equals(newVersion) || V_1_0_83.equals(newVersion))  {
			Log.info(CAT, "Need to Clean Up Cities to support Lyon City.");
			
			tasksToRun.addElement(new BasicTask("Cleaning up City related data") {
				public void execute() {
					Log.info(CAT, "Cleaning up City related data");
					CityManager.cleanUpSavedData();
				}
			});
			
			Log.info(CAT, "Reloading City Data");
			IContentAccessor cityContentAccessor = new HttpContentAccessor("http://www.velocite.org/cities.xml");
			IContentProvider contentProvider = new DefaultCityContentProvider(cityContentAccessor);
			IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);

			tasksToRun.addElement(progressTask);
		}

		int tasksToRunCount = tasksToRun.size();
		ITask[] tasks = new ITask[tasksToRunCount];

		if (tasksToRunCount > 0) {
			tasksToRun.copyInto(tasks);
		}
		
		return tasks;
	}

}
