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
package org.helyx.app.j2me.getabike.lib.util;

import java.util.Vector;

import org.helyx.app.j2me.getabike.lib.util.EmptyStackException;
import org.helyx.logging4me.Logger;


public class Stack extends Vector {
	
	private static final Logger logger = Logger.getLogger("STACK");

	public Stack() {
		super();
	}

	public Stack(int initialCapacity, int capacityIncrement) {
		super(initialCapacity, capacityIncrement);
	}

	public Stack(int initialCapacity) {
		super(initialCapacity);
	}

    public Object push(Object item)  {
        addElement(item);
        return item;
    }

    public synchronized Object pop()
    {
        int lenght = size();
        Object object = peek();
        removeElementAt(lenght - 1);
        return object;
    }

    public synchronized Object peek()
    {
        int lenght = size();
        if(lenght == 0) {
            throw new EmptyStackException();
        }
        else {
            return elementAt(lenght - 1);
        }
    }

    public boolean empty() {
        return size() == 0;
    }

    public synchronized int search(Object object) {
        int index = lastIndexOf(object);
        if(index >= 0) {
            return size() - index;
        }
        else {
            return -1;
        }
    }

}
