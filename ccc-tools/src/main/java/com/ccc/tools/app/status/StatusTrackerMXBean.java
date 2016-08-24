/*
**  Copyright (c) 2016, Chad Adams.
**
**  This program is free software: you can redistribute it and/or modify
**  it under the terms of the GNU Lesser General Public License as 
**  published by the Free Software Foundation, either version 3 of the 
**  License, or any later version.
**
**  This program is distributed in the hope that it will be useful,
**  but WITHOUT ANY WARRANTY; without even the implied warranty of
**  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
**  GNU General Public License for more details.

**  You should have received copies of the GNU GPLv3 and GNU LGPLv3
**  licenses along with this program.  If not, see http://www.gnu.org/licenses
*/
package com.ccc.tools.app.status;

import java.util.List;

import com.ccc.tools.app.status.StatusTracker.Status;

/**
 * A complementary JMX interface to the <code>StatusTrackerProvider</code> implementation.
 * </p>
 * This interface defines the set of methods for reporting status information to JMX.
 *  </p>
 *  The consolidated status (highest level of concern) of all the named subsystems 
 *  as well as a list of all subsystem names, and ability to obtain the individual 
 *  status of each named subsystem is provided.
 * @see StatusTracker
 * @see StatusTrackerProvider 
 */
public interface StatusTrackerMXBean
{
    /**
     * Get the consolidated status of all known subsystems.
     * @return the consolidated status. Must not return null.
     */
    public Status getStatus();
    
    /**
     * Return a list of currently known subsystem names. 
     * @return a list of all currently known subsystem names.  Must not return null, may return an empty list.
     */
    public List<String> getNames();
    
    /**
     * Return the status of the given named subsystem.
     * @param name the subsystem name to return the status for.  Must not be null.
     * @return the status of the named subsystem.  Must not return null.
     */
    public Status getStatus(String name);
}
