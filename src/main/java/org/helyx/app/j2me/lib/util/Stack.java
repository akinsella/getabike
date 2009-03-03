package org.helyx.app.j2me.lib.util;

import java.util.Vector;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class Stack extends Vector {
	
	private static final Logger logger = LoggerFactory.getLogger("STACK");

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
