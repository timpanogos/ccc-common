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
package com.ccc.oauth.login;

import com.ccc.oauth.UserAuthenticationHandler;
import com.ccc.oauth.UserAuthenticationHandler.AuthenticateUserData;
import com.ccc.oauth.clientInfo.Base20ClientInfo;
import com.ccc.oauth.clientInfo.SessionClientInfo;


@SuppressWarnings({ "javadoc" })
public final class SignIn20Page
{
    public SignIn20Page()
    {
    }
    
    public void handleSignIn(SessionClientInfo clientInfo) throws Exception
    {
        UserAuthenticationHandler handler = UserAuthenticationHandler.getInstance();
        AuthenticateUserData oauthData = handler.authenticateUser();
        ((Base20ClientInfo)clientInfo.getOauthClientInfo()).setLoginUrl(oauthData.loginUrl);
        ((Base20ClientInfo)clientInfo.getOauthClientInfo()).setOauthConfig(oauthData.oauthConfig);
    }
}
