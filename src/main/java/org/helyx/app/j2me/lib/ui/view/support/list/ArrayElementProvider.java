package org.helyx.app.j2me.lib.ui.view.support.list;

public class ArrayElementProvider implements IElementProvider {

	private Object[] elementArray;
	
	public ArrayElementProvider(Object[] elementArray) {
		super();
		this.elementArray = elementArray;
	}
	
	public Object get(int offset) {
		return elementArray[offset];
	}

	public int length() {
		return elementArray.length;
	}

}
