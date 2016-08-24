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
package com.ccc.servlet.wicket;

import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.cycle.RequestCycle;

import com.ccc.oauth.clientInfo.BaseClientInfo;
import com.ccc.oauth.clientInfo.SessionClientInfo;

@SuppressWarnings("javadoc")
public class WicketClientInfo extends WebClientInfo implements SessionClientInfo
{
    private static final long serialVersionUID = 2619172563751052839L;
    
    private boolean authenticated;
    private BaseClientInfo oauthClientInfo;
    
    public WicketClientInfo(RequestCycle requestCycle)
    {
        super(requestCycle);
    }

    @Override
    public synchronized void setAuthenticated(boolean value)
    {
        authenticated = value;
    }
    
    @Override
    public synchronized boolean isAuthenticated()
    {
        return authenticated;
    }

    @Override
    public synchronized BaseClientInfo getOauthClientInfo()
    {
        return oauthClientInfo;
    }

    @Override
    public synchronized void setOauthClientInfo(BaseClientInfo oauthClientInfo)
    {
        this.oauthClientInfo = oauthClientInfo;
    }
}
