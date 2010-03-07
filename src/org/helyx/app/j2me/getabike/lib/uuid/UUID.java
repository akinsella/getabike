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
package org.helyx.app.j2me.getabike.lib.uuid;

import org.helyx.app.j2me.getabike.lib.uuid.Hex;
import org.helyx.app.j2me.getabike.lib.uuid.UUID;
import org.helyx.app.j2me.getabike.lib.uuid.UUIDGen;


/**
 * Creates UUIDs according to the DCE Universal Token Identifier specification.
 * <p>
 * UUIDs of this class are suitable as primary keys for EJB Entity Beans. You
 * will need to add the two following methods to your CMP Entity Beans:
 * <pre>
 * public abstract long getTime();
 * public abstract void setTime(long time);
 * 
 * public abstract long getClockSeqAndNode(); 
 * public abstract void setClockSeqAndNode(long clockSeqAndNode);
 * </pre>
 * <p>
 * Don't forget to set com.eaio.uuid.UUID as the primary key in your
 * descriptor and to initialize the primary key in your bean by calling
 * <pre>
 * public UUID ejbCreate(...) throws CreateException {
 *  setTime(UUIDGen.newTime());
 *  setClockAndNode(UUIDGen.getClockSeqAndNode());
 *  return null;
 * }
 * </pre>
 * <p>
 * For usage with the Sun Application Server, you must comment out the
 * <code>serialVersionUID</code> field and recompile the sources.
 * 
 * @see <a href="http://www.opengroup.org/onlinepubs/9629399/apdxa.htm">
 * http://www.opengroup.org/onlinepubs/9629399/apdxa.htm
 * </a>
 * @see <a href="http://www.uddi.org/pubs/draft-leach-uuids-guids-01.txt">
 * http://www.uddi.org/pubs/draft-leach-uuids-guids-01.txt
 * </a>
 * @author <a href="mailto:jb@eaio.de">Johann Burkard</a>
 * @version $Id: UUID.java,v 1.3 2008/02/20 07:37:40 Johann Exp $
 */
public class UUID {

    /**
     * If you are working with the Sun One Application Server and you are using
     * this class as primary keys for EJBs, you must comment this field out.
     * <p>
     * This applies to version 7.
     */
    static final long serialVersionUID = 7435962790062944603L;

    /**
     * The time field of the UUID.
     * 
     * @serial
     */
    public long time;

    /**
     * The clock sequence and node field of the UUID.
     * 
     * @serial
     */
    public long clockSeqAndNode;

    /**
     * Constructor for UUID. Constructs a new, unique UUID.
     * 
     * @see UUIDGen#newTime()
     * @see UUIDGen#getClockSeqAndNode()
     */
    public UUID() {
        this(UUIDGen.newTime(), UUIDGen.getClockSeqAndNode());
    }

    /**
     * Constructor for UUID. Constructs a UUID from two <code>long</code> values.
     * 
     * @param time the upper 64 bits
     * @param clockSeqAndNode the lower 64 bits
     */
    public UUID(long time, long clockSeqAndNode) {
        this.time = time;
        this.clockSeqAndNode = clockSeqAndNode;
    }

    /**
     * Copy constructor for UUID. Values of the given UUID are copied.
     *
     * @param u the UUID, may not be <code>null</code>
     */
    public UUID(UUID u) {
        this(u.time, u.clockSeqAndNode);
    }

    /**
     * Compares this UUID to another Object. Throws a {@link ClassCastException} if
     * the other Object is not an instance of the UUID class. Returns a value
     * smaller than zero if the other UUID is "larger" than this UUID and a value
     * larger than zero if the other UUID is "smaller" than this UUID.
     * 
     * @param o the other Object, may not be <code>null</code>
     * @return a value &lt; 0, 0 or a value &gt; 0
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @throws ClassCastException
     */
    public int compareTo(Object o) {
        if (this == o) {
            return 0;
        }
        if (!(o instanceof UUID)) {
            throw new ClassCastException(
                    "The argument must be of type '" + getClass().getName() + "'.");
        }
        UUID t = (UUID) o;
        if (time > t.time) {
            return 1;
        }
        if (time < t.time) {
            return -1;
        }
        if (clockSeqAndNode > t.clockSeqAndNode) {
            return 1;
        }
        if (clockSeqAndNode < t.clockSeqAndNode) {
            return -1;
        }
        return 0;
    }

    /**
     * Returns this UUID as a String.
     * 
     * @return a String, never <code>null</code>
     * @see java.lang.Object#toString()
     * @see #toStringBuffer(StringBuffer)
     */
    public final String toString() {
        return toStringBuffer(null).toString();
    }

    /**
     * Appends a String representation of this to the given {@link StringBuffer} or
     * creates a new one if none is given.
     * 
     * @param in the StringBuffer to append to, may be <code>null</code>
     * @return a StringBuffer
     */
    public StringBuffer toStringBuffer(StringBuffer in) {
        if (in == null) {
            in = new StringBuffer(36);
        }
        else {
            in.ensureCapacity(in.length() + 36);
        }
        in.append(Hex.asChars((int) (time >> 32))).append('-').append(
                Hex.asChars((short) (time >> 16))).append('-').append(
                Hex.asChars((short) time)).append('-').append(
                Hex.asChars((short) (clockSeqAndNode >> 48))).append('-').append(
                Hex.asChars(clockSeqAndNode, 12));
        return in;
    }

    /**
     * Returns a hash code of this UUID. The hash code is calculated by XOR'ing the
     * upper 32 bits of the time and clockSeqAndNode fields and the lower 32 bits of
     * the time and clockSeqAndNode fields.
     * 
     * @return an <code>int</code> representing the hash code
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (int) ((time >> 32) ^ time ^ (clockSeqAndNode >> 32) ^ clockSeqAndNode);
    }

    /**
     * Clones this UUID.
     * 
     * @return a new UUID with identical values, never <code>null</code>
     */
    public Object clone() {
        return new UUID(this);
    }

    /**
     * Returns the time field of the UUID (upper 64 bits).
     * 
     * @return the time field
     */
    public final long getTime() {
        return time;
    }

    /**
     * Returns the clock and node field of the UUID (lower 64 bits).
     * 
     * @return the clockSeqAndNode field
     */
    public final long getClockSeqAndNode() {
        return clockSeqAndNode;
    }

    /**
     * Compares two Objects for equality.
     * 
     * @see java.lang.Object#equals(Object)
     * @param obj the Object to compare this UUID with, may be <code>null</code>
     * @return <code>true</code> if the other Object is equal to this UUID,
     * <code>false</code> if not
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof UUID)) {
            return false;
        }
        return compareTo(obj) == 0;
    }

    /**
     * Returns the nil UUID (a UUID whose values are both set to zero).
     * <p>
     * Starting with version 2.0, this method does return a new UUID instance every
     * time it is called. Earlier versions returned one instance. This has now been
     * changed because this UUID has public, non-final instance fields. Returning a
     * new instance is therefore more safe.
     * 
     * @return a nil UUID, never <code>null</code>
     */
    public static UUID nilUUID() {
        return new UUID(0, 0);
    }

}
