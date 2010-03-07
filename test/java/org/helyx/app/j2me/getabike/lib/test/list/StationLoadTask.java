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

import java.util.Vector;

import org.helyx.app.j2me.getabike.lib.task.AbstractProgressTask;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.logging4me.Logger;


public class StationLoadTask extends AbstractProgressTask {
	
	private static final Logger logger = Logger.getLogger("STATION_LOAD_TASK");
		
	public StationLoadTask() {
		super(logger.getCategory().getName());
	}

	public Runnable getRunnable() {
		return new Runnable() {

			public void run() {
				System.gc();
				
				Station[] stationArray = new Station[0];
				try {
					try {
						progressDispatcher.fireEvent(EventType.ON_START);
						
						progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Lecture des données");
				
						Vector stationList = new Vector(4096);
						
						int count = 0;
						for (count = 1 ; count < 5 ; count++) {
			
							Station station = new Station();
							station.name = "Porte de Vincennes";
							station.bonus = false;
							station.city = "Paris";
							station.hasLocalization = false;
							station.number = count;
							station.fullAddress = "24, cours de Vincennes";
							station.open = true;
							
							Thread.currentThread().sleep(10);
							if (count % 5 == 0) {
								progressDispatcher.fireEvent(EventType.ON_PROGRESS, count + " stations chargées");
							}
			
							stationList.addElement(station);
						}
			
						
						progressDispatcher.fireEvent(EventType.ON_PROGRESS, count + " stations chargées");
				
						logger.info("Copying Station Array ...");
						stationArray = new Station[stationList.size()];
						stationList.copyInto(stationArray);
						stationList = null;
						System.gc();
					}
			    	finally {
			    	}
					progressDispatcher.fireEvent(EventType.ON_SUCCESS, stationArray);
					stationArray = null;
					System.gc();
				}
				catch(Throwable t) {
					logger.warn(t);
					progressDispatcher.fireEvent(EventType.ON_ERROR, t);
				}
				
			}
			
		};
	
	}

	public boolean isCancellable() {
		return false;
	}
	
}
