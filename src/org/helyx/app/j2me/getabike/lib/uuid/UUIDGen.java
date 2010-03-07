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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.helyx.basics4me.io.BufferedReader;
import org.helyx.app.j2me.getabike.lib.uuid.UUID;

/**
 * This class contains methods to generate UUID fields. These methods have been
 * refactored out of {@link org.helyx.app.j2me.getabike.lib.uuid.UUID} because of the problems certain
 * application servers have with static fields.
 * <p>
 * Starting with version 2, this implementation tries to obtain the MAC address
 * of the network card. Under Microsoft Windows, the <code>ifconfig</code>
 * command is used which may pop up a command window in Java Virtual Machines
 * prior to 1.4 once this class is initialized. The command window is closed
 * automatically.
 * <p>
 * The MAC address code has been tested extensively in Microsoft Windows,
 * Linux, Solaris 8, HP-UX 11, but should work in MacOS X and BSDs, too.
 * 
 * @author <a href="mailto:jb@eaio.de">Johann Burkard</a>
 * @version $Id: UUIDGen.java,v 1.3 2008/02/20 07:37:40 Johann Exp $
 * @see org.helyx.app.j2me.getabike.lib.uuid.UUID
 */
public final class UUIDGen {

    /**
     * No instances needed.
     */
    private UUIDGen() {}

    /**
     * The last time value. Used to remove duplicate UUIDs.
     */
    private static long lastTime = Long.MIN_VALUE;

    /**
     * The current clock and node value.
     */
    private static long clockSeqAndNode = 0x8000000000000000L;
    
    private static Random random = new Random();

    static {

        clockSeqAndNode |= (long) (random.nextLong() * 0x7FFFFFFF);

        // Skip the clock sequence generation process and use random instead.

        clockSeqAndNode |= (long) (random.nextLong() * 0x3FFF) << 48;

    }

    /**
     * Returns the current clockSeqAndNode value.
     * 
     * @return the clockSeqAndNode value
     * @see UUID#getClockSeqAndNode()
     */
    public static long getClockSeqAndNode() {
        return clockSeqAndNode;
    }

    /**
     * Generates a new time field. Each time field is unique and larger than the
     * previously generated time field.
     * 
     * @return a new time value
     * @see UUID#getTime()
     */
    public static synchronized long newTime() {

        long time;

        // UTC time

        long timeMillis = (System.currentTimeMillis() * 10000) + 0x01B21DD213814000L;

        if (timeMillis > lastTime) {
            lastTime = timeMillis;
        }
        else {
            timeMillis = ++lastTime;
        }

        // time low

        time = timeMillis << 32;

        // time mid

        time |= (timeMillis & 0xFFFF00000000L) >> 16;

        // time hi and version

        time |= 0x1000 | ((timeMillis >> 48) & 0x0FFF); // version 1

        return time;

    }

}
