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
package com.ccc.oauth.clientInfo;

import com.ccc.tools.TabToLevel;
import com.github.scribejava.core.model.OAuth2AccessToken;
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
    private transient OAuthConfig oauthConfig;
    
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

    public synchronized String getRefreshToken()
    {
        if(accessToken == null)
            return null;
        return ((OAuth2AccessToken)accessToken).getRefreshToken();
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

    @Override
    public String toString()
    {
        TabToLevel format = new TabToLevel();
        return toString(format).toString();
    }
    
    public TabToLevel toString(TabToLevel format)
    {
        format.ttl("Base20ClientInfo");
        format.inc();
        format.ttl("code: ", code);
        format.ttl("state: ", state);
        format.ttl("accessToken: ", accessToken);
        format.ttl("loginBaseUrl: ", loginBaseUrl);
        format.ttl("loginUrl: ", loginUrl);
        format.ttl("tokenUrl: ", tokenUrl);
        format.ttl("oauthConfig:");
        format.inc();
        if(oauthConfig == null)
        {
            format.ttl("null");
            format.dec();
            format.dec();
            return format;
        }
        format.ttl("apiKey: ", oauthConfig.getApiKey());
        format.ttl("apiSecret: ", oauthConfig.getApiSecret());
        format.ttl("callback: ", oauthConfig.getCallback());
        format.ttl("signatureType: ", oauthConfig.getSignatureType());
        format.ttl("scope: ", oauthConfig.getScope());
        format.ttl("grantType: ", oauthConfig.getGrantType());
        format.ttl("connectTimeout: ", oauthConfig.getConnectTimeout());
        format.ttl("readTimeout: ", oauthConfig.getReadTimeout());
        format.ttl("state: ", oauthConfig.getState());
        format.ttl("responseType: ", oauthConfig.getResponseType());
        format.dec();
        format.dec();
        return format;
    }
}
