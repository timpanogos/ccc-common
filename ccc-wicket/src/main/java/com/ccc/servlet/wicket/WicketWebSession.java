/*
**  Copyright (c) 2016, Cascade Computer Consulting.
**
**  Permission to use, copy, modify, and/or distribute this software for any
**  purpose with or without fee is hereby granted, provided that the above
**  copyright notice and this permission notice appear in all copies.
**
**  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
**  WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
**  MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
**  ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
**  WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
**  ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
**  OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
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
        clientInfo.setOauthClientInfo(((WicketServlet)getApplication()).getBaseClientInfo());
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