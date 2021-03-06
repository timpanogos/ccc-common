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

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;

@SuppressWarnings("javadoc")
public class WicketWebSession extends AuthenticatedWebSession
{
    private static final long serialVersionUID = -3746714701542229374L;

    public WicketWebSession(Request request)
    {
        super(request);
        WicketClientInfo clientInfo = new WicketClientInfo(RequestCycle.get());
        clientInfo.setOauthClientInfo(((WicketBaseServlet)getApplication()).getBaseClientInfo());
        setClientInfo(clientInfo);
    }

    @Override
    public boolean authenticate(final String username, final String password)
    {
        return ((WicketClientInfo)getClientInfo()).isAuthenticated();
    }

    @Override
    public Roles getRoles()
    {
        if(((WicketClientInfo)getClientInfo()).isAuthenticated())
            return new Roles(Roles.ADMIN);
        return null;
    }
}
