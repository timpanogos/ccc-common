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

import org.opendof.core.oal.DOFException;
import org.opendof.core.oal.DOFServer;


/**
 * A <code>DOFServer.StateListener</code> implementation that reflects the 
 * server state in a <code>StatusTracker</code>.
 * </p>
 * The server name will be used as the basis for the subsystem name to be used with the <code>StatusTracker</code>.
 * The user can optionally specify a prefix to be prepended to the server name. Because the status 
 * subsystem name is based on the server name, a single instance of this listener can be added
 * to multiple <code>DOFServer</code>s.
 * </p>
 * If not started, the status will be set to <code>StatusTracker.Status.Warn</code>.
 * If started, the servers's named status will be set to <code>StatusTracker.Status.Ok</code>.
 * When the server is destroyed the named status is removed from the <code>StatusTracker</code>
 * </p>
 *  Typically a <code>org.emitdo.service.util.Service</code> implementation's System Level will 
 *  create the <code>DOFServer</code>.  The System Level can also instantiate this listener and 
 *  add it to the server.  
 * @see StatusTracker
 * @see Service
 */

public class StatusServerStateListener implements DOFServer.StateListener
{
    private volatile StatusTracker statusTracker;
    private volatile String prefixName;
    
    /**
     * Default Constructor.
     */
    public StatusServerStateListener()
    {
    }
    
    /**
     * Initialization method that provides the <code>StatusTracker</code> and optionally a prefix name.
     * If the prefix name is null or zero length, only the connection name will be used.
     * @param statusTracker the <code>StatusTracker</code> to be used.  Must not 
     * be null;
     * @param prefixName the status subsystem prefix to prepend to the connection name.  If null or zero length 
     * the connection name is used.
     * @throws IllegalArgumentException if statusTracker is null.
     */
    public void init(StatusTracker statusTracker, String prefixName)
    {
        if(statusTracker == null)
            throw new IllegalArgumentException("statusTracker == null"); 
        if(prefixName != null && prefixName.length() > 0)
            this.prefixName = prefixName;
        else
            this.prefixName = "";
        this.statusTracker = statusTracker;
   }
    
    
    /* ************************************************************************
     * DOFServer.StateListener implementation
     **************************************************************************/
    
    @Override
    public void removed(DOFServer server, DOFException exception) 
    {
        String name = prefixName + server.getState().getName();
        statusTracker.removeStatus(name);
    }

    @Override
    public void stateChanged(DOFServer server, DOFServer.State state)
    {
        String name = (prefixName != null ? prefixName : "") + server.getState().getName();
        if(state.isStarted())
            statusTracker.setStatus(name, StatusTracker.Status.Ok);
        else
            statusTracker.setStatus(name, StatusTracker.Status.Warn);
    }

}
