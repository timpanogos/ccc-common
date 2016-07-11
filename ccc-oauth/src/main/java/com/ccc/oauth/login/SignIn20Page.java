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
