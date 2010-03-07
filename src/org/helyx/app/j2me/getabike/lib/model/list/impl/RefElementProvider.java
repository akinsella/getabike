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
package org.helyx.app.j2me.getabike.lib.model.list.impl;

import org.helyx.app.j2me.getabike.lib.model.list.IElementProvider;
import org.helyx.app.j2me.getabike.lib.model.list.IRefElementProvider;

public class RefElementProvider implements IRefElementProvider {

	IElementProvider elementProvider;
	
	public RefElementProvider() {
		super();
	}
	
	public RefElementProvider(IElementProvider elementProvider) {
		super();
		if (elementProvider == null) {
			throw new NullPointerException("elementProvider cannot be null");
		}
		this.elementProvider = elementProvider;
	}
	
	public Object get(int offset) {
		return elementProvider == null ? null : elementProvider.get(offset);
	}

	public Object[] getElements() {
		return elementProvider == null ? new Object[0] : elementProvider.getElements();
	}

	public int length() {
		return elementProvider == null ? 0 : elementProvider.length();
	}
	
	public void setElementProvider(IElementProvider elementProvider) {
		if (elementProvider == null) {
			throw new NullPointerException("elementProvider cannot be null");
		}
		this.elementProvider = elementProvider;
	}
	
	public IElementProvider getElementProvider() {
		return elementProvider;
	}

}
