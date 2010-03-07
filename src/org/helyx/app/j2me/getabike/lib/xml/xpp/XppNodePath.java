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
package org.helyx.app.j2me.getabike.lib.xml.xpp;

import org.helyx.app.j2me.getabike.lib.util.Stack;
import org.helyx.logging4me.Logger;



public class XppNodePath {
	
	private static final Logger logger = Logger.getLogger("XPP_NODE_PATH");
	
	public static final String SEPARATOR = "/";

	private Stack nodeNameStack = new Stack();
	
	private boolean changed = true;
	
	private String cachedPath;
	
	public XppNodePath() {
		super();
	}
	
	public void push(String nodeName) {
		changed = true;
		nodeNameStack.push(nodeName);
	}
	
	public void pop() {
		changed = true;
		nodeNameStack.pop();
	}
	
	public void clear() {
		changed = true;
		nodeNameStack.removeAllElements();
	}
	
	public String toXmlPath() {
		if (changed) {
			StringBuffer sb = new StringBuffer(SEPARATOR);
			int length = nodeNameStack.size();
			for (int i = 0 ; i < length ; i++) {
				sb.append(nodeNameStack.elementAt(i));
				if (i + 1 < length) {
					sb.append(SEPARATOR);
				}
			}
			changed = false;
			cachedPath = sb.toString();
		}
		
		return cachedPath;
	}
	
}
