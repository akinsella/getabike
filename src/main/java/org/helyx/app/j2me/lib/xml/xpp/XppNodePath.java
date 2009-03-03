package org.helyx.app.j2me.lib.xml.xpp;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.util.Stack;


public class XppNodePath {
	
	private static final Logger logger = LoggerFactory.getLogger("XPP_NODE_PATH");
	
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
