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
package com.ccc.tools.executor;

import com.ccc.tools.app.status.StatusTracker;
import com.ccc.tools.app.status.StatusTrackerProvider;

/**
 * Declares the required ExecutorQueueConfig interface
 */
public interface ExecutorQueueConfig
{
    /**
     * Get the queues core pool size.
     * </p>
     * @return size the core pool size.
     */
    public int getCorePoolSize();

    /**
     * Get the thread name prefix for pool threads.
     * </p>
     * @return name the thread prefix name to use.  Must not be null.
     * @throws IllegalArgumentException if name is null.
     */
    public String getThreadNamePrefix();

    /**
     * Get the <code>StatusTracker</code> to use.
     * </p>
     * @return the <code>StatusTracker</code> to use.  Must not return null.
     * @see StatusTrackerProvider
     */
    public StatusTracker getStatusTracker();

    /**
     * Get the <code>StatusTracker</code> subsystem name to use.
     * @return the <code>StatusTracker</code>'s subsystem name.
     * @see StatusTrackerProvider
     */
    public String getStatusSubsystemName();
}