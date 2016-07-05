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
package com.ccc.tools.servlet.clientInfo;

import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Token;

@SuppressWarnings("javadoc")
public class Base20ClientInfo implements BaseClientInfo 
{
    private static final long serialVersionUID = -3381601101197739678L;
    
    private String code;
    private String state;
    private Token accessToken;
    private String loginBaseUrl;    // from config
    private String loginUrl;        // totally filled out from scribejava
    private String tokenUrl;
    private OAuthConfig oauthConfig;
    
    public Base20ClientInfo()
    {
    }

    public void validateState() throws Exception
    {
        if (oauthConfig.getState().equals(state))
            return;
        throw new Exception("unexpected oauth state returned");
    }
    
    public synchronized String getLoginUrl()
    {
        return loginUrl;
    }

    public synchronized void setLoginUrl(String loginUrl)
    {
        this.loginUrl = loginUrl;
    }

    public synchronized OAuthConfig getOauthConfig()
    {
        return oauthConfig;
    }

    public synchronized void setOauthConfig(OAuthConfig oauthConfig)
    {
        this.oauthConfig = oauthConfig;
    }
    
    public synchronized Token getAccessToken()
    {
        return accessToken;
    }

    public synchronized void setAccessToken(Token accessToken)
    {
        this.accessToken = accessToken;
    }

    public synchronized String getLoginBaseUrl()
    {
        return loginBaseUrl;
    }

    public synchronized void setLoginBaseUrl(String loginBaseUrl)
    {
        this.loginBaseUrl = loginBaseUrl;
    }
    
    public synchronized String getTokenUrl()
    {
        return tokenUrl;
    }

    public synchronized void setTokenUrl(String tokenUrl)
    {
        this.tokenUrl = tokenUrl;
    }
    
    public synchronized String getCode()
    {
        return code;
    }

    public synchronized void setCode(String code)
    {
        this.code = code;
    }

    public synchronized String getState()
    {
        return state;
    }

    public synchronized void setState(String state)
    {
        this.state = state;
    }
    
}
