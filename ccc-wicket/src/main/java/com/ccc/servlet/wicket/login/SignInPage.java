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

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authentication.pages.SignOutPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.LoggerFactory;

import com.ccc.oauth.clientInfo.Base20ClientInfo;
import com.ccc.oauth.login.SignIn20Page;
import com.ccc.servlet.wicket.WicketClientInfo;


@SuppressWarnings({ "javadoc" })
public final class SignInPage extends WebPage
{
    private static final long serialVersionUID = 1L;

    public SignInPage(final PageParameters parameters)
    {
        super(parameters);
        WicketClientInfo clientInfo = (WicketClientInfo) WebSession.get().getClientInfo();
        try
        {
            SignIn20Page signin = new SignIn20Page();
            signin.handleSignIn(clientInfo);
        } catch (Exception e)
        {
            LoggerFactory.getLogger(getClass()).info("OAuth authentication phase 1 failed", e);
            throw new RestartResponseAtInterceptPageException(SignOutPage.class);
        }
        throw new RedirectToUrlException(((Base20ClientInfo)clientInfo.getOauthClientInfo()).getLoginUrl());
    }
}
