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
package com.ccc.servlet.wicket.login;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.ccc.oauth.clientInfo.SessionClientInfo;
import com.ccc.oauth.login.Auth20Callback;

@SuppressWarnings("javadoc")
public abstract class AuthCallback extends WebPage
{
    private static final long serialVersionUID = 7397831975135168252L;

    @SuppressWarnings("unused")
    public AuthCallback(PageParameters parameters) throws Exception
    {
    }

    protected void handleCallback(PageParameters parameters, SessionClientInfo sessionClientInfo) throws Exception
    {
        Auth20Callback callback = new Auth20Callback();
        callback.handleCallback(parameters.get("code").toString(), parameters.get("state").toString(), sessionClientInfo);
    }
}
